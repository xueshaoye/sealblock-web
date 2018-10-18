package com.thirdparty.passway.debit.aino.example;

import com.thirdparty.passway._base.IPasswayApi.EnumPasswayOrderStatus;
import com.thirdparty.passway._data.PasswayRequestData;
import com.thirdparty.passway._data.PasswayResponseData;
import com.thirdparty.passway.debit.aino.AiNongApi;
import com.thirdparty.passway.debit.aino.util.AiNongConstant;
import com.woodare.framework.utils.JsonUtils;
import com.woodare.template.jpa.model.DownMerchantDeposit;
import com.woodare.template.jpa.model.data.EnumDownNoCardChannel;

/**
 * 
 * @author Luke
 *
 */
public class TransferQueryExample {
	static AiNongApi api = new AiNongApi();

    public static void main(String[] args) throws Exception {
		// 初始配置渠道参数
		AiNongConstant.initPasswayMerchants();
		
		PasswayRequestData req = new PasswayRequestData();
		
		DownMerchantDeposit deposit = new DownMerchantDeposit();
		deposit.setChannel(EnumDownNoCardChannel.Bitcoin);
		deposit.setChannelAccNo(AiNongConstant.MEC_YINMAO);
		deposit.setTransNo("D1804201857250941000");
		req.setDeposit(deposit);
		
		PasswayResponseData resp = api.transferQuery(req);
		System.out.println(JsonUtils.toJson(resp));

		if (EnumPasswayOrderStatus.S.equals(resp.getStatus())) {
			System.out.println("代付成功，请求流水号：" + deposit.getTransNo() + ", 下发流水号：" + resp.getPasswayOrderId());
		}
		else if (EnumPasswayOrderStatus.R.equals(resp.getStatus())) {
			System.out.println("代付待查证，请求流水号：" + deposit.getTransNo() + ", 下发流水号：" + resp.getPasswayOrderId());
			System.out.println("代付返回，" + resp.getRetCode() + "：" + resp.getRetDesc());
		}
		else {
			System.out.println("代付失败，" + resp.getRetCode() + "：" + resp.getRetDesc());
		}
		
		api.shutdown();
	}
}
