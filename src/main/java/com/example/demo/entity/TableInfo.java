package com.example.demo.entity;

import lombok.Data;

/**
 * @description:
 * @author: LiuZhiJun
 * @create: 2020-08-31 17:11
 **/
@Data
public class TableInfo {
    private String tableName;
    private String tableComment;

    @Override
    public String toString() {
        return "TableInfo{" +
                "tableName='" + tableName + '\'' +
                ", tableComment='" + tableComment + '\'' +
                '}';
    }
}
