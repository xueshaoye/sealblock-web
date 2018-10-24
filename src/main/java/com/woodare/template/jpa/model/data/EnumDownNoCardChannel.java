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
	Bitcoin("sealblock", "比特网", "bitcoin", "0102030405060708"),
	
	// 以太坊
	Ethereum("sealblock", "以太坊", "ethereum", "0102030405060708"),;

	private String friendName;
	private String simpleName;
	private String egwName;
	private String aesKey;

	/**
	 * @param egwName
	 *            渠道适配器
	 * @param aeskey
	 * @param extra
	 */
	EnumDownNoCardChannel(String egwName, String friendName, String simpleName, String aesKey) {
		this.friendName = friendName;
		this.simpleName = simpleName;
		this.egwName = egwName;
		this.aesKey = aesKey;
	}

	/**
	 * @return the friendName
	 */
	public String getFriendName() {
		return friendName;
	}

	/**
	 * @return the simpleName
	 */
	public String getSimpleName() {
		return simpleName;
	}

	/**
	 * @return the egwName
	 */
	public String getEgwName() {
		return egwName;
	}

	/**
	 * @return the aesKey
	 */
	public String getAesKey() {
		return aesKey;
	}

}
