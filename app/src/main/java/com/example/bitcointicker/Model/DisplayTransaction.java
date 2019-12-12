package com.example.bitcointicker.Model;

public class DisplayTransaction {
    Long time;
    Long amount;
    String hash;

    public DisplayTransaction(Long time, Long amount, String hash) {
        this.time = time;
        this.amount = amount;
        this.hash = hash;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
