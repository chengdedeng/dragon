/*
 * Copyright jd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package info.yangguo.dragon.storage.mysql.dao.impl;

import info.yangguo.dragon.storage.mysql.dao.TraceMapper;
import info.yangguo.dragon.storage.mysql.dao.pojo.TracePojo;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.DuplicateKeyException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraceMapperImpl implements TraceMapper {
    private SqlSessionTemplate sqlSession;

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    public void addTrace(TracePojo tracePojo) {
        try {
            sqlSession.insert("addTrace", tracePojo);
        } catch (DuplicateKeyException e) {
        }
    }

    @Override
    public List<String> getTraceId(long beginTime, int limit) {
        Map map = new HashMap();
        map.put("beginTime", beginTime);
        map.put("limit", limit);
        return sqlSession.selectList("getTraceId", map);
    }


    @Override
    public List<String> getTraceIdByServiceId(int serviceId, long beginTime, int offset, int limit) {
        Map map = new HashMap();
        map.put("serviceId", serviceId);
        map.put("beginTime", beginTime);
        map.put("offset", offset);
        map.put("limit", limit);
        return sqlSession.selectList("getTraceIdByServiceId", map);
    }

    @Override
    public void deleteTrace(String traceId) {
        sqlSession.delete("deleteTraceById", traceId);
    }
}
