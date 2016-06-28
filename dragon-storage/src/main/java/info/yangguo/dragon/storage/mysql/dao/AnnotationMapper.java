package info.yangguo.dragon.storage.mysql.dao;

import info.yangguo.dragon.storage.mysql.dao.pojo.AnnotationPojo;

import java.util.List;

public interface AnnotationMapper {
    void addAnnotation(AnnotationPojo annotationPojo);

    List<AnnotationPojo> getAnnotation(String spanId,String traceId);

    void deleteAnnotation(String spanId,String traceId);
}
