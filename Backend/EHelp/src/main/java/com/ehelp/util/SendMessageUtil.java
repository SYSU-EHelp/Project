package com.ehelp.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ehelp.dao.EmergencyDaoImpl;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class SendMessageUtil {
	
	private final static String url = "http://gw.api.taobao.com/router/rest";
	private final static String appkey = "23773560";
	private final static String secret = "72271da5a79b24c4922f90cc2deb82ea";
	
	//发送验证码
	public static String send(String phone, String code) throws ApiException {
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("");
		req.setSmsType("normal");
		req.setSmsFreeSignName("中大EHelp");
		req.setSmsParamString("{\"number\":\"" + code + "\"}");
		req.setRecNum(phone);
		req.setSmsTemplateCode("SMS_63295118");
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		JSONObject json = JSON.parseObject(rsp.getBody());
		System.out.println(json);
		String jsonStr = json.getString("alibaba_aliqin_fc_sms_num_send_response");    
        if (jsonStr != null && !jsonStr.isEmpty()) {    
            json = JSON.parseObject(jsonStr);    
            String result = json.getString("result");    
            if (result != null && !result.isEmpty()) {    
                json = JSON.parseObject(result);    
                System.out.println("json:"+json);  
                String errorCode = json.getString("err_code");    
                if ("0".equals(errorCode)) {  
                    //发送成功    
                    return "success";  
                } else {  
                    //发送失败  
                    return "fail";  
                }    
            }    
        }  
		return "fail";
	}
	
	//发送紧急短信
	public static void send2(String name, List<String> phoneList) throws ApiException {
        for(String phone : phoneList) {
        	TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
    		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
    		req.setExtend("");
    		req.setSmsType("normal");
    		req.setSmsFreeSignName("中大EHelp");
    		req.setSmsParamString("{\"name\":\"" + name + "\"}");
    		req.setRecNum(phone);
    		req.setSmsTemplateCode("SMS_67140741");
    		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
    		System.out.println(rsp.getBody());
        }
	}
	
	public static void main(String[] args) throws ApiException {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("国丹", "18819253762");
//		map.put("SB子凌", "18819253697");
//		send2(map);
	}
	
}

