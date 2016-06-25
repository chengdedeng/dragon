package info.yangguo.dragon.common.service;

import info.yangguo.dragon.common.dto.SpanDto;

import java.util.List;

/**
 * @author:杨果
 * @date:16/5/26 下午2:17
 *
 * Description:
 *
 */
public interface TraceService {
    void sendSpan(List<SpanDto> spanDtos);
}
