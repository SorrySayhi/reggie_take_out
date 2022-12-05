package com.javaitem.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.javaitem.reggie.domain.User;
import com.javaitem.reggie.service.UserService;
import com.reggie.util.CommonResult;
import com.reggie.util.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.COMM_FAILURE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author win
 * @create 2022/11/25 17:45
 */
@RestController
@Slf4j
@RequestMapping("/front/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public CommonResult<User> login(HttpServletRequest request, @RequestBody Map map){
        String sms = (String)request.getSession().getAttribute("SMS");
        String phone = (String)map.get("phone");
        String code = (String)map.get("code");
        log.info(sms);
        if (sms.equals(code)){LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = null;
            user = userService.getOne(queryWrapper);
            if (user==null){
                user = new User();
                user.setPhone(phone);
                boolean save = userService.save(user);
                if (!save){
                    return CommonResult.error("重试");
                }

            }else if(user.getStatus()==0){
                return CommonResult.error("账号被禁用");
            }
            Long id = user.getId();

            request.getSession().removeAttribute("SMS");
            request.getSession().setAttribute("user",id);


            return CommonResult.success(user);
        }else {
            return CommonResult.error("验证码错误");
        }

    }

    @PostMapping("/sms")
    public CommonResult sendMassage(HttpServletRequest request,@RequestBody User user){
        String phone = user.getPhone();
        log.info(phone);
        try {
            String s = SMSUtils.sendMessage();
            log.info(s);
            request.getSession().setAttribute("SMS",s);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(null);

    }

}
