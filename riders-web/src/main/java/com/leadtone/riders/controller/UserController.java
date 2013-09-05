package com.leadtone.riders.controller;

import org.apache.shiro.SecurityUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.leadtone.riders.entity.User;
import com.leadtone.riders.filter.ShiroDbRealm.ShiroUser;
import com.leadtone.riders.service.impl.UserService;

@Controller
@RequestMapping(value="user")
public class UserController {
	public static Logger log = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value="toFindFriend",method = RequestMethod.GET)
	public String toFindFriend() {
		return "user/findFriend";
	}
	
	@RequestMapping(value="findFriend",method = RequestMethod.POST)
	public ModelAndView FindFriend(@RequestParam("email") String email) {
		ModelAndView mav = new ModelAndView();
		User user = userService.findUserByEmailOrNickname(email);
		mav.addObject("user", user);
		mav.setViewName("user/showFindFriend");
		return mav;
	}

	@RequestMapping(value="addFriend/{uid}",method = RequestMethod.GET)
	public String addFriend(@PathVariable long uid, RedirectAttributes redirectAttributes) {
		ShiroUser su = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		User user  = userService.findUserByEmail(su.getUsername());
		User friendUser = userService.getUserByUid(uid);
		if(!user.getFriendList().contains(friendUser)){
			user.getFriendList().add(friendUser);
		}
		userService.saveUser(user);
		redirectAttributes.addFlashAttribute("message", "添加好友" + user.getEmail() + "成功");
		return "redirect:/team/list";
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