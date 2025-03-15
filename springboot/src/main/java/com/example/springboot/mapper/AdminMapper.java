package com.example.springboot.mapper;

import com.example.springboot.entity.Admin;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

@Mapper
public interface AdminMapper {
    // 查询所有
    @SelectProvider(type = AdminSqlProvider.class, method = "selectAll")
    List<Admin> selectAll(Admin admin);

    @Select("select * from admin where username = #{username}")
    Admin selectByUsername(String username);

    class AdminSqlProvider {
        public String selectAll(Admin admin) {
            return new SQL() {{
                SELECT("*");
                FROM("admin");
                if (admin.getName() != null && !admin.getName().isEmpty()) {
                    WHERE("name LIKE concat('%', #{name}, '%')");
                }
                ORDER_BY("id desc");
            }}.toString();
        }
    }

    // 根据id查询
    @Select("select * from admin where id = #{id}")
    Admin selectById(Integer id);
    // 添加
    @Insert("insert into admin(username, password, role, name, avatar) values(#{username}, #{password}, #{role}, #{name},{avatar})")
    void insert(Admin admin);

    // 修改
    @Update("update admin set username = #{username}, password = #{password}, role = #{role}, name = #{name},avatar=#{avatar} where id = #{id}")
    void updateById(Admin admin);

    // 根据id删除
    @Delete("delete from admin where id = #{id}")
    void deleteById(Integer id);
}

