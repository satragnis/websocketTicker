package com.example.bitcointicker.Model;

import java.io.Serializable;

public class Block implements Serializable {
    String hash;
    long height;
    double reward,totalBTCSent;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public double getTotalBTCSent() {
        return totalBTCSent;
    }

    public void setTotalBTCSent(double totalBTCSent) {
        this.totalBTCSent = totalBTCSent;
    }
}
