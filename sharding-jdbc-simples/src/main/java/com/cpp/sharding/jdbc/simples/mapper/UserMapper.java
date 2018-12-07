package com.cpp.sharding.jdbc.simples.mapper;

import com.cpp.sharding.jdbc.simples.entity.SysUser;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    int insert(SysUser user);
}
