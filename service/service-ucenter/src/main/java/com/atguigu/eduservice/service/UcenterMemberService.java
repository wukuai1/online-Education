package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.UcenterMember;
import com.atguigu.eduservice.vo.LoginVo;
import com.atguigu.eduservice.vo.RegistryVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-08-03
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(LoginVo loginVo);

    void registry(RegistryVo registryVo);

    UcenterMember getByOpenId(String openid);

    int getCountRegister(String day);
}
