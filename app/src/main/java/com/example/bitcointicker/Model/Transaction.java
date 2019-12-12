package com.example.bitcointicker.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Transaction {
    @SerializedName("x")
    @Expose
    private X x;

    /**
     * No args constructor for use in serialization
     *
     */
    public Transaction() {
    }

    /**
     *
     * @param x
     */
    public Transaction(X x) {
        super();
        this.x = x;
    }

    public X getX() {
        return x;
    }

    public void setX(X x) {
        this.x = x;
    }

    public static class X {

        @SerializedName("inputs")
        @Expose
        private List<Input> inputs = null;
        @SerializedName("time")
        @Expose
        private Long time;
        @SerializedName("hash")
        @Expose
        private String hash;

        /**
         * No args constructor for use in serialization
         *
         */
        public X() {
        }

        /**
         *
         * @param inputs
         * @param time
         * @param hash
         */
        public  X(List<Input> inputs, Long time, String hash) {
            super();
            this.inputs = inputs;
            this.time = time;
            this.hash = hash;
        }

        public List<Input> getInputs() {
            return inputs;
        }

        public void setInputs(List<Input> inputs) {
            this.inputs = inputs;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

    }


    public static class Input {

        @SerializedName("prev_out")
        @Expose
        private PrevOut prevOut;

        /**
         * No args constructor for use in serialization
         *
         */
        public Input() {
        }

        /**
         *
         * @param prevOut
         */
        public Input(PrevOut prevOut) {
            super();
            this.prevOut = prevOut;
        }

        public PrevOut getPrevOut() {
            return prevOut;
        }

        public void setPrevOut(PrevOut prevOut) {
            this.prevOut = prevOut;
        }

    }

    public static class PrevOut {

        @SerializedName("spent")
        @Expose
        private Boolean spent;
        @SerializedName("tx_index")
        @Expose
        private Integer txIndex;
        @SerializedName("type")
        @Expose
        private Integer type;
        @SerializedName("addr")
        @Expose
        private String addr;
        @SerializedName("value")
        @Expose
        private Long value;
        @SerializedName("n")
        @Expose
        private Integer n;
        @SerializedName("script")
        @Expose
        private String script;

        /**
         * No args constructor for use in serialization
         *
         */
        public PrevOut() {
        }

        /**
         *
         * @param spent
         * @param type
         * @param addr
         * @param value
         * @param txIndex
         * @param n
         * @param script
         */
        public  PrevOut(Boolean spent, Integer txIndex, Integer type, String addr, Long value, Integer n, String script) {
            super();
            this.spent = spent;
            this.txIndex = txIndex;
            this.type = type;
            this.addr = addr;
            this.value = value;
            this.n = n;
            this.script = script;
        }

        public Boolean getSpent() {
            return spent;
        }

        public void setSpent(Boolean spent) {
            this.spent = spent;
        }

        public Integer getTxIndex() {
            return txIndex;
        }

        public void setTxIndex(Integer txIndex) {
            this.txIndex = txIndex;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }

        public Integer getN() {
            return n;
        }

        public void setN(Integer n) {
            this.n = n;
        }

        public String getScript() {
            return script;
        }

        public void setScript(String script) {
            this.script = script;
        }

    }
}
