package com.example.demo.controller;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzj
 * @create 2021/5/12 14:13
 **/
@RestController
@RequestMapping("/wechat")
public class WeChatController {

    @Autowired
    WxMpService wxMpService;

    @GetMapping("/check")
    public String check(String signature, String timestamp,String nonce, String echostr){
        if(wxMpService.checkSignature(timestamp, nonce, signature)){
            return echostr;
        }else{
            throw new RuntimeException("验证错误");
        }
    }

    @GetMapping("/test")
    public void test(){
        try {
            wxMpService.getAccessToken();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }


}
