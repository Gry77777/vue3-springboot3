package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.server.HttpServerResponse;
import com.example.springboot.common.Result;
import com.example.springboot.execption.CustomException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.RuntimeMBeanException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {
    //获取当前项目路径
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        String fileName = System.currentTimeMillis() + "_" + originalFilename; // 文件名
        String realPath = filePath + fileName;
        try {
            FileUtil.writeBytes(file.getBytes(), realPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500", "文件上传失败");
        }
        String url = "http://localhost:8080/files/download/" + fileName;
        //返回网络链接
        return Result.success(url);
    }

    @GetMapping("/download/{fileName}")  // 使用 @PathVariable
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        try {
            String realPath = filePath + fileName;
            byte[] bytes = FileUtil.readBytes(realPath);

            if (bytes == null || bytes.length == 0) {
                throw new CustomException("404", "文件不存在");
            }

            // 设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.setContentType("application/octet-stream");

            // 获取输出流
            OutputStream os = response.getOutputStream();
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500", "文件下载失败");
        }
    }

    @PostMapping("/wang/upload")
    public Map<String,Object>wangEditorUpload(MultipartFile file){

        String originalFilename = file.getOriginalFilename();
        if (FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        String fileName = System.currentTimeMillis() + "_" + originalFilename; // 文件名
        String realPath = filePath + fileName;
        try {
            FileUtil.writeBytes(file.getBytes(), realPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500", "文件上传失败");
        }
        String url = "http://localhost:8080/files/download/" + fileName;
        //返回网络链接

        Map<String,Object> resMap = new HashMap<>();
        List<Map<String,Object>>list=new ArrayList<>();
        Map<String,Object> urlMap=new HashMap<>();
        urlMap.put("url",url);
        list.add(urlMap);
        resMap.put("errno",0);
        resMap.put("data",list);
        return resMap;
    }


}

