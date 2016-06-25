package info.yangguo.dragon.manager.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.HashSet;
import java.util.Set;

/**
 * @author:杨果
 * @date:16/6/8 上午11:56
 *
 * Description:
 *
 */
@ApiModel(value = "NodeVo", description = "树节点数据结构")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class NodeVo {
    @JsonIgnore
    private String pid;
    @JsonIgnore
    private String id;
    @ApiModelProperty("项目名称")
    private String appName;
    @ApiModelProperty("服务的名称")
    private String serviceName;
    @ApiModelProperty("节点ip:端口")
    private String address;
    @ApiModelProperty("方法执行时间")
    private long invokeTime;
    @ApiModelProperty("网络耗时")
    private String networkTime;
    @ApiModelProperty("异常信息")
    private String exceptionValue;
    @ApiModelProperty("子节点")
    private Set<NodeVo> children = new HashSet<>();
    @JsonIgnore
    private NodeVo pNodeVo;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getInvokeTime() {
        return invokeTime;
    }

    public void setInvokeTime(long invokeTime) {
        this.invokeTime = invokeTime;
    }

    public String getNetworkTime() {
        return networkTime;
    }

    public void setNetworkTime(String networkTime) {
        this.networkTime = networkTime;
    }

    public String getExceptionValue() {
        return exceptionValue;
    }

    public void setExceptionValue(String exceptionValue) {
        this.exceptionValue = exceptionValue;
    }

    public Set<NodeVo> getChildren() {
        return children;
    }

    public void setChildren(Set<NodeVo> children) {
        this.children = children;
    }

    public void addChild(NodeVo nodeVo) {
        children.add(nodeVo);
    }

    public NodeVo getpNodeVo() {
        return pNodeVo;
    }

    public void setpNodeVo(NodeVo pNodeVo) {
        this.pNodeVo = pNodeVo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeVo nodeVo = (NodeVo) o;

        if (invokeTime != nodeVo.invokeTime) return false;
        if (pid != null ? !pid.equals(nodeVo.pid) : nodeVo.pid != null) return false;
        if (!id.equals(nodeVo.id)) return false;
        if (appName != null ? !appName.equals(nodeVo.appName) : nodeVo.appName != null)
            return false;
        if (serviceName != null ? !serviceName.equals(nodeVo.serviceName) : nodeVo.serviceName != null)
            return false;
        if (address != null ? !address.equals(nodeVo.address) : nodeVo.address != null)
            return false;
        if (networkTime != null ? !networkTime.equals(nodeVo.networkTime) : nodeVo.networkTime != null)
            return false;
        if (exceptionValue != null ? !exceptionValue.equals(nodeVo.exceptionValue) : nodeVo.exceptionValue != null)
            return false;
        return children != null ? children.equals(nodeVo.children) : nodeVo.children == null;

    }

    @Override
    public int hashCode() {
        int result = pid != null ? pid.hashCode() : 0;
        result = 31 * result + id.hashCode();
        result = 31 * result + (appName != null ? appName.hashCode() : 0);
        result = 31 * result + (serviceName != null ? serviceName.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (int) (invokeTime ^ (invokeTime >>> 32));
        result = 31 * result + (networkTime != null ? networkTime.hashCode() : 0);
        result = 31 * result + (exceptionValue != null ? exceptionValue.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }
}
