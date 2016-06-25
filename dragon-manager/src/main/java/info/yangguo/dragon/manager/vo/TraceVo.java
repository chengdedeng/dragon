package info.yangguo.dragon.manager.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author:杨果
 * @date:16/6/8 上午11:56
 *
 * Description:
 *
 */
@ApiModel(value = "TraceVo", description = "Trace数据结构")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TraceVo {
    @ApiModelProperty("调用链追踪的全局id")
    private String traceId;
    @ApiModelProperty("调用链追踪的开始时间")
    private long traceTime;
    @ApiModelProperty("完整调用链耗时")
    private long totalTime;
    @ApiModelProperty("选定服务的耗时")
    private long chooseTime;
    @ApiModelProperty("调用链中根节点")
    private NodeVo nodeVo;
    @ApiModelProperty("调用链中是否有异常发生")
    private boolean exc;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }


    public long getTraceTime() {
        return traceTime;
    }

    public void setTraceTime(long traceTime) {
        this.traceTime = traceTime;
    }


    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getChooseTime() {
        return chooseTime;
    }

    public void setChooseTime(long chooseTime) {
        this.chooseTime = chooseTime;
    }


    public NodeVo getNodeVo() {
        return nodeVo;
    }

    public void setNodeVo(NodeVo nodeVo) {
        this.nodeVo = nodeVo;
    }


    public boolean isExc() {
        return exc;
    }

    public void setExc(boolean exc) {
        this.exc = exc;
    }
}
