package com.example.springboot.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Employee;
import com.example.springboot.execption.CustomException;
import com.example.springboot.service.AdminService;
import com.example.springboot.service.ArticleService;
import com.example.springboot.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class WebController {
    @Resource
   private EmployeeService employeeService;
    @Resource
    private AdminService adminService;
    @Resource
    private ArticleService articleService;
    @GetMapping("/hello")
    public String hello(){
        return "Hello World,你好小古";
    }
    @GetMapping("/count")
    public Result count(){
        return Result.success(10);
    }

    //管理员/员工登录
    @PostMapping("/login")
    public Result login(@RequestBody Account account){
        Account result = null;
        if("ADMIN".equals(account.getRole())){
        result= adminService.login(account);
        }
        else if("EMP".equals(account.getRole())){
        result= employeeService.login(account);
        }
        else{
            throw new CustomException("500","角色错误");
        }
        return Result.success(result);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Employee employee){
        employeeService.register(employee);
        return Result.success();
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account){
        if("ADMIN".equals(account.getRole())){
            adminService.updatePassword(account);
        }
        else if("EMP".equals(account.getRole())){
            employeeService.updatePassword(account);
        }
        else{
            throw new CustomException("500","角色错误");
        }
        return Result.success();
    }

    @GetMapping("/barData")
    public Result getBarData(){
        Map<String,Object> map=new HashMap<>();
        List<Employee> employeeList = employeeService.selectAll(null);
        Set<String> departmentNameSet = employeeList.stream().map(Employee::getDepartmentName)
                .collect(Collectors.toSet());
        map.put("department",departmentNameSet);// x轴将部门名称添加到结果中
        List<Long> countList=new ArrayList<>();
        for (String departmentName : departmentNameSet) {
            // 统计每个部门的员工数量
            long count = employeeList.stream().filter(employee -> employee.getDepartmentName().equals(departmentName))
                    .count();
            countList.add(count);
        }
        map.put("count",countList);// y轴将员工数量添加到结果中
        return Result.success(map);
    }

    @GetMapping("/lineData")
    public Result getLineData(){
        Map<String,Object> map = new HashMap<>();
        Date date = new Date(); // 获取当前日期
        DateTime start = DateUtil.offsetDay(date, -7); // 获取当前日期往前7天的日期
        List<DateTime> dateTimeList = DateUtil.rangeToList(start, date, DateField.DAY_OF_YEAR);

        // 格式化日期为"MM月dd日"（不改变时间部分）
        List<String> dateStrList = dateTimeList.stream()
                .map(dateTime -> DateUtil.format(dateTime, "MM月dd日"))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());

        map.put("date", dateStrList); // x轴将日期添加到结果中

        List<Integer> countList = new ArrayList<>();
        for (DateTime day : dateTimeList) {
            // 格式化日期为"yyyy-MM-dd"（仅包含日期部分）
            String dayFormat = DateUtil.formatDate(day); // 只获取日期部分

            // 获取当天所有发布的文章数量
            Integer count = articleService.selectByDate(dayFormat); // 查询时使用日期部分
            countList.add(count);
        }

        map.put("count", countList); // y轴将文章数量添加到结果中
        return Result.success(map);
    }


    @GetMapping("/pieData")
    public Result getPieData(){
        List<Map<String, Object>> list=new ArrayList<>();
        List<Employee> employeeList = employeeService.selectAll(null);
        Set<String> departmentNameSet = employeeList.stream().map(Employee::getDepartmentName)
                .collect(Collectors.toSet());
        List<Long> countList=new ArrayList<>();
        for (String departmentName : departmentNameSet) {
           HashMap<String,Object> map =new HashMap<>();
           map.put("name",departmentName);
            // 统计每个部门的员工数量
            long count = employeeList.stream().filter(employee -> employee.getDepartmentName().equals(departmentName))
                    .count();
            map.put("value",count);
            list.add(map);
        }
        return Result.success(list);
    }




}
