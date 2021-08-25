package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.eduservice.entity.vo.ExcelSubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-07-28
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void addSubject(MultipartFile file,EduSubjectService eduSubjectService) {

        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, ExcelSubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<OneSubject> getSubjectList() {
        /*
        * 先查一级目录 再查二级目录
        * 循环封装一级目录
        * 将二级目录与以及目录匹配 封装到最终的集合中
        *
        * */
        List<OneSubject>  finalList = new ArrayList<>();
        List<EduSubject> oneSubjectList = new ArrayList<>();
        List<EduSubject> twoSubjectList = new ArrayList<>();

        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("parent_id",0);
        queryWrapper1.orderByDesc("gmt_create");
        oneSubjectList = baseMapper.selectList(queryWrapper1);

        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2.ne("parent_id",0);
        queryWrapper2.orderByDesc("gmt_create");
        twoSubjectList = baseMapper.selectList(queryWrapper2);

        for(int i=0;i<oneSubjectList.size();i++){
            EduSubject eduSubject = oneSubjectList.get(i);
            OneSubject oneSubject = new OneSubject();
            //只处理相同的字段
            BeanUtils.copyProperties(eduSubject,oneSubject);

            List<TwoSubject> twoList = new ArrayList<>();
            for(int j= 0;j<twoSubjectList.size();j++){
                EduSubject eduSubject1 = twoSubjectList.get(j);

                if(eduSubject1.getParentId().equals(oneSubject.getId())){
                    TwoSubject twoSubject = new TwoSubject();
                    //只处理相同的字段
                    BeanUtils.copyProperties(eduSubject1,twoSubject);
                    twoList.add(twoSubject);
                }
            }

             oneSubject.setTwoSubjectList(twoList);
             finalList.add(oneSubject);
        }

        return finalList;
    }
}
