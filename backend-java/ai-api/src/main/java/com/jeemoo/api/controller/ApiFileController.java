package com.jeemoo.api.controller;

import com.jeemoo.common.utils.file.FileUploadUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("")
public class ApiFileController {

    @GetMapping("/file/{year}/{month}/{day}/{fileName:.+}")
    public void file(@PathVariable String year,
                     @PathVariable String month,
                     @PathVariable String day,
                     @PathVariable String fileName, HttpServletResponse response) {
        FileUploadUtils.preview(year +"/"+ month +"/"+ day +"/"+ fileName, response);
    }


    @GetMapping("/file/static/{fileName:.+}")
    public void file(@PathVariable String fileName, HttpServletResponse response) {
        FileUploadUtils.preview("static/"+ fileName, response);
    }
}
