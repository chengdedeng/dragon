package info.yangguo.dragon.storage.mysql.dao.pojo;

import java.io.Serializable;

/**
 * @author:杨果
 * @date:16/6/6 上午10:54
 *
 * Description:
 *
 */
public class AnnotationPojo implements Serializable {
    private static final long serialVersionUID = 8229331674571219289L;
    private String type;
    private String ip;
    private int port;
    private long time;
    private String spanId;
    private String value;
    private String app;
    private String traceId;

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
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

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    //主要用在去掉相同异常,特别是既是provider有事consumer的地方
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotationPojo that = (AnnotationPojo) o;

        if (!type.equals(that.type)) return false;
        if (!ip.equals(that.ip)) return false;
        if (!spanId.equals(that.spanId)) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return app.equals(that.app);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + ip.hashCode();
        result = 31 * result + spanId.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + app.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AnnotationPojo{" +
                "type='" + type + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", time=" + time +
                ", spanId='" + spanId + '\'' +
                ", value='" + value + '\'' +
                ", app='" + app + '\'' +
                '}';
    }
}
