package info.yangguo.dragon.manager.dto;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * @author:杨果
 * @date:16/6/8 下午4:29
 *
 * Description:
 *
 */
@ApiModel(value = "DragonTraceRequest", description = "调用链查询数据结构")
public class DragonTraceRequest {
    private int serviceId;//serviceId
    private long traceTime;//选定服务的追踪时间

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public long getTraceTime() {
        return traceTime;
    }

    public void setTraceTime(long traceTime) {
        this.traceTime = traceTime;
    }
}
