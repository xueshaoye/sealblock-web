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
package com.woodare.framework.persistence.service;

import java.util.List;

import com.woodare.framework.model.AbstractModel;

/**
 * 
 * ClassName: ISimpleDAO
 * 
 * @description
 * @author framework
 * @Date 2013-4-1
 * 
 */
public interface ISimpleDAO<T extends AbstractModel> {

    /**
     * 
     * @param id
     * @return
     */
	T findOne(final Object id);

    /**
     * 
     * @return
     */
    List<T> findAll();

    /**
     * 
     * @param entity
     */
    void save(final T entity);

    /**
     * 
     * @param entity
     */
    void update(final T entity);

    /**
     * 
     * @param entity
     */
    void saveForce(final T entity);

    /**
     * 
     * @param entity
     */
    void updateForce(final T entity);
    
    /**
     * 
     * @param entityId
     */
    void deleteById(final Object entityId);

    /**
     * 
     * @param entityId
     */
    void deleteByIds(final List<String> entityIds);

    /**
     * 
     * @param entity
     */
    void deleteWithLogical(final T entity);

    /**
     * 
     * @param entity
     */
    void delete(final T entity);
    
    /**
     * 
     * @param entity
     */
    void deleteForce(final T entity);
    
    /**
     * 
     * @param entityId
     */
    void deleteByIdForce(final String entityId);

}
