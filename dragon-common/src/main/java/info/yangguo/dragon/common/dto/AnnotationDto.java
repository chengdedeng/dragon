package info.yangguo.dragon.common.dto;

import java.io.Serializable;

public class AnnotationDto implements Serializable {
    private static final long serialVersionUID = 1840245774768176307L;
    private String type;
    private String ip;
    private int port;
    private Long time;
    private String value;
    private String app;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "AnnotationDto{" +
                "type='" + type + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", time=" + time +
                ", value='" + value + '\'' +
                ", app='" + app + '\'' +
                '}';
    }
}
