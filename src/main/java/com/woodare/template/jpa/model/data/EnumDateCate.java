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
 * ClassName: EnumDateCate
 * 
 * @description
 * @author Xinxing Jiang
 * @Date Jan 7, 2017
 */
public enum EnumDateCate {
	//
	CUSTOM("自定义"),
	//
	TODAY("今天"),
	//
	YESTERDAY("昨天"),
	//
	LAST_7_DAYS("过去七天"),
	//
	LAST_30_DAYS("过去30天"),;

	private String desc;

	EnumDateCate(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return this.desc;
	}

}
