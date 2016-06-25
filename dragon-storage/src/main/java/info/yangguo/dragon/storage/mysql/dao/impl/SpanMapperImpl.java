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

import info.yangguo.dragon.storage.mysql.dao.SpanMapper;
import info.yangguo.dragon.storage.mysql.dao.pojo.SpanPojo;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
