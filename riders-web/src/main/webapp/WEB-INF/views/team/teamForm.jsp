<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>车队管理</title>
	
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#task_title").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/team/${action}" method="post" class="form-horizontal">
		<fieldset>
			<legend><small>管理车队</small></legend>
			<div class="control-group">
				<label for="leader" class="control-label">车队队长:</label>
				<div class="controls">
					<select name="leader">
						<option value="${team.leader.uid }">${team.leader.email }</option>
					</select>
				<!-- 	<input type="text" id="leader" name="leader"  value="${team.leader.uid}" class="input-large required" minlength="3"/>   -->
				</div>
			</div>
			<div class="control-group">
				<label for="teamname" class="control-label">车队名:</label>
				<div class="controls">
					<input type="text" id="teamname" name="teamname"  value="${team.teamname}" class="input-large required"/>
				</div>
			</div>
			<div class="control-group">
				<label for="declaration" class="control-label">车队申明:</label>
				<div class="controls">
					<textarea id="declaration" name="declaration" class="input-large">${team.declaration}</textarea>
				</div>
			</div>	
			<div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;	
				<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
		</fieldset>
	</form>
</body>
</html>
