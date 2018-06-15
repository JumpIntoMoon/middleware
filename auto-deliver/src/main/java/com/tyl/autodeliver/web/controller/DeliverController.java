package com.tyl.autodeliver.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-05-25 11:14
 **/
@Controller
@RequestMapping("deliver")
public class DeliverController {

    @RequestMapping("uploadAccount")
    public String index(HttpServletRequest request) {
        return "accountLoader";
    }

    @GetMapping("access")
    public ModelAndView importAccess(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        //封装要显示到视图的数据
        mv.addObject("path", "/importXXX/import");
        //视图名
        mv.setViewName("testImport");
        return mv;
    }
}
