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
		<h4>User: ${user.username }</h4>
		
		<br><br>
			<table>
				<c:forEach items="${user.trades }" var="trade">
					
					<tr>
					<td>
					<table border="1" >
					
						<tr>
							<td colspan="3">TradTime: ${trade.tradeTime }</td>
						</tr>
		
						<c:forEach items="${trade.items }" var="item">
							
							<tr>
								<td>${item.book.title }</td>
								<td>${item.book.price }</td>
								<td>${item.quantity }</td>
							</tr>
							
						</c:forEach>
						
					</table>
					<br><br>
					</td>
					</tr>
					
				</c:forEach>
			</table>
		

</body>
</html>