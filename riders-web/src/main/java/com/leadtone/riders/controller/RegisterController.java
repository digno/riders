package com.leadtone.riders.controller;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.leadtone.riders.entity.User;
import com.leadtone.riders.service.impl.UserService;

@Controller
public class RegisterController {
	public static Logger log = Logger.getLogger(RegisterController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value="register",method = RequestMethod.GET)
	public String registerForm() {
		return "user/register";
	}

	@RequestMapping(value="register",method = RequestMethod.POST)
	public String register(@Valid User user, RedirectAttributes redirectAttributes) {
		log.info(" begin to register");
		userService.registerUser(user);
		log.info("222222222222222222222");
		redirectAttributes.addFlashAttribute("username", user.getEmail());
		log.info("33333333333333333333");
		return "redirect:/login";
	}

	/**
	 * Ajax请求校验email是否唯一。
	 */
	@RequestMapping(value = "/register/checkEmail")
	@ResponseBody
	public String checkEmail(@RequestParam("email") String email) {
		if (userService.findUserByEmail(email) == null) {
			return "true";
		} else {
			return "false";
		}
	}
	
//	@InitBinder  
//	//此处的参数也可以是ServletRequestDataBinder类型  
//	public void initBinder(WebDataBinder binder) throws Exception {  
//	    //注册自定义的属性编辑器  
//	    //1、日期  
//	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//	    CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
//	    //表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换  
//	    binder.registerCustomEditor(Date.class, dateEditor);      
//	    //自定义的电话号码编辑器(和【4.16.1、数据类型转换】一样)  
////	    binder.registerCustomEditor(User, new MyPropertyEditor());  
//	}  

}