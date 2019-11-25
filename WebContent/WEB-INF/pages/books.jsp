<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="script/jquery-2.2.4.js"></script>
<script type="text/javascript">
	
	$(function(){
		$("#pageNo").change(function(){
			var val = $(this).val();
			val = $.trim(val);
			
			// 校验 val 是否为数字
			var flag = false;
			var reg = /^\d+$/g;
			var pageNo = 0;
			
			if(reg.test(val)){
				// 效验 val 在一个合法范围内 1-totalPageNumber
				pageNo = parseInt(val);
				if(pageNo >= 1 || pageNo <= parseInt("${bookpage.totalPageNumber }")) {
					flag = true;
				}
			}
			
			if(!flag){
				alert("输入的不是合法的页码");
				$(this).val("");
				return;
			}
			
			// 页面跳转
			var href = "bookServlet?method=getBooks&pageNo=" + pageNo + "&" + $(":hidden").serialize();
			window.location.href = href;
		});
	})
	
</script>
<%@ include file="/commons/queryCondition.jsp" %>
</head>
<body>

	<c:if test="${title != null}">
		已经将${title}放入到购物车中
		<br><br>
	</c:if>
	<c:if test="${!empty sessionScope.ShoppingCart.books }">
		您的购物车中有${sessionScope.ShoppingCart.bookNumber }本书, <a href="bookServlet?method=forwardPage&page=cart.jsp&pageNo=${bookpage.pageNo }">查看购物车</a>
	</c:if>
	
	<br><br>
	<form action="bookServlet?method=getBooks" method="post">
		Price:
		<input type="text" size="1" name="minPrice"> - 
		<input type="text" size="1" name="maxPrice"> - 
		
		<input type="submit" value="submit">
		
	</form>
	
	<br><br>
	<table>
	
		<c:forEach items="${bookpage.list }" var="book">
			<tr>
				<td>
					<a href="bookServlet?method=getBook&pageNo=${bookpage.pageNo }&id=${book.id}">${book.title }</a>
					<br>
					${book.author }
				</td>
				<td>${book.price }</td>
				<td><a href="bookServlet?method=addToCart&pageNo=${bookpage.pageNo }&id=${book.id}&title=${book.title}">加入购物车</a></td>
			</tr>
		</c:forEach>
	
	</table>
	
	<br><br>
	共${bookpage.totalPageNumber } 页
	&nbsp;&nbsp;
	当前第${bookpage.pageNo } 页
	&nbsp;&nbsp;
	
	<c:if test="${bookpage.hasPrev }">
		<a href="bookServlet?method=getBooks&pageNo=1">首页</a>
		&nbsp;&nbsp;
		<a href="bookServlet?method=getBooks&pageNo=${bookpage.prevPage }">上一页</a>
	</c:if>
	
	<c:if test="${bookpage.hasNext }">
		<a href="bookServlet?method=getBooks&pageNo=${bookpage.nextPage }">下一页</a>
		&nbsp;&nbsp;
		<a href="bookServlet?method=getBooks&pageNo=${bookpage.totalPageNumber }">末页</a>
	</c:if>
	
	&nbsp;&nbsp;
	
	转到<input type="text" size="1" id="pageNo">页
	
	<a href="users.jsp?">查看历史记录</a>

</body>
</html>