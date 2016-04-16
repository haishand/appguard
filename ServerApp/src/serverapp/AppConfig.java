/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;

/**
 *
 * @author haishand
 */
public class AppConfig {

    public static final int DEFAULT_PORT = 6666;
    public static final int DEFAULT_CONN_NUM = 10;

    public static final String RECEIVED_SMS_FILENAME = "received_sms";
    public static final String DEVICE_FILENAME = "device";
    public static final String SMS_FILENAME = "sms";
    public static final String CONTACTS_FILENAME = "contacts";
    public static final String CONTACTS_HISTORY_FILENAME = "contacts_history";
    public static final String GPRS_FILENAME = "gprs";
    int port;
    int connNum;

    public AppConfig() {
        this.port = DEFAULT_PORT;
        this.connNum = DEFAULT_CONN_NUM;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnNum() {
        return connNum;
    }

    public void setConnNum(int connNum) {
        this.connNum = connNum;
    }

}
