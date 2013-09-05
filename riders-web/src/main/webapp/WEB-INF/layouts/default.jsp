<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<title>骑乐汇<sitemesh:title/></title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

<link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">
<link href="${ctx}/static/bootstrap/2.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/jquery-validation/1.11.0/validate.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/styles/default.css" type="text/css" rel="stylesheet" />
<script src="${ctx}/static/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-validation/1.11.0/messages_bs_zh.js" type="text/javascript"></script>


<sitemesh:head/> 
</head>

<body>
	<div class="container">
		<%@ include file="/WEB-INF/layouts/header.jsp"%>
		
		
		<div id="content">
			<div class="btn-group">
			<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
					 车队操作
					<span class="caret"></span>
				</a>
			<ul  class="dropdown-menu">
				<shiro:hasRole name="user">
					<li><a href="${ctx}/team/list">我的车队</a></li>
					<li class="divider"></li>
				</shiro:hasRole>
				<shiro:hasRole name="user">
					<li><a href="${ctx}/team/create">创建车队</a></li>
					<li class="divider"></li>
				</shiro:hasRole>
				<shiro:hasRole name="user">
					<li><a href="${ctx}/user/toFindFriend">寻找好友</a></li>
					<li class="divider"></li>
				</shiro:hasRole>
			</ul>
		</div>
		<div class="btn-group">
			<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
					我的好友
					<span class="caret"></span>
				</a>
			<ul  class="dropdown-menu">
				<shiro:hasRole name="user">
					<li><a href="${ctx}/team/create">创建车队</a></li>
					<li class="divider"></li>
				</shiro:hasRole>
				<shiro:hasRole name="user">
					<li><a href="${ctx}/user/toFindFriend">寻找好友</a></li>
					<li class="divider"></li>
				</shiro:hasRole>
			</ul>
		</div>
			<sitemesh:body/>
		</div>
		<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	</div>
	<script src="${ctx}/static/bootstrap/2.3.0/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>