package com.example.springboot.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Article;
import com.example.springboot.execption.CustomException;
import com.example.springboot.mapper.ArticleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    //添加
    public void add(Article article) {
        article.setTime(DateUtil.now());
        articleMapper.insert(article);
    }

    //查询
    public List<Article> selectAll(Article article) {
        return articleMapper.selectAll(article);
    }

    //查询单个
    public Article selectById(Integer id) {
        return articleMapper.selectById(id);
    }

    //分页
    public PageInfo<Article> selectPage(Article article, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.selectAll(article);
        return PageInfo.of(list);
    }

    //修改
    public void update(Article article) {
        articleMapper.updateById(article);
    }

    public void delete(Integer id) {
        articleMapper.deleteById(id);
    }

    //批量删除
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            articleMapper.deleteById(id);
        }

    }
    public Integer selectByDate(String date) {

       return articleMapper.selectByDate(date);
    }
}
