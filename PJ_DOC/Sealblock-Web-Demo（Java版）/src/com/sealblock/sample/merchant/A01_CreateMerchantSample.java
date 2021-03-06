package com.sealblock.sample.merchant;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sealblock.wallet.SealBlockApi;

/**
 * 用户入网注册
 * 
 * @author
 */
public class A01_CreateMerchantSample {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		createUser();
	}

	/**
	 * @throws Exception
	 */
	public static void createUser() throws Exception {
		JSONObject reqData = new JSONObject();

		// 机构号
		reqData.put("mchNo", SealBlockApi.MCH_NO);
		// 机构用户标识
		reqData.put("mchUserNo", "ORG181022144844018");
		// 用户姓名
		reqData.put("userName", "香蜜3");
		// 用户身份证
		reqData.put("userCertId", "321323844414444939");
		// 用户联系电话
		reqData.put("userPhone", "16442841872");

		// 开通货币，用逗号隔开
		reqData.put("enabledCoin", "ETH");

		try {
			JSONObject respData = SealBlockApi.doEncPost(SealBlockApi.Api.Merchant.Create, reqData);
			System.out.println("平台用户标识： " + respData.getString("userNo"));

			// 提取返回的货账户信息
			JSONArray enabledCoinItems = respData.getJSONArray("enabledCoinItems");
			if (enabledCoinItems != null) {
				for (Object coinObj : enabledCoinItems) {
					JSONObject coinItem = (JSONObject) coinObj;
					System.out.println(coinItem.getString("coin") + "公链地址 = " + coinItem.getString("address"));
					// 入账金额
					BigDecimal inAmt = coinItem.getBigDecimal("settleInAmt");
					// 消费金额
					BigDecimal outAmt = coinItem.getBigDecimal("settleOutAmt");
					// 冻结&锁定金额
					BigDecimal frozenAmt = coinItem.getBigDecimal("frozenAmt");
					System.out.println(coinItem.getString("coin") + "可用余额 = " + inAmt.subtract(outAmt).subtract(frozenAmt));
				}
			}
		}
		//
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
