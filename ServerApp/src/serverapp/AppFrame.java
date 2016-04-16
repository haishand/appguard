/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author haishand
 */
public class AppFrame extends javax.swing.JFrame {

    private JDialog appCfgDialog = null;
    private AppConfig appCfg = null;
    private FileServer server = null;
    private Timer refreshTimer;
    private GprsInfo gprsInfo;

    /**
     * Creates new form NewJFrame
     */
    public AppFrame() {
        init();

//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
//                updateUI();
//            }
//
//        });
        initComponents();
        reloadAllData();
    }

    private void init() {
        appCfg = new AppConfig();
    }

    private void reloadAllData() {
        reloadPhoneList();
    }

    private void updateUI() {
        refreshTimer = new Timer(500, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
//                reloadDeviceInfoTbl();
            }
        });
        refreshTimer.setRepeats(true);
        refreshTimer.start();
    }

    private void reloadPhoneList() {
        Vector v = new Vector();
        File dir = new File("files/");
        for (File f : dir.listFiles()) {
            if (!f.isHidden()) {
                v.add(f.getName());
            }
        }
        jlstPhoneList.setListData(v);
    }

    private Vector parseFile(File f) {
        Vector data = new Vector();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ";");
                while (st.hasMoreElements()) {
                    Vector v = new Vector();
                    String s = st.nextToken();
//                    System.out.println(s);
                    StringTokenizer ast = new StringTokenizer(s, "=");
                    while (ast.hasMoreElements()) {
                        String ss = ast.nextToken();
                        v.add(ss);
                    }
                    data.add(v);
                }

            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AppFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    private void genMapFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("map_template.html")));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("map.html")));
            String line;
//            GprsInfo gprsInfo = new GprsInfo();
//            gprsInfo.setLatitude("120.159274");
//            gprsInfo.setLongitude("35.967449");
            String gprsLine = "var " + "lat=" + gprsInfo.getLatitude() + "," + "lgt=" + gprsInfo.getLongitude() + ";";
System.out.println(gprsLine);            
            while ((line = br.readLine()) != null) {
                if (line.contains("APP_CHANGE_POINT_FLAG")) {
                    bw.write(gprsLine + "\n");
                    continue;
                }
                bw.write(line + "\n");
            }
            br.close();
            bw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AppFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reloadAllTables(String which) {
        System.out.println(which);
        File dir = new File("files/" + which);
        Vector titles = new Vector();
        titles.add("信息列表");
        titles.add("数据项");
        for (File f : dir.listFiles()) {
            if (f.getName().equalsIgnoreCase(AppConfig.DEVICE_FILENAME)) {
                Vector data = parseFile(f);
                DefaultTableModel model = new DefaultTableModel(data, titles);
                jtblDevice.setModel(model);
                jtblDevice.updateUI();
            } else if (f.getName().equalsIgnoreCase(AppConfig.SMS_FILENAME)) {
                Vector data = parseFile(f);
                DefaultTableModel model = new DefaultTableModel(data, titles);
                jtblSms.setModel(model);
                jtblSms.updateUI();
            } else if (f.getName().equalsIgnoreCase(AppConfig.CONTACTS_FILENAME)) {
                Vector data = parseFile(f);
                DefaultTableModel model = new DefaultTableModel(data, titles);
                jtblContacts.setModel(model);
                jtblContacts.updateUI();
            } else if (f.getName().equalsIgnoreCase(AppConfig.CONTACTS_HISTORY_FILENAME)) {
                Vector data = parseFile(f);
                DefaultTableModel model = new DefaultTableModel(data, titles);
                jtblContactsHistory.setModel(model);
                jtblContactsHistory.updateUI();
            } else if (f.getName().equalsIgnoreCase(AppConfig.GPRS_FILENAME)) {
                Vector data = parseFile(f);
                gprsInfo = new GprsInfo();
//                System.out.println(data.toString());
                Vector v0 = (Vector) data.get(0);
                gprsInfo.setLatitude((String) v0.get(1));
                Vector v1 = (Vector) data.get(1);
                gprsInfo.setLongitude((String) v1.get(1));

//                System.out.println(gprsInfo.getLatitude());
//                System.out.println(gprsInfo.getLongitude());
                genMapFile();
            } else if (f.getName().equalsIgnoreCase(AppConfig.RECEIVED_SMS_FILENAME)) {
                Vector data = parseFile(f);
                DefaultTableModel model = new DefaultTableModel(data, titles);
                jtblReceivedSms.setModel(model);
                jtblReceivedSms.updateUI();
            } else if (f.getName().endsWith(".amr")) {
                Vector recTitles = new Vector();
                recTitles.add("时间");
                recTitles.add("录音文件");
                Vector data = new Vector<Vector>();
                Vector v1 = new Vector();
                long time = f.lastModified();
                String stime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(time));
                v1.add(stime); v1.add(f.getAbsoluteFile());
                data.add(v1);
                DefaultTableModel model = new DefaultTableModel(data, recTitles);
                jtblRecord.setModel(model);
                jtblRecord.updateUI();

            }
        }

    }

    private void startService() {
        server = new FileServer(appCfg.getPort(), appCfg.connNum);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.start();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuBar4 = new javax.swing.JMenuBar();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuBar5 = new javax.swing.JMenuBar();
        jMenu7 = new javax.swing.JMenu();
        jMenu8 = new javax.swing.JMenu();
        jToolBar1 = new javax.swing.JToolBar();
        jbtnToggleService = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jbtnConfig = new javax.swing.JButton();
        jbtnGenAPK = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jPanel6 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        deviceInfoSPane = new javax.swing.JScrollPane();
        jtblDevice = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtblSms = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtblContacts = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jtblContactsHistory = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jtblReceivedSms = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jtblRecord = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jlstPhoneList = new javax.swing.JList();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jMenuBar2 = new javax.swing.JMenuBar();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("File");
        jMenuBar3.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar3.add(jMenu4);

        jMenu5.setText("File");
        jMenuBar4.add(jMenu5);

        jMenu6.setText("Edit");
        jMenuBar4.add(jMenu6);

        jMenu7.setText("File");
        jMenuBar5.add(jMenu7);

        jMenu8.setText("Edit");
        jMenuBar5.add(jMenu8);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setRollover(true);
        jToolBar1.setBorderPainted(false);

        jbtnToggleService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/启动.png"))); // NOI18N
        jbtnToggleService.setText("开启服务");
        jbtnToggleService.setFocusable(false);
        jbtnToggleService.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnToggleService.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnToggleService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnToggleServiceActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtnToggleService);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/刷新.png"))); // NOI18N
        jButton1.setText("刷新设备");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/位置.png"))); // NOI18N
        jButton3.setText("显示位置");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jbtnConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/设置.png"))); // NOI18N
        jbtnConfig.setText("程序设置");
        jbtnConfig.setFocusable(false);
        jbtnConfig.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnConfig.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnConfigActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtnConfig);

        jbtnGenAPK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/生成.png"))); // NOI18N
        jbtnGenAPK.setText("生成APK");
        jbtnGenAPK.setFocusable(false);
        jbtnGenAPK.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnGenAPK.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnGenAPK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGenAPKActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtnGenAPK);
        jToolBar1.add(jSeparator1);

        deviceInfoSPane.setToolTipText("");

        jtblDevice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        deviceInfoSPane.setViewportView(jtblDevice);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(deviceInfoSPane, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(deviceInfoSPane, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("设备信息", jPanel2);

        jtblSms.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jtblSms);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("短信", jPanel3);

        jtblContacts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jtblContacts);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("联系人", jPanel4);

        jtblContactsHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(jtblContactsHistory);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("通话记录", jPanel1);

        jtblReceivedSms.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(jtblReceivedSms);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("接收短信", jPanel5);

        jtblRecord.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(jtblRecord);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("录音", jPanel8);

        jlstPhoneList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jlstPhoneListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jlstPhoneList);

        jLabel1.setText("日志记录");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2)))
        );

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnToggleServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnToggleServiceActionPerformed
        // TODO add your handling code here:

        startService();
        jbtnToggleService.setEnabled(false);
    }//GEN-LAST:event_jbtnToggleServiceActionPerformed

    private void jbtnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnConfigActionPerformed
        // TODO add your handling code here:
        if (appCfgDialog == null) {
            appCfgDialog = new AppConfigDialog(this, true, appCfg);
        }

        appCfgDialog.setLocationRelativeTo(this);
        appCfgDialog.setVisible(true);
        appCfgDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    }//GEN-LAST:event_jbtnConfigActionPerformed

    private void jlstPhoneListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jlstPhoneListValueChanged
        // TODO add your handling code here:
        // changed
        if (!evt.getValueIsAdjusting()) {
            String v = (String) ((JList) evt.getSource()).getSelectedValue();
            if (v== null) return;
            if (!v.isEmpty()) {
                reloadAllTables(v.trim());
            }
        }
    }//GEN-LAST:event_jlstPhoneListValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        reloadPhoneList();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        new WebViewTest().run();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jbtnGenAPKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGenAPKActionPerformed
        // TODO add your handling code here:
        // change android app ip/port
        
    }//GEN-LAST:event_jbtnGenAPKActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AppFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AppFrame().setVisible(true);
            }

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane deviceInfoSPane;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JMenuBar jMenuBar4;
    private javax.swing.JMenuBar jMenuBar5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton jbtnConfig;
    private javax.swing.JButton jbtnGenAPK;
    private javax.swing.JButton jbtnToggleService;
    private javax.swing.JList jlstPhoneList;
    private javax.swing.JTable jtblContacts;
    private javax.swing.JTable jtblContactsHistory;
    private javax.swing.JTable jtblDevice;
    private javax.swing.JTable jtblReceivedSms;
    private javax.swing.JTable jtblRecord;
    private javax.swing.JTable jtblSms;
    // End of variables declaration//GEN-END:variables

}
