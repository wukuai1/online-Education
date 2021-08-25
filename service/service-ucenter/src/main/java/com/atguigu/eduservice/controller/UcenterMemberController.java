package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.jwt.JwtUtils;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.eduservice.entity.UcenterMember;
import com.atguigu.eduservice.service.UcenterMemberService;
import com.atguigu.eduservice.vo.LoginVo;
import com.atguigu.eduservice.vo.RegistryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-08-03
 */
@RestController
@RequestMapping("/educenter/member")
//@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo){

       String token = ucenterMemberService.login(loginVo);

        return R.ok().data("token",token);
    }


    @PostMapping("registry")
    public R registry(@RequestBody RegistryVo registryVo){

        ucenterMemberService.registry(registryVo);
        return R.ok();
    }

    @GetMapping("getUserInfo")
    public R getUserInfo(HttpServletRequest request){

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember ucenterMember=   ucenterMemberService.getById(memberId);

        return R.ok().data("items",ucenterMember);
    }

    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id){
        UcenterMember member = ucenterMemberService.getById(id);
        UcenterMemberOrder userOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,userOrder);
        return userOrder;
    }

    @GetMapping("getCountRegister/{day}")
    public int getCountRegister(@PathVariable String day){
        int count = ucenterMemberService.getCountRegister(day);
        return count;
    }
}

