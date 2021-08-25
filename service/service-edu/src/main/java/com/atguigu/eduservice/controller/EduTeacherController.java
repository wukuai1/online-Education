package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.commonutils.exception.GuliException;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-07-24
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("/eduservice/teacher")
//@CrossOrigin
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;
    @Autowired
    private EduCourseService  eduCourseService;


    @GetMapping("getTeacherAndCourse/{id}")
    public R getTeacherAndCourse(@PathVariable String id){

        EduTeacher teacher = eduTeacherService.getById(id);
        List<EduCourse> courseList = eduCourseService.getByTeacherId(id);
        return R.ok().data("teacher",teacher).data("courseList",courseList);
    }


    @GetMapping ("findall")
    public R getStudentCount(){
        List<EduTeacher> allTeacher = eduTeacherService.list(null);
        Map map = new HashMap<String,List>();
        map.put("allTeacher",allTeacher);
        return R.ok().data(map);
    }




    @GetMapping("pageList/{pageNo}/{pageSize}")
    public R pageList(@PathVariable long pageNo,@PathVariable long pageSize){
        Page<EduTeacher> page = new Page<EduTeacher>(pageNo,pageSize);
        eduTeacherService.page(page,null);
        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }



    @PostMapping("pageTeacherCondition/{current}/{limit}")
    @ApiOperation(value = "条件分页",notes = "通过传入json格式的条件进行分页查询")
    public R conditonPage(@PathVariable long current,
                          @PathVariable long limit,
                          @ApiParam(name="teacherQuery",value = "查询对象")
                          @RequestBody(required = false) TeacherQuery teacherQuery){

        Page<EduTeacher> page = new Page<>(current,limit);
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(teacherQuery.getName())){
            queryWrapper.like("name",teacherQuery.getName());
        }

        if(!StringUtils.isEmpty(teacherQuery.getLevel())){
            queryWrapper.eq("level",teacherQuery.getLevel());
        }

        if(!StringUtils.isEmpty(teacherQuery.getBegin())){
            queryWrapper.ge("gmt_create",teacherQuery.getBegin());
        }

        if(!StringUtils.isEmpty(teacherQuery.getEnd())){
            queryWrapper.le("gmt_create",teacherQuery.getEnd());
        }

        queryWrapper.orderByDesc("gmt_create");
        IPage<EduTeacher> page1 = eduTeacherService.page(page, queryWrapper);

        long total = page1.getTotal();
        List<EduTeacher> teacherList = page1.getRecords();

        return R.ok().data("total",total).data("teacherQuery",teacherList);
    }


    @ApiOperation("新增讲师")
    @PostMapping("save")
    public R save(
            @ApiParam(name="teacher",value ="讲师对象",required = true )
            @RequestBody EduTeacher eduTeacher){
        boolean flag = eduTeacherService.save(eduTeacher);
        if(flag){
            return R.ok();
        }
        return R.error();
    }

    @ApiOperation("修改讲师信息")
    @PostMapping("updateTeacherById")
    public R updateTeacher(
            @ApiParam(name="teacher",value ="讲师对象",required = true )
            @RequestBody EduTeacher eduTeacher){
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if(flag){
            return R.ok();
        }
        return R.error();
    }

    @ApiOperation("根据id查询讲师")
    @PostMapping("selectTeacherById/{id}")
    public R selectTeacherById(
            @ApiParam(name="id",value ="讲师id",required = true )
            @PathVariable String id){
        EduTeacher eduTeacher = eduTeacherService.getById(id);

        return R.ok().data("teacher",eduTeacher);
    }

    @ApiOperation("删除讲师")
    @DeleteMapping("deleteTeacherById/{id}")
    public R deleteTeacherById(
            @ApiParam(name="id",value ="讲师id",required = true )
            @PathVariable String id){
        boolean flag = eduTeacherService.removeById(id);
        if(flag){
            return R.ok();
        }
        return R.error();
    }


    @ApiOperation("特殊错误处理")
    @DeleteMapping("error/{id}/{num}")
    public R errorSpecialHandler(
            @PathVariable long id,@PathVariable long num){
        long result = id/num;
        return R.ok().data("result",result);
    }


    @ApiOperation("自定义错误处理")
    @GetMapping("comError/{current}/{num}")
    public R errorAutoHandler(
            @PathVariable long current,@PathVariable long num){
        try {
            long result = 10 / 0;
        }catch (Exception e){
            throw new GuliException(20001,"处理自定义异常");
        }
        return R.ok();
    }



}

