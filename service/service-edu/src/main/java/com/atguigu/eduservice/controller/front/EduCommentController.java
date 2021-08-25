package com.atguigu.eduservice.controller.front;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.service.EduCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-08-06
 */
@RestController
@RequestMapping("/eduservice/comment")
//@CrossOrigin
public class EduCommentController {

    @Autowired
    private EduCommentService commentService;

    @GetMapping("getAllComment/{page}/{limit}")
    public R getAllComment(@PathVariable() long page,
                           @PathVariable long limit){
        Map<String,Object> map = commentService.pageAllComment(page,limit);

        return R.ok().data(map);
    }
    @PostMapping("saveComment")
    public R saveComment(@RequestBody EduComment comment){
        boolean result = commentService.save(comment);
        if(result){
            return R.ok();
        }else {
            return R.error().message("添加评论失败");
        }

    }

}

