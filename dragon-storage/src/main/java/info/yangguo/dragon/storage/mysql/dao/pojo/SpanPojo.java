package info.yangguo.dragon.storage.mysql.dao.pojo;

import java.io.Serializable;

/**
 * @author:杨果
 * @date:16/6/6 上午10:56
 *
 * Description:
 *
 */
public class SpanPojo implements Serializable {
    private static final long serialVersionUID = 8408051597651695553L;
    private String spanId;
    private String traceId;
    private String parentId;
    private long serviceId;

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "SpanPojo{" +
                "spanId='" + spanId + '\'' +
                ", traceId='" + traceId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", serviceId=" + serviceId +
                '}';
    }
}
