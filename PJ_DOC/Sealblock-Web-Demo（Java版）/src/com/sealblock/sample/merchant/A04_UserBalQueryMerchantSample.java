package com.sealblock.sample.merchant;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sealblock.wallet.SealBlockApi;

/**
 * 用户余额查询
 * 
 * @author
 */
public class A04_UserBalQueryMerchantSample {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		userBalQuery();
	}

	/**
	 * @throws Exception
	 */
	public static void userBalQuery() throws Exception {
		JSONObject reqData = new JSONObject();

		// 机构号
		reqData.put("mchNo", SealBlockApi.MCH_NO);
		// 平台用户标识
		reqData.put("userNo", "0D37BE2D94DA4EC8879BE082DBC27C77");

		try {
			JSONObject respData = SealBlockApi.doEncPost(SealBlockApi.Api.Merchant.BalQuery, reqData);
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
