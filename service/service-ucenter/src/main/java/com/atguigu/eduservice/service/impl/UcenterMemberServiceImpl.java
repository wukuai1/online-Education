package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.MD5;
import com.atguigu.commonutils.exception.GuliException;
import com.atguigu.commonutils.jwt.JwtUtils;
import com.atguigu.eduservice.entity.UcenterMember;
import com.atguigu.eduservice.mapper.UcenterMemberMapper;
import com.atguigu.eduservice.service.UcenterMemberService;
import com.atguigu.eduservice.vo.LoginVo;
import com.atguigu.eduservice.vo.RegistryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-08-03
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private UcenterMemberMapper ucenterMemberMapper;

    @Override
    public String login(LoginVo loginVo) {

        if(StringUtils.isEmpty(loginVo.getMobile())||StringUtils.isEmpty(loginVo.getPassword())){
            throw new GuliException(20001,"用户名和密码不能为空");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile",loginVo.getMobile());
        UcenterMember user = baseMapper.selectOne(queryWrapper);

        String password = MD5.encrypt(loginVo.getPassword());
        if(StringUtils.isEmpty(user.getPassword())||!password.equals(user.getPassword())){
         throw new GuliException(20001,"密码错误");
        }

        if(user.getIsDisabled()){
            throw new GuliException(20001,"error");
        }

        String token = JwtUtils.getJwtToken(user.getId(),user.getNickname());


        return token;
    }

    @Override
    public void registry(RegistryVo registryVo) {;
        String code=registryVo.getCode();
        String mobile=registryVo.getMobile();
        String password=registryVo.getPassword();
        String nickname=registryVo.getNickname();


        if(StringUtils.isEmpty(mobile)||
                StringUtils.isEmpty(password)||
                StringUtils.isEmpty(nickname)||
                StringUtils.isEmpty(code)){
            throw new GuliException(20001,"信息不能为空");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile",mobile);
        int result = baseMapper.selectCount(queryWrapper);
        if(result!=0){
            throw new GuliException(20001,"收集号码已存在");
        }

        String code1 = redisTemplate.opsForValue().get("code");
//        String code1 = "111111";
        if(!code.equals(code1)){
            throw new GuliException(20001,"验证码错误");
        }


        registryVo.setPassword(MD5.encrypt(password));
        UcenterMember ucenterMember = new UcenterMember();
        BeanUtils.copyProperties(registryVo,ucenterMember);

        int count = baseMapper.insert(ucenterMember);
        if(count!=1){
            throw new GuliException(20001,"注册用户异常");
        }

    }

    @Override
    public UcenterMember getByOpenId(String openid) {
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);

        UcenterMember member = baseMapper.selectOne(queryWrapper);
        return member;
    }

    @Override
    public int getCountRegister(String day) {
       int count =  ucenterMemberMapper.getCountRegister(day);

        return count;
    }


}
