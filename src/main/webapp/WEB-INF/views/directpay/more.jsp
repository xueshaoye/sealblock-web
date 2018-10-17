<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String resourceUrl = com.woodare.framework.utils.SysProperties.getInstance().getProperty(com.woodare.template.constant.SystemPropertiesConstant.CODE_ROOT_RESOURCE_URL, "http://open.mishua.cn");
%>
<style>
.error {
	margin-left:0;
}
.CardNum {
	padding-left:1rem;
}
input+.error {
	display:block;
    position: fixed;
    top: 4.5rem;
    left: 0;
    margin: 0;
    z-index: 100;
    width: 100%;
    text-align: center;
    color: #FFF;
    background: #FF5E4E;
    line-height: 28px;
    height:28px;
}
.input-wapper {
    width: 100%;
    position: absolute;
    left: 0;
    padding-left: 7rem;
}
.card-exp {
    width: 6rem;
    text-align: center;
}
.card-exp-split {
	line-height:3.6rem;
}
.card-phone {
    width: 12rem;
}
.regcode-btn {
    position: absolute;
    right: 1rem;
    height: 2.6rem;
    line-height: 2.6REM;
    color: #FFF;
    border: none;
    border-radius: 5px;
    width: 8rem;
    text-align: center;
    margin-top: 0.4rem;
    cursor: pointer;
}
.card-type {
	background: url('<%=resourceUrl%>/resources/mf/img/ico_arrow_down.png') no-repeat 96% 50%;
    background-size: 10px 5px;
}
</style>
<header class="pay-bar">
	<div class="tc">完善资料</div>
	<div class=" aui-pull-right" style="position: absolute;right: 1rem;top: 1.2rem;"><img src="<%=resourceUrl%>/resources/mf/img/ico_logo.png" width="100px"></div>
</header>
<main class="MyContent bottom0 orderPayCon">
	<form action="<c:url value="/directpay/verify"/>/${inv.id}" id="submit-form" method="post">
	<input type="hidden" name="id" value="${inv.id}"/>
	<div class="OrderDeatilBox">
		<p>结算卡号：<span id="card-info" class="color_red">${inv.cardNoFormat}</span></p>
		<div class="show-arrow" id="show-arrow"><img src="<%=resourceUrl%>/resources/mf/img/ico_arrow_down.png"></div>
		<div class="MoreDetailBox" id="MoreDetailBox">
			<div class="hide-arrow" id="hide-arrow"><img src="<%=resourceUrl%>/resources/mf/img/ico_arrow_up.png"></div>
			<p>结算卡持卡人：<span class="color_999" id="acc-name">${inv.accNameFormat}</span></p>
			<p>身份证号：<span class="color_999" id="bank-name">${inv.accIdCardFormat}</span></p>
		</div>
	</div>
	<div class="aui-toast" style="display:none" id="loading">
		<div class="aui-toast-loading"></div>
		<div class="aui-toast-content">加载中</div>
	</div>
	<div class="CardPayBox" style="margin-top:1.5rem;">
		<p class="CardNum fix">
			<span class="Cardtext fl">金额</span>
			<span class="input-wapper fl">
				<input type="text" class="card-input" placeholder="请输入金额" name="price" value="${inv.price}" readonly>
			</span>
		</p>
		<p class="CardNum fix btNone">
			<span class="Cardtext fl">预留手机号</span>
			<span class="input-wapper fl" >
				<c:if test="${not empty inv.accTel and inv.accTel ne '00000000000' }">
					<input type="text" class="card-input" required placeholder="结算卡预留手机号" name="accTel" >
				</c:if>
				<c:if test="${empty inv.accTel or inv.accTel eq '00000000000' }"><input type="text" class="card-input" required placeholder="结算卡预留手机号" name="accTel" ></c:if>
			</span>
		</p>
		<p class="CardNum fix btNone">
			<span class="Cardtext fl">支付卡</span>
			<span class="input-wapper fl" >
				<input type="text" class="card-input" placeholder="请输入支付卡号" name="payCardNo" value="${inv.payCardNoFormat}" readonly>
			</span>
		</p>
		<p class="CardNum fix btNone">
			<span class="Cardtext fl">手机号</span>
			<span class="input-wapper fl" >
				<input type="text" class="card-input" placeholder="预留手机号" name="userToken" >
			</span>
		</p>
		<div class="fix conBtnBox"><div class="ml10 mr10"><input id="ChuPayFor" type="button" class="pay-btn bg_red" onclick="check()" value="提交资料"></div></div>
	</div>
	<div class="MyHeight"></div>
</form>
</main>
<script>
$("#show-arrow").click(function () {
	$("#MoreDetailBox").slideDown(200);
	$("#show-arrow").hide();
});
$("#hide-arrow").click(function () {
	$("#MoreDetailBox").slideUp(200);
	$("#show-arrow").show();
});

var isLock = false;
document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	WeixinJSBridge.call('hideOptionMenu');
});
var vali = $("#submit-form").validate({
	errorElement: "span",
	rules: {
		userToken: {
			required: true,
			pattern:"1[0-9]{10}"
		},
		accTel: {
			pattern:"1[0-9]{10}"
		}
	},
	messages: {
		userToken: {
			required: "手机号不能为空",
			pattern:"手机号不正确"
		}
	}
});
function check() {
	if (vali.form()) {
		$("#submit-form").submit();
	} else {
		setTimeout(function(){
			$("input+.error").slideUp(200);
		},1500);
		
	}
};
<c:if test="${not empty error}">
_WOO.modal.alert("${error}");
</c:if>
</script>