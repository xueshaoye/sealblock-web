<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="utils" tagdir="/WEB-INF/tags/utils"%>
<jsp:include page="/WEB-INF/views/iframe-status.jsp"/>
<%
String resourceUrl = com.woodare.framework.utils.SysProperties.getInstance().getProperty(com.woodare.template.constant.SystemPropertiesConstant.CODE_ROOT_RESOURCE_URL, "http://open.mishua.cn");%>

<link href="<%=resourceUrl%>/resources/js/bootstrap/bootstrap-select.css" rel="stylesheet" type="text/css" />
<link href="<%=resourceUrl%>/resources/js/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=resourceUrl%>/resources/js/bootstrap/bootstrap-select.js"></script>
<script type="text/javascript" src="<%=resourceUrl%>/resources/js/bootstrap/bootstrap.min.js"></script>

<style>
.liner-box-left {
    vertical-align: top;
    color: #333;
    font-family: 'Microsoft YaHei', Arial, Helvetica, sans-serif;
    font-size: 0.9rem;
        line-height: 34px;
}

.input-box, form label {
    color: #333;
    font-family: 'Microsoft YaHei', Arial, Helvetica, sans-serif;
    font-size: 0.9rem;
}
</style>
<form action="<c:url value="/admin/passwayRouteMerchant/add" />" method="post">
	<input type="hidden" name="id" value="${item.id}" />
	<table class="liner-box">
		<c:if test="${not empty error}">
			<tr class="liner-error">
				<td class="liner-box-left"></td>
				<td class="liner-box-right">${error}</td>
			</tr>
		</c:if>
		<tr>
			<td class="liner-box-left required">通道:</td>
			<td class="liner-box-right">
				<utils:combo mode="sel3" name="channel" extraAttr="required='required'" needDefaultValue="请选择" cssName="input-box" value="${item.channel}"></utils:combo>
			</td>
		</tr>
		<tr>
			<td class="liner-box-left required">结算模式:</td>
			<td class="liner-box-right">
				<utils:enum key="EnumSettleType" extraAttr="required='required' " needDefaultValue="请选择" name="settleType" cssName="input-box" value="${item.settleType}"></utils:enum>
			</td>
		</tr>
		<tr>
			<td class="liner-box-left required">账户名称:</td>
			<td class="liner-box-right">
				<input type="text" class="input-box" required="required" placeholder="账户名称" name="channelAccName" value="${item.channelAccName}" />
			</td>
		</tr>
		<tr>
			<td class="liner-box-left required">账户编号:</td>
			<td class="liner-box-right">
				<input type="text" class="input-box" required="required" placeholder="账户编号" name="channelAccNo" value="${item.channelAccNo}" />
			</td>
		</tr>
		<tr>
			<td class="liner-box-left required">状态:</td>
			<td class="liner-box-right">
				<utils:enum mode="sel" name="status" extraAttr="required='required'" needDefaultValue="请选择" cssName="input-box" value="${item.status}" key="EnumDownUserStatus" />
			</td>
		</tr>
		<c:if test="${not empty item.createDate }">
			<c:if test="${not empty item.signKeyMd5 }">
			<tr style="">
				<td class="liner-box-left ">原签名密钥:</td>
				<td class="liner-box-right">
					<label style="color: red; font-weight: bold;">${item.signKeyMd5}</label>&nbsp;<label style="color: blue; ">（仅显示明文密钥的MD5值）</label>
				</td>
			</tr>
			</c:if>
			<tr>
				<td class="liner-box-left required">签名Key（明文）:</td>
				<td class="liner-box-right">
					<input type="hidden" name="signKey" value="${item.signKey }" />
					<input type="text" class="input-box" placeholder="留空表示不修改" name="signKeyPlain" />
				</td>
			</tr>
			
			<c:if test="${not empty item.encKeyMd5 }">
			<tr style="">
				<td class="liner-box-left ">原加密密钥:</td>
				<td class="liner-box-right">
					<label style="color: red; font-weight: bold;">${item.encKeyMd5}</label>&nbsp;<label style="color: blue; ">（仅显示明文密钥的MD5值）</label>
				</td>
			</tr>
			</c:if>
			<tr>
				<td class="liner-box-left required">加密Key（明文）:</td>
				<td class="liner-box-right">
					<input type="hidden" name="encKey" value="${item.encKey }" />
					<input type="text" class="input-box" placeholder="加密Key（明文）" name="encKeyPlain" />
				</td>
			</tr>
		</c:if>
		<c:if test="${empty item.createDate }">
			<tr>
				<td class="liner-box-left required">签名Key（明文）:</td>
				<td class="liner-box-right">
					<input type="text" class="input-box" required="required" placeholder="签名Key（明文）" name="signKeyPlain" />
				</td>
			</tr>
			<tr>
				<td class="liner-box-left required">加密Key（明文）:</td>
				<td class="liner-box-right">
					<input type="text" class="input-box" required="required" placeholder="加密Key（明文）" name="encKeyPlain" />
				</td>
			</tr>
		</c:if>
		
		
		<%-- <c:if test="${not empty item.createDate }">
			<tr>
				<td class="liner-box-left ">原加密Key(效验值):</td>
				<td class="liner-box-right">
					<input type="text" class="input-box" placeholder="无历史数据" name="encKeyMd5" value="${item.encKeyMd5}" />
				</td>
			</tr>
		</c:if>
		<tr>
			<td class="liner-box-left ">加密Key（明文）:</td>
			<td class="liner-box-right">
				<input type="text" class="input-box" placeholder="留空表示不修改" name="encKeyPlain"  />
			</td>
		</tr>
		<c:if test="${not empty item.createDate }">
			<tr>
				<td class="liner-box-left ">原支付Key(效验值):</td>
				<td class="liner-box-right">
					<input type="text" class="input-box" placeholder="无历史数据" name="payKeyMd5" value="${item.payKeyMd5}" />
				</td>
			</tr>
		</c:if>
		<tr>
			<td class="liner-box-left ">支付Key（明文）:</td>
			<td class="liner-box-right">
				<input type="text" class="input-box" placeholder="留空表示不修改" name="payKeyPlain"  />
			</td>
		</tr> --%>
		<tr>
			<td class="liner-box-left ">费率信息:</td>
			<td class="liner-box-right">
				<textarea class="input-box" style="height: 50px;" name="feeText">${item.feeText }</textarea>
			</td>
		</tr>
		<tr>
			<td class="liner-box-left ">额外配置:</td>
			<td class="liner-box-right">
				<textarea class="input-box" style="height: 50px;" name="extra">${item.extra }</textarea>
			</td>
		</tr>
		<tr>
			<td class="liner-box-left ">备注:</td>
			<td class="liner-box-right">
				<input type="text" class="input-box" placeholder="备注  " name="remark" value="${item.remark}" />
			</td>
		</tr>
		<tr>
			<td colspan="2" class="liner-box-one-line">
				<input type="submit" value="提交" class="btn" id="confirmBtn" />
			</td>
		</tr>
	</table>
</form>