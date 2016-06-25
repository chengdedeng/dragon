package info.yangguo.dragon.storage.mysql.dao.pojo;

import java.io.Serializable;

public class TracePojo implements Serializable {
    private static final long serialVersionUID = 535352885272916500L;
    private String traceId;
    private int serviceId;
    private long time;


    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TracePojo that = (TracePojo) o;

        if (serviceId != that.serviceId) return false;
        if (time != that.time) return false;
        return traceId.equals(that.traceId);

    }

    @Override
    public int hashCode() {
        int result = traceId.hashCode();
        result = 31 * result + serviceId;
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "TracePojo{" +
                "traceId='" + traceId + '\'' +
                ", serviceId=" + serviceId +
                ", time=" + time +
                '}';
    }
}
