package com.chad.demo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.chad.demo.beans.User;


public interface UserService extends IService<User> {
    void addAll();
}
