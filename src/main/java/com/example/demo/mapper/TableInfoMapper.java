package com.example.demo.mapper;

import com.example.demo.entity.ColumnInfo;
import com.example.demo.entity.TableInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liuzj
 * @create 2021/6/3 13:31
 **/
@Mapper
public interface TableInfoMapper {
    @Select("<script>" +
            "select table_name,table_comment from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA=#{dbName} and table_type='BASE TABLE'" +
            " <if test=\"tableName != null  and tableName != ''\"> and  table_name = #{tableName}</if>" +
            "</script>")
    List<TableInfo> selectTableInfo(@Param("dbName") String dbName, @Param("tableName") String tableName);

    @Select("<script>" +
            "select A.table_name,B.column_name,B.data_type,B.COLUMN_comment from INFORMATION_SCHEMA.TABLES A left JOIN INFORMATION_SCHEMA.COLUMNS B on A.table_name=B.table_name and A.TABLE_SCHEMA = B.TABLE_SCHEMA where A.TABLE_SCHEMA=#{dbName} and A.table_type='BASE TABLE'" +
            " <if test=\"tableName != null  and tableName != ''\"> and  A.TABLE_NAME = #{tableName}</if>" +
            "</script>")
    List<ColumnInfo> selectColumnInfo(@Param("dbName") String dbName, @Param("tableName") String tableName);

    @Select("<script>" +
            "select A.column_name, B.data_type, B.COLUMN_comment from INFORMATION_SCHEMA.STATISTICS A" +
            " left JOIN INFORMATION_SCHEMA.COLUMNS B on (A.table_name = B.table_name and A.COLUMN_NAME = B.COLUMN_NAME and A.TABLE_SCHEMA = B.TABLE_SCHEMA)" +
            " where A.TABLE_SCHEMA=#{dbName} and A.table_name =#{tableName} and A.index_name = 'PRIMARY'" +
            "</script>")
    List<ColumnInfo> selectPrimaryKey(@Param("dbName") String dbName, @Param("tableName") String tableName);
}
