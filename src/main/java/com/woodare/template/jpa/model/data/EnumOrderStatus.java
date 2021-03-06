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
 * EnumOrderStatus
 * 
 * @author Luke
 */
public enum EnumOrderStatus {
	// 已创建 - 未提交
	CREATE("已创建"),
	// 处理中
	PROCESSING("处理中"),
	// 待查证
	PENDING("待查证"),
	// 处理成功
	SUCCESS("成功"),
	// 失败
	FAIL("失败"),
	// 异常
	ERROR("异常"),;

	private String desc;

	EnumOrderStatus(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return this.desc;
	}
}
