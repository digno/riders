<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>车队列表</title>
</head>

<body>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<c:if test="${ empty user}">
				<thead><tr><td cols="3"><font color="red" align="center">	sorry,您查找的好友不存在         </font></td></tr></thead></c:if>
			<c:if test="${!empty user}">
			<thead><tr><th>用户名</th><th>昵称</th><th>性别</th><th>操作</th></tr></thead>
				<tbody>
					<tr>
						<td>${user.email}</td>
						<td>${user.nickname}</td>
						<td>
							<c:if test="${user.sex=='1'}">男</c:if>
							<c:if test="${user.sex=='0'}">女</c:if>
						</td>
						<td>
							<a href="${ctx}/user/addFriend/${user.uid} ">添加好友</a>
						</td>
					</tr>
				</tbody>
			</c:if>
		</table>
</body>
</html>
