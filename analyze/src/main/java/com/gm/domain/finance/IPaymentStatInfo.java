package com.gm.domain.finance;

import com.gm.domain.IBaseInfo;

/**
 * @Description:
 * @Author: zhouj
 * @Date: 2019/7/8 10:27
 */
public interface IPaymentStatInfo extends IBaseInfo {
    String paymentId = "payment_id";
    String paymentName = "payment_name";
    String platformCode = "platform_code";
    String platformName = "platform_name";
    String type = "type";
    String dayType = "day";
    String time = "time";
    String unpaidType = "unpaid";
    String deposit = "deposit";
    String serviceCharge = "serviceCharge";
    String successCount = "successCount";
    String totalCount = "totalCount";
}
