<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>邀请好友</title>
</head>

<body>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<c:if test="${empty list }">
		<tr><td>该车队还没有骑友参加， 快去邀请吧！</td></tr>
	</c:if>
	<c:if test="${!empty list }">
	<tr><th>车队${team.teamname }队员列表</th></tr>
	 	<c:forEach items="${list }" var="user">
	 	<tr>
		 	<td>
			 	<input id="ckBefor" type="checkbox" value="${user.uid }" onclick="formname.ckBefor.checked=!this.checked"/>${user.nickname }
			<!-- 	<input id="ckAfter" checked="checked" type="checkbox" value="${user.uid }" onclick="formname.ckBefor.checked=!this.checked"/>${user.nickname }  -->
		 	</td>
	 	</tr>
	 	</c:forEach>
	<!-- 
	 	<tr>
	 		<td><input type="button" id="inventation"  value="邀请"></td>
	 	</tr>
	 -->
	 </c:if>
	</table>
</body>
</html>
