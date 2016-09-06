package info.yangguo.dragon.manager.controller;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import info.yangguo.dragon.manager.config.ResultCode;
import info.yangguo.dragon.manager.vo.ResultVo;
import info.yangguo.dragon.storage.mysql.dao.ServiceMapper;
import info.yangguo.dragon.storage.mysql.dao.pojo.ServicePojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author:杨果
 * @date:16/6/7 下午2:14
 *
 * Description:
 *
 */
@Controller
@RequestMapping("service")
@Api(value = "service", description = "service api")
public class ServiceController {
    private static Logger logger = LoggerFactory.getLogger(ServiceController.class);
    @Autowired
    private ServiceMapper serviceMapper;
    LoadingCache<String, List<ServicePojo>> cache = CacheBuilder.newBuilder()
            .maximumSize(200)
            .expireAfterWrite(10, TimeUnit.MINUTES).
                    build(
                            new CacheLoader<String, List<ServicePojo>>() {
                                public List<ServicePojo> load(String prefix) {
                                    return serviceMapper.searchServiceByPrefix(prefix);
                                }
                            }

                    );


    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功")})
    @ApiOperation(value = "getServiceByOffset", notes = "分页获取所有service,第一页page为1")
    @RequestMapping(value = "getServiceByOffset/{page}/{limit}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultVo<List<ServicePojo>> getServiceByOffset(@PathVariable("page") int page, @PathVariable("limit") int limit) {
        ResultVo resultVo = new ResultVo();
        try {
            int offset = (page - 1) * limit;
            resultVo.setValue(serviceMapper.getServiceByOffset(offset, limit));
            resultVo.setCode(ResultCode.C200.getCode());
        } catch (Exception e) {
            resultVo.setCode(ResultCode.C500.getCode());
        }
        return resultVo;
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功")})
    @ApiOperation(value = "searchService", notes = "搜索服务")
    @RequestMapping(value = "searchService", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultVo<List<ServicePojo>> searchServiceByPrefix(@RequestParam String prefix) {
        ResultVo resultVo = new ResultVo();
        try {
            List<ServicePojo> list = cache.get(prefix);
            resultVo.setValue(list);
            resultVo.setCode(ResultCode.C200.getCode());
        } catch (Exception e) {
            resultVo.setCode(ResultCode.C500.getCode());
        }
        return resultVo;
    }
}
