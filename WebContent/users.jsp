<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<br><br>
	<form action="userServlet" method="post">
		username:<input type="text" name="username" />
		<input type="submit" value="Submit"/>
	</form>

</body>
</html>