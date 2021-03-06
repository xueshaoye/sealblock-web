<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utils" tagdir="/WEB-INF/tags/utils"%>
<c:if test="${not empty errorMsg}">
<script>
$(function(){
	$.messager.alert('错误','${errorMsg}');
});
</script>
</c:if>
<form class="search-form" action="<c:url value="/admin/downBalanceTransfer/index" />" method="post">
	<div class="search-group">
		<label for="keywords">关键字：</label>
		<input type="text" class="input-box search-input-box" placeholder="关键字" name="keywords" value="${search.keywords}"></input>
	</div>
	<input type="hidden" name="pageIndex" value="${search.pageIndex}"></input>
	<input type="hidden" name="pageSize" value="${search.pageSize}"></input>
	<input type="submit" value="搜索" class="btn"></input>
	<a href="<c:url value="/admin/downBalanceTransfer/add" />" class="btn btn-orange pull-right operate-detail" >新建</a>
</form>
<table class="list-wapper">
	<tr class="list-header">
		<td>公链类型</td>
		<td>币标识</td>
		<td>币名称</td>
		<td>代理商</td>
		<td>机构号</td>
		<td>机构名称</td>
		<td>交易用户标识</td>
		<td>平台流水号</td>
		<td>平台日期</td>
		<td>下游交易流水号</td>
		<td>下游交易时间</td>
		<td>下游异步通知地址</td>
		<td>下游接收方货币地址</td>
		<td>交易额</td>
		<td>交易备注信息</td>
		<td>交易矿工费</td>
		<td>用户货币地址</td>
		<td>用户姓名</td>
		<td>用户身份证号</td>
		<td>用户联系电话</td>
		<td>保留缺省域1</td>
		<td>保留缺省域2</td>
		<td>上游流水号</td>
		<td>交易状态</td>
		<td>交易状态描述</td>
		<td>状态更新时间</td>
		<td>公网交易状态</td>
		<td>公网状态描述</td>
		<td>公网更新时间</td>
		<td>操作</td>
	</tr>
	<c:forEach var="item" items="${res.items}" varStatus="itemStatus" >
		<tr class="list-item">
			<td>
				<c:out value="${item.channel}" />
			</td>
			<td>
				<c:out value="${item.coin}" />
			</td>
			<td>
				<c:out value="${item.coinName}" />
			</td>
			<td>
				<c:out value="${item.agentNo}" />
			</td>
			<td>
				<c:out value="${item.mchNo}" />
			</td>
			<td>
				<c:out value="${item.mchName}" />
			</td>
			<td>
				<c:out value="${item.userNo}" />
			</td>
			<td>
				<c:out value="${item.transNo}" />
			</td>
			<td>
				<c:out value="${item.transDate}" />
			</td>
			<td>
				<c:out value="${item.tradeNo}" />
			</td>
			<td>
				<c:out value="${item.orderDate}" />
			</td>
			<td>
				<c:out value="${item.notifyUrl}" />
			</td>
			<td>
				<c:out value="${item.receivCoinAddr}" />
			</td>
			<td>
				<c:out value="${item.price}" />
			</td>
			<td>
				<c:out value="${item.remark}" />
			</td>
			<td>
				<c:out value="${item.subUserFee}" />
			</td>
			<td>
				<c:out value="${item.userCoinAddr}" />
			</td>
			<td>
				<c:out value="${item.userName}" />
			</td>
			<td>
				<c:out value="${item.userCertId}" />
			</td>
			<td>
				<c:out value="${item.userPhone}" />
			</td>
			<td>
				<c:out value="${item.merResv1}" />
			</td>
			<td>
				<c:out value="${item.merResv2}" />
			</td>
			<td>
				<c:out value="${item.upTransNo}" />
			</td>
			<td>
				<c:out value="${item.status}" />
			</td>
			<td>
				<c:out value="${item.statusDesc}" />
			</td>
			<td>
				<c:out value="${item.statusChgDate}" />
			</td>
			<td>
				<c:out value="${item.fundStatus}" />
			</td>
			<td>
				<c:out value="${item.fundStatusDesc}" />
			</td>
			<td>
				<c:out value="${item.fundChgDate}" />
			</td>
			<td>
				<a href="<c:url value="/admin/downBalanceTransfer/add?id=${item.id}" />" class="choose-link operate-detail">编辑</a>
				<!-- a href="<c:url value="/admin/downBalanceTransfer/delete/${item.id}" />" class="operate-delete">删除</a-->
			</td>
		</tr>
	</c:forEach>
</table>
<utils:pager/>
