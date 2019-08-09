package com.gm.domain.activity;

import java.math.BigDecimal;

public class ActivityStat {
    private String activityModelName;
    private BigDecimal amount;
    private BigDecimal prizeAmount;
    private String type;
    private Integer times;

    private ActivityStat(Builder builder){
        this.activityModelName = builder.activityModelName;
        this.times = builder.times;
        this.amount = builder.amount;
        this.prizeAmount = builder.prizeAmount;
        this.type = builder.type;
    }

    public ActivityStat(){
    }

    public String getActivityModelName() {
        return activityModelName;
    }

    public void setActivityModelName(String activityModelName) {
        this.activityModelName = activityModelName;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrizeAmount() {
        return prizeAmount;
    }

    public void setPrizeAmount(BigDecimal prizeAmount) {
        this.prizeAmount = prizeAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class Builder {
        private String activityModelName;
        private Integer times;
        private BigDecimal amount;
        private BigDecimal prizeAmount;
        private String type;

        public Builder() {

        }

        public Builder activityModelName(String activityModelName) {
            this.activityModelName = activityModelName;
            return this;
        }

        public Builder times(Integer times) {
            this.times = times;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder prizeAmount(BigDecimal prizeAmount) {
            this.prizeAmount = prizeAmount;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public ActivityStat build() {
            return new ActivityStat(this);
        }

    }
}
