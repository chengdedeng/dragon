package info.yangguo.dragon.storage.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author:杨果
 * @date:2016/10/28 上午10:22
 *
 * Description:
 *
 */
public class ScheduledService {
    @Autowired
    private DeleteLogicService deleteLogicService;

    @Scheduled(cron = "${delete.mysql.deleteInterval}")
    public void scheduled1() {
        deleteLogicService.process();
    }
}
