package com.example.springboot.mapper;

import com.example.springboot.entity.Department;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    // 查询所有
    @SelectProvider(type = DepartmentSqlProvider.class, method = "selectAll")
    List<Department> selectAll(Department department);

    @Select("select id from department where name = #{name}")
    Department selectByName(String name);

    class DepartmentSqlProvider {
        public String selectAll(Department department) {
            return new SQL() {{
                SELECT("*");
                FROM("department");
                if (department.getName() != null && !department.getName().isEmpty()) {
                    WHERE("name LIKE concat('%', #{name}, '%')");
                }
                ORDER_BY("id desc");
            }}.toString();
        }
    }

    // 根据id查询
    @Select("select * from department where id = #{id}")
    Department selectById(Integer id);
    // 添加
    @Insert("INSERT INTO department(name) VALUES(#{name})")
    void insert(Department department);


    // 修改
    @Update("update department set id = #{id},  name = #{name} where id = #{id}")
    void update(Department department);

    // 根据id删除
    @Delete("delete from department where id = #{id}")
    void deleteById(Integer id);
}

