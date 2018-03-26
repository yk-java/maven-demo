package com.glens.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ims.common.ConfigHolder;

@Controller
@RequestMapping("")
public class MainController extends BaseController{
	
	/**
	 * 跳转到登录界面
	 */
	@RequestMapping("login") 
	public ModelAndView login(){
		ModelAndView view = new ModelAndView("login.jsp");
		return view;
	}
	
	/**
	 * 注销
	 */
	@RequestMapping("logout")
	public String logout(){
		session.removeAttribute(ConfigHolder.getValue("session.sys.user"));
		return "redirect:/login.do";
	}
	
}
