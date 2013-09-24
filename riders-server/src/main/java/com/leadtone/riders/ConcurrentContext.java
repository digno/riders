package com.leadtone.riders;

import java.util.concurrent.ConcurrentHashMap;

import com.leadtone.riders.server.RiderChannel;

public class ConcurrentContext {

	private static volatile ConcurrentHashMap<String, RiderChannel> map = null;
	private static volatile ConcurrentHashMap<Integer, RiderChannel> cMap  = null;

	public static ConcurrentHashMap<String, RiderChannel> getChannelMapInstance() {
		if (map == null) {
			map = new ConcurrentHashMap<String, RiderChannel>();
		}
		return map;
	}
	
	public static ConcurrentHashMap<Integer, RiderChannel> getAvailableChannelMapInstance() {
		if (cMap == null) {
			cMap = new ConcurrentHashMap<Integer, RiderChannel>();
		}
		return cMap;
	}

}
