package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.mapper.EduCommentMapper;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-08-06
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    @Override
    public Map<String, Object> pageAllComment(long page, long limit) {

        Page<EduComment>  commentPage = new Page<>(page,limit);

        QueryWrapper<EduComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        baseMapper.selectPage(commentPage,queryWrapper);

        List<EduComment> records = commentPage.getRecords();
        long current = commentPage.getCurrent();
        long pages = commentPage.getPages();
        long size = commentPage.getSize();
        long total = commentPage.getTotal();
        boolean hasNext = commentPage.hasNext();
        boolean hasPrevious = commentPage.hasPrevious();

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
}
