package info.yangguo.dragon.manager.config;

/**
 * @author:杨果
 * @date:16/6/7 上午10:15
 *
 * Description:
 *
 * REST接口返回的结果状态码,这些结果状态码参照HTTP协议
 */
public enum ResultCode {
    C200(200, "Success"), C403(403, "Forbidden"), C500(500, "Internal Server Error");

    ResultCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int code;
    private String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
