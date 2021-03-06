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
package com.woodare.template.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.woodare.core.base.BaseController;
import com.woodare.core.exception.ControllerException;
import com.woodare.core.util.GoogleVerifyHelper;
import com.woodare.core.util.SDFFactory;
import com.woodare.core.web.common.viewdata.AjaxResponseData;
import com.woodare.core.web.common.viewdata.ListResponseData;
import com.woodare.framework.data.EResponseState;
import com.woodare.framework.data.IPagedList;
import com.woodare.framework.exception.AbstractWooException;
import com.woodare.framework.model.data.EnumDeleteStatus;
import com.woodare.framework.utils.SaftyBeanUtils;
import com.woodare.template.busi.service.CacheDataChangeService;
import com.woodare.template.egw.base.IPasswayEgw;
import com.woodare.template.helper.cache.DownAgents;
import com.woodare.template.helper.cache.DownMerchantFundAccounts;
import com.woodare.template.helper.cache.PasswayRouteMerchants;
import com.woodare.template.jpa.model.DownMerchant;
import com.woodare.template.jpa.model.DownMerchantFundAccount;
import com.woodare.template.jpa.model.data.EnumDownUserStatus;
import com.woodare.template.jpa.model.data.EnumFundAccountType;
import com.woodare.template.jpa.persistence.data.downmerchant.DownMerchantData;
import com.woodare.template.jpa.persistence.data.downmerchantfundaccount.DownMerchantFundAccountData;
import com.woodare.template.jpa.persistence.data.passwayroutemerchant.PasswayRouteMerchantData;
import com.woodare.template.jpa.persistence.data.submerchant.SubMerchantData;
import com.woodare.template.jpa.persistence.persistence.IDownMerchantDAO;
import com.woodare.template.jpa.persistence.persistence.IDownMerchantFundAccountDAO;
import com.woodare.template.web.viewdata.downmerchant.DownMerchantViewData;
import com.woodare.template.web.viewdata.downmerchant.SearchDownMerchantViewData;
import com.woodare.template.web.viewdata.downmerchantfundaccount.DownMerchantFundAccountViewData;
import com.woodare.template.web.viewdata.passwayroutemerchant.PasswayRouteMerchantViewData;

/**
 * ClassName: AdminDownMerchantController
 * 
 * @description
 * @author Luke
 * @Date Feb 28, 2018
 */
@Controller
@RequestMapping("/admin/downMerchant")
public class AdminDownMerchantController extends BaseController {

	private static Logger log = Logger.getLogger(AdminDownMerchantController.class);

	@Autowired
	private IDownMerchantDAO downMerchantDAO;

	@Autowired
	private IDownMerchantFundAccountDAO downMerchantFundAccountDAO;

	@Autowired
	private CacheDataChangeService cacheDataChangeService;

	@Autowired
	private IPasswayEgw sealblockPasswayEgw;

	@Transactional(propagation = Propagation.NEVER)
	@RequestMapping({ "/index", "/export" })
	public ModelAndView index(SearchDownMerchantViewData searchData, HttpServletResponse response) throws ControllerException {
		if (searchData.getDoExportFlag() != null && searchData.getDoExportFlag()) {
			searchData.setPageSize(Integer.MAX_VALUE);
		}

		IPagedList<DownMerchant> items = downMerchantDAO.searchDownMerchants(searchData);

		if (searchData.getDoExportFlag() != null && searchData.getDoExportFlag()) {
			String responName = "merchant-" + SDFFactory.DATETIME.format(new Date()) + ".csv";
			return this.exportToResponse(response, responName, formatString(items));
		}

		ModelAndView mav = new ModelAndView(getTemplate("/admin/downMerchant/index"));
		mav.addObject("search", searchData);
		mav.addObject("res", convertToList(items));
		return mav;
	}

	@Transactional(propagation = Propagation.NEVER)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(DownMerchantViewData data) throws ControllerException {
		ModelAndView mav = new ModelAndView(getTemplate("/admin/downMerchant/add"));

		DownMerchantViewData item = null;

		List<DownMerchantFundAccount> fundModels = null;
		if (StringUtils.isNotEmpty(data.getId())) {
			DownMerchant model = downMerchantDAO.findOne(data.getId());
			item = convertDetails(model);

			fundModels = downMerchantFundAccountDAO.searchByMchNo(model.getMchNo());
		}
		else {
			item = new DownMerchantViewData();
			item.setCreditRatio(70l);
		}

		mav.addObject("item", item);
		// 代理列表
		mav.addObject("agents", DownAgents.getAll());
		// 产品列表
		mav.addObject("products", getProducts(fundModels));

		return mav;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView addP(DownMerchantViewData data) throws ControllerException {
		// 预处理数据
		String error = preHandleData(data);

		//
		List<DownMerchantFundAccount> fundItems = data.getFundItems();

		if (StringUtils.isEmpty(error)) {
			DownMerchant model = null;
			if (StringUtils.isNotEmpty(data.getId())) {
				model = downMerchantDAO.findOne(data.getId());
			}
			boolean update = true;
			if (model == null) {
				model = new DownMerchant();
				model.setMchNo(downMerchantDAO.getMaxMechNo());
				update = false;
			}

			SaftyBeanUtils.copyNotEmptyProperties(data, model, new String[] { "id", "mchNo", "settleDate" });
			if (model.getCreditRatio() == null) {
				model.setCreditRatio(0l);
			}
			if (update) {
				downMerchantDAO.update(model);
			}
			else {
				model.setSettleDate(SDFFactory.DATE.format(new Date()));

				// 生成谷歌验证Key
				String googleSecret = GoogleVerifyHelper.createKey();
				model.setGoogleSecret(googleSecret);

				// 生成Sealblock钱包地址信息
				try {
					SubMerchantData blockAccount = sealblockPasswayEgw.generateAddress(googleSecret);
					model.setAddress(blockAccount.getAddress());
					model.setPrikey(blockAccount.getPrikey());
				}
				// 生成地址异常
				catch (AbstractWooException e) {
					log.error(e, e);
					return buildAddErrorPage(data, fundItems, e.getMessage());
				}
				downMerchantDAO.save(model);
			}

			// 保存机构开通产品费率信息
			for (int i = 0; i < fundItems.size(); i++) {
				DownMerchantFundAccount fundItem = fundItems.get(i);

				// 生成账户余额表数据
				DownMerchantFundAccount fundModel = downMerchantFundAccountDAO.findByMchNoAndCoin(model.getMchNo(), fundItem.getCoin());
				if (fundModel == null) {
					fundModel = fundItem;
					// SaftyBeanUtils.copyNotEmptyProperties(fundItem, fundModel, new String[] { "id" });

					fundModel.setMchNo(model.getMchNo());
					fundModel.setMchName(model.getName());
					fundModel.setAccountType(EnumFundAccountType.Merchant);
					
					// 期初余额
					fundModel.setLastSettleAmt(new BigDecimal("0"));
					// 账户余额
					fundModel.setSettleInAmt(new BigDecimal("0"));
					// 清算金额
					fundModel.setSettleOutAmt(new BigDecimal("0"));
					// 冻结金额
					fundModel.setFrozenAmt(new BigDecimal("0"));
					// 当日入账额
					fundModel.setCurInAmt(new BigDecimal("0"));
					// 提现金额
					fundModel.setCurOutAmt(new BigDecimal("0"));
					// 最后变更日期
					fundModel.setChangeDate(SDFFactory.DATE.format(new Date()));

					// 生成Sealblock钱包地址信息
					try {
						String coinAddress = sealblockPasswayEgw.createCoinWallet(model.getAddress(), fundModel.getCoin());
						fundModel.setAddress(coinAddress);
					}
					// 生成地址异常
					catch (AbstractWooException e) {
						log.error(e, e);
						return buildAddErrorPage(data, fundItems, e.getMessage());
					}

					downMerchantFundAccountDAO.save(fundModel);
				}
				else {
					downMerchantFundAccountDAO.updateById(fundItem, fundModel.getId());
				}
			}
			cacheDataChangeService.notifyOthers(DownMerchantData.class);
			cacheDataChangeService.notifyOthers(DownMerchantFundAccountData.class);

			return new ModelAndView("redirect:/admin/downMerchant/add?id=" + model.getId());
		}
		else {
			return buildAddErrorPage(data, fundItems, error);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/delete/{itemId}", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResponseData<Boolean> delete(@PathVariable String itemId) {
		AjaxResponseData<Boolean> ret = new AjaxResponseData<Boolean>(false);
		ret.setState(EResponseState.CustomMsg);
		String message = "";
		try {
			if (StringUtils.isNotEmpty(itemId)) {
				DownMerchant model = downMerchantDAO.findOne(itemId);
				if (model == null || model.getDeleteStatus() == EnumDeleteStatus.DELETED) {
					message = "数据已被删除，请重新刷新画面！";
				}
				else {
					downMerchantDAO.deleteWithLogical(model);

					cacheDataChangeService.notifyOthers(DownMerchantData.class);

					message = "删除成功！";
					ret.setState(EResponseState.Successful);
				}
			}
			else {
				message = "数据已被删除，请重新刷新画面！";
			}
		} catch (Exception e) {
			log.error(e, e);
			message = "删除失败！ " + e.getMessage();
		}
		ret.setMessage(message);
		return ret;
	}

	private ListResponseData<DownMerchantViewData> convertToList(IPagedList<DownMerchant> items) {
		ListResponseData<DownMerchantViewData> response = new ListResponseData<DownMerchantViewData>();
		if (items != null) {
			for (DownMerchant item : items) {
				response.addItem(convert(item));
			}
		}
		response.setIndex(items.getPageIndex());
		response.setPages(items.getMaxPages());
		response.setSize(items.getPageSize());
		response.setTotal(items.getTotalSize());
		return response;
	}

	private DownMerchantViewData convertDetails(DownMerchant item) {
		DownMerchantViewData viewData = convert(item);
		return viewData;
	}

	private DownMerchantViewData convert(DownMerchant item) {
		DownMerchantViewData viewData = SaftyBeanUtils.cloneTo(item, DownMerchantViewData.class);

		if (item != null && StringUtils.isNotEmpty(item.getMchNo())) {
			List<DownMerchantFundAccountData> products = DownMerchantFundAccounts.getByMchNo(item.getMchNo());
			String fundSummry = "-";
			if (products != null && products.size() > 0) {
				fundSummry = "";
				for (DownMerchantFundAccountData product : products) {
					fundSummry += String.format("<span class='lbl-summary'>%s</span>= %s /%s", product.getCoin(), product.getFeeRatio() + "‰", product.getAddFeeAmt()) + "<br>";
				}
			}
			viewData.setFundSummary(fundSummry);
		}

		return viewData;
	}

	private String preHandleData(DownMerchantViewData data) {
		String error = "";

		List<DownMerchantFundAccount> fundItems = new ArrayList<DownMerchantFundAccount>();
		List<String> selCoins = null;
		if (data.getCoinArr() != null) {
			selCoins = data.getSelCoinArr() != null ? Arrays.asList(data.getSelCoinArr()) : new ArrayList<String>();

			for (int i = 0; i < data.getCoinArr().length; i++) {
				DownMerchantFundAccount fundItem = new DownMerchantFundAccount();
				fundItem.setMchNo(data.getMchNo());
				fundItem.setCoin(data.getCoinArr()[i]);

				fundItem.setMchName(data.getName());
				fundItem.setAddFeeAmt(StringUtils.isNotEmpty(data.getAddFeeAmtArr()[i]) ? new BigDecimal(data.getAddFeeAmtArr()[i]) : new BigDecimal("-1"));
				fundItem.setFeeRatio(StringUtils.isNotEmpty(data.getFeeRatioArr()[i]) ? new BigDecimal(data.getFeeRatioArr()[i]) : new BigDecimal("-1"));

				if (selCoins.contains(fundItem.getCoin())) {
					fundItem.setStatus(EnumDownUserStatus.ACTIVE);
				}
				else {
					fundItem.setStatus(EnumDownUserStatus.PENDING);
				}
				fundItems.add(fundItem);
			}
		}
		if (selCoins == null) {
			data.setSupportCoin("");
		}
		else {
			data.setSupportCoin(StringUtils.join(selCoins, ","));
		}
		data.setFundItems(fundItems);
		return error;
	}

	/**
	 * @param existedFundItems
	 * @return
	 */
	private List<DownMerchantFundAccountViewData> getProducts(List<DownMerchantFundAccount> existedFundItems) {
		List<DownMerchantFundAccountViewData> products = new ArrayList<DownMerchantFundAccountViewData>();

		for (PasswayRouteMerchantViewData coinItem : PasswayRouteMerchants.getAll()) {
			DownMerchantFundAccountViewData fundItem = null;
			DownMerchantFundAccount existProduct = null;
			if (existedFundItems != null) {
				for (DownMerchantFundAccount tmpProduct : existedFundItems) {
					if (coinItem.getCoin().equals(tmpProduct.getCoin())) {
						existProduct = tmpProduct;
						break;
					}
				}
			}
			if (existProduct != null) {
				fundItem = SaftyBeanUtils.cloneTo(existProduct, DownMerchantFundAccountViewData.class);
			}
			else {
				fundItem = new DownMerchantFundAccountViewData();
				fundItem.setCoin(coinItem.getCoin());
			}

			fundItem.setCoinName(coinItem.getCoinName());
			if (fundItem.getFeeRatio() == null || new BigDecimal("-1").compareTo(fundItem.getFeeRatio()) == 0) {
				fundItem.setFeeRatio(new BigDecimal("0"));
			}
			if (fundItem.getAddFeeAmt() == null || new BigDecimal("-1").compareTo(fundItem.getAddFeeAmt()) == 0) {
				fundItem.setAddFeeAmt(new BigDecimal("0"));
			}

			if (EnumDownUserStatus.PENDING.equals(fundItem.getStatus())) {
				if (fundItem.getFeeRatio() != null && new BigDecimal("-1").compareTo(fundItem.getFeeRatio()) == 0) {
					fundItem.setFeeRatio(null);
				}
				if (fundItem.getAddFeeAmt() != null && new BigDecimal("-1").compareTo(fundItem.getAddFeeAmt()) == 0) {
					fundItem.setAddFeeAmt(null);
				}
			}
			else if (fundItem.getStatus() == null) {
				fundItem.setStatus(EnumDownUserStatus.PENDING);
			}

			PasswayRouteMerchantData coinSettings = PasswayRouteMerchants.getByCoin(fundItem.getCoin());
			String minPerAmt = coinSettings.getMinPerAmt() == null ? "-" : coinSettings.getMinPerAmt();
			String maxPerAmt = coinSettings.getMaxTotAmt() == null ? "-" : coinSettings.getMaxTotAmt();

			fundItem.setCoinDescription(String.format("精度: %s, 最低: %s, 最高: %s", coinSettings.getPriceScale().toString(), minPerAmt, maxPerAmt));
			products.add(fundItem);
		}
		return products;
	}

	/**
	 * @return
	 */
	private ModelAndView buildAddErrorPage(DownMerchantViewData data, List<DownMerchantFundAccount> fundItems, String error) {
		// 强制回滚事务处理
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		ModelAndView mav = alertFailed(getTemplate("/admin/downMerchant/add"), data, error);

		// 初始化代理列表
		mav.addObject("agents", DownAgents.getAll());
		// 初始化产品列表
		mav.addObject("products", getProducts(fundItems));

		return mav;
	}

	/**
	 * @param items
	 * @return
	 */
	private String formatString(List<DownMerchant> items) {
		StringBuffer sb = new StringBuffer();
		sb.append("机构类别,");
		sb.append("机构编号,");
		sb.append("机构名称,");
		sb.append("状态,");
		sb.append("代理商");
		sb.append("\n");
		if (items != null) {
			for (DownMerchant item : items) {
				sb.append(formatValue(item.getMercCategory()));
				sb.append(formatValue(item.getMchNo(), true));
				sb.append(formatValue(item.getName()));
				if (item.getStatus() == EnumDownUserStatus.ACTIVE) {
					sb.append(formatValue("使用中"));
				}
				else if (item.getStatus() == EnumDownUserStatus.PENDING) {
					sb.append(formatValue("未使用"));
				}
				else if (item.getStatus() == EnumDownUserStatus.HOLD) {
					sb.append(formatValue("暂停使用"));
				}
				sb.append(formatValue(item.getAgentNo()));
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
