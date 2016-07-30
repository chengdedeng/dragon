package info.yangguo.dragon.storage.mysql.dao;

import info.yangguo.dragon.storage.mysql.dao.pojo.TracePojo;

import java.util.List;

public interface TraceMapper {
    void addTrace(TracePojo tracePojo);

    List<String> getTraceId(long beginTime, int limit);

    List<String> getTraceIdByServiceId(int serviceId, long beginTime, long endTime);

    void deleteTrace(String traceId);
}
