package com.leadtone.riders.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.DateProvider;
import org.springside.modules.utils.Encodes;

import com.leadtone.riders.dao.UserDao;
import com.leadtone.riders.entity.User;

@Component
@Transactional
public class UserService {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	private DateProvider dateProvider = DateProvider.DEFAULT;
	
	@Autowired
	private UserDao userDao;
	
	public void registerUser(User user){
		entryptPassword(user);
		user.setRoles("user");
		user.setCtime(dateProvider.getDate());

		
		userDao.save(user);
	}

	public User findUserByEmail(String email) {
		User user  = userDao.findByEmail(email);
		return user;
	}
	
	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 */
	private void entryptPassword(User user) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));  

		byte[] hashPassword = Digests.sha1(user.getPwd().getBytes(), salt, HASH_INTERATIONS);
		user.setPwd(Encodes.encodeHex(hashPassword));
	}

	public User getUserByUid(Long uid) {
		User user  = userDao.findUserByUid(uid);
		return user;
	}
	
	public void saveUser(User user){
		userDao.save(user);
	}
	
	public User findUserByEmailOrNickname(String searchKey){
		User user = userDao.findUserByEmailOrNickname(searchKey,searchKey);
		return user;
	}
	
}
