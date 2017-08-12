package com.macheteteam.bogo.controller;

import com.macheteteam.bogo.commons.JschUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Created by Lenny on 2017/8/12.
 */
@Controller
public class HomeController {

    @RequestMapping("/home/index1")
    public String index() {
        return "index";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        StringBuilder sb = new StringBuilder();
        JschUtil.execCommand("ls", in -> {
            byte[] tmp = new byte[1024];
            try {
                int i ;
                while ((i = in.read(tmp, 0, 1024)) > 0) {
                    sb.append(new String(tmp, 0, i));
                    System.out.println(new String(tmp, 0, i));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return sb.toString();
    }
}
