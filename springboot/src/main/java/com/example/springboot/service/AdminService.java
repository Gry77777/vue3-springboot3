package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.execption.CustomException;
import com.example.springboot.mapper.AdminMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Resource
    private AdminMapper adminMapper;

    //添加
    public void add(Admin admin) {
        String username = admin.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin != null) { //账号已存在
            throw new CustomException("500", "账号已存在,请更改");
        }
        if (StrUtil.isBlank(admin.getPassword())) {
            admin.setPassword("ADMIN");//默认密码
        }
        if (StrUtil.isBlank(admin.getName())) {
            admin.setName(admin.getUsername());
        }
        //设置角色
        admin.setRole("ADMIN");
        adminMapper.insert(admin);
    }

    //查询
    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    //查询单个
    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    //分页
    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    //修改
    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void delete(Integer id) {
        adminMapper.deleteById(id);
    }

    //批量删除
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            adminMapper.deleteById(id);
        }

    }

    //登录
    public Admin login(Account account) {
        String username = account.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin == null) {
            throw new CustomException("500", "账号不存在");
        }
        String password = account.getPassword();
        if (!dbAdmin.getPassword().equals(password)) {
            throw new CustomException("500", "账号或密码错误");
        }
        return dbAdmin;
    }

    public void updatePassword(Account account) {
        Integer id = account.getId();
        Admin admin = this.selectById(id);
        if(!admin.getPassword().equals(account.getPassword())){
            throw new CustomException("500","原密码错误");
        }
        admin.setPassword(account.getNewPassword());
        this.update(admin);
    }
}
