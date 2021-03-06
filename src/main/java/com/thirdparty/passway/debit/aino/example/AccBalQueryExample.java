package com.thirdparty.passway.debit.aino.example;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;
import com.thirdparty.passway._base.IPasswayApi.EnumPasswayOrderStatus;
import com.thirdparty.passway._data.PasswayRequestData;
import com.thirdparty.passway._data.PasswayResponseData;
import com.thirdparty.passway.debit.aino.AiNongApi;
import com.thirdparty.passway.debit.aino.util.AiNongConstant;
import com.woodare.framework.utils.JsonUtils;
import com.woodare.template.jpa.model.DownNoCardInvoice;
import com.woodare.template.jpa.model.data.EnumDownNoCardChannel;

/**
 * @author Luke
 */
public class AccBalQueryExample {
	static AiNongApi api = new AiNongApi();

	public static void main(String[] args) throws Exception {
		// 初始配置渠道参数
		AiNongConstant.initPasswayMerchants();

		PasswayRequestData req = new PasswayRequestData();

		DownNoCardInvoice invoice = new DownNoCardInvoice();
		invoice.setChannel(EnumDownNoCardChannel.Bitcoin);
		req.setInvoice(invoice);

		PasswayResponseData resp = api.accBalQuery(req);
		System.out.println(JsonUtils.toJson(resp));

		if (EnumPasswayOrderStatus.S.equals(resp.getStatus())) {
			System.out.println("余额查询成功");

			JSONObject jsonObj = (JSONObject) resp.getExtraObject();
			// 账户可用余额 balance M 单位：“分”
			System.out.println(String.format("账户可用余额=%s", new BigDecimal(jsonObj.getString("balance")).divide(new BigDecimal("100")).toString()));
			// T0授信额度 creditLines M 单位：“分”
			System.out.println(String.format("T0授信额度=%s", new BigDecimal(jsonObj.getString("creditLines")).divide(new BigDecimal("100")).toString()));
			// 冻结余额 frozenAmt M 单位：“分”
			System.out.println(String.format("冻结余额=%s", new BigDecimal(jsonObj.getString("frozenAmt")).divide(new BigDecimal("100")).toString()));
			// 欠费金额 owedAmt M 单位：“分”
			System.out.println(String.format("欠费金额=%s", new BigDecimal(jsonObj.getString("owedAmt")).divide(new BigDecimal("100")).toString()));
			// 当日入金 curInAmt M 单位：“分”
			System.out.println(String.format("当日入金=%s", new BigDecimal(jsonObj.getString("curInAmt")).divide(new BigDecimal("100")).toString()));
			// 当日出金 curOutAmt M 单位：“分”
			System.out.println(String.format("当日出金=%s", new BigDecimal(jsonObj.getString("curOutAmt")).divide(new BigDecimal("100")).toString()));
		}
		else {
			System.out.println("余额查询失败，" + resp.getRetCode() + "：" + resp.getRetDesc());
		}

		api.shutdown();
	}
}
