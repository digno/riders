package com.leadtone.riders.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.leadtone.riders.entity.Team;
import com.leadtone.riders.entity.User;
import com.leadtone.riders.filter.ShiroDbRealm.ShiroUser;
import com.leadtone.riders.service.impl.TeamService;
import com.leadtone.riders.service.impl.UserService;

@Controller
@RequestMapping(value="team")
public class TeamController {
	
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="create",method = RequestMethod.GET)
	public String  toCreate(Model model){
		ShiroUser su = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		User user  = userService.findUserByEmail(su.getUsername());
		Team team = new Team();
		team.setLeader(user);
		model.addAttribute("team", team);  
		model.addAttribute("action", "create");  //标记是添加操作
		return "team/teamForm";
	}
	
	@RequestMapping(value="create",method = RequestMethod.POST)
	public String create(@Valid Team team,RedirectAttributes redirectAttributes,HttpServletRequest request){
		String uid = (String)request.getParameter("leader");
		User leader = userService.getUserByUid(Long.parseLong(uid));
		Date date = new Date();
		team.setCtime(date);
		team.setLeader(leader);
		team.setMaster_id(leader.getUid());
		teamService.saveTeam(team);
		redirectAttributes.addFlashAttribute("message", "添加team成功");
		return "redirect:/team/list";
	}
	
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String list(Model model){
		List<Team> teamList = teamService.getAllList();
		List<Team> List = new ArrayList<Team>();
		for(Team team:teamList){
			User leader = getUser(team.getMaster_id());
			team.setLeader(leader);
			List.add(team);
		}
		model.addAttribute("list", List);
		
		return "team/teamList";
	}
	
	@RequestMapping(value="toAddTeamMember",method = RequestMethod.GET)
	public ModelAndView toAddTeamMember(@RequestParam("tid") String tid){
		ModelAndView mav = new ModelAndView();
		ShiroUser su = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		User user  = userService.findUserByEmail(su.getUsername());
		List<User> list = user.getFriendList();
		mav.addObject("list", list);
		mav.addObject("tid",tid);
		mav.setViewName("team/addTeamMember");
		return mav;
	}
	
	@RequestMapping(value="addTeamMember",method = RequestMethod.GET)
	public String addTeamMember(@RequestParam("tid") String tid,@RequestParam("uids") String uids){
		Team team = teamService.findTeamByTid(tid);
		String[] uidArr = uids.split(",");
		for(String uid:uidArr){
			User user = userService.getUserByUid(Long.parseLong(uid));
			if(!team.getTeamMembers().contains(user)){
				team.getTeamMembers().add(user);
			}
		}
		teamService.saveTeam(team);
		return "redirect:/team/list";
	}
	
	@RequestMapping(value="toShowTeamMember",method = RequestMethod.GET)
	public ModelAndView toShowTeamMember(@RequestParam("tid") String tid){
		ModelAndView mav = new ModelAndView();
//		ShiroUser su = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
//		User user  = userService.findUserByEmail(su.getUsername());
		Team team = teamService.findTeamByTid(tid);
		List<User> list = team.getTeamMembers();
		mav.addObject("list", list);
		mav.addObject("team",team);
		mav.setViewName("team/showTeamMember");
		return mav;
	}
	
	
	
	@ModelAttribute("user")
	public User getUser(@RequestParam(value = "uid", required = false) Long uid) {
		if (uid != null) {
			return userService.getUserByUid(uid);
		}
		return null;
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("leader");
	}


	
}
