package com.example.springboot.controller;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Department;
import com.example.springboot.entity.Employee;
import com.example.springboot.service.DepartmentService;
import com.example.springboot.service.EmployeeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;
    @Resource
    private DepartmentService departmentService;
    // 添加员工
    @PostMapping("/add")
    public Result add(@RequestBody Employee employee){
     employeeService.add(employee);
     return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Employee employee){
        employeeService.update(employee);
        return Result.success();
    }
    // 删除单个数据
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        employeeService.delete(id);
        return Result.success();
    }
    // 删除多个数据
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        employeeService.deleteBatch(ids);
        return Result.success();
    }
    @GetMapping("/selectAll")
    public Result selectAll(Employee employee) {
        List<Employee> list=employeeService.selectAll(employee);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Employee employee=employeeService.selectById(id);
        return Result.success(employee);
    }
    @GetMapping("/selectByIdOne")
    public Result selectByIdOne(@RequestParam Integer id,@RequestParam(required = false) String no) {
        Employee employee=employeeService.selectById(id);
        return Result.success(employee);
    }
    @GetMapping("/selectPage")
    public Result selectPage(Employee employee,
            @RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize)
    {
        PageInfo<Employee> pageInfo
                =employeeService.selectPage(employee,pageNum,pageSize);
        return Result.success(pageInfo);
    }


//     导出excel代码
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        List<Employee> employeesList = employeeService.selectAll(null);
        // 在内存操作，写出到浏览器
        ExcelWriter writer= ExcelUtil.getWriter(true);
        //设置中文表头
        writer.addHeaderAlias("username","用户名");
        writer.addHeaderAlias("name","名称");
        writer.addHeaderAlias("sex","性别");
        writer.addHeaderAlias("age","年龄");
        writer.addHeaderAlias("no","工号");
        writer.addHeaderAlias("description","个人介绍");
        writer.addHeaderAlias("departmentId","部门");
        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.setOnlyAlias(true);
        writer.write(employeesList,true);
        //设置浏览器响应格式(固定)
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName= URLEncoder.encode("员工信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        // 输出
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os, true);
        os.close();
        writer.close();
    }


    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        // Excel 读入流
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        reader.addHeaderAlias("账号", "username");
        reader.addHeaderAlias("名称", "name");
        reader.addHeaderAlias("性别", "sex");
        reader.addHeaderAlias("年龄", "age");
        reader.addHeaderAlias("工号", "no");
        reader.addHeaderAlias("个人介绍", "description");
        reader.addHeaderAlias("部门", "departmentName");

        // 读取数据
        List<Employee> employeeList = reader.readAll(Employee.class);

        for (Employee employee : employeeList) {
            // 根据部门名称查找部门ID
            String departmentName = employee.getDepartmentName();
            Department department = departmentService.selectByName(departmentName);
            if (department != null) {
                // 设置部门ID
                employee.setDepartmentId(department.getId());
            } else {
                // 如果找不到部门，可以考虑设置默认ID或者抛出异常
                employee.setDepartmentId(null);  // 或者设置一个默认值
            }
            // 批量插入数据
            employeeService.add(employee);
        }

        return Result.success();
    }

}

