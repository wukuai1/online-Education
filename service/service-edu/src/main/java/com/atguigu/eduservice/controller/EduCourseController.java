package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.PublishVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.entity.course.CourseMessage;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-07-29
 */
@RestController
@RequestMapping("/eduservice/educourse")
//@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    @PostMapping("addCourse")
    public R addCourse(@RequestBody CourseMessage courseMessage){
        String id = courseService.addCourse(courseMessage);
        return  R.ok().data("courseId",id);
    }

    @GetMapping("getCourseMessage/{id}")
    public R getCourseMessage(@PathVariable String id){
        CourseMessage courseMessage = courseService.getCourseMessage(id);
        return R.ok().data("CourseMessage",courseMessage);
    }

    @PostMapping("updateCourse")
    public R updateCourse(@RequestBody CourseMessage courseMessage){
        courseService.updateCourse(courseMessage);
        return R.ok();
    }

    @GetMapping("getPublishVo/{id}")
    public R getPublishVo(@PathVariable String id){
       PublishVo publishVo = courseService.getPublishVo(id);
       return R.ok().data("publishVo",publishVo);
    }

    @PostMapping("updatePublishStatus/{id}")
    public R updatePublishcatus(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        boolean result =courseService.updateById(eduCourse);
            return R.ok();

    }

   @ApiOperation("分页课程查询")
    @PostMapping("courseList/{page}/{limit}")
    public R courseList(@PathVariable long page,
                        @PathVariable long limit,
                        @RequestBody(required = false) CourseQuery courseQuery){
        Page<EduCourse> pageParam = new Page<>(page,limit);
       System.out.println(courseQuery);

       IPage<EduCourse> page1 = courseService.getCourseList(pageParam,courseQuery);

       List<EduCourse> records = page1.getRecords();
       long tatal = page1.getTotal();

       return R.ok().data("courseList",records).data("total",tatal);
   }

   @DeleteMapping("deleteCourseById/{courseId}")
    public R deleteCourseById(@PathVariable String courseId){

        boolean result = courseService.deleteCourseById(courseId);
        if(result){
            return R.ok();
        }else {
            return R.error().message("删除失败");
        }

   }
}

