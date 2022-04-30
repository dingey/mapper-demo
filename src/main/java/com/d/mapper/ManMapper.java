package com.d.mapper;

import com.d.model.Man;
import com.github.dingey.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;

@Mapper
public interface ManMapper extends BaseMapper<Man> {

    @Select("Select * from man where id=#{id}")
    Man selectById(Serializable id);
}