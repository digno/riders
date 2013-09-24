package com.leadtone.riders.service.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.DateProvider;
import org.springside.modules.utils.Encodes;

import com.leadtone.riders.MsgConstants;
import com.leadtone.riders.RidersBiz;
import com.leadtone.riders.dao.TeamDao;
import com.leadtone.riders.dao.UserDao;
import com.leadtone.riders.entity.Team;
import com.leadtone.riders.entity.User;
import com.leadtone.riders.protocol.beans.Content;
import com.leadtone.riders.protocol.converter.ResponseContentHelper;

@Service
public class UserServiceImpl {

	private Logger log = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private TeamDao teamDao;
	

	private static String DEFAULT_ROLES = "user";
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	private DateProvider dateProvider = DateProvider.DEFAULT;

	@RidersBiz("login")
	public Content authUser(HashMap<String, Object> contentMap) {
		Content resultContent = null;
		try {
			String email = (String) contentMap.get("email");
			String pwd = (String) contentMap.get("pwd");
			
			User existUser = userDao.findByEmail(email);

			if (existUser != null && !"".endsWith(existUser.getEmail())) {
				if(isEqualPassword(existUser, pwd)){
					resultContent = new Content();
					HashMap<String, Object> resultMap = new HashMap<String, Object>();
					HashMap<String, String> temp = boxingUserData(existUser);
					resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
					resultMap.put(MsgConstants.USER, temp);
					resultContent.setData(resultMap);
				}else{
					resultContent = ResponseContentHelper
							.genSimpleResponseContentWithoutType(
									MsgConstants.ERROR_CODE_3, "incorrect password!");
				}
				
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_2, "no such user!");
			}

		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("AuthUser Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	@RidersBiz("register")
	public Content register(HashMap<String, Object> contentMap) {
		Content resultContent = null;
		try {
			User saveUser = new User();
			BeanUtils.populate(saveUser, contentMap);
			User existUser = userDao.findByEmail(saveUser.getEmail());
			if (existUser != null && !"".endsWith(existUser.getEmail())) {
				resultContent = ResponseContentHelper.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_2,"user already registered!");

			} else {
				entryptPassword(saveUser);
				saveUser.setRoles(DEFAULT_ROLES);
				saveUser.setCtime(dateProvider.getDate());
				userDao.save(saveUser);
				resultContent = ResponseContentHelper.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0,"user register successed!");
			}
		} catch (Exception e) {
			resultContent = ResponseContentHelper.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("AuthUser Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	
	@RidersBiz("getUserList")
	public Content getUserList(HashMap<String, Object> contentMap) {

		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Iterable<User> users = userDao.findAll();
			List<HashMap<String,String>>  userList = new ArrayList<HashMap<String,String>>();
			for (User user : users){
				HashMap<String, String> temp = boxingUserData(user);
				userList.add(temp);
			}
			
			Iterable<Team> teams = teamDao.findAll();
			List<HashMap<String,String>>  teamList = new ArrayList<HashMap<String,String>>();
			for (Team team : teams){
				HashMap<String, String> temp = boxingTeamData(team);
				teamList.add(temp);
			}
			
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap.put(MsgConstants.USERS, userList);
			resultMap.put(MsgConstants.TEAMS, teamList);
			resultContent.setData(resultMap);
			
		} catch (Exception e) {
			resultContent = ResponseContentHelper.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("getUserList Error : " + e.getMessage());
			e.printStackTrace();
			log.error(e);
		}
		return resultContent;
	}

	private HashMap<String, String> boxingTeamData(Team team) {
		HashMap<String,String> temp = new HashMap<String,String>();
		temp.put("id", String.valueOf(team.getTid()));
		temp.put("teamname", team.getTeamname());
		temp.put("declaration", team.getDeclaration());
		temp.put("ttype", team.getTtype().toString());
		if(team.getCtime()!=null){
			temp.put("ctime", DateFormatUtils.format(team.getCtime(), "yyyy-MM-dd hh:mm:ss"));
		}
		temp.put("membersnum",  String.valueOf(0));
		return temp;
	}

	private HashMap<String, String> boxingUserData(User user) {
		HashMap<String,String> temp = new HashMap<String,String>();
		temp.put("id", String.valueOf(user.getUid()));
		temp.put("nickname", user.getNickname());
		temp.put("email", user.getEmail());
		temp.put("signature", user.getSignature());
		temp.put("brand", user.getBrand());
		temp.put("mobile", user.getMobile());
		temp.put("picture", user.getPicture());
		temp.put("tools", user.getTools());
		if(user.getBrithday()!=null){
			temp.put("brithday", DateFormatUtils.format(user.getBrithday(), "yyyy-MM-dd"));
		}
		if(user.getCtime()!=null){
			temp.put("ctime", DateFormatUtils.format(user.getCtime(), "yyyy-MM-dd hh:mm:ss"));
		}
		temp.put("sex",  String.valueOf(user.getSex()));
		return temp;
	}
	
	
	
	@RidersBiz("getUserDetail")
	public Content getUserDetail(HashMap<String, Object> contentMap) {

		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Long userId  =  Long.valueOf(contentMap.get("id").toString());
			List<Team> teams = teamDao.findTeamByUid(userId);
			List<HashMap<String,String>>  teamList = new ArrayList<HashMap<String,String>>();
			for (Team team : teams){
				HashMap<String, String> temp = boxingTeamData(team);
				teamList.add(temp);
			}
			
			
			User user = userDao.findOne(userId);
			HashMap<String, String> userMap = boxingUserData(user);
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap.put(MsgConstants.USER, userMap);
			resultMap.put(MsgConstants.TEAMS, teamList);
			resultContent.setData(resultMap);
			
		} catch (Exception e) {
			resultContent = ResponseContentHelper.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("getUserDetail Error : " + e.getMessage());
			e.printStackTrace();
			log.error(e);
		}
		return resultContent;
	}
	
	
	public Content addFriend(HashMap<String, Object> contentMap) {
		Content resultContent = null;
		try {

		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("AuthUser Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	public Content removeFriend(HashMap<String, Object> contentMap) {
		Content resultContent = null;
		try {

		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("AuthUser Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 */
	private void entryptPassword(User user) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(user.getPwd().getBytes(), salt,
				HASH_INTERATIONS);
		user.setPwd(Encodes.encodeHex(hashPassword));
	}
	
	private boolean isEqualPassword(User user,String pwd) {
		byte[] salt = Encodes.decodeHex(user.getSalt());
		byte[] hashPassword = Digests.sha1(pwd.getBytes(), salt,
				HASH_INTERATIONS);
		return user.getPwd().equals(Encodes.encodeHex(hashPassword));
	}

	public static void main(String[] args) {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("email", "a");
		map.put("pwd", "b");

		User user = new User();
		try {
			BeanUtils.populate(user, map);
			System.out.println(user.getEmail());
			System.out.println(user.getPwd());
			System.out.println(user.getMobile());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
