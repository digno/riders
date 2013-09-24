package com.leadtone.riders.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.leadtone.riders.entity.Team;

public interface TeamDao extends PagingAndSortingRepository<Team, Long> {
	
	public Team findTeamByTid(long tid);
	
	@Query("select t from Team t left join t.teamMembers u where u.uid = ?1")
	public List<Team> findTeamByUid(long uid);
}
