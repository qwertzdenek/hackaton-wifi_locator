package com.rychlici.wifilocator.com.rychlici.wifilocator.common;

public class Scan {
    private String bssid;
    private int signalLoss;
    private long time;
    private int channel;

    public Scan(String bssid, int signalLoss, long time) {
        this.bssid = bssid;
        this.signalLoss = signalLoss;
        this.time = time;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public int getSignalLoss() {
        return signalLoss;
    }

    public void setSignalLoss(int signalLoss) {
        this.signalLoss = signalLoss;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
