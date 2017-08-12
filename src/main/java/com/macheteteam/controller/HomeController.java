package com.macheteteam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Lenny on 2017/8/12.
 */
@Controller
public class HomeController {

    @RequestMapping("/home/index1")
    public String index() {
        return "index";
    }
}
