package info.yangguo.dragon.storage.mysql.dao.impl;

import info.yangguo.dragon.storage.mysql.dao.SpanMapper;
import info.yangguo.dragon.storage.mysql.dao.pojo.SpanPojo;

import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

public class SpanMapperImpl implements SpanMapper {
    private SqlSessionTemplate sqlSession;

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public void addSpan(SpanPojo spanPojo) {
        sqlSession.insert("addSpan", spanPojo);
    }

    @Override
    public List<SpanPojo> getSpanByTraceId(String traceIds) {
        return sqlSession.selectList("getSpanByTraceId", traceIds);
    }

    @Override
    public void deleteSpan(String spanId) {
        sqlSession.delete("deleteSpanById", spanId);
    }
}
