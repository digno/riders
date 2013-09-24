package com.leadtone.riders.service.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.utils.DateProvider;

import com.leadtone.riders.MsgConstants;
import com.leadtone.riders.RidersBiz;
import com.leadtone.riders.dao.ActivityDao;
import com.leadtone.riders.dao.UserDao;
import com.leadtone.riders.entity.Activity;
import com.leadtone.riders.entity.User;
import com.leadtone.riders.protocol.beans.Content;
import com.leadtone.riders.protocol.converter.ResponseContentHelper;

@Service
public class ActivityServiceImpl {

	private Logger log = Logger.getLogger(ActivityServiceImpl.class);

	@Autowired
	private ActivityDao activityDao;
	
	@Autowired
	private UserDao userDao;

	
	private DateProvider dateProvider = DateProvider.DEFAULT;
	
	
	@RidersBiz("join_activity")
	public Content joinActivity(HashMap<String, Object> contentMap) {
		Content resultContent = null;
		try {
		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("joinActivity Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}
	
	@RidersBiz("add_activity")
	public Content addActivity(HashMap<String, Object> contentMap) {
		Content resultContent = null;
		try {
			Activity saveActivity = new Activity();
			BeanUtils.populate(saveActivity, contentMap);
			saveActivity.setCtime(dateProvider.getDate());
			Long id = Long.valueOf(contentMap.get("owner_id").toString());
			User owner = userDao.findOne(id);
			saveActivity.setOwner(owner);
			saveActivity.setAtype(0);
			activityDao.save(saveActivity);
			resultContent = ResponseContentHelper.genSimpleResponseContentWithoutType(
					MsgConstants.ERROR_CODE_0,"add activity successed!");
		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("addActivity Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}
	
	@RidersBiz("list_activities")
	public Content getActivitiesList(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Iterable<Activity> activities = activityDao.findAll();
			List<HashMap<String,String>>  tList = new ArrayList<HashMap<String,String>>();
			List<HashMap<String,String>>  gList = new ArrayList<HashMap<String,String>>();
			List<HashMap<String,String>>  uList = new ArrayList<HashMap<String,String>>();
			for (Activity activity : activities){
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put("owner",activity.getOwner()!=null? activity.getOwner().getNickname():"");
				temp.put("expire", activity.getExpireTime());
				temp.put("title", activity.getTitle());
				temp.put("content", activity.getContent());
				if (activity.getCtime()!=null){
					temp.put("ctime", DateFormatUtils.format(activity.getCtime(), "yyyy-MM-dd hh:mm:ss"));
				}
				if(activity.getAtype() == 2){
					tList.add(temp);
				} else if (activity.getAtype() == 1){
					gList.add(temp);
				}else{
					uList.add(temp);
				}
			}
			
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap.put(MsgConstants.TEAMS, tList);
			resultMap.put(MsgConstants.GROUPS, gList);
			resultMap.put(MsgConstants.USERS, uList);
			resultContent.setData(resultMap);
		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("getActivitiesList Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

}
