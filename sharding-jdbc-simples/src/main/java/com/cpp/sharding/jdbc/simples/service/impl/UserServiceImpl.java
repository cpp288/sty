package com.cpp.sharding.jdbc.simples.service.impl;

import com.cpp.sharding.jdbc.simples.entity.SysUser;
import com.cpp.sharding.jdbc.simples.mapper.UserMapper;
import com.cpp.sharding.jdbc.simples.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * user service
 *
 * @author chenjian
 * @date 2018-12-06 19:17
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int insert(SysUser user) {
        return userMapper.insert(user);
    }
}
