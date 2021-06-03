package com.example.demo.entity;

import freemarker.template.Template;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @description:
 * @author: LiuZhiJun
 * @create: 2020-09-03 15:28
 **/

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GenerateTemplateInfo {
    private String[] frontDir;
    private String type;
    private String prefix;
    private String suffix;
    private Template template;
}
