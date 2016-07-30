package info.yangguo.dragon.manager.controller;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import info.yangguo.dragon.manager.config.ResultCode;
import info.yangguo.dragon.manager.dto.DragonTraceRequest;
import info.yangguo.dragon.manager.vo.NodeVo;
import info.yangguo.dragon.manager.vo.ResultVo;
import info.yangguo.dragon.manager.vo.TraceVo;
import info.yangguo.dragon.storage.mysql.PropertiesUtil;
import info.yangguo.dragon.storage.mysql.dao.AnnotationMapper;
import info.yangguo.dragon.storage.mysql.dao.ServiceMapper;
import info.yangguo.dragon.storage.mysql.dao.SpanMapper;
import info.yangguo.dragon.storage.mysql.dao.TraceMapper;
import info.yangguo.dragon.storage.mysql.dao.pojo.AnnotationPojo;
import info.yangguo.dragon.storage.mysql.dao.pojo.ServicePojo;
import info.yangguo.dragon.storage.mysql.dao.pojo.SpanPojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:杨果
 * @date:16/6/8 上午11:05
 *
 * Description:
 *
 */
@Controller
@RequestMapping("trace")
@Api(value = "trace", description = "trace api")
public class TraceController {
    private static Logger logger = LoggerFactory.getLogger(TraceController.class);
    private static int timeConfine = Integer.parseInt(PropertiesUtil.getProperty("dragon.properties").get("select.time.confine"));
    LoadingCache<String, TraceVo> cache = CacheBuilder.newBuilder()
            .maximumSize(100000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, TraceVo>() {
                        public TraceVo load(String key) {
                            String[] tmpKeys = key.split("-");
                            return getTraceVo(Integer.parseInt(tmpKeys[0]), tmpKeys[1]);
                        }
                    });
    private Map<Long, String> serviceMap = new ConcurrentHashMap<>();
    @Autowired
    private AnnotationMapper annotationMapper;
    @Autowired
    private ServiceMapper serviceMapper;
    @Autowired
    private SpanMapper spanMapper;
    @Autowired
    private TraceMapper traceMapper;


    private String getServiceName(long serviceId) {
        String serviceName = serviceMap.get(serviceId);
        if (serviceName == null) {
            ServicePojo servicePojo = serviceMapper.getServiceById(serviceId);
            if (servicePojo != null) {
                serviceName = servicePojo.getServiceName();
            }
        }
        return serviceName;
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功")})
    @ApiOperation(value = "getTraces", notes = "分页获取所有service")
    @RequestMapping(value = "getTraces", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultVo<List<TraceVo>> getDefaultTraces(@RequestBody DragonTraceRequest dragonTraceRequest) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(ResultCode.C200.getCode());
        List<TraceVo> traceVos = new ArrayList<>();
        resultVo.setValue(traceVos);
        List<String> traceIds = traceMapper.getTraceIdByServiceId(dragonTraceRequest.getServiceId(), dragonTraceRequest.getTraceTime(), dragonTraceRequest.getTraceTime() + timeConfine);
        for (String traceId : traceIds) {
            try {
                TraceVo traceVo = cache.get(dragonTraceRequest.getServiceId() + "-" + traceId);
                TraceVo tmp = new TraceVo();
                tmp.setTraceId(traceVo.getTraceId());
                tmp.setTraceTime(traceVo.getTraceTime());
                tmp.setChooseTime(traceVo.getChooseTime());
                tmp.setTotalTime(traceVo.getTotalTime());
                tmp.setExc(traceVo.isExc());
                traceVos.add(tmp);
            } catch (Exception e) {
                logger.debug("traceId={} metadata is broken", traceId);
            }
        }
        return resultVo;
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功")})
    @ApiOperation(value = "getTraceById", notes = "根据traceId获取调用链信息")
    @RequestMapping(value = "getTrace/{serviceId}/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultVo<TraceVo> getTraceById(@PathVariable("serviceId") String serviceId, @PathVariable("id") String id) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(ResultCode.C200.getCode());
        TraceVo traceVo = null;
        try {
            traceVo = cache.get(serviceId + "-" + id);
        } catch (ExecutionException e) {
        }
        resultVo.setValue(traceVo);
        return resultVo;
    }

    private TraceVo getTraceVo(int serviceId, String traceId) {
        List<SpanPojo> spanPojos = spanMapper.getSpanByTraceId(traceId);
        Map<String, NodeVo> nodeVoMap = new HashMap<>();
        NodeVo root = new NodeVo();
        TraceVo traceVo = new TraceVo();
        //构造树
        traceVo.setTraceId(traceId);
        for (SpanPojo spanPojo : spanPojos) {
            NodeVo nodeVo = new NodeVo();
            nodeVo.setPid(spanPojo.getParentId());
            nodeVo.setId(spanPojo.getSpanId());
            nodeVoMap.put(spanPojo.getSpanId(), nodeVo);
        }
        for (Map.Entry<String, NodeVo> entry : nodeVoMap.entrySet()) {
            NodeVo nodeVo = entry.getValue();
            String pid = nodeVo.getPid();
            if (pid != null) {
                nodeVoMap.get(pid).addChild(nodeVo);
                nodeVo.setpNodeVo(nodeVoMap.get(pid));
            } else {
                root.addChild(nodeVo);
                nodeVo.setpNodeVo(root);
            }
        }

        //迭代span,完善树的属性
        for (SpanPojo spanPojo : spanPojos) {
            String pid = spanPojo.getParentId();
            String id = spanPojo.getSpanId();
            AnnotationTypeMap annotationTypeMap = new AnnotationTypeMap();
            //将annotation结构化
            List<AnnotationPojo> annotationPojos = annotationMapper.getAnnotation(id, traceId);
            for (AnnotationPojo annotationPojo : annotationPojos) {
                if (annotationPojo.getType().equals("cs")) {
                    annotationTypeMap.CSAnnotationPojo = annotationPojo;
                }
                if (annotationPojo.getType().equals("sr")) {
                    annotationTypeMap.SRAnnotationPojo = annotationPojo;
                }
                if (annotationPojo.getType().equals("ss")) {
                    annotationTypeMap.SSAnnotationPojo = annotationPojo;
                }
                if (annotationPojo.getType().equals("cr")) {
                    annotationTypeMap.CRAnnotationPojo = annotationPojo;
                }
                if (annotationPojo.getType().equals("de") && annotationPojo.getPort() == 0) {
                    annotationTypeMap.CDEAnnotationPojo.add(annotationPojo);
                }
                if (annotationPojo.getType().equals("de") && annotationPojo.getPort() != 0) {
                    annotationTypeMap.SDEAnnotationPojo.add(annotationPojo);
                }
            }

            //调用链信息完善
            if (pid == null) {
                traceVo.setTraceTime(annotationTypeMap.CSAnnotationPojo.getTime());
                traceVo.setTotalTime(annotationTypeMap.CRAnnotationPojo.getTime() - annotationTypeMap.CSAnnotationPojo.getTime());
            }
            //设置网络耗时
            long requestNetworkTime = 0;
            long responseNetworkTime = 0;
            if (annotationTypeMap.SRAnnotationPojo != null && annotationTypeMap.CSAnnotationPojo != null) {
                requestNetworkTime = annotationTypeMap.SRAnnotationPojo.getTime() - annotationTypeMap.CSAnnotationPojo.getTime();
            }
            if (annotationTypeMap.CRAnnotationPojo != null && annotationTypeMap.SSAnnotationPojo != null) {
                responseNetworkTime = annotationTypeMap.CRAnnotationPojo.getTime() - annotationTypeMap.SSAnnotationPojo.getTime();
                if (responseNetworkTime < 0) {
                    for (AnnotationPojo tmp : annotationTypeMap.CDEAnnotationPojo) {
                        String error = tmp.getValue();
                        Pattern pattern = Pattern.compile("timeout\\=(\\d)");
                        Matcher matcher = pattern.matcher(error);
                        if (matcher.find()) {
                            String timeout = matcher.group(1);
                            responseNetworkTime = Long.parseLong(timeout);
                        }
                    }
                }
            }
            nodeVoMap.get(id).setNetworkTime(String.valueOf(requestNetworkTime) + "-" + String.valueOf(responseNetworkTime >= 0 ? responseNetworkTime : "?"));
            //设置invoke耗时
            if (annotationTypeMap.CRAnnotationPojo != null && annotationTypeMap.CSAnnotationPojo != null) {
                long invokeTime = annotationTypeMap.CRAnnotationPojo.getTime() - annotationTypeMap.CSAnnotationPojo.getTime();
                nodeVoMap.get(id).getpNodeVo().setInvokeTime(invokeTime);
                if (serviceId >= 0 && spanPojo.getServiceId() == serviceId) {
                    traceVo.setChooseTime(invokeTime);
                }
            }
            //client端异常
            HashSet<String> ces = new HashSet<>();
            for (AnnotationPojo annotationPojo : annotationTypeMap.CDEAnnotationPojo) {
                traceVo.setExc(true);
                ces.add(annotationPojo.getValue());
            }
            if (ces.size() > 0) {
                StringBuilder cde = new StringBuilder();
                int i = 0;
                for (String errorStr : ces) {
                    cde.append(errorStr);
                    if (i < ces.size() - 1) {
                        cde.append("\\n");
                    }
                    i++;
                }
                nodeVoMap.get(id).getpNodeVo().setExceptionValue(cde.toString());
            }


            //server端异常
            HashSet<String> ses = new HashSet<>();
            for (AnnotationPojo annotationPojo : annotationTypeMap.SDEAnnotationPojo) {
                traceVo.setExc(true);
                ses.add(annotationPojo.getValue());
            }
            if (ses.size() > 0) {
                StringBuilder sde = new StringBuilder();
                int i = 0;
                for (String errorStr : ses) {
                    sde.append(errorStr);
                    if (i < ses.size() - 1) {
                        sde.append("\\n");
                    }
                    i++;
                }
                nodeVoMap.get(id).setExceptionValue(sde.toString());
            }

            //app设置
            nodeVoMap.get(id).getpNodeVo().setAppName(annotationTypeMap.CSAnnotationPojo.getApp());
            nodeVoMap.get(id).setAppName(annotationTypeMap.SRAnnotationPojo.getApp());
            //service名称设置
            String serviceName = getServiceName(spanPojo.getServiceId());
            nodeVoMap.get(id).setServiceName(serviceName);
            //地址设置
            nodeVoMap.get(id).setAddress(String.valueOf(annotationTypeMap.SRAnnotationPojo.getIp() + ":" + annotationTypeMap.SRAnnotationPojo.getPort()));
            if (pid == null) {
                nodeVoMap.get(id).getpNodeVo().setAddress(annotationTypeMap.CSAnnotationPojo.getIp());
            }
        }
        traceVo.setNodeVo(root);
        cache.put(serviceId + "-" + traceId, traceVo);
        return traceVo;
    }

    private static class AnnotationTypeMap {
        public AnnotationPojo CSAnnotationPojo;
        public AnnotationPojo SRAnnotationPojo;
        public AnnotationPojo SSAnnotationPojo;
        public AnnotationPojo CRAnnotationPojo;
        public Set<AnnotationPojo> CDEAnnotationPojo = new HashSet<>();
        public Set<AnnotationPojo> SDEAnnotationPojo = new HashSet<>();
    }
}
