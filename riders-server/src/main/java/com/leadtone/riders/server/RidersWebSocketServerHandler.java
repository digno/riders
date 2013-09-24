/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.leadtone.riders.server;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.HOST;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.leadtone.riders.ConcurrentContext;
import com.leadtone.riders.MsgConstants;
import com.leadtone.riders.ServerConstants;
import com.leadtone.riders.protocol.beans.Content;
import com.leadtone.riders.protocol.beans.RidersMessage;
import com.leadtone.riders.protocol.converter.ProtocolConverter;
import com.leadtone.riders.protocol.converter.ResponseContentHelper;
import com.leadtone.riders.service.IProtocolService;

/**
 * Handles handshakes and messages
 */

@Scope("prototype")
@Component
public class RidersWebSocketServerHandler extends
		ChannelInboundMessageHandlerAdapter<Object> {
	private static final Logger logger = Logger
			.getLogger(RidersWebSocketServerHandler.class);

	private ConcurrentHashMap<String, RiderChannel> channelsMap = ConcurrentContext
			.getChannelMapInstance();

	private ConcurrentHashMap<Integer, RiderChannel> cMap  =ConcurrentContext.getAvailableChannelMapInstance();
//	private List<RiderChannel> cList = new ArrayList<RiderChannel>();
	
//	private RiderChannel riderChannel = new RiderChannel();

	private static final String WEBSOCKET_PATH = "/websocket";

	private WebSocketServerHandshaker handshaker;

	@Autowired
	private IProtocolService protocolService;

	@Override
	public void messageReceived(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}

	private void handleHttpRequest(ChannelHandlerContext ctx,
			FullHttpRequest req) throws Exception {
		// Handle a bad request.
		if (!req.getDecoderResult().isSuccess()) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1,
					BAD_REQUEST));
			return;
		}

		// Allow only GET methods.
		if (req.getMethod() != GET) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1,
					FORBIDDEN));
			return;
		}

		// Send the demo page and favicon.ico
		if ("/".equals(req.getUri())) {
			ByteBuf content = RidersWebSocketServerIndexPage
					.getContent(getWebSocketLocation(req));
			FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK,
					content);

			res.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
			setContentLength(res, content.readableBytes());
			logger.info("response content " + res.content().toString());
			sendHttpResponse(ctx, req, res);
			return;
		}
		if ("/favicon.ico".equals(req.getUri())) {
			FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1,
					NOT_FOUND);
			sendHttpResponse(ctx, req, res);
			return;
		}

		// Handshake
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
				getWebSocketLocation(req), null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory
					.sendUnsupportedWebSocketVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), req);
			RiderChannel riderChannel = new RiderChannel();
			riderChannel.setChannelId(ctx.channel().id());
			riderChannel.setChannel(ctx.channel());
			cMap.put(ctx.channel().id(), riderChannel);
//			cList.add(riderChannel);
		}
	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx,
			WebSocketFrame frame) {

		// Check for closing frame
		if (frame instanceof CloseWebSocketFrame) {
			frame.retain();
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame);
			logger.info("received CloseWebSocketFrame");
			cMap.remove(ctx.channel().id());
			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			frame.content().retain();
			ctx.channel().write(new PongWebSocketFrame(frame.content()));
			return;
		}
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(String.format(
					"%s frame types not supported", frame.getClass().getName()));
		}

		// Send the uppercase string back.
		String request = ((TextWebSocketFrame) frame).text();
		if (logger.isInfoEnabled()) {
			logger.info(String.format("Channel %s received %s", ctx.channel()
					.id(), request));
		}
		// ctx.channel().write(new TextWebSocketFrame(request.toUpperCase()));

		try {
			RidersMessage message = ProtocolConverter.marshallBasicMsg(request);
			String result = "";
			Content resultContent;
			// 如果TO== server 则表明为同步操作，否则为异步
			if (ServerConstants.TO_SERVER.equals(message.getTo())) {
				resultContent = protocolService.dispatch(message);
				// 首先判断subject是不是LOGIN 如果是就判断返回值如果是 {"result":0,"msg":"xxx"}
				// 则表明登陆成功，将EMAIL地址和通道信息缓存
				// map 里面存放的是 From 为key ，RiderChannel为Value 的键值对。
				if (ServerConstants.SUBJECT_LOGIN.equalsIgnoreCase(message.getContent().getType())) {
					String resultCode = (String) resultContent.getData().get("result");
					if (Integer.valueOf(resultCode) == 0) {
						RiderChannel riderChannel  = cMap.get(ctx.channel().id());
						riderChannel.setLogined(true);
						String email = (String) message.getContent().getData().get("email");
						channelsMap.put(email, riderChannel);
					}
				}
				message.setContent(resultContent);
				result = ResponseHelper.genSyncResponse(message);
			} else {
				if (protocolService.route(message.getFrom(), message.getTo(),request)) {
					message.setContent(ResponseContentHelper.genSimpleResponseContentWithDefaultType(
									MsgConstants.ERROR_CODE_0,
									"message send successed."));
				} else {
					message.setContent(ResponseContentHelper.genSimpleResponseContentWithDefaultType(
									MsgConstants.ERROR_CODE_1,
									"message send failed."));
				}
				result = ResponseHelper.genAsyncResponse(message);
			}
			logger.info(cMap.size() + " channels in Server");
//			logger.info(channelsMap.size() + " channels in Server");
//			for(String id : channelsMap.keySet()){
//				logger.info(id + " : " + channelsMap.get(id));
//			}
			ctx.channel().write(new TextWebSocketFrame(result));

		} catch (Exception e) {
			logger.error("handleWebSocketFrame error : " + e.getMessage());
			e.printStackTrace();
		}

	}

	private static void sendHttpResponse(ChannelHandlerContext ctx,
			FullHttpRequest req, FullHttpResponse res) {
		// Generate an error page if response getStatus code is not OK (200).
		if (res.getStatus().code() != 200) {
			res.content().writeBytes(
					Unpooled.copiedBuffer(res.getStatus().toString(),
							CharsetUtil.UTF_8));
			setContentLength(res, res.content().readableBytes());
		}

		// Send the response and close the connection if necessary.
		ChannelFuture f = ctx.channel().write(res);
		if (!isKeepAlive(req) || res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	private static String getWebSocketLocation(FullHttpRequest req) {
		return "ws://" + req.headers().get(HOST) + WEBSOCKET_PATH;
	}
}
