package com.ads.adserver.entities;

/**
 * Entity to handle LastID Database fields.
 */
public class LastId {
    int lastadid;
    int lastclickid;
    int lastimpressionid;

    public int getLastadid() {
        return lastadid;
    }

    public void setLastadid(int lastadid) {
        this.lastadid = lastadid;
    }

    public int getLastclickid() {
        return lastclickid;
    }

    public void setLastclickid(int lastclickid) {
        this.lastclickid = lastclickid;
    }

    public int getLastimpressionid() {
        return lastimpressionid;
    }

    public void setLastimpressionid(int lastimpressionid) {
        this.lastimpressionid = lastimpressionid;
    }
}
