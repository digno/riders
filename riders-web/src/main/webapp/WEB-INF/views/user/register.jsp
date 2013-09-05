<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
	<title>骑乐汇用户注册</title>
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
	<script src="${ctx}/static/datepicker/WdatePicker.js" type="text/javascript"></script>">
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#email").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate({
				rules: {
					email: {
						remote: "${ctx}/register/checkEmail"
					}
				},
				messages: {
					email: {
						remote: "该邮件地址已存在"
					}
				}
			});
		});
		var nowDate = new Date();
	</script>
</head>
<body>
	<div class="container">
	<%@ include file="/WEB-INF/layouts/header.jsp"%>
	<form id="inputForm" action="${ctx}/register" method="post" class="form-horizontal">
		<fieldset>
			<legend><small>用户注册</small></legend>
			<div class="control-group">
				<label for="email" class="control-label">邮件地址(登录名):</label>
				<div class="controls">
					<input type="text" id="email" name="email" class="input-large required" minlength="3"/>
				</div>
			</div>
			<div class="control-group">
				<label for="nickname" class="control-label">昵称:</label>
				<div class="controls">
					<input type="text" id="nickname" name="nickname" class="input-large required"/>
				</div>
			</div>
			<div class="control-group">
				<label for="pwd" class="control-label">密码:</label>
				<div class="controls">
					<input type="password" id="pwd" name="pwd" class="input-large required"/>
				</div>
			</div>
			<div class="control-group">
				<label for="confirmPassword" class="control-label">确认密码:</label>
				<div class="controls">
					<input type="password" id="confirmPassword" name="confirmPassword" class="input-large required" equalTo="#pwd"/>
				</div>
			</div>
			<div class="control-group">
				<label for="age" class="control-label">年龄:</label>
				<div class="controls">
					<input type="text" id="age" name="age" class="input-large"/>
				</div>
			</div>
			<div class="control-group">
				<label for="sex" class="control-label">性别:</label>
				<div class="controls">
					<input type="radio" name="sex" value="1" checked/>男
					<input type="radio" name="sex" value="0" />女
				<!-- 	<input type="text" id="sex" name="sex" class="input-large"/> -->
				</div>
			</div>
			<div class="control-group">
				<label for="brithday" class="control-label">生日:</label>
				<div class="controls">
						<input type="text" name="birthday" id="birthday" class="Wdate"  onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:nowDate});" style="width:200px;"/>
				</div>
			</div>
			<div class="control-group">
				<label for="mobile" class="control-label">手机:</label>
				<div class="controls">
					<input type="text" id="mobile" name="mobile" class="input-large required"/>
				</div>
			</div>
			<div class="control-group">
				<label for="picture" class="control-label">头像:</label>
				<div class="controls">
					<input type="text" id="picture" name="picture" class="input-large"/>
				</div>
			</div>
			<div class="control-group">
				<label for="signature" class="control-label">个性签名:</label>
				<div class="controls">
					<textarea id="signature" name="signature" class="input-large"></textarea>
				</div>
			</div>
			<div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;	
				<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
		</fieldset>
	</form>
	<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	</div>
	<script src="${ctx}/static/bootstrap/2.3.0/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>
