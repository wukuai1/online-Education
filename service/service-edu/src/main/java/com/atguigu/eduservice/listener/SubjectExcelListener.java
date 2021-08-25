package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.commonutils.exception.GuliException;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.eduservice.entity.vo.ExcelSubjectData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class SubjectExcelListener extends AnalysisEventListener<ExcelSubjectData> {

    public EduSubjectService eduSubjectService;


    public SubjectExcelListener() {
    }

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
       if(excelSubjectData==null){
            throw new GuliException(20001,"文件数据为空");
        }

       EduSubject existOneSubject = this.existOneSubject(eduSubjectService,excelSubjectData.getOneSubjectName());
       if(existOneSubject==null){
           existOneSubject= new EduSubject();
           existOneSubject.setParentId("0");
           existOneSubject.setTitle(excelSubjectData.getOneSubjectName());
           eduSubjectService.save(existOneSubject);
       }

       String pid = existOneSubject.getId();
        EduSubject existTwoSubject = this.existTwoSubject(eduSubjectService, excelSubjectData.getTwoSubjectName(),pid);
        if(existTwoSubject==null){
            existTwoSubject= new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(excelSubjectData.getTwoSubjectName());
            eduSubjectService.save(existTwoSubject);
        }

    }

    private EduSubject existOneSubject(EduSubjectService subjectService,String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",0);
        EduSubject oneSubject = subjectService.getOne(wrapper);
        return oneSubject;
    }

    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String id){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",id);
        EduSubject twoSubject = subjectService.getOne(wrapper);
        return twoSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
