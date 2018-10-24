<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="utils" tagdir="/WEB-INF/tags/utils"%>
<c:if test="${not empty error}">
<script>
$(function(){
	$.messager.alert('错误','${error}');
});
</script>
</c:if>
<style>
table.list-wapper td {
	text-align: center;
}
table.list-wapper td.num {
	text-align: right;
}
</style>
<form class="search-form" action="<c:url value="/admin/downMerchantFundAccount/index" />" method="post">
	<label for="keywords">过滤条件：</label>
	<input type="hidden" name="searchFlag" value="true"></input>
	
	<input type="text" class="input-box search-input-box" placeholder="关键字" name="keywords" value="${search.keywords}"></input>
	
	<label for="accountType">账户类别：</label>
	<select name="accountType" class="input-box search-input-box">
		<option value="">全部</option>
		<option value="Merchant" <c:if test="${empty search.accountType or search.accountType eq 'Merchant'}">selected</c:if>>机构</option>
		<option value="Agent" <c:if test="${search.accountType eq 'Agent'}">selected</c:if>>代理商</option>
	</select>

	<label for="coinName">公链平台：</label>
		<select name="coinName" class="input-box search-input-box">
		<option value="">全部</option>
		<option value="比特币">比特币</option>
		<option value="以太币">以太币</option>
	</select>
	
	<label for="startDate" class="custom-d-cate">开始时间：</label>
	<input type="text" class="input-box search-input-box date-picker custom-d-cate" placeholder="开始时间" name="startDate" value="${search.startDate}">
	<label for="endDate" class="custom-d-cate">结束时间：</label>
	<input type="text" class="input-box search-input-box date-picker custom-d-cate" placeholder="结束时间" name="endDate" value="${search.endDate}">
	
	<script>
		$(".date-picker").datepicker($.datepicker.regional[ "zh-CN" ]);
		$("select[name='dateCate']").change(function(){
			$(".custom-d-cate")[$(this).val() == 'CUSTOM' ? "show" : "hide"]();
		}).change();
	</script>
	
	 <label>&nbsp;</label>
	<input type="checkbox" style="width: 15px;" <c:if test="${search.searchFlag eq true  }">checked="checked"</c:if> class="input-box search-input-box" 
		id="changedFlag" placeholder="仅显示交易商户" name="searchFlag"></input>
	<label for="changedFlag" style="text-align: left; padding-left: 5px;">存在余额变动</label>
	
	<%-- <input type="hidden" name="pageIndex" value="${search.pageIndex}"></input><input type="hidden" name="pageSize" value="${search.pageSize}"></input> --%>
	<div class="space"></div>
	<input type="submit" value="搜索" class="btn"></input>
</form>
<form action="<c:url value="/admin/downMerchantFundAccount/export" />" target="_blank" method="post" class="export-form">
	<input type="submit" value="导出" class="btn btn-orange export-btn" ></input>
</form>
<table class="list-wapper">
	<tr class="list-header">
		<td>账户类别</td>
		<td style="text-align: left;">名称</td>
		<td>公链平台</td>
		<td>授信比</td>
		<td>记账日期</td>
		<td>T1结算额（元）</td>
		<td>T1已代付（元）</td>
		<td>T1剩余额度（元）</td>
		
		<td>当日入账（元）</td>
		<td>T0授信额</td>
		<td>T0已代付（元）</td>
		
		<td>冻结金额（元）</td>
		<td>&nbsp;</td>
	</tr>
	
	<c:set var="rowNum" value="0" />
	
	<c:set var="settleInAmt" value="0" />
	<c:set var="settleOutAmt" value="0" />
	<c:set var="curInAmt" value="0" />
	<c:set var="creditAmt" value="0" />
	<c:set var="curOutAmt" value="0" />
	<c:set var="frozenAmt" value="0" />
	
	<c:forEach var="item" items="${res.items}" varStatus="status">
		<tr class="list-item">
			<td>
				<utils:enum mode="lbl"  value="${item.accountType}" key="EnumDownUserStatus" />
				<c:if test="${item.accountType eq 'Merchant'}">机构</c:if>
				<c:if test="${item.accountType eq 'Agent'}">代理商</c:if>
			</td>
			<td style="text-align: left;"><c:out value="${item.mchName}" />(<c:out value="${item.mchNo}" />)</td>
			<td><c:out value="${item.coinName}" /></td>
			<td><c:out value="${item.creditRatio}"/>%</td>
		    <td><utils:dateFormat value="${item.changeDate}" /></td>
			<td class="num">${item.settleInAmt }</td>
			<td class="num">${item.settleOutAmt }</td>
			<td class="num">${item.settleInAmt - item.settleOutAmt }</td>
			<td class="num">${item.curInAmt }</td>
			<td class="num">${item.curInAmt * item.creditRatio / 100 }</td>
			<td class="num">${item.curOutAmt }</td>
			<td class="num">${item.frozenAmt }</td>
			<td><a href="<c:url value="/admin/downMerchantFundAccount/frozen?id=${item.id}" />" class="choose-link operate-detail">变动冻结</a></td>
			 
			<c:set var="rowNum" value="${rowNum + 1 }" />
			<c:set var="settleInAmt" value="${item.settleInAmt + settleInAmt }" />
			<c:set var="settleOutAmt" value="${item.settleOutAmt + settleOutAmt }" />
			<c:set var="curInAmt" value="${item.curInAmt + curInAmt }" />
			<c:set var="curOutAmt" value="${item.curOutAmt + curOutAmt }" />
			<c:set var="creditAmt" value="${item.curInAmt * item.creditRatio / 100 + creditAmt }" />
			
			<c:set var="frozenAmt" value="${item.frozenAmt / 100 + frozenAmt }" />
		</tr>
	</c:forEach> 
	
	<!-- 取消合计 -->
	<%-- <c:if test="${rowNum gt 0 }">
		<tr class="list-item" style="font-weight: bold;">
			<td></td>
			<td align='right'>合计：</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td class="num"><fmt:formatNumber value="${settleInAmt / 100 }" pattern="#,##0.00#"/>&nbsp;</td>
			<td class="num"><fmt:formatNumber value="${settleOutAmt / 100 }" pattern="#,##0.00#"/>&nbsp;</td>
			<td class="num"><fmt:formatNumber value="${(settleInAmt - item.settleOutAmt) / 100}" pattern="#,##0.00#"/>&nbsp;</td>
			<td class="num"><fmt:formatNumber value="${curInAmt / 100}" pattern="#,##0.00#"/>&nbsp;</td>
			<td class="num"><fmt:formatNumber value="${item.curInAmt * item.creditRatio / 10000}" pattern="#,##0.00#"/>&nbsp;</td>
			<td class="num"><fmt:formatNumber value="${item.curOutAmt / 100}" pattern="#,##0.00#"/>&nbsp;</td>
			<td class="num"><fmt:formatNumber value="${item.frozenAmt / 100}" pattern="#,##0.00#"/>&nbsp;</td>
			<td class="num">&nbsp;</td>
		</tr>
	</c:if> --%>
</table>
<div style="border: none; text-align: right;width: 98%;margin: 0px auto; color: #1C1C1E; font-size: 0.9rem;">
	<utils:pager></utils:pager>
</div>