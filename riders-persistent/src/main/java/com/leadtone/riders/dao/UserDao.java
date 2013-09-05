package com.leadtone.riders.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.leadtone.riders.entity.User;

public interface UserDao extends PagingAndSortingRepository<User, Long> {
	User findByEmail(String email);

	User findUserByUid(Long uid);

	User findUserByEmailOrNickname(String email,String nickname);
	
	User findByEmailAndPwd(String email,String pwd);
}
