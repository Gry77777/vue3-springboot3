package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Employee;
import com.example.springboot.execption.CustomException;
import com.example.springboot.mapper.EmployeeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmployeeService {
@Resource
private EmployeeMapper employeeMapper;
    //添加
    public void add(Employee employee) {
        String username=employee.getUsername();
        Employee dbEmployee=  employeeMapper.selectByUsername(username);
        if(dbEmployee != null){
            throw new CustomException("500","账号已存在,请更改");
        }
        Employee dbEmployee1=  employeeMapper.selectByNo(employee.getNo());
        if(dbEmployee1 != null){
            throw new CustomException("500","工号已存在,请更换");
        }
        if(StrUtil.isBlank(employee.getPassword())){
            employee.setPassword("123");
        }
        if(StrUtil.isBlank(employee.getName())){
            employee.setName(employee.getUsername());
        }
        //设置角色
        employee.setRole("EMP");
        employeeMapper.insert(employee);
    }
    //查询
    public List<Employee> selectAll(Employee employee) {
        return employeeMapper.selectAll(employee);
    }
    //查询单个
    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }
    //分页
    public PageInfo<Employee> selectPage(Employee employee,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Employee> list=employeeMapper.selectAll(employee);
        return PageInfo.of(list);
    }
    //修改
    public void update(Employee employee) {
        employeeMapper.update(employee);
    }

    public void delete(Integer id) {
        employeeMapper.deleteById(id);
    }
    //批量删除
    public void deleteBatch(List<Integer> ids) {
        for(Integer id:ids){
            employeeMapper.deleteById(id);
        }

    }
    //登录
    public Employee login(Account account) {
        String username=account.getUsername();
        Employee dbEmployee=  employeeMapper.selectByUsername(username);
        if(dbEmployee == null){
            throw new CustomException("500","账号不存在");
        }
        String password=account.getPassword();
        if(!dbEmployee.getPassword().equals(password)){
            throw new CustomException("500","账号或密码错误");
        }
        return dbEmployee;
    }
    //注册
    public void register(Employee employee) {
        this.add(employee);
    }

    public void updatePassword(Account account) {
        Integer id = account.getId();
        Employee employee = this.selectById(id);
        if(!employee.getPassword().equals(account.getPassword())){
            throw new CustomException("500","原密码错误");
        }
        employee.setPassword(account.getNewPassword());
        this.update(employee);
    }
}
