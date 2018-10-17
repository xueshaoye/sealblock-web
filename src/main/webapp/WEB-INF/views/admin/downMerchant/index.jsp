<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="utils" tagdir="/WEB-INF/tags/utils"%>
<c:if test="${not empty error}">
<script>
$(function(){
	$.messager.alert('错误','${error}');
});
</script>
</c:if>
<style>
.lbl-summary {
display:inline-block;width:70px;font-weight:bold;
}
</style>
<form class="search-form" action="<c:url value="/admin/downMerchant/index" />" method="post">
	<label for="keywords">过滤条件：</label>
	<input type="text" class="input-box search-input-box" placeholder="关键字" name="keywords" value="${search.keywords}"></input>
	
	<label for="mercCategory">商户类别：</label>
	<utils:merccate id="mercCategory" name="mercCategory" cssName="input-box search-input-box" value="${search.mercCategory}"></utils:merccate>
	
	<input type="hidden" name="pageIndex" value="${search.pageIndex}"></input><input type="hidden" name="pageSize" value="${search.pageSize}"></input>
	<div class="space"></div>
	<input type="submit" value="搜索" class="btn"></input>
	<a href="<c:url value="/admin/downMerchant/add" />" class="btn btn-orange pull-right operate-detail">新建</a>
</form>
<form action="<c:url value="/admin/downMerchant/export" />" target="_blank" method="post" class="export-form">
	<input type="submit" value="导出" class="btn btn-orange export-btn" ></input>
</form>
<table class="list-wapper">
	<tr class="list-header">
		<td>类别</td>
		<td>机构号</td>
		<td>机构名称</td>
		<td>结算账户</td>
		<!-- <td>结算卡卡号</td> -->
		<td>开通产品</td>
		<td>信任IP</td>
		<td>创建时间</td>
		<td>状态</td>
		<td>操作</td>
	</tr>
	<c:forEach var="item" items="${res.items}" varStatus="status">
		<tr class="list-item">
			<td>
				<utils:merccate mode="lbl" value="${item.mercCategory}" />
			</td>
			<td>
				<c:out value="${item.mchNo}" />
			</td>
			<td>
				<c:out value="${item.name}" />
			</td>
			<td>
				<c:out value="${item.accCardHolder}" />
			</td>
			<%-- <td>
				<c:out value="${item.accCardNo}" />
			</td> --%>
			<%-- <td>
				<c:out value="${item.feeRatio}" />‰ /<c:out value="${item.addFeeAmt}" />元
			</td>
			<td>
				<c:out value="${item.drawFeeRatio}" />‰ /<c:out value="${item.addDrawFeeAmt}" />元
			</td> 
			<td>
				<c:if test="${not empty item.supportPayType }">
					<c:if test="${fn:contains(item.supportPayType,'01')==true}">网银、</c:if>
					<c:if test="${fn:contains(item.supportPayType,'02')==true}">银联在线、</c:if>
				</c:if>
				<c:if test="${empty item.supportPayType }">
					<c:choose>
						<c:when test="${item.payType eq '01' }">网银</c:when>
						<c:when test="${item.payType eq '02' }">银联在线</c:when>
						<c:otherwise>${item.payType }</c:otherwise>
					</c:choose>
				</c:if>
			</td>--%>
			<td>
				${item.productTypeSummary }
			</td>
			<td><c:out value="${item.limitIps }" /></td>
			<td>
				<c:out value="${item.createDate}" />
			</td>
			<td>
				<c:out value="${item.status}" />
			</td>
			<td>
				<a href="<c:url value="/admin/downMerchant/add?id=${item.id}" />" class="choose-link operate-detail">编辑</a>
				<a href="<c:url value="/admin/downMerchant/delete/${item.id}" />" class="choose-link operate-delete" >删除</a>
			</td>
		</tr>
	</c:forEach>
	<tr class="list-footer">
		<td colspan="11">
			<utils:pager></utils:pager>
		</td>
	</tr>
</table>