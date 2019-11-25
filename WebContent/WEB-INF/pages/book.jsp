<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="script/jquery-2.2.4.js"></script>
<%@ include file="/commons/queryCondition.jsp" %>


</head>
<body>
	
	<br><br>
	Title: ${book.title }
	<br><br>
	Author: ${book.author }
	<br><br>
	Price: ${book.price }
	<br><br>
	PublishingDate: ${book.publishingDate }
	<br><br>
	Remark: ${book.remark }
	<br><br>
	
	<a href="bookServlet?method=getBooks&pageNo=${param.pageNo }">继续购物</a>
	

</body>
</html>