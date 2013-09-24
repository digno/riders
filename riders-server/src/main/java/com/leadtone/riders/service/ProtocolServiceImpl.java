package com.leadtone.riders.service;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leadtone.riders.ConcurrentContext;
import com.leadtone.riders.MsgConstants;
import com.leadtone.riders.ServerConstants;
import com.leadtone.riders.ServiceKeyEnum;
import com.leadtone.riders.protocol.beans.Content;
import com.leadtone.riders.protocol.beans.RidersMessage;
import com.leadtone.riders.protocol.converter.ResponseContentHelper;
import com.leadtone.riders.server.RiderChannel;
import com.leadtone.riders.service.biz.ActivityServiceImpl;
import com.leadtone.riders.service.biz.UserServiceImpl;

@Service
public class ProtocolServiceImpl implements IProtocolService {

	private static final Logger log = Logger
			.getLogger(ProtocolServiceImpl.class);

	private ConcurrentHashMap<String, RiderChannel> channelsMap = ConcurrentContext
			.getChannelMapInstance();
	
	private ConcurrentHashMap<Integer, RiderChannel> cMap = ConcurrentContext.getAvailableChannelMapInstance();

	@Autowired
	private IBizService bizService;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private ActivityServiceImpl activityService;

	@Override
	public Content dispatch(RidersMessage message) {
		Content result = null;
		String subject = message.getSubject();
		if (StringUtils.isBlank(subject)) {
			result = ResponseContentHelper.genSimpleResponseContentWithoutType(
					MsgConstants.ERROR_CODE_2, "unsupport subject");
			result.setType(MsgConstants.SERVER);
			return result;

		}
		ServiceKeyEnum mk = ServiceKeyEnum.getEnum(subject.toUpperCase());
		Content requestContent = message.getContent();
		switch (mk) {
		case USER:
			result = bizService.process(requestContent,userService);
			break;
		case FRIEND:
			// result = register(message.getContent());
			break;
		case ACTIVITY:
			result = bizService.process(requestContent,activityService);
			break;
		case COMMENT:
			break;
		case TEAM:
			break;
		case SOS:
			break;
		case REPORT:
			break;
		case SUGGESTION:
			break;
		case VERSION:
			break;
		default:
			result = ResponseContentHelper.genSimpleResponseContentWithoutType(
					MsgConstants.ERROR_CODE_2, "unsupport subject");
			result.setType(MsgConstants.SERVER);
		}
		return result;

	}

	@Override
	public boolean route(String from, String to, String request) {
		if (ServerConstants.TO_ALL.equalsIgnoreCase(to)) {
//			for (String email : channelsMap.keySet()) {
//				if (from.equalsIgnoreCase(email)) {
//					continue;
//				}
//				RiderChannel channel = channelsMap.get(email);
//				writeToChannel(channel, request);
//			}
			for (Integer id : cMap.keySet()){
				
				if (id == cMap.get(id).getChannelId()) {
					continue;
				}
				RiderChannel channel = cMap.get(id);
				writeToChannel(channel, request);
			}
		} else {
			RiderChannel channel = channelsMap.get(to);
			writeToChannel(channel, request);
		}
		return true;
	}

	
	
	private void writeToChannel(RiderChannel channel, String request) {
		try {
			channel.getChannel().write(new TextWebSocketFrame(request));
//			if (channel.isLogined()) {
//				channel.getChannel().write(new TextWebSocketFrame(request));
//			} else {
//				log.info("BAD REQUEST. (not logined)");
//			}
		} catch (Exception e) {
			log.error(e);
		}
	}

}
