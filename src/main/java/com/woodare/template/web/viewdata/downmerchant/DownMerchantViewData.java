/******************************************************************************
 *                                                                             
 *                      Woodare PROPRIETARY INFORMATION                        
 *                                                                             
 *          The information contained herein is proprietary to Woodare         
 *           and shall not be reproduced or disclosed in whole or in part      
 *                    or used for any design or manufacture                    
 *              without direct written authorization from FengDa.              
 *                                                                             
 *            Copyright (c) 2013 by Woodare.  All rights reserved.             
 *                                                                             
 *****************************************************************************/
package com.woodare.template.web.viewdata.downmerchant;

import java.util.List;

import com.woodare.template.jpa.model.DownMerchantFundAccount;
import com.woodare.template.jpa.persistence.data.downmerchant.DownMerchantData;

/**
 * ClassName: DownMerchantViewData
 * 
 * @description
 * @author to_be_replaced_by_owner
 * @Date 2017/02/27
 */
public class DownMerchantViewData extends DownMerchantData {
	private static final long serialVersionUID = -5303765049019581524L;

	private String fundSummary;

	/** 支持的币类型 */
	private List<String> supportCoins;

	/** 生效产品类别 */
	private String[] selCoinArr;

	/** 产品类别 */
	private String[] coinArr;

	/** 交易费率，单位：千分之 */
	private String[] feeRatioArr;

	/** 单笔交易加收费，单位：元 */
	private String[] addFeeAmtArr;

	// 解析处理后数据
	private List<DownMerchantFundAccount> fundItems;

	/**
	 * @return the fundItems
	 */
	public List<DownMerchantFundAccount> getFundItems() {
		return fundItems;
	}

	/**
	 * @param fundItems
	 *            the fundItems to set
	 */
	public void setFundItems(List<DownMerchantFundAccount> fundItems) {
		this.fundItems = fundItems;
	}

	/**
	 * @return the supportCoins
	 */
	public List<String> getSupportCoins() {
		return supportCoins;
	}

	/**
	 * @param supportCoins
	 *            the supportCoins to set
	 */
	public void setSupportCoins(List<String> supportCoins) {
		this.supportCoins = supportCoins;
	}

	/**
	 * @return the fundSummary
	 */
	public String getFundSummary() {
		return fundSummary;
	}

	/**
	 * @param fundSummary
	 *            the fundSummary to set
	 */
	public void setFundSummary(String fundSummary) {
		this.fundSummary = fundSummary;
	}

	/**
	 * @return the selCoinArr
	 */
	public String[] getSelCoinArr() {
		return selCoinArr;
	}

	/**
	 * @param selCoinArr
	 *            the selCoinArr to set
	 */
	public void setSelCoinArr(String[] selCoinArr) {
		this.selCoinArr = selCoinArr;
	}

	/**
	 * @return the coinArr
	 */
	public String[] getCoinArr() {
		return coinArr;
	}

	/**
	 * @param coinArr
	 *            the coinArr to set
	 */
	public void setCoinArr(String[] coinArr) {
		this.coinArr = coinArr;
	}

	/**
	 * @return the feeRatioArr
	 */
	public String[] getFeeRatioArr() {
		return feeRatioArr;
	}

	/**
	 * @param feeRatioArr
	 *            the feeRatioArr to set
	 */
	public void setFeeRatioArr(String[] feeRatioArr) {
		this.feeRatioArr = feeRatioArr;
	}

	/**
	 * @return the addFeeAmtArr
	 */
	public String[] getAddFeeAmtArr() {
		return addFeeAmtArr;
	}

	/**
	 * @param addFeeAmtArr
	 *            the addFeeAmtArr to set
	 */
	public void setAddFeeAmtArr(String[] addFeeAmtArr) {
		this.addFeeAmtArr = addFeeAmtArr;
	}
}
