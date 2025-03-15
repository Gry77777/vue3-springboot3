package com.example.springboot.mapper;

import com.example.springboot.entity.Employee;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
@Mapper
public interface EmployeeMapper {
    // 查询所有
    @SelectProvider(type = EmployeeSqlProvider.class, method = "selectAll")
    List<Employee> selectAll(Employee employee);

    @Select("select * from employee where username = #{username}")
    Employee selectByUsername(String username);

    @Select("select * from employee where no = #{no}")
    Employee selectByNo(String no);

    class EmployeeSqlProvider {
        public String selectAll(Employee employee) {
            // 如果 employee 为 null，返回一个没有条件的查询语句
            if (employee == null) {
                return new SQL() {{
                    SELECT("e.*, d.name AS departmentName");
                    FROM("employee e LEFT JOIN department d ON e.department_id = d.id");
                    ORDER_BY("e.id desc");
                }}.toString();
            }

            return new SQL() {{
                SELECT("e.*, d.name AS departmentName");
                FROM("employee e LEFT JOIN department d ON e.department_id = d.id");

                // 检查 employee 的 name 是否为 null 或空
                if (employee.getName() != null && !employee.getName().isEmpty()) {
                    WHERE("e.name LIKE concat('%', #{name}, '%')");
                }

                ORDER_BY("e.id desc");
            }}.toString();
        }


    }

    // 根据id查询
    @Select("select * from employee where id = #{id}")
    Employee selectById(Integer id);
    // 添加
    @Insert("insert into employee(username, password, role, name, no, sex, age, description, department_id,avatar) values(#{username}, #{password}, #{role}, #{name}, #{no}, #{sex}, #{age}, #{description}, #{departmentId},#{avatar})")
    void insert(Employee employee);

    // 修改
    @Update("update employee set username = #{username}, password = #{password}, role = #{role}, name = #{name}, no = #{no}, sex = #{sex}, age = #{age}, description = #{description}, department_id = #{departmentId},avatar=#{avatar} where id = #{id}")
    void update(Employee employee);

    // 根据id删除
    @Delete("delete from employee where id = #{id}")
    void deleteById(Integer id);
}

