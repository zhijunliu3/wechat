package com.example.demo.entity;

import lombok.Data;

/**
 * @description:
 * @author: LiuZhiJun
 * @create: 2020-09-01 11:21
 **/
@Data
public class ColumnInfo {
    private String tableName;
    private String columnName;
    private String dataType;
    private String columnComment;
}
