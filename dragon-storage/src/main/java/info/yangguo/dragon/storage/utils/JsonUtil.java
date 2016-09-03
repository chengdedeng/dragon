package info.yangguo.dragon.storage.utils;

import info.yangguo.dragon.common.dto.SpanDto;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.List;

/**
 * Json序列化的工具。使用Jackson作为序列化的library
 * <p/>
 * author: 杨果 date: 11-3-22 time: 下午2:06
 */
public class JsonUtil {
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, SpanDto.class);
    private static JsonFactory jsonFactory = new JsonFactory();

    /**
     * 将json发序列化为对象
     *
     * @param jsonAsString json字符串
     */
    public static <T> Object fromJson(String jsonAsString) {
        Object result = null;
        try {
            result = objectMapper.readValue(jsonAsString, javaType);
        } catch (Exception e) {
            logger.error("JSON[{}]反序列化失败", jsonAsString);
        }
        return result;
    }

    /**
     * 将对象转化为json
     *
     * @param pojo        需要序列化的对象
     * @param prettyPrint 是否打印优化，即换行
     * @return String 返回序列化的字符串
     */
    public static String toJson(Object pojo, boolean prettyPrint) {
        String result = null;
        try {
            StringWriter sw = new StringWriter();
            JsonGenerator jg = jsonFactory.createJsonGenerator(sw);
            if (prettyPrint) {
                jg.useDefaultPrettyPrinter();
            }
            objectMapper.writeValue(jg, pojo);
            result = sw.toString();
        } catch (Exception e) {
            logger.error("[{}]序列化成JSON失败", pojo);
        }
        return result;
    }
}
