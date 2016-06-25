package info.yangguo.dragon.common.service;

/**
 * @author:杨果
 * @date:16/6/14 下午6:25
 *
 * Description:
 *
 */
public interface RegisterService {
    void registerApp(String app);

    void registerService(int app, String ServiceName);
}
