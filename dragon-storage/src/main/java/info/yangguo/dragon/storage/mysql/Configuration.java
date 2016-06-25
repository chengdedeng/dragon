package info.yangguo.dragon.storage.mysql;


public class Configuration {
    /**
     * 接收端队列长度
     */
    private Integer queueSize;
    /**
     * trace日志的生命周期,单位天
     */
    private Integer lifeTime;
    /**
     * 过期日志回收间隔时间,单位分钟
     */
    private Integer deleteInterval;

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Integer getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(Integer lifeTime) {
        this.lifeTime = lifeTime;
    }

    public Integer getDeleteInterval() {
        return deleteInterval;
    }

    public void setDeleteInterval(Integer deleteInterval) {
        this.deleteInterval = deleteInterval;
    }
}
