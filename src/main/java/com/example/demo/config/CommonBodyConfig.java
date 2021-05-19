package com.example.demo.config;

import com.example.demo.common.entity.Result;
import com.example.demo.controller.WeChatController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author liuzj
 * @create 2021/5/12 18:40
 **/
@ControllerAdvice
public class CommonBodyConfig implements ResponseBodyAdvice<Object> {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if(methodParameter.getMethod().getDeclaringClass().isAssignableFrom(WeChatController.class) && "check".equals(methodParameter.getMethod().getName())){
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return new Result<>();
        }
        if (String.class.isAssignableFrom(body.getClass())) {
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return objectMapper.writeValueAsString(new Result<>().setData(body));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!Result.class.isAssignableFrom(body.getClass())) {
            return new Result<>().setData(body);
        } else {
            return body;
        }
    }
}
