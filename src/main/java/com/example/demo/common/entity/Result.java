package com.example.demo.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liuzj
 * @create 2021/5/12 18:39
 **/

@Data
@Accessors(chain = true)
public class Result<T> {
    private int code = 0;
    private T data;
    private String msg;
    private String cause;
}
