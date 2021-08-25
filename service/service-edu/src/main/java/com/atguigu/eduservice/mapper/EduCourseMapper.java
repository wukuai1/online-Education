package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.PublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-07-29
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    PublishVo getPublishVo(String courseId);

    CourseWebVo getCourseDetail(String courseId);
}
