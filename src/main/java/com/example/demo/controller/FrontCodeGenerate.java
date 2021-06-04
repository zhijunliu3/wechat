package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.mapper.TableInfoMapper;
import com.example.demo.utils.ZipUtils;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuzj
 * @create 2021/6/3 11:33
 **/
@RestController
@RequestMapping("/front")
public class FrontCodeGenerate {

    @Autowired
    TableInfoMapper tableInfoMapper;

    @Value("${dbName}")
    String dbName;

    String regexStr = "(_)(\\w)";

    List<GenerateTemplateInfo> templateInfos = new ArrayList<>();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public FrontCodeGenerate() throws Exception{
        templateInfos.add(new GenerateTemplateInfo(new String[]{"api"}, "", "", "js", createTemplate("api")));
        templateInfos.add(new GenerateTemplateInfo(new String[]{"view"}, "", "", "vue", createTemplate("index")));
    }

    public Template createTemplate(String type) throws Exception{
        return new Template(null, new BufferedReader(
                new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("templates/"+type+".ftl"), "UTF-8")
        ),null,"UTF-8");
    }

    @RequestMapping("/generateCode")
    public void generateCode(String[] tableNames, String[] tableInfos, HttpServletResponse response) {
        String filePath = System.getProperty("user.dir") + File.separator + UUID.randomUUID();
        createFolder(filePath);
        for (int i = 0; i < tableNames.length; i++) {
            String tableName = tableNames[i];
            generateServerCode(tableName, tableInfos[i], filePath);
        }
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename="+FORMATTER.format(LocalDateTime.now())+".zip");
        try {
            ZipUtils.toZip(filePath, response.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileUtils.deleteDirectory(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateServerCode(String tableName, String tableInfo, String filePath){
        List<ColumnInfo> columnInfos = selectColumnInfo(tableName);
        String humpClassName = convertHumpNaming(tableName);
        String className = upperFirstLetter(humpClassName);
        List<FieldInfo> fieldInfos = convertFields(columnInfos);
        List<ColumnInfo> pkInfos = tableInfoMapper.selectPrimaryKey(dbName, tableName);
        List<FieldInfo> primaryKeys = convertFields(pkInfos);
        BusinessTemplate businessTemplate = new BusinessTemplate();
        businessTemplate.setClassName(className);
        businessTemplate.setFields(fieldInfos);
        businessTemplate.setHumpClassName(humpClassName);
        businessTemplate.setPrimaryKeys(primaryKeys);
        businessTemplate.setTableInfo(tableInfo);
        for (GenerateTemplateInfo template : templateInfos) {
            Map<String,Object> root = new HashMap<>(16);
            StringBuffer path = new StringBuffer(filePath);
            for (String dir : template.getFrontDir()) {
                path.append(File.separator).append(dir);
            }
            root.put("obj", businessTemplate);
            String fileName = template.getPrefix() + businessTemplate.getClassName() + template.getType() + "." + template.getSuffix();
            try(Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path.toString()+File.separator+fileName)), "UTF-8"))){
                template.getTemplate().process(root, out);
                out.flush();
            }catch(Exception e) {

            }
        }
    }



    public void createFolder(String filePath){
        File rootFile = new File(filePath);
        if(!rootFile.exists()){
            rootFile.mkdir();
        }
        for (GenerateTemplateInfo template : templateInfos) {
            StringBuffer path = new StringBuffer(filePath);
            for (String dir : template.getFrontDir()) {
                path.append(File.separator).append(dir);
                File file = new File(path.toString());
                if(!file.exists()){
                    file.mkdir();
                }
            }
        }
    }

    public List<ColumnInfo> selectColumnInfo(String tableName) {
        return tableInfoMapper.selectColumnInfo(dbName, tableName);
    }

    public List<FieldInfo> convertFields(List<ColumnInfo> columnInfos){
        List<FieldInfo> fieldInfos = new ArrayList<>();
        columnInfos.forEach(item->{
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setLabel(item.getColumnComment());
            fieldInfo.setName(convertHumpNaming(item.getColumnName()));
//            if(typeMap.containsKey(item.getDataType())){
//                fieldInfo.setType(typeMap.get(item.getDataType()));
//            }else{
//                fieldInfo.setType("String");
//            }
            fieldInfo.setInitialUppercaseName(upperFirstLetter(fieldInfo.getName()));
            fieldInfo.setColumnName(item.getColumnName());
            fieldInfo.setDataType(item.getDataType());
            fieldInfos.add(fieldInfo);
        });
        return fieldInfos;
    }

    /**
     * 首字母大写
     * @param letter
     * @return
     */
    public String upperFirstLetter(String letter){
        char[] chars = letter.toCharArray();
        if(chars[0]>='a' && chars[0]<='z'){
            chars[0] = (char) (chars[0]-32);
        }
        return new String(chars);
    }

    /**
     * 转换成驼峰命名
     * @param source
     * @return
     */
    public String convertHumpNaming(String source){
        Matcher matcher = Pattern.compile(regexStr).matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String g = matcher.group(2);
            matcher.appendReplacement(sb, g.toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
