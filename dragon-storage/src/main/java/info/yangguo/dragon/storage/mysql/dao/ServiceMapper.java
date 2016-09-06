package info.yangguo.dragon.storage.mysql.dao;


import info.yangguo.dragon.storage.mysql.dao.pojo.ServicePojo;

import java.util.List;

public interface ServiceMapper {
    Integer addService(ServicePojo servicePojo);

    ServicePojo getServiceByName(String name);

    List<ServicePojo> getServiceByOffset(int offset, int limit);

    ServicePojo getServiceById(long serviceId);

    List<ServicePojo> searchServiceByPrefix(String servicePrefix);
}
