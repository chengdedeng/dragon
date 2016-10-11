package info.yangguo.dragon.client;


import info.yangguo.dragon.common.dto.SpanDto;
import info.yangguo.dragon.common.service.TraceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author:杨果
 * @date:16/6/1 下午9:21
 *
 * Description:
 * 数据传输类,主要是将trace数据传输到Collecter
 */
public class Transfer {
    private static Logger logger = LoggerFactory.getLogger(Transfer.class);
    private ArrayBlockingQueue<SpanDto> queue;
    private List<SpanDto> spansCache;
    private TransferTask task;
    private TraceService traceService;

    public void setTraceService(TraceService traceService) {
        this.traceService = traceService;
    }

    public Transfer() {
        this.queue = new ArrayBlockingQueue<>(1024);
        this.spansCache = new ArrayList<>();
        this.task = new TransferTask();
    }

    private class TransferTask extends Thread {
        TransferTask() {
            this.setName("TransferTask");
        }

        @Override
        public void run() {
            while (true) {
                try {
                    SpanDto first = queue.take();
                    spansCache.add(first);
                    queue.drainTo(spansCache);
                    traceService.sendSpan(spansCache);
                } catch (Throwable e) {
                    logger.info("TraceService send span ignore,because of:{}", e.getMessage());
                } finally {
                    spansCache.clear();
                }
            }
        }
    }

    public void send(SpanDto spanDto) {
        try {
            queue.add(spanDto);
        } catch (Exception e) {
            logger.info("Span ignore,because of:{}", e.getMessage());
        }
    }

    public void start() throws Exception {
        if (traceService != null && !task.isAlive()) {
            task.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    cancel();
                }
            });
        } else if (traceService == null) {
            throw new Exception("TraceServie is null.can't starting Transfer");
        }
    }

    private void cancel() {
        task.interrupt();
    }
}
