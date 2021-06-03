package com.example.demo.mapper;

import com.example.demo.entity.PaSeatLayout;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liuzj
 * @create 2021/6/3 11:25
 **/
@Mapper
public interface PaSeatLayoutMapper {
    @Select("select * from pa_seat_layout")
    List<PaSeatLayout> getPaSeatLayoutList();
}
