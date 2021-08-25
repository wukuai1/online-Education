package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.exception.GuliException;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-07-29
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {


    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVo> getChapterVo(String courseId) {

        /*
        * 根据id查询出章节和小节分别的集合
        * 根据章节的遍历 将对应的小节赋值进去
        * */

        List<ChapterVo> finalList = new ArrayList<>();


        QueryWrapper chapterQuery = new QueryWrapper();
        chapterQuery.eq("course_id",courseId);
        List<EduChapter> eduChapters = baseMapper.selectList(chapterQuery);

        QueryWrapper videoQuery = new QueryWrapper();
        videoQuery.eq("course_id",courseId);
        List<EduVideo> eduVideos = eduVideoService.list(videoQuery);

        for(int i=0;i<eduChapters.size();i++){
               ChapterVo chapterVo = new ChapterVo();
               EduChapter eduChapter =eduChapters.get(i);
               BeanUtils.copyProperties(eduChapter,chapterVo);

            List<VideoVo> videoVoList = new ArrayList<>();
               for(int j=0;j<eduVideos.size();j++){
                   EduVideo eduVideo = eduVideos.get(j);

                   if(eduVideo.getChapterId().equals(chapterVo.getId())){
                        VideoVo videoVo = new VideoVo();
                        BeanUtils.copyProperties(eduVideo,videoVo);
                        videoVoList.add(videoVo);
                   }

               }
               chapterVo.setVideoVoList(videoVoList);
               finalList.add(chapterVo);
        }

        return finalList;
    }

    @Override
    public boolean deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",chapterId);
        int count = eduVideoService.count(queryWrapper);
        if(count>0){
            throw new GuliException(20001,"不能删除,请先删除小节");
        }else{
         int result = baseMapper.deleteById(chapterId);
         return result>0;
        }

    }
}
