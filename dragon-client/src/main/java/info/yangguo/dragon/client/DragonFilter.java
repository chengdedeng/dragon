/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.yangguo.dragon.client;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;

import info.yangguo.dragon.common.dto.AnnotationDto;
import info.yangguo.dragon.common.dto.AnnotationType;
import info.yangguo.dragon.common.dto.SpanDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.Map;

/**
 * @author:杨果
 * @date:16/6/1 下午9:00
 *
 * Description:
 * Dubbo Filter扩展点实现,主要是利用AOP原理来获取各个时间点和执行状态
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class DragonFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(DragonFilter.class);
    public static final String TID = "TId";
    public static final String SID = "SId";
    public static final String PSID = "PSId";

    private static Tracer tracer = Tracer.getTracer();

    static {
        String resourceName = "classpath*:dragon.xml";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                resourceName
        });
        Transfer transfer = (Transfer) context.getBean("transfer");
        tracer.setTransfer(transfer);
        logger.info("Dragon config context is starting,config file path is:{}", resourceName);
        context.start();
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long start = System.currentTimeMillis();
        RpcContext context = RpcContext.getContext();
        boolean isConsumerSide = context.isConsumerSide();
        SpanDto spanDto = null;
        String ip = context.getLocalHost();
        int port = context.getLocalPort();
        String serviceName = context.getInvoker().getInterface().getCanonicalName() + "-" + context.getMethodName();
        String app = context.getUrl().getParameter("application");
        try {
            if (context.isConsumerSide()) {
                SpanDto currentSpanDto = tracer.getCurrentSpan();
                if (currentSpanDto == null) {
                    spanDto = tracer.genRootSpan(serviceName);
                } else {
                    spanDto = tracer.genSpan(currentSpanDto.getTraceId(), currentSpanDto.getSpanId(), Tracer.getId(), serviceName);
                }
            } else if (context.isProviderSide()) {
                String traceId, parentId, spanId;
                traceId = invocation.getAttachment(TID);
                parentId = invocation.getAttachment(PSID);
                spanId = invocation.getAttachment(SID);
                spanDto = tracer.genSpan(traceId, parentId, spanId, serviceName);
            }
            invokerBefore(invocation, spanDto, ip, port, start);
            RpcInvocation invocationTmp = (RpcInvocation) invocation;
            setAttachment(spanDto, invocationTmp);
            Result result = invoker.invoke(invocation);
            if (result.getException() != null) {
                catchException(result.getException(), ip, port, app);
            }
            return result;
        } catch (Exception e) {
            //主要是捕获网络异常
            catchException(e, ip, port, app);
            throw e;
        } finally {
            if (spanDto != null) {
                long end = System.currentTimeMillis();
                invokerAfter(invocation, ip, port, spanDto, end, isConsumerSide);
            }
        }
    }

    private void catchException(Throwable e, String ip, int port, String app) {
        AnnotationDto exAnnotationDto = new AnnotationDto();
        exAnnotationDto.setType(AnnotationType.DubboException);
        exAnnotationDto.setValue(e.getMessage());
        exAnnotationDto.setIp(ip);
        exAnnotationDto.setPort(port);
        exAnnotationDto.setApp(app);
        exAnnotationDto.setTime(new Date().getTime());
        tracer.exceptionRecord(exAnnotationDto);
    }

    private void setAttachment(SpanDto spanDto, RpcInvocation invocation) {
        invocation.setAttachment(PSID, spanDto.getParentId() != null ? String.valueOf(spanDto.getParentId()) : null);
        invocation.setAttachment(SID, spanDto.getSpanId() != null ? String.valueOf(spanDto.getSpanId()) : null);
        invocation.setAttachment(TID, spanDto.getTraceId() != null ? String.valueOf(spanDto.getTraceId()) : null);
    }

    private void invokerBefore(Invocation invocation, SpanDto spanDto, String ip, int port, long start) {
        MDC.put("traceId", spanDto.getTraceId());
        if (checkFilter(invocation)) {
            RpcContext context = RpcContext.getContext();
            String app = context.getUrl().getParameter("application");
            if (context.isConsumerSide()) {
                tracer.clientSendRecord(spanDto, ip, port, start, app);
            } else {
                tracer.serverReceiveRecord(spanDto, ip, port, start, app);
            }
        }
    }

    private void invokerAfter(Invocation invocation, String ip, int port, SpanDto spanDto, long end, boolean isConsumerSide) {
        if (checkFilter(invocation)) {
            RpcContext context = RpcContext.getContext();
            String app = context.getUrl().getParameter("application");
            if (isConsumerSide) {
                tracer.clientReceiveRecord(spanDto, ip, port, end, app);
            } else {
                tracer.serverSendRecord(spanDto, ip, port, end, app);
            }
        }
        MDC.remove("traceId");
    }


    private static boolean checkFilter(Invocation invocation) {
        String interfaceCanonicalName = invocation.getInvoker().getInterface().getCanonicalName();
        if ("com.alibaba.dubbo.monitor.MonitorService".equals(interfaceCanonicalName)) {
            return false;
        }
        return true;
    }
}