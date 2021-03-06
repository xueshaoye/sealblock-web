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
package com.woodare.core.jpa.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woodare.core.jpa.data.systemconfig.SearchSystemConfigData;
import com.woodare.core.jpa.model.SystemConfig;
import com.woodare.core.jpa.service.ISystemConfigDAO;
import com.woodare.framework.data.IPagedList;
import com.woodare.framework.model.data.EnumDeleteStatus;
import com.woodare.framework.persistence.service.impl.AbstractPagedDAO;

/**
 * 
 * ClassName: SystemConfigDAO
 * 
 * @description
 * @author city_maven_auto_generate
 * @Date Aug 31, 2013
 * 
 */
@Service
public class SystemConfigDAO extends AbstractPagedDAO<SystemConfig> implements ISystemConfigDAO {

    @Override
    protected Class<SystemConfig> getDomainClass() {
        return SystemConfig.class;
    }
    
	@Override
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.SUPPORTS, readOnly = true)
	public IPagedList<SystemConfig> searchSystemConfigs(SearchSystemConfigData searchData) {
		
		StringBuffer sql = new StringBuffer("from SystemConfig a");
		
		List<TypeCondition> conditions = new ArrayList<TypeCondition>();

		conditions.add(new TypeCondition("deleteStatus", "a.deleteStatus = :deleteStatus", EnumDeleteStatus.VALID));

		// Add filter conditions
		if(StringUtils.isNotEmpty(searchData.getId())) {
			conditions.add(new TypeCondition("id", "a.id = :id", searchData.getId()));
		}
		if(searchData.getIds() != null && searchData.getIds().size() > 0) {
			conditions.add(new TypeCondition("ids", "a.ids in (:ids)", searchData.getIds()));
		}
		// TODO: add more conditions
		
		// Append conditions
		if(conditions != null && conditions.size() > 0) {
			sql.append(" where ").append(this.joinConditions(conditions, " and "));
		}
		
		// Create query statements
		TypedQuery<SystemConfig> query = this.getEntityManager().createQuery(sql.toString(), SystemConfig.class);
		TypedQuery<Long> totalQuery = this.getEntityManager().createQuery("select count(a.id) " + sql.toString(), Long.class);
		
		// Append conditions' variables
		this.addParameters(query, conditions);
		this.addParameters(totalQuery, conditions);
		
		// Send back returns
		return this.getPagedList(totalQuery, query, searchData);
	}
}


