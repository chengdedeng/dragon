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
}
