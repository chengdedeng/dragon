package info.yangguo.dragon.storage.mysql.dao.pojo;

import java.io.Serializable;

/**
 * @author:杨果
 * @date:16/6/6 上午10:53
 *
 * Description:
 *
 */
public class ServicePojo implements Serializable {
    private static final long serialVersionUID = -7204023817375371870L;

    private Integer serviceId;
    private String serviceName;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "ServicePojo{" +
                "serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}
