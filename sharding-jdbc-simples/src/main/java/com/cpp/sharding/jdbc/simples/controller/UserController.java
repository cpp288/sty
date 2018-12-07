package com.cpp.sharding.jdbc.simples.controller;

import com.cpp.sharding.jdbc.simples.entity.SysUser;
import com.cpp.sharding.jdbc.simples.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * user controller
 *
 * @author chenjian
 * @date 2018-12-06 19:18
 */
@RestController
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/insert")
    public void insert(@RequestParam String name) {
        SysUser user = new SysUser();
        user.setName(name);
        userService.insert(user);
    }
}
