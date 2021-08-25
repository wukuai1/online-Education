package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.jwt.JwtUtils;
import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/Coursefront")
//@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService eduChapterService;
    @Autowired
    private OrderClient orderClient;

    @PostMapping("getFrontCourseList/{page}/{limit}")
    public R getCourseList(@PathVariable long page, @PathVariable long limit,
                           @RequestBody CourseQueryVo courseQueryVo){
        Page<EduCourse> eduCoursePage = new Page<>(page,limit);
        Map<String,Object> map = courseService.getCourseListCondition(eduCoursePage,courseQueryVo);
        return R.ok().data(map);
    }

    @GetMapping("getCourseDetail/{courseId}")
    public R getCourseDetail(@PathVariable String courseId, HttpServletRequest request){

        CourseWebVo courseWebVo =courseService.getCourseDetail(courseId);
        List<ChapterVo> chapterVoList = eduChapterService.getChapterVo(courseId);
        boolean byCourse = orderClient.isByCourse(courseId,JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("courseWebVo",courseWebVo).data("chapterVoList",chapterVoList).data("byCourse",byCourse);
    }

    @PostMapping("getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id){

        CourseWebVo courseWebVo = courseService.getCourseDetail(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseWebVo,courseWebVoOrder);

        return courseWebVoOrder;
    }

}
