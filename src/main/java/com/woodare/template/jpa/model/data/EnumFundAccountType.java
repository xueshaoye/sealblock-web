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
 * ClassName: EnumFundAccountType
 * 
 * @description
 * @author Luke
 * @Date Mar 10, 2018
 */
public enum EnumFundAccountType {
	// 代理
	Agent("代理"),
	// 机构
	Merchant("机构"),;

	private String desc;

	EnumFundAccountType(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

}
