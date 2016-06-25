package info.yangguo.dragon.manager.vo;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author:杨果
 * @date:15/12/21 下午2:24
 * <p/>
 * Description:
 */
@ApiModel(value = "结果Vo", description = "REST请求结果")
public class ResultVo<T> {
    @ApiModelProperty(value = "结果代码")
    private int code;
    @ApiModelProperty(value = "有效的数据载体")
    private T value;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
