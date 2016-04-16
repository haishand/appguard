/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileServer {

    ExecutorService threadPool;
    int port;
    boolean isStopped;
    ServerSocket server;
    Map<Long, FileLog> data = new HashMap<Long, FileLog>();
    
    FileServer(int port, int connNum) {
        this.isStopped = false;
        this.port = port;
        threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * connNum);
    }

    void stop() {
        this.isStopped = true;
    }

    // start service
    void start() throws IOException {
        server = new ServerSocket(port);
        while (!isStopped) {
            Socket socket = server.accept();
            threadPool.execute(new SocketTask(socket));
        }
    }

    FileLog find(long id) {
        return data.get(id);
    }

    public static void main(String[] args) {

    }

    private class SocketTask implements Runnable {

        Socket socket = null;
        
        public SocketTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                System.out.println("accepted connection" + socket.getInetAddress() + ":" + socket.getPort());
                PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());

		// The C/S Agreement:
                // Content-Length=xxx;filename=xxx;sourceid=xxx;IMEI=xxxx;";
                String head = StreamTool.readLine(inStream);
                System.out.println(head);
                if (head != null) {
                    String[] items = head.split(";");
                    String fileLength = items[0].substring(items[0].indexOf("=") + 1);
                    String fileName = items[1].substring(items[1].indexOf("=") + 1);
                    String sourceId = items[2].substring(items[2].indexOf("=") + 1);
                    String imei = items[3].substring(items[3].indexOf("=") + 1);
                    long id = System.currentTimeMillis();

                    FileLog log = null;
                    if (sourceId != null && !"".equals(sourceId)) {
                        id = Long.valueOf(sourceId);
                        log = find(id);
                    }

                    File file = null;
                    int position = 0;
                    if (log == null) {
//                        String path = new SimpleDateFormat("yyyy/MM/dd/HH/mm").format(new Date());
                        String path = "" + socket.getInetAddress() + "-" + imei;
                        File dir = new File("files/" + path);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        
//                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date());
//                        if (!fileName.startsWith("device_info")) {
//                            fileName += "_" + timeStamp + ".txt";
//                        }

// backup                        
//                        file = new File(dir, fileName);
//                        if (file.exists()) {
//                            String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date());  
//                            File newDir = new File("files/backup/");
//                            if (!newDir.exists()) {
//                                newDir.mkdirs();
//                            }
//                            File newFile = new File("files/backup/", timeStamp + "_" + fileName);
//                            file.renameTo(newFile);
//                        }
                        
//                        if (file.exists()) {
//                            fileName += fileName.substring(0, fileName.indexOf(".") + 1)
//                                    + dir.listFiles().length
//                                    + fileName.substring(fileName.indexOf("."));
//                            file = new File(dir, fileName);
//                        }
                        file = new File(dir, fileName);
                        if (!file.exists()) {
                            save(id, file);
                        }
                    } else {
                        file = new File(log.getPath());
                        if (file.exists()) {
                            File logFile = new File(file.getParentFile(), file.getName() + ".log");
                            if (logFile.exists()) {
                                Properties prop = new Properties();
                                prop.load(new FileInputStream(logFile));
                                position = Integer.valueOf(prop.getProperty("length"));
                            }
                        }
                    }

                    OutputStream outStream = socket.getOutputStream();
                    String response = "sourceId=" + id + ";position=" + position + "\r\n";
                    outStream.write(response.getBytes());

                    RandomAccessFile fileOutStream = new RandomAccessFile(file, "rwd");
                    if (position == 0) {
                        fileOutStream.setLength(Integer.valueOf(fileLength));
                    }
                    fileOutStream.seek(position);
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    int length = position;
                    while ((len = inStream.read(buffer)) != -1) {
                        fileOutStream.write(buffer, 0, len);
                        length += len;
                        Properties prop = new Properties();
                        prop.put("length", String.valueOf(length));
                        FileOutputStream logFile = new FileOutputStream(new File(file.getParentFile(), file.getName() + ".log"));
                        prop.store(logFile, null);
                        logFile.close();
                    }
                    if (length == fileOutStream.length()) {
                        delete(id);
                    }
                    fileOutStream.close();
                    inStream.close();
                    outStream.close();
                    file = null;

                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        void save(long id, File file) {
            data.put(id, new FileLog(id, file.getAbsolutePath()));
        }

        void delete(long id) {
            if (data.containsKey(id)) {
                data.remove(id);
            }
        }
    }
}
