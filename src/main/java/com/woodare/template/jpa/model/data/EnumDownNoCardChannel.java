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
package com.woodare.template.jpa.model.data;

/**
 * ClassName: EnumDownNoCardChannel
 * 
 * @description
 * @author Luke
 * @Date Feb 26, 2018
 */
public enum EnumDownNoCardChannel {
	// 比特网
	Bitcoin("比特网", "bitcoin", "0102030405060708"),
	// 以太坊
	Ethereum("以太坊", "ethereum", "0102030405060708"),;

	private String desc;
	private String egwName;
	private String aeskey;

	/**
	 * @param egwName
	 *            渠道适配器
	 * @param aeskey
	 * @param extra
	 */
	EnumDownNoCardChannel(String desc, String egwName, String aeskey) {
		this.desc = desc;
		this.egwName = egwName;
		this.aeskey = aeskey;
	}

	public String getDesc() {
		return this.desc;
	}

	public String getEgwName() {
		return this.egwName;
	}

	public String getAesKey() {
		return this.aeskey;
	}

}
