package com.javaitem.reggie.controller;

import com.reggie.util.CommonResult;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author win
 * @create 2022/11/24 11:26
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.image}")
    private String pathName;

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response)  {

        try {
            FileInputStream fileInputStream = new FileInputStream(pathName + name);;
            byte[] file = new byte[1024];
            ServletOutputStream outputStream = response.getOutputStream();
            int len =0;
            response.setContentType("image/jpeg");
            while ((len = fileInputStream.read(file)) != -1){
                outputStream.write(file,0,len);
                outputStream.flush();
            }
            fileInputStream.close();
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
    @PostMapping("/upload")
    public CommonResult upload(MultipartFile file) throws IOException {
        int i = file.getOriginalFilename().lastIndexOf(".");
        String suffix = file.getOriginalFilename().substring(i);
        String fileName = UUID.randomUUID().toString()+suffix;
        String originalFileName = pathName + fileName;
        file.transferTo(new File(originalFileName));
        return CommonResult.success(fileName);
    }
}
