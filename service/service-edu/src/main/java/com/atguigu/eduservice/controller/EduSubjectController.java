package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.eduservice.entity.subject.OneSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-07-28
 */
@RestController
@RequestMapping("/eduservice/edusubject")
//@CrossOrigin
public class EduSubjectController {

    @Autowired
   private EduSubjectService subjectService;

    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        subjectService.addSubject(file,subjectService);
        return R.ok();
    }

    @GetMapping("getSubjectList")
    public R getSubjectList(){
        List<OneSubject> data = subjectService.getSubjectList();
        return R.ok().data("data",data);
    }


}

