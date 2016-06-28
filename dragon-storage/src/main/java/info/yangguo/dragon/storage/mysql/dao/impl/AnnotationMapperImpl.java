package info.yangguo.dragon.storage.mysql.dao.impl;


import info.yangguo.dragon.storage.mysql.dao.AnnotationMapper;
import info.yangguo.dragon.storage.mysql.dao.pojo.AnnotationPojo;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationMapperImpl implements AnnotationMapper {
    private SqlSessionTemplate sqlSession;

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }


    @Override
    public void addAnnotation(AnnotationPojo annotationPojo) {
        sqlSession.insert("addAnnotation", annotationPojo);
    }

    @Override
    public List<AnnotationPojo> getAnnotation(String spanId,String traceId) {
        Map map=new HashMap();
        map.put("spanId",spanId);
        map.put("traceId",traceId);
        return sqlSession.selectList("getAnnotatin", map);
    }

    @Override
    public void deleteAnnotation(String spanId,String traceId) {
        Map map=new HashMap();
        map.put("spanId",spanId);
        map.put("traceId",traceId);
        sqlSession.delete("deleteAnnotationBySpanid", map);
    }
}
