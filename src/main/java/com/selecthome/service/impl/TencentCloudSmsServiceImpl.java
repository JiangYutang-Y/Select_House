package com.selecthome.service.impl;

import com.selecthome.base.BusinessException;
import com.selecthome.base.Status;
import com.selecthome.base.TencentSmsProperties;
import com.selecthome.service.SmsService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "app.sms", name = "provider", havingValue = "tencent")
public class TencentCloudSmsServiceImpl implements SmsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TencentCloudSmsServiceImpl.class);

    private final TencentSmsProperties smsProperties;

    public TencentCloudSmsServiceImpl(TencentSmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    @Override
    public void sendSms(String phoneNumber, String code) {
        try {
            Credential cred = new Credential(smsProperties.getSecretId(), smsProperties.getSecretKey());
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {phoneNumber};
            req.setPhoneNumberSet(phoneNumberSet1);
            req.setSmsSdkAppId(smsProperties.getSdkAppId());
            req.setSignName(smsProperties.getSign());
            req.setTemplateId(smsProperties.getTemplateId());
            String[] templateParamSet1 = {code};
            req.setTemplateParamSet(templateParamSet1);
            SendSmsResponse resp = client.SendSms(req);
            LOGGER.info(SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            LOGGER.error("Error occurred while sending SMS code: ", e);
            throw new BusinessException(Status.UNKNOWN);
        }
    }
}
