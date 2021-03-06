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
package com.woodare.template.web.controller.content;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.woodare.core.base.BaseController;
import com.woodare.core.exception.ControllerException;
import com.woodare.core.util.SDFFactory;
import com.woodare.core.web.common.viewdata.AjaxResponseData;
import com.woodare.core.web.common.viewdata.ListResponseData;
import com.woodare.framework.data.EResponseState;
import com.woodare.framework.data.IPagedList;
import com.woodare.framework.exception.MessageWooException;
import com.woodare.framework.jersey.IWebService;
import com.woodare.framework.spring.ApplicationContextHolder;
import com.woodare.framework.utils.ExcelUtils;
import com.woodare.framework.utils.ExcelUtils.ExportSetInfo;
import com.woodare.framework.utils.SaftyBeanUtils;
import com.woodare.framework.utils.StringUtils;
import com.woodare.template.helper.cache.DownMerchants;
import com.woodare.template.jersery.servicedata.downmerchantdeposit.DownMerchantDepositServiceData;
import com.woodare.template.jpa.model.DownMerchantDeposit;
import com.woodare.template.jpa.model.NotifyRecord;
import com.woodare.template.jpa.model.data.EnumDateCate;
import com.woodare.template.jpa.model.data.EnumNotifyRecordType;
import com.woodare.template.jpa.persistence.data.downmerchant.DownMerchantData;
import com.woodare.template.jpa.persistence.data.downmerchantdeposit.DownMerchantDepositSumData;
import com.woodare.template.jpa.persistence.persistence.IDownMerchantDepositDAO;
import com.woodare.template.jpa.persistence.persistence.INotifyRecordDAO;
import com.woodare.template.web.controller.helper.CellFormatterFactory;
import com.woodare.template.web.viewdata.downmerchantdeposit.DownMerchantDepositViewData;
import com.woodare.template.web.viewdata.downmerchantdeposit.SearchDownMerchantDepositViewData;

/**
 * ClassName: ContentDownMerchantDepositController
 * 
 * @description
 * @author Luke
 * @Date Mar 4,2018
 */
@Controller
@RequestMapping("/content/downMerchantDeposit")
public class ContentDownMerchantDepositController extends BaseController {
	private static Logger log = Logger.getLogger(ContentDownMerchantDepositController.class);

	@Autowired
	private IDownMerchantDepositDAO downMerchantDepositDAO;

	@Autowired
	private INotifyRecordDAO notifyRecordDAO;

	private void formatSearchData(SearchDownMerchantDepositViewData searchData) {
		searchData.setMchNo(getUsername());

		if (searchData.getDateCate() == null) {
			searchData.setDateCate(EnumDateCate.CUSTOM);
		}
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.MILLISECOND, -1);
		switch (searchData.getDateCate()) {
		case TODAY:
			searchData.setEndDate(c.getTime());
			c.add(Calendar.DAY_OF_MONTH, -1);
			c.add(Calendar.MILLISECOND, 1);
			searchData.setStartDate(c.getTime());
			break;
		case YESTERDAY:
			c.add(Calendar.DAY_OF_MONTH, -1);
			searchData.setEndDate(c.getTime());
			c.add(Calendar.DAY_OF_MONTH, -1);
			c.add(Calendar.MILLISECOND, 1);
			searchData.setStartDate(c.getTime());
			break;
		case LAST_7_DAYS:
			searchData.setEndDate(c.getTime());
			c.add(Calendar.DAY_OF_MONTH, -7);
			c.add(Calendar.MILLISECOND, 1);
			searchData.setStartDate(c.getTime());
			break;
		case LAST_30_DAYS:
			searchData.setEndDate(c.getTime());
			c.add(Calendar.DAY_OF_MONTH, -30);
			c.add(Calendar.MILLISECOND, 1);
			searchData.setStartDate(c.getTime());
			break;
		default:
			if (searchData.getEndDate() == null) {
				searchData.setEndDate(c.getTime());
			}
			else {
				Calendar c1 = Calendar.getInstance();
				c1.setTime(searchData.getEndDate());
				c1.set(Calendar.HOUR_OF_DAY, 0);
				c1.set(Calendar.MINUTE, 0);
				c1.set(Calendar.SECOND, 0);
				c1.set(Calendar.MILLISECOND, 0);
				c1.add(Calendar.DAY_OF_MONTH, 1);
				c1.add(Calendar.MILLISECOND, -1);
				searchData.setEndDate(c1.getTime());
			}
			if (searchData.getStartDate() == null) {
				c.add(Calendar.DAY_OF_MONTH, -1);
				c.add(Calendar.MILLISECOND, 1);
				searchData.setStartDate(c.getTime());
			}
			else {
				Calendar c1 = Calendar.getInstance();
				c1.setTime(searchData.getStartDate());
				c1.set(Calendar.HOUR_OF_DAY, 0);
				c1.set(Calendar.MINUTE, 0);
				c1.set(Calendar.SECOND, 0);
				c1.set(Calendar.MILLISECOND, 0);
				searchData.setStartDate(c1.getTime());
			}
			break;
		}
	}

	@Transactional(propagation = Propagation.NEVER)
	@RequestMapping(value = "/sum")
	public ModelAndView sum(SearchDownMerchantDepositViewData searchData) throws ControllerException {
		ModelAndView mav = new ModelAndView(getTemplate("/content/downMerchantDeposit/sum"));
		formatSearchData(searchData);

		List<DownMerchantDepositSumData> items = downMerchantDepositDAO.sumDeposit(searchData);
		mav.addObject("items", items);
		mav.addObject("search", searchData);
		return mav;
	}

	@Transactional(propagation = Propagation.NEVER)
	@RequestMapping(value = "/index")
	public ModelAndView index(SearchDownMerchantDepositViewData searchData) throws ControllerException {
		ModelAndView mav = showIndex(getTemplate("/content/downMerchantDeposit/index"), searchData, null);
		mav.addObject("allowFlag", ContentHomeController.isAllowedManuallyAccount(this.getUsername()) ? "Y" : "N");
		return mav;
	}

	@Transactional(propagation = Propagation.NEVER)
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(SearchDownMerchantDepositViewData searchData, HttpServletResponse response) throws ControllerException {
		formatSearchData(searchData);
		searchData.setPageSize(Integer.MAX_VALUE);
		searchData.setPageIndex(0);

		IPagedList<DownMerchantDeposit> items = downMerchantDepositDAO.searchItems(searchData);

		String fileName = "deposit-" + SDFFactory.DATETIME_DASH.format(new Date()) + ".xls";
		try {
			byte[] content = formatString(items);
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.addHeader("Content-Length", "" + content.length);
			response.setContentType("application/octet-stream");
			response.setCharacterEncoding("GBK");
			response.getOutputStream().write(content);
			response.getOutputStream().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private byte[] formatString(List<DownMerchantDeposit> items) throws ParseException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		List<String[]> headNames = new ArrayList<String[]>();
		headNames.add(new String[] { "平台流水号", "机构号", "机构名称", "清算类型", "用途", "代付金额", "手续费", "收款帐号", "收款人", "机构流水号", "机构订单时间", "代付状态", "状态描述" });
		List<String[]> fieldNames = new ArrayList<String[]>();
		fieldNames.add(new String[] { "transNo", "mchNo", "mchName", "mode", "purpose", "price", "downDrawFee", "accCardNo", "accName", "tradeNo", "orderDate", "status", "statusDesc" });

		ExportSetInfo setInfo = new ExportSetInfo();
		setInfo.addFomatter("status", CellFormatterFactory.TRANSFER_STATUS);
		setInfo.addFomatter("mode", CellFormatterFactory.TRANSFER_MODE);

		LinkedHashMap<String, List<?>> objsMap = new LinkedHashMap<String, List<?>>();
		objsMap.put("数据", items);
		setInfo.setObjsMap(objsMap);
		setInfo.setFieldNames(fieldNames);
		setInfo.setTitles(new String[] { "代付交易记录" });
		setInfo.setHeadNames(headNames);
		setInfo.setOut(baos);
		try {
			ExcelUtils.export2Excel(setInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	@Transactional(propagation = Propagation.NEVER)
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public ModelAndView details(DownMerchantDepositViewData data) throws ControllerException {
		ModelAndView mav = new ModelAndView(getTemplate("/content/downMerchantDeposit/details"));
		DownMerchantDeposit item = null;
		if (StringUtils.isNotEmpty(data.getId())) {
			item = downMerchantDepositDAO.findOne(data.getId());
		}
		mav.addObject("item", convertDetails(item));
		return mav;
	}

	@Transactional(propagation = Propagation.NEVER)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(DownMerchantDepositViewData data) throws ControllerException {
		ModelAndView mav = new ModelAndView(getTemplate("/content/downMerchantDeposit/add"));
		DownMerchantDeposit item = new DownMerchantDeposit();
		item.setTradeNo(SDFFactory.getLogId());
		mav.addObject("item", convertDetails(item));
		return mav;
	}

	@Transactional(propagation = Propagation.NEVER)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView doAdd(HttpServletRequest httpRequest, DownMerchantDepositServiceData reqData) throws ControllerException {
		DownMerchantData downMerchant = DownMerchants.getByMchNo(getUsername());

		if (!ContentHomeController.isAllowedManuallyAccount(this.getUsername())) {
			return this.alertFailed("/content/downMerchantDeposit/add", reqData, "未开通此权限");
		}

		Date nowTime = new Date();

		reqData.setVersionNo(1);
		// 商户号
		reqData.setMchNo(downMerchant.getMchNo());
		// 商户名
		reqData.setMchName(downMerchant.getName());
		// 平台代付流水号
		reqData.setTransNo(reqData.getMode() + SDFFactory.getOrderNo());
		// 代付登记日期
		reqData.setTransDate(SDFFactory.DATE.format(nowTime));
		// 外发渠道流水号
		reqData.setRealTransNo(reqData.getTransNo());
		// 订单时间, 格式：yyyyMMddHHmmss
		reqData.setOrderDate(SDFFactory.DATETIME.format(nowTime));
		// 用途
		reqData.setPurpose("手动提现");
		// 异步通知地址
		reqData.setNotifyUrl("http://www.baidu.com");

		httpRequest.setAttribute("ignoreIpLimits", "Y");
		
		String errMsg = null;
		DownMerchantDepositServiceData respData = null;
		String oriThreadName = Thread.currentThread().getName();
		try {
			String newThreadName = SDFFactory.getLogId();
			Thread.currentThread().setName(newThreadName);

			// DownMerchantDepositServiceData transferApply(DownMerchantDepositServiceData reqData, HttpServletRequest httpRequest)

			IWebService webService = (IWebService) ApplicationContextHolder.getApplicationContext().getBean("transNocardWebService");
			Method actionMethod = webService.getActionMethod("transferApply");
			respData = (DownMerchantDepositServiceData) webService.executeWithNoTransaction(httpRequest, actionMethod, reqData);
			if (!"00".equals(respData.getStatus())) {
				errMsg = respData.getStatusDesc();
			}
			else {
				return this.alertSuccess("/content/downMerchantDeposit/add", reqData);
			}
		} catch (Exception e) {
			log.error(e, e);
			errMsg = e.getMessage();
		} finally {
			Thread.currentThread().setName(oriThreadName);
		}

		ModelAndView mav = new ModelAndView(getTemplate("/content/downMerchantDeposit/add"));
		mav.addObject("error", errMsg);
		mav.addObject("item", reqData);
		return mav;
	}

	private ModelAndView showIndex(String jsp, SearchDownMerchantDepositViewData searchData, String error) {
		ModelAndView mav = new ModelAndView(jsp);
		formatSearchData(searchData);
		searchData.setMchNo(getUsername());
		IPagedList<DownMerchantDeposit> items = downMerchantDepositDAO.searchItems(searchData);
		mav.addObject("search", searchData);
		mav.addObject("res", convertToList(items));
		mav.addObject("error", error);
		return mav;
	}

	@RequestMapping(value = "/notify/{itemId}", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResponseData<Boolean> notify(@PathVariable String itemId) throws ControllerException, MessageWooException {
		AjaxResponseData<Boolean> ret = new AjaxResponseData<Boolean>(false);

		DownMerchantDeposit model = downMerchantDepositDAO.findOne(itemId);

		addToNotifyRecord(model);

		ret.setMessage("已加入队列");
		ret.setState(EResponseState.Successful);
		return ret;
	}

	/**
	 * @param model
	 */
	private void addToNotifyRecord(DownMerchantDeposit model) {
		NotifyRecord record = notifyRecordDAO.findOne(model.getId());
		if (record == null) {
			record = new NotifyRecord();
			record.setId(model.getId());
			record.setNotifyType(EnumNotifyRecordType.DEPOSIT);
			record.setTradeNo(model.getTradeNo());
			record.setTransNo(model.getTransNo());
			record.setNotifyTimes(0);
			record.setNotifySuccess(false);
			record.setNotifyUrl(model.getNotifyUrl());
			record.setNotifyLastDate(new Date());
			notifyRecordDAO.saveForce(record);
		}
		else {
			record.setNotifyTimes(0);
			record.setNotifyType(EnumNotifyRecordType.DEPOSIT);
			record.setCreateDate(new Date());
			record.setNotifySuccess(false);
			record.setNotifyLastDate(new Date());
			notifyRecordDAO.updateForce(record);
		}
	}

	private ListResponseData<DownMerchantDepositViewData> convertToList(IPagedList<DownMerchantDeposit> items) {
		ListResponseData<DownMerchantDepositViewData> response = new ListResponseData<DownMerchantDepositViewData>();
		if (items != null) {
			for (DownMerchantDeposit item : items) {
				response.addItem(convert(item));
			}
		}
		response.setIndex(items.getPageIndex());
		response.setPages(items.getMaxPages());
		response.setSize(items.getPageSize());
		response.setTotal(items.getTotalSize());
		return response;
	}

	private DownMerchantDepositViewData convert(DownMerchantDeposit item) {
		DownMerchantDepositViewData viewData = SaftyBeanUtils.cloneTo(item, DownMerchantDepositViewData.class);
		return viewData;
	}

	private DownMerchantDepositViewData convertDetails(DownMerchantDeposit item) {
		DownMerchantDepositViewData viewData = convert(item);
		return viewData;
	}
}
