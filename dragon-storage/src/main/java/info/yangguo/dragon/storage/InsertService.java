package info.yangguo.dragon.storage;


import info.yangguo.dragon.common.dto.SpanDto;

import java.util.List;

/**
 * @author:杨果
 * @date:16/6/6 上午9:11
 *
 * Description:
 *
 */
public interface InsertService {
    void insert(List<SpanDto> spanDtos);
}
