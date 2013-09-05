package com.leadtone.riders.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.leadtone.riders.entity.Team;

public interface TeamDao extends PagingAndSortingRepository<Team, Long> {
	
	public Team findTeamByTid(long tid);
}
