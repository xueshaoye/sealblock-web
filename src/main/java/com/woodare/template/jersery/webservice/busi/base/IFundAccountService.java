package com.woodare.template.jersery.webservice.busi.base;

import com.woodare.framework.exception.MessageWooException;
import com.woodare.template.jpa.model.DownNoCardInvoice;
import com.woodare.template.jpa.persistence.data.downmerchant.DownMerchantData;

/**
 * 账户处理服务类
 * 
 * @author Luke
 */
public interface IFundAccountService {

	/**
	 * 结算交易处理
	 * 
	 * @param invoice
	 * @param merchantData
	 * @return
	 * @throws MessageWooException
	 */
	DownNoCardInvoice settleInvoice(DownNoCardInvoice invoice, DownMerchantData merchantData) throws MessageWooException;
}
