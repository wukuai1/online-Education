package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.exception.GuliException;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.PublishVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.entity.course.CourseMessage;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-07-29
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private VodClient vodClient;

    @Override
    public boolean deleteCourseById(String courseId) {

        boolean flag = true;

        QueryWrapper queryWrapper3 = new QueryWrapper();
        queryWrapper3.eq("course_id",courseId);
        queryWrapper3.select("video_source_id");

       List<EduVideo> videoList = eduVideoService.listObjs(queryWrapper3);
        System.out.println(videoList);
        List<String> videoIdList=new ArrayList<>();

        for(EduVideo eduVideo : videoList){
            if(!StringUtils.isEmpty(eduVideo.getVideoSourceId())){
                videoIdList.add(eduVideo.getVideoSourceId());
            }
        }
       vodClient.deleteVodByIdList(videoIdList);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("course_id",courseId);

        //TODO小节中的视频还没有删
        boolean result = eduVideoService.remove(queryWrapper);
        if(!result){
            flag=false;
        }
        boolean result1 = eduChapterService.remove(queryWrapper);
        if(!result1){
            flag=false;
        }



        boolean result2 = eduCourseDescriptionService.removeById(courseId);
        if(!result2){
            flag=false;
        }

        int result3 = baseMapper.deleteById(courseId);
        if(result3!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public List<EduCourse> getByTeacherId(String id) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("teacher_id",id);
        List<EduCourse> courseList = baseMapper.selectList(queryWrapper);
        return courseList;
    }

    @Override
    public Map<String, Object> getCourseListCondition(Page page, CourseQueryVo courseQueryVo) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();

        if(!StringUtils.isEmpty(courseQueryVo.getSubjectParentId())){
            queryWrapper.eq("subject_parent_id",courseQueryVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseQueryVo.getSubjectId())){
            queryWrapper.eq("subject_id",courseQueryVo.getSubjectId());
        }
        if (!StringUtils.isEmpty(courseQueryVo.getBuyCountSort())) {
            queryWrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(courseQueryVo.getGmtCreateSort())) {
            queryWrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(courseQueryVo.getPriceSort())) {
            queryWrapper.orderByDesc("price");
        }

        baseMapper.selectPage(page,queryWrapper);

        List<EduCourse> records = page.getRecords();
        long current = page.getCurrent();
        long pages = page.getPages();
        long size = page.getSize();
        long total = page.getTotal();
        boolean hasNext = page.hasNext();
        boolean hasPrevious = page.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public CourseWebVo getCourseDetail(String courseId) {
        return baseMapper.getCourseDetail(courseId);
    }

    @Override
    public void updateCourse(CourseMessage courseMessage) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseMessage,eduCourse);

        int result = baseMapper.updateById(eduCourse);
        if(result!=1){
            throw new GuliException(20001,"课程更新失败");
        }

        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseMessage.getId());
        eduCourseDescription.setDescription(courseMessage.getDescription());

        boolean result2 = eduCourseDescriptionService.updateById(eduCourseDescription);
        if(!result2){

            throw new GuliException(20001,"课程描述更新失败");
        }

    }

    @Override
    public PublishVo getPublishVo(String id) {
        PublishVo publishVo = baseMapper.getPublishVo(id);
        return publishVo;
    }

    @Override
    public IPage<EduCourse> getCourseList(Page<EduCourse> pageParam, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        if(courseQuery==null){
            return baseMapper.selectPage(pageParam,queryWrapper);
        }
        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();

        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }

        if (!StringUtils.isEmpty(teacherId) ) {
            queryWrapper.eq("teacher_id", teacherId);
        }

        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.ge("subject_parent_id", subjectParentId);
        }

        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.ge("subject_id", subjectId);
        }

        return baseMapper.selectPage(pageParam,queryWrapper);
    }

    @Override
    public CourseMessage getCourseMessage(String id) {

        CourseMessage courseMessage = new CourseMessage();
        EduCourse eduCourse = baseMapper.selectById(id);
        BeanUtils.copyProperties(eduCourse,courseMessage);

        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(id);
        courseMessage.setDescription(eduCourseDescription.getDescription());

        return courseMessage;
    }

    @Override
    public String addCourse(CourseMessage courseMessage) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseMessage,eduCourse);
        int result = baseMapper.insert(eduCourse);
        if(result!=1){
            throw new GuliException(20001,"课程添加失败");
        }

        String cid =  eduCourse.getId();
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseMessage.getDescription());
        eduCourseDescription.setId(cid);
        boolean result1 =eduCourseDescriptionService.save(eduCourseDescription);
        if(!result1){
            throw new GuliException(20001,"课程描述添加失败");
        }
        return cid;
    }
}
