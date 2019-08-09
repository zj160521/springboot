package com.gm.domain.finance;

import java.math.BigDecimal;

public class PaymentStatement {
    private String paymentName;//支付线路名
    private BigDecimal deposit;//存款总额
    private BigDecimal serviceCharge;//服务费总额
    private int successCount;//支付成功次数
    private int totalCount;//总提交支付次数

    private PaymentStatement(Builder builder){
        this.deposit = builder.deposit;
        this.paymentName = builder.paymentName;
        this.serviceCharge = builder.serviceCharge;
        this.successCount = builder.successCount;
        this.totalCount = builder.totalCount;
    }

    public PaymentStatement(){
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public static class Builder {
        private String paymentName;
        private BigDecimal deposit;
        private BigDecimal serviceCharge;
        private int successCount;
        private int totalCount;

        public Builder() {

        }

        public Builder deposit(BigDecimal deposit) {
            this.deposit = deposit;
            return this;
        }

        public Builder paymentName(String paymentName) {
            this.paymentName = paymentName;
            return this;
        }

        public Builder serviceCharge(BigDecimal serviceCharge) {
            this.serviceCharge = serviceCharge;
            return this;
        }

        public Builder successCount(int successCount) {
            this.successCount = successCount;
            return this;
        }

        public Builder totalCount(int totalCount) {
            this.totalCount = totalCount;
            return this;
        }

        public PaymentStatement build() {
            return new PaymentStatement(this);
        }

    }
}
