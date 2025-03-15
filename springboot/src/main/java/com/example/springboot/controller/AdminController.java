package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Admin;
import com.example.springboot.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;
    // 添加员工
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin){
        adminService.add(admin);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin){
        adminService.update(admin);
        return Result.success();
    }
    // 删除单个数据
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        adminService.delete(id);
        return Result.success();
    }
    // 删除多个数据
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        adminService.deleteBatch(ids);
        return Result.success();
    }
    @GetMapping("/selectAll")
    public Result selectAll(Admin admin) {
        List<Admin> list=adminService.selectAll(admin);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Admin admin=adminService.selectById(id);
        return Result.success(admin);
    }
    @GetMapping("/selectByIdOne")
    public Result selectByIdOne(@RequestParam Integer id,@RequestParam(required = false) String no) {
        Admin admin=adminService.selectById(id);
        return Result.success(admin);
    }
    @GetMapping("/selectPage")
    public Result selectPage(Admin admin,
                             @RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize)
    {
        PageInfo<Admin> pageInfo
                =adminService.selectPage(admin,pageNum,pageSize);
        return Result.success(pageInfo);
    }

}
