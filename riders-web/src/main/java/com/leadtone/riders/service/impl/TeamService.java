package com.leadtone.riders.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.leadtone.riders.dao.TeamDao;
import com.leadtone.riders.entity.Team;

@Component
@Transactional
public class TeamService {
	
	@Autowired
	private TeamDao teamDao;
	
	
	public void saveTeam(Team team){
		teamDao.save(team);
	}


	public List<Team> getAllList() {
		List<Team> list =(List<Team>) teamDao.findAll();
		return list;
	}
	
	public Team findTeamByTid(String tid){
		Team team  = teamDao.findTeamByTid(Long.parseLong(tid));
		return team;
	}
}
