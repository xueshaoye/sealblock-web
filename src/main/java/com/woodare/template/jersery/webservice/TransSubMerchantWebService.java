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
package com.woodare.template.jersery.webservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.woodare.core.base.AbstractBusiWebService;
import com.woodare.framework.exception.MessageWooException;
import com.woodare.template.constant.EnumError;
import com.woodare.template.helper.cache.DownMerchants;
import com.woodare.template.jersery.servicedata.submerchant.SubMerchantServiceData;
import com.woodare.template.jersery.webservice.busi.base.ISubMerchantService;
import com.woodare.template.jersery.webservice.utils.ValidHelper;
import com.woodare.template.jpa.model.SubMerchant;
import com.woodare.template.jpa.model.SubMerchantFundAccount;
import com.woodare.template.jpa.model.data.EnumOrderStatus;
import com.woodare.template.jpa.persistence.data.submerchantfundaccount.SubMerchantFundAccountData;
import com.woodare.template.jpa.persistence.persistence.ISubMerchantDAO;
import com.woodare.template.jpa.persistence.persistence.ISubMerchantFundAccountDAO;
import com.woodare.template.web.viewdata.downmerchant.DownMerchantViewData;

/**
 * ClassName: 数字货币用户接口
 * 
 * @description 进行入参基本格式效验、拼装返回结果、共通数据库日志记录
 * @author luke
 * @Date 2018/10/17
 */
@Service
@Scope("request")
public class TransSubMerchantWebService extends AbstractBusiWebService {
	// private Log log = LogFactory.getLog(TransSubMerchantWebService.class);

	// private static TransExpireSet<String> uniquedIds = new TransExpireSet<String>(10);
	// private static TransExpireSet<String> transferIds = new TransExpireSet<String>(10);

	// @Autowired
	// private IDownTradeLogDAO downTradeLogDAO;

	@Autowired
	private ISubMerchantDAO subMerchantDAO;

	@Autowired
	private ISubMerchantFundAccountDAO subMerchantFundAccountDAO;

	@Autowired
	private ISubMerchantService subMerchantService;

	/**
	 * 创建子用户, 生成地址
	 * 
	 * @param reqData
	 * @return
	 * @throws Exception
	 */
	public SubMerchantServiceData createUser(SubMerchantServiceData reqData) throws Exception {
		// 【1】基本参数信息效验
		ValidHelper.notNull(reqData, "解密失败，请求参数为空", EnumError.ERR_9001);

		// 机构用户标识
		ValidHelper.notNull(reqData.getMchUserNo(), "机构用户标识[mchUserNo]不能为空", EnumError.ERR_2001);
		if (reqData.getMchUserNo().length() > 32) {
			throw new MessageWooException("机构用户标识[mchUserNo]不能超过32", EnumError.ERR_2001);
		}
		// /** 用户姓名 */
		ValidHelper.notNull(reqData.getUserName(), "用户姓名[userName]不能为空", EnumError.ERR_2001);
		if (reqData.getUserName().length() > 30) {
			throw new MessageWooException("用户姓名[userName]不能超过30", EnumError.ERR_2001);
		}
		// 用户身份证号
		ValidHelper.notNull(reqData.getUserCertId(), "身份证号 [userCertId]不能为空", EnumError.ERR_2001);
		if (reqData.getUserCertId().length() > 30) {
			throw new MessageWooException("身份证号[userCertId]不能超过30", EnumError.ERR_2001);
		}
		// 用户联系电话
		ValidHelper.notNull(reqData.getUserPhone(), "联系电话 [userPhone]不能为空", EnumError.ERR_2001);
		ValidHelper.validPhone(reqData.getUserPhone(), "联系电话 [userPhone]格式有误", EnumError.ERR_2001);

		DownMerchantViewData downMerchant = DownMerchants.getByMchNo(reqData.getMchNo());
		if (StringUtils.isNotEmpty(reqData.getEnabledCoin())) {
			String[] enabledCoinArr = reqData.getEnabledCoin().split(",", -1);
			for (String enabledCoin : enabledCoinArr) {
				if (!downMerchant.getSupportCoins().contains(enabledCoin)) {
					throw new MessageWooException("机构未开放[" + enabledCoin + "]交易", EnumError.ERR_2001);
				}
			}
			reqData.setEnabledCoinArr(enabledCoinArr);
		}

		SubMerchantServiceData respData = null;
		SubMerchant existedUser = this.subMerchantDAO.findByMchNoAndMchUserNo(downMerchant.getMchNo(), reqData.getMchUserNo());
		if (existedUser != null) {
			throw new MessageWooException("机构子用户编号已注册", EnumError.ERR_2001);
		}
		existedUser = this.subMerchantDAO.findByMchNoAndCertId(downMerchant.getMchNo(), reqData.getUserCertId());
		if (existedUser != null) {
			throw new MessageWooException("身份证已注册，原用户编号[" + existedUser.getMchUserNo() + "]", EnumError.ERR_2001);
		}
		existedUser = this.subMerchantDAO.findByMchNoAndMobile(downMerchant.getMchNo(), reqData.getUserPhone());
		if (existedUser != null) {
			throw new MessageWooException("手机号已注册，原用户编号[" + existedUser.getMchUserNo() + "]", EnumError.ERR_2001);
		}

		SubMerchant model = subMerchantService.createUser(reqData, downMerchant);
		respData = toServiceData(reqData, model, downMerchant.getSupportCoins(), true);
		//
		// // 判断账户号已存在时，进行更新操作
		// if (existedUser != null) {
		// respData = updateUser(reqData);
		// }
		// else {
		// existedUser = this.subMerchantDAO.findByMchNoAndCertId(downMerchant.getMchNo(), reqData.getUserCertId());
		// if (existedUser != null) {
		// throw new MessageWooException("身份证已注册[" + existedUser.getUserCertId() + "]", EnumError.ERR_2001);
		// }
		// existedUser = this.subMerchantDAO.findByMchNoAndMobile(downMerchant.getMchNo(), reqData.getUserPhone());
		// if (existedUser != null) {
		// throw new MessageWooException("手机号已注册[" + existedUser.getUserPhone() + "]", EnumError.ERR_2001);
		// }
		//
		// SubMerchant model = subMerchantService.createUser(reqData, downMerchant);
		// respData = toServiceData(reqData, model, downMerchant.getSupportCoins(), true);
		// }
		return respData;
	}

	/**
	 * 变更子商户信息
	 * 
	 * @param reqData
	 * @return
	 * @throws Exception
	 */
	public SubMerchantServiceData updateUser(SubMerchantServiceData reqData) throws Exception {
		// 【1】基本参数信息效验
		ValidHelper.notNull(reqData, "解密失败，请求参数为空", EnumError.ERR_9001);

		// 平台用户标识
		ValidHelper.notNull(reqData.getUserNo(), "平台用户标识[userNo]不能为空", EnumError.ERR_2001);

		// 用户姓名
		ValidHelper.notNull(reqData.getUserName(), "用户姓名[userName]不能为空", EnumError.ERR_2001);
		if (reqData.getUserName().length() > 30) {
			throw new MessageWooException("用户姓名[userName]不能超过30", EnumError.ERR_2001);
		}
		// 用户身份证号
		ValidHelper.notNull(reqData.getUserCertId(), "身份证号 [userCertId]不能为空", EnumError.ERR_2001);
		if (reqData.getUserCertId().length() > 30) {
			throw new MessageWooException("身份证号[userCertId]不能超过30", EnumError.ERR_2001);
		}
		// 用户联系电话
		ValidHelper.notNull(reqData.getUserPhone(), "联系电话 [userPhone]不能为空", EnumError.ERR_2001);
		ValidHelper.validPhone(reqData.getUserPhone(), "联系电话 [userPhone]格式有误", EnumError.ERR_2001);

		// 强制清理掉开货币操作
		reqData.setEnabledCoin(null);

		SubMerchant model = this.subMerchantDAO.findByMchNoAndMobile(reqData.getMchNo(), reqData.getUserPhone());
		if (model == null || !model.getMchNo().equals(reqData.getMchNo())) {
			throw new MessageWooException("用户不存在", EnumError.ERR_2001);
		}

		DownMerchantViewData downMerchant = DownMerchants.getByMchNo(reqData.getMchNo());
		// 判断用户未认证, 允许更换姓名和身份证号
		if (!EnumOrderStatus.SUCCESS.equals(model.getAuthStatus())) {
			model.setUserName(reqData.getUserName());

			// 变更身份证，验证是否重复
			if (!model.getUserCertId().equals(reqData.getUserCertId())) {
				SubMerchant existedUser = this.subMerchantDAO.findByMchNoAndCertId(downMerchant.getMchNo(), reqData.getUserCertId());
				if (existedUser != null && !existedUser.getUserNo().equals(reqData.getUserNo())) {
					throw new MessageWooException("身份证已注册[" + existedUser.getUserCertId() + "]", EnumError.ERR_3005);
				}
				model.setUserCertId(reqData.getUserCertId());
			}
		}
		// 已认证用户
		else {
			if (!reqData.getUserName().equals(model.getUserName())) {
				throw new MessageWooException("已认证用户无法修改户名", EnumError.ERR_3004);
			}
			if (!model.getUserCertId().equals(reqData.getUserCertId())) {
				throw new MessageWooException("已认证用户无法修改身份证号", EnumError.ERR_3004);
			}
		}

		// 变更联系电话，验证是否重复
		if (!model.getUserPhone().equals(reqData.getUserPhone())) {
			SubMerchant existedUser = this.subMerchantDAO.findByMchNoAndMobile(downMerchant.getMchNo(), reqData.getUserPhone());
			if (existedUser != null && !existedUser.getUserNo().equals(reqData.getUserNo())) {
				throw new MessageWooException("手机号已注册[" + existedUser.getUserPhone() + "]", EnumError.ERR_3005);
			}
			model.setUserPhone(reqData.getUserPhone());
		}

		model = subMerchantService.updateUser(reqData, model, downMerchant);

		return toServiceData(reqData, model, downMerchant.getSupportCoins(), true);
	}

	/**
	 * 开通指定货币
	 * 
	 * @param reqData
	 * @return
	 * @throws Exception
	 */
	public SubMerchantServiceData openUserCoin(SubMerchantServiceData reqData) throws Exception {
		// 【1】基本参数信息效验
		ValidHelper.notNull(reqData, "解密失败，请求参数为空", EnumError.ERR_9001);

		// 平台用户标识
		ValidHelper.notNull(reqData.getUserNo(), "平台用户标识[userNo]不能为空", EnumError.ERR_2001);
		// 开通指定货币
		ValidHelper.notNull(reqData.getEnabledCoin(), "开通货币[enabledCoin]不能为空", EnumError.ERR_2001);

		SubMerchant model = this.subMerchantDAO.findByUserNo(reqData.getUserNo());
		if (model == null || !model.getMchNo().equals(reqData.getMchNo())) {
			throw new MessageWooException("用户不存在", EnumError.ERR_2001);
		}

		DownMerchantViewData downMerchant = DownMerchants.getByMchNo(reqData.getMchNo());

		String[] enabledCoinArr = reqData.getEnabledCoin().split(",", -1);
		for (String enabledCoin : enabledCoinArr) {
			if (!downMerchant.getSupportCoins().contains(enabledCoin)) {
				throw new MessageWooException("机构未开放[" + enabledCoin + "]交易", EnumError.ERR_2001);
			}
		}
		reqData.setEnabledCoinArr(enabledCoinArr);

		model = subMerchantService.openCoin(reqData, model, downMerchant);

		return toServiceData(reqData, model, downMerchant.getSupportCoins(), true);
	}

	/**
	 * 查询用户账户余额
	 * 
	 * @param reqData
	 * @return
	 * @throws Exception
	 */
	public SubMerchantServiceData userBalQuery(SubMerchantServiceData reqData) throws Exception {
		// 【1】基本参数信息效验
		ValidHelper.notNull(reqData, "解密失败，请求参数为空", EnumError.ERR_9001);

		// 平台用户标识
		ValidHelper.notNull(reqData.getUserNo(), "平台用户标识[userNo]不能为空", EnumError.ERR_2001);

		SubMerchant model = this.subMerchantDAO.findByUserNo(reqData.getUserNo());
		if (model == null || !model.getMchNo().equals(reqData.getMchNo())) {
			throw new MessageWooException("用户不存在", EnumError.ERR_2001);
		}

		DownMerchantViewData downMerchant = DownMerchants.getByMchNo(reqData.getMchNo());

		return toServiceData(reqData, model, downMerchant.getSupportCoins(), true);
	}

	/**
	 * 拼装返回数据结构
	 * 
	 * @param model
	 * @return
	 */
	public SubMerchantServiceData toServiceData(SubMerchantServiceData reqData, SubMerchant model, List<String> supportedCoins, boolean withBalFlag) {
		SubMerchantServiceData isd = new SubMerchantServiceData();

		isd.setMchNo(model.getMchNo());
		isd.setMchName(model.getMchName());
		isd.setUserNo(model.getUserNo());
		// 容易引起用户费解
		// isd.setAddress(model.getAddress());
		isd.setUserName(model.getUserName());
		isd.setUserCertId(model.getUserCertId());
		isd.setUserPhone(model.getUserPhone());
		isd.setEnabledCoin(model.getEnabledCoin());
		isd.setStatus(model.getStatus());
		isd.setAuthStatus(model.getAuthStatus());

		if (withBalFlag && supportedCoins != null && supportedCoins.size() > 0) {
			List<SubMerchantFundAccount> fundingModels = this.subMerchantFundAccountDAO.findByUserNoAndCoins(model.getUserNo(), supportedCoins);
			List<String> userEnabledCoins = Arrays.asList(model.getEnabledCoin().split(",", -1));
			List<SubMerchantFundAccountData> coinBals = new ArrayList<>(16);
			for (SubMerchantFundAccount fundingModel : fundingModels) {
				if (userEnabledCoins.contains(fundingModel.getCoin())) {
					SubMerchantFundAccountData coinBal = new SubMerchantFundAccountData();
					coinBal.setCoin(fundingModel.getCoin());
					// 区块链真实币地址
					coinBal.setAddress(fundingModel.getAddress());
					// respData.setCoinName(coinName);
					// coinBal.setLastSettleAmt(fundingModel.getLastSettleAmt());
					coinBal.setSettleInAmt(fundingModel.getSettleInAmt());
					coinBal.setSettleOutAmt(fundingModel.getSettleOutAmt());
					coinBal.setFrozenAmt(fundingModel.getFrozenAmt());
					coinBal.setStatus(fundingModel.getStatus());
					coinBals.add(coinBal);
				}
			}
			isd.setEnabledCoinItems(coinBals);
		}

		return isd;
	}
}
