package com.gm.domain.platform;


public class PlatformStat {
    private long betAmount;
    private long gainAmount;
    private Integer memberCount;

    private PlatformStat(Builder builder){
        this.memberCount = builder.memberCount;
        this.betAmount = builder.betAmount;
        this.gainAmount = builder.gainAmount;
    }

    public PlatformStat(){
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public long getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(long betAmount) {
        this.betAmount = betAmount;
    }

    public long getGainAmount() {
        return gainAmount;
    }

    public void setGainAmount(long gainAmount) {
        this.gainAmount = gainAmount;
    }

    public static class Builder {
        private Integer memberCount;
        private int gainAmount;
        private int betAmount;

        public Builder() {

        }

        public Builder memberCount(Integer memberCount) {
            this.memberCount = memberCount;
            return this;
        }

        public Builder gainAmount(int gainAmount) {
            this.gainAmount = gainAmount;
            return this;
        }

        public Builder betAmount(int betAmount) {
            this.betAmount = betAmount;
            return this;
        }

        public PlatformStat build() {
            return new PlatformStat(this);
        }

    }
}
