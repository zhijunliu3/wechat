package com.example.demo.entity;

import lombok.Data;

/**
 * @description:
 * @author: LiuZhiJun
 * @create: 2020-09-01 15:05
 **/
@Data
public class FieldInfo {
    private String name;
    private String initialUppercaseName;
    private String type;
    private String label;
    private String dataType;
    private String columnName;

    @Override
    public String toString() {
        return "FieldInfo{" +
                "name='" + name + '\'' +
                ", initialUppercaseName='" + initialUppercaseName + '\'' +
                ", type='" + type + '\'' +
                ", label='" + label + '\'' +
                ", dataType='" + dataType + '\'' +
                ", columnName='" + columnName + '\'' +
                '}';
    }
}
