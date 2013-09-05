<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>邀请好友</title>
	<script>
		$(document).ready(function() {
			$("#inventation").bind("click",function(){
				var tid = $("#tid").val();
				var uids = "";
				var uidArray = $("input[type='checkbox']");
				for(var i=0;i<uidArray.length;i++){
					if(uidArray[i].checked){
						uids += uidArray[i].value +","
					}
				}
				if(uids.charAt(uids.length-1)==","){
					uids = uids.substring(0,uids.length-1);
				}
				
				 window.location.href="${ctx}/team/addTeamMember?tid="+tid+"&uids="+uids+"&time="+new Date();
			});
		});
	</script>
</head>

<body>
	<form id="inputForm"  class="form-horizontal">
		<input type="hidden" id="tid" name="tid" value="${tid }">
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<c:if test="${empty list }">
			<tr><td>您还没有好友，赶快添加好友吧！</td></tr>
		</c:if>
		<c:if test="${!empty list }">
		<tr><th>好友列表</th></tr>
		 	<c:forEach items="${list }" var="user">
		 	<tr>
			 	<td>
				 	<input  type="checkbox" value="${user.uid }" onclick="formname.uid.checked=!this.checked"/>${user.nickname }
				<!-- 	<input id="ckAfter" checked="checked" type="checkbox" value="${user.uid }" onclick="formname.ckBefor.checked=!this.checked"/>${user.nickname }  -->
			 	</td>
		 	</tr>
		 	</c:forEach>
		 	<tr>
		 		<td><input type="button" id="inventation"  value="邀请"></td>
		 	</tr>
		 </c:if>
		</table>
	</form>
</body>
</html>
