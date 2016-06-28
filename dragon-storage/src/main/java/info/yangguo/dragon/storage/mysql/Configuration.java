package info.yangguo.dragon.storage.mysql;


public class Configuration {
    /**
     * 接收端队列长度
     */
    private int queueSize;
    /**
     * trace日志的生命周期,单位天
     */
    private long lifeTime;
    /**
     * 过期日志回收间隔时间,单位分钟
     */
    private Integer deleteInterval;

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public long getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(long lifeTime) {
        this.lifeTime = lifeTime;
    }

    public Integer getDeleteInterval() {
        return deleteInterval;
    }

    public void setDeleteInterval(Integer deleteInterval) {
        this.deleteInterval = deleteInterval;
    }
}
