<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>车队列表</title>
</head>

<body>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>车队id</th><th>车队名</th><th>车队队长</th><th>车队申明</th><th>建队时间</th><th>邀请</th></tr></thead>
		<tbody>
		<c:forEach items="${list}" var="team">
			<tr>
				<td>${team.tid}</td>
				<td>${team.teamname }</td>
				<td>${team.leader.nickname}</td>
				<td>${team.declaration}</td>
				<td>
					<fmt:formatDate pattern='yyyy-MM-dd HH:mm:ss' value='${team.ctime}'></fmt:formatDate>
				</td>
				<td><a href="${ctx}/team/toAddTeamMember?tid=${team.tid} ">邀请队员</a></td>
				<td><a href="${ctx}/team/toShowTeamMember?tid=${team.tid} ">查看队员</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
