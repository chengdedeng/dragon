package info.yangguo.dragon.storage.mysql.dao.impl;

import info.yangguo.dragon.storage.mysql.dao.ServiceMapper;
import info.yangguo.dragon.storage.mysql.dao.pojo.ServicePojo;

import org.mybatis.spring.SqlSessionTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceMapperImpl implements ServiceMapper {
    private SqlSessionTemplate sqlSession;

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Integer addService(ServicePojo servicePojo) {
        sqlSession.insert("addService", servicePojo);
        return servicePojo.getServiceId();
    }

    @Override
    public ServicePojo getServiceByName(String name) {
        ServicePojo servicePojo = new ServicePojo();
        servicePojo.setServiceName(name);
        return (ServicePojo) sqlSession.selectOne("getServiceByName", servicePojo);
    }

    @Override
    public List<ServicePojo> getServiceByOffset(int offset, int limit) {
        Map map = new HashMap();
        map.put("offset", offset);
        map.put("limit", limit);
        return sqlSession.selectList("getServiceByOffset", map);
    }

    @Override
    public ServicePojo getServiceById(long serviceId) {
        return sqlSession.selectOne("getServiceById", serviceId);
    }
}
