package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Department;
import com.example.springboot.execption.CustomException;
import com.example.springboot.mapper.DepartmentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
@Resource
private DepartmentMapper departmentMapper;
    //添加
    public void add(Department department) {
       
        departmentMapper.insert(department);
    }
    //查询
    public List<Department> selectAll(Department department) {
        return departmentMapper.selectAll(department);
    }
    //查询单个
    public Department selectById(Integer id) {
        return departmentMapper.selectById(id);
    }

    public Department selectByName(String name) {
        return departmentMapper.selectByName(name);
    }

    //分页
    public PageInfo<Department> selectPage(Department department,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Department> list=departmentMapper.selectAll(department);
        return PageInfo.of(list);
    }
    //修改
    public void update(Department department) {
        departmentMapper.update(department);
    }

    public void delete(Integer id) {
        departmentMapper.deleteById(id);
    }
    //批量删除
    public void deleteBatch(List<Integer> ids) {
        for(Integer id:ids){
            departmentMapper.deleteById(id);
        }

    }

}
