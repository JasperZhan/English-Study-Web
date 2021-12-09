package edu.hzu.englishstudyweb.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.zhenzi.sms.ZhenziSmsClient;
import edu.hzu.englishstudyweb.model.Code;
import edu.hzu.englishstudyweb.service.SmsService;
import edu.hzu.englishstudyweb.util.Result;
import edu.hzu.englishstudyweb.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Jasper Zhan
 * @date 2021年12月09日 20:31
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    private static final String NUMBERCHAR = "0123456789";

    private static String APP_ID;
    private static String APP_KEY;
    private static String TEMPLATE_ID;

    @Value("${spring.project.sms.apiUrl}")
    private String apiUrl;

    @Value("${spring.project.sms.timeOut}")
    private Integer timeOut;

    @Value("${spring.project.sms.appId}")
    public void setAppId(String appId) {
        APP_ID = appId;
    }

    @Value("${spring.project.sms.appSecret}")
    public void setAppKey(String appKey) {
        APP_KEY = appKey;
    }

    @Value("${spring.project.sms.template-id}")
    public void setTemplateId(String templateId) {
        TEMPLATE_ID = templateId;
    }

    @Override
    public Result sendCode(String tell) {

        String num = code(6);

        ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, APP_ID, APP_KEY);

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("number", tell);
        map.put("templateId", TEMPLATE_ID);

        // 添加模板参数
        String[] templateParams = new String[2];
        templateParams[0] = num;
        templateParams[1] = "3分钟";
        map.put("templateParams", templateParams);
        try {
            String result_sms = client.send(map);
            JSONObject json = JSONObject.parseObject(result_sms);
            System.out.println(json);
            if (json.getIntValue("code") != 0) {
                // TODO 处理服务端错误码
                log.error("验证码发送失败，手机号：{}，错误信息：{}", tell, "发送短信失败");
                return Result.failure(ResultCode.FAILURE);
                        //.setResultFailed("发送短信失败");
            } else {
                Code code = new Code();
                code.setCode(num);
                code.setCurrentTime(System.currentTimeMillis() / 1000);
                return Result.success(code);
            }
        } catch (Exception e) {
            String str;

            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            str = stringWriter.toString();
            log.error("验证码发送出现异常：{}", str);
            return Result.failure(ResultCode.FAILURE);
//            result.setResultFailed("发送短信失败");
        }
    }


    /**
     * 返回固定长度的数字
     */
    private static String code(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBERCHAR.charAt(random.nextInt(NUMBERCHAR.length())));
        }
        return sb.toString();
    }
}
