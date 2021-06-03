package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Configuration
public class JacksonConfig implements ApplicationRunner {
	
	@Autowired
    ObjectMapper objectMapper;
	
	@Autowired
    RequestMappingHandlerAdapter adapter;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 允许返回体为Object.class
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
		// 设置日期格式
		objectMapper.setDateFormat(new CustomDateFormat());
		// 通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
		// Include.Include.ALWAYS 默认
		// Include.NON_DEFAULT 属性为默认值不序列化
		// Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化，则返回的json是没有这个字段的。这样对移动端会更省流量
		// Include.NON_NULL 属性为NULL 不序列化
		objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 允许出现特殊字符和转义符
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		// 允许出现单引号
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		// 设置反序列化时时间不变成时分秒
		objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
		// 字段保留，将null值转为""
		objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
			@Override
			public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException{
				String fieldName = jsonGenerator.getOutputContext().getCurrentName();
				try {
					Field field = getFieldWithClass(jsonGenerator.getCurrentValue().getClass(), fieldName);
					if(null != field) {
						Class<?> cls = field.getType();
						if(cls.isAssignableFrom(List.class) || cls.isArray()) {
							jsonGenerator.writeStartArray();
							jsonGenerator.writeEndArray();
						} else if(cls.isAssignableFrom(Map.class)){
							jsonGenerator.writeStartObject();
							jsonGenerator.writeEndObject();
						} else {
							jsonGenerator.writeString("");
						}
					} else {
						jsonGenerator.writeString("");
					}
				} catch (Exception e) {
					jsonGenerator.writeString("");
				}
			}
		});
		// 将返回适配器的objectmapper替换
		adapter.getMessageConverters().forEach(hmc -> {
			if(hmc instanceof MappingJackson2HttpMessageConverter) {
				((MappingJackson2HttpMessageConverter)hmc).setObjectMapper(objectMapper);
			}
		});
	}
	
	/**
	 * 	根据类名和字段名获取字段
	 * @param cls
	 * @param fieldName
	 * @return
	 * @throws Exception
	 * @see [类、类#方法、类#成员]
	 */
	private Field getFieldWithClass(Class<?> cls,String fieldName) throws Exception {
		Field field = null;
		LinkedList<Class<?>> clsList = new LinkedList<>();
		clsList.add(cls);
		while(!clsList.isEmpty()) {
			Class<?> rCls = clsList.removeFirst();
			Class<?> superCls = rCls.getSuperclass();
			if(!Object.class.equals(superCls)) {
				clsList.addLast(superCls);
			}
			field = rCls.getDeclaredField(fieldName);
			if(field != null) {
				break;
			}
		}
		return field;
	}
	
}
