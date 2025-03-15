package com.example.springboot.mapper;

import com.example.springboot.entity.Article;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

@Mapper
public interface ArticleMapper {
    // 查询所有
    @SelectProvider(type = ArticleSqlProvider.class, method = "selectAll")
    List<Article> selectAll(Article article);

    @Select("SELECT count(*) FROM article WHERE DATE(time) = #{date}")
    Integer selectByDate(String date);


    class ArticleSqlProvider {
        public String selectAll(Article article) {
            return new SQL() {{
                SELECT("*");
                FROM("article");
                if (article.getTitle() != null && !article.getTitle().isEmpty()) {
                    WHERE("name LIKE concat('%', #{name}, '%')");
                }
                ORDER_BY("id desc");
            }}.toString();
        }
    }

    // 根据id查询
    @Select("select * from article where id = #{id}")
    Article selectById(Integer id);
    // 添加
    @Insert("insert into article(title,img,description,content,time) values(#{title}, #{img}, #{description}, #{content},#{time})")
    void insert(Article article);

    // 修改
    @Update("update article set title = #{title}, img = #{img}, description= #{description}, content = #{content},time=#{time} where id = #{id}")
    void updateById(Article article);

    // 根据id删除
    @Delete("delete from article where id = #{id}")
    void deleteById(Integer id);
}

