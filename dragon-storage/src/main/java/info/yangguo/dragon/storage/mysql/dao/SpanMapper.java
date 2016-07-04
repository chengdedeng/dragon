package info.yangguo.dragon.storage.mysql.dao;


import info.yangguo.dragon.storage.mysql.dao.pojo.SpanPojo;

import java.util.List;

public interface SpanMapper {
    void addSpan(SpanPojo spanPojo);

    List<SpanPojo> getSpanByTraceId(String traceId);

    void deleteSpan(String spanId,String traceId);
}
