package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.course.CourseMessage;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.PublishVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-07-29
 */
public interface EduCourseService extends IService<EduCourse> {


    String addCourse(CourseMessage courseMessage);

    CourseMessage getCourseMessage(String id);

    void updateCourse(CourseMessage courseMessage);

    PublishVo getPublishVo(String id);

    IPage<EduCourse> getCourseList(Page<EduCourse> pageParam, CourseQuery courseQuery);

    boolean deleteCourseById(String courseId);

    List<EduCourse> getByTeacherId(String id);

    Map<String, Object> getCourseListCondition(Page page, CourseQueryVo courseQueryVo);

    CourseWebVo getCourseDetail(String courseId);
}
