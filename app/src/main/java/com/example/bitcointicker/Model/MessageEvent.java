package com.example.bitcointicker.Model;

public class MessageEvent {
    String eventType;
    Block block;
    Transaction transaction;
    String socketState;

    public MessageEvent(String eventType, Block block, Transaction transaction) {
        this.eventType = eventType;
        this.block = block;
        this.transaction = transaction;
    }

    public MessageEvent(String eventType, String socketState) {
        this.eventType = eventType;
        this.socketState = socketState;

    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getSocketState() {
        return socketState;
    }

    public void setSocketState(String socketState) {
        this.socketState = socketState;
    }
}
