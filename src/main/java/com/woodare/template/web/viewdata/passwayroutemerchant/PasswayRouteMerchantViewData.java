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
package com.woodare.template.web.viewdata.passwayroutemerchant;

import java.math.BigDecimal;
import java.util.Date;

import com.woodare.template.jpa.persistence.data.passwayroutemerchant.PasswayRouteMerchantData;

/**
 * ClassName: PasswayRouteMerchantViewData
 * 
 * @description
 * @author woo_maven_auto_generate
 * @Date 2018/03/06
 */
public class PasswayRouteMerchantViewData extends PasswayRouteMerchantData {
	private static final long serialVersionUID = 6919042250631515574L;
	private Date viewUpdateDate;

	private BigDecimal minPerAmtNum;
	private BigDecimal maxPerAmtNum;
	private BigDecimal maxTotAmtNum;

	// 明文密钥
	private String signKeyPlain;
	private String encKeyPlain;

	// 明文效验值
	private String signKeyMd5;
	private String encKeyMd5;

	/**
	 * @return the maxTotAmtNum
	 */
	public BigDecimal getMaxTotAmtNum() {
		return maxTotAmtNum;
	}

	/**
	 * @param maxTotAmtNum
	 *            the maxTotAmtNum to set
	 */
	public void setMaxTotAmtNum(BigDecimal maxTotAmtNum) {
		this.maxTotAmtNum = maxTotAmtNum;
	}

	/**
	 * @return the minPerAmtNum
	 */
	public BigDecimal getMinPerAmtNum() {
		return minPerAmtNum;
	}

	/**
	 * @param minPerAmtNum
	 *            the minPerAmtNum to set
	 */
	public void setMinPerAmtNum(BigDecimal minPerAmtNum) {
		this.minPerAmtNum = minPerAmtNum;
	}

	/**
	 * @return the maxPerAmtNum
	 */
	public BigDecimal getMaxPerAmtNum() {
		return maxPerAmtNum;
	}

	/**
	 * @param maxPerAmtNum
	 *            the maxPerAmtNum to set
	 */
	public void setMaxPerAmtNum(BigDecimal maxPerAmtNum) {
		this.maxPerAmtNum = maxPerAmtNum;
	}

	/**
	 * @return the signKeyMd5
	 */
	public String getSignKeyMd5() {
		return signKeyMd5;
	}

	/**
	 * @param signKeyMd5
	 *            the signKeyMd5 to set
	 */
	public void setSignKeyMd5(String signKeyMd5) {
		this.signKeyMd5 = signKeyMd5;
	}

	/**
	 * @return the encKeyMd5
	 */
	public String getEncKeyMd5() {
		return encKeyMd5;
	}

	/**
	 * @param encKeyMd5
	 *            the encKeyMd5 to set
	 */
	public void setEncKeyMd5(String encKeyMd5) {
		this.encKeyMd5 = encKeyMd5;
	}

	/**
	 * @return the signKeyPlain
	 */
	public String getSignKeyPlain() {
		return signKeyPlain;
	}

	/**
	 * @param signKeyPlain
	 *            the signKeyPlain to set
	 */
	public void setSignKeyPlain(String signKeyPlain) {
		this.signKeyPlain = signKeyPlain;
	}

	/**
	 * @return the encKeyPlain
	 */
	public String getEncKeyPlain() {
		return encKeyPlain;
	}

	/**
	 * @param encKeyPlain
	 *            the encKeyPlain to set
	 */
	public void setEncKeyPlain(String encKeyPlain) {
		this.encKeyPlain = encKeyPlain;
	}

	/**
	 * @return the viewUpdateDate
	 */
	public Date getViewUpdateDate() {
		return viewUpdateDate;
	}

	/**
	 * @param viewUpdateDate
	 *            the viewUpdateDate to set
	 */
	public void setViewUpdateDate(Date viewUpdateDate) {
		this.viewUpdateDate = viewUpdateDate;
	}

}
