package com.gm.domain.finance;

import java.math.BigDecimal;

public class MemberStatement {
    private BigDecimal deposit;
    private BigDecimal drawMoney;
    private BigDecimal bonus;
    private BigDecimal discounts;

    private MemberStatement(Builder builder){
        this.deposit = builder.deposit;
        this.drawMoney = builder.drawMoney;
        this.bonus = builder.bonus;
        this.discounts = builder.discounts;
    }

    public MemberStatement(){
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getDrawMoney() {
        return drawMoney;
    }

    public void setDrawMoney(BigDecimal drawMoney) {
        this.drawMoney = drawMoney;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getDiscounts() {
        return discounts;
    }

    public void setDiscounts(BigDecimal discounts) {
        this.discounts = discounts;
    }

    public static class Builder {
        private BigDecimal deposit;
        private BigDecimal drawMoney;
        private BigDecimal bonus;
        private BigDecimal discounts;

        public Builder() {

        }

        public Builder deposit(BigDecimal deposit) {
            this.deposit = deposit;
            return this;
        }

        public Builder drawMoney(BigDecimal drawMoney) {
            this.drawMoney = drawMoney;
            return this;
        }

        public Builder bonus(BigDecimal bonus) {
            this.bonus = bonus;
            return this;
        }

        public Builder discounts(BigDecimal discounts) {
            this.discounts = discounts;
            return this;
        }

        public MemberStatement build() {
            return new MemberStatement(this);
        }

    }
}
