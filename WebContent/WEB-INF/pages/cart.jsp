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
<script type="text/javascript">

	$(function(){
		$(".delete").click(function(){
			var $tr = $(this).parent().parent();
			var title = $.trim($tr.find("td:first").text());
			var flag = confirm("确定要删除" + title + "的信息吗？")
			
			if(flag) {
				return true;
			}
			
			return false;
		});
		
		//ajax修改单个商品的数量：
		//1.获取页面中所有的text，并为其添加onchange响应函数
		$(":text").change(function(){
			var quantityVal = $.trim(this.value);
			
			var flag = false;
			
			var reg = /^\d+$/g;
			var quantity = -1;
			if(reg.test(quantityVal)) {
				quantity = parseInt(quantityVal);
				if(quantity >= 0) {
					flag = true;
				}
			}
			
			if(!flag) {
				alert("输入的数量不合法!");
				$(this).val($(this).attr("class"));
				return;
			}
			
			var $tr = $(this).parent().parent();
			var title = $.trim($tr.find("td:first").text());
			
			if(quantity == 0) {
				var flag2 = confirm("确定要删除" + title + "吗？")
				if(flag2) {
					var $a = $tr.find("td:last").find("a");
					
					$a[0].onclick();
					
					return;
				}
			}
			
			var flag = confirm("确定要修改" + title + "的数量吗？")
			
			if(!flag) {
				$(this).val($(this).attr("class"));
				return;
			}
		//2.请求地址为：bookServlet
			var url = "bookServlet";
			
		//3.请求参数为：method:updateItemQuantity，id:name属性值，quantity:val，time:new Date()
			var idVal = $.trim(this.name);
			var args = {"method":"updateItemQuantity", "id":idVal, "quantity":quantityVal, "time":new Date()};
		
		//4.在updateItemQuantity方法中，获取quanity，id，再获取购物车对象，调用service的方法做修改
		//5.传回JSON数据：bookNumber:xx，totalMoney
		
		//6.更新当前页面的bookNumber和totalMoney
			$.post(url, args, function(data){
				var bookNumber = data.bookNumber;
				var totalMoney = data.totalMoney;
				
				$("#totalMoney").text("总金额￥ " + totalMoney);
				$("#bookNumber").text("您的购物车中共有 " + bookNumber + "本书。");
			},"JSON");
		
		});
		
	})

</script>

</head>
<body>

	<br><br>
	<div id="bookNumber">
		您的购物车中共有 ${sessionScope.ShoppingCart.bookNumber } 本书。
	</div>
	<br><br>
	<table>
		<tr>
			<td>Title</td>
			<td>Quantity</td>
			<td>Price</td>
			<td>&nbsp;</td>
		</tr>
	
	<c:forEach items="${sessionScope.ShoppingCart.items }" var="item">
	
		<tr>
			<td>${item.book.title }</td>
			<td>
				<input class="${item.quantity}" type="text" size="1" name="${item.book.id }" value="${item.quantity }">
			</td>
			<td>${item.book.price }</td>
			<td><a href="bookServlet?method=remove&pageNo=${param.pageNo }&id=${item.book.id }" class="delete">删除</a></td>
		</tr>
	</c:forEach>
	
		<tr>
			<td id="totalMoney">总金额￥ ${sessionScope.ShoppingCart.totalMoney }</td>
		</tr>
		<tr>
			<td colspan="4">
				<a href="bookServlet?method=getBooks&pageNo=${param.pageNo }">继续购物</a>
				&nbsp;
				
				<a href="bookServlet?method=clear">清空购物车</a>
				&nbsp;
				
				<a href="bookServlet?method=forwardPage&page=cash.jsp">结账</a>
				&nbsp;
			</td>
		</tr>
	
	</table>
	


</body>
</html>