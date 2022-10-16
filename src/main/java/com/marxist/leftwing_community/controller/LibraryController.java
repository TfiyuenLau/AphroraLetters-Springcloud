package com.marxist.leftwing_community.controller;

import com.marxist.leftwing_community.util.OperateLog;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LibraryController {

    //访问文库
    @OperateLog(operateDesc = "访问文库")
    @RequestMapping("/library")
    public String toLibrary() {

        return "library";
    }

}
