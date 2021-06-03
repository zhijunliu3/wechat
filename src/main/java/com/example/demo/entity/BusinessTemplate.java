package com.example.demo.entity;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: LiuZhiJun
 * @create: 2020-09-02 18:18
 **/
@Data
public class BusinessTemplate {
    private String packName;
    private String className;
    private String humpClassName;
    private String tableInfo;
    private String moduleName;
    private List<FieldInfo> fields;
    private List<FieldInfo> primaryKeys;

    @Override
    public String toString() {
        return "BusinessTemplate{" +
                "packName='" + packName + '\'' +
                ", className='" + className + '\'' +
                ", humpClassName='" + humpClassName + '\'' +
                ", tableInfo=" + tableInfo +
                ", moduleName='" + moduleName + '\'' +
                ", fields=" + fields +
                ", primaryKeys=" + primaryKeys +
                '}';
    }
}
