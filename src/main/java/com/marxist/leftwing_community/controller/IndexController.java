package com.marxist.leftwing_community.controller;

import com.marxist.leftwing_community.util.OperateLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    //访问导航页面
    @OperateLog(operateDesc = "废弃的导航页")
    @RequestMapping("/index")
    public String index(Model model){

        return "index";
    }

    //访问首页
    @OperateLog(operateDesc = "访问首页")
    @RequestMapping("/home")
    public String home() {

        return "home";
    }

    //error页面
    @OperateLog(operateDesc = "发生错误")
    @RequestMapping("/error_page")
    public String error(){
        return "error";
    }

}
