package com.woodare.template.jersery.webservice.busi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woodare.core.util.GoogleVerifyHelper;
import com.woodare.core.util.SDFFactory;
import com.woodare.framework.exception.MessageWooException;
import com.woodare.framework.utils.CommonUtils;
import com.woodare.template.egw.base.IPasswayEgw;
import com.woodare.template.helper.cache.CoinAddrs;
import com.woodare.template.jersery.servicedata.submerchant.SubMerchantServiceData;
import com.woodare.template.jersery.webservice.busi.base.ISubMerchantService;
import com.woodare.template.jpa.model.SubMerchant;
import com.woodare.template.jpa.model.SubMerchantFundAccount;
import com.woodare.template.jpa.model.data.EnumDownUserStatus;
import com.woodare.template.jpa.model.data.EnumOrderStatus;
import com.woodare.template.jpa.persistence.data.downmerchant.DownMerchantData;
import com.woodare.template.jpa.persistence.data.submerchant.SubMerchantData;
import com.woodare.template.jpa.persistence.persistence.ISubMerchantDAO;
import com.woodare.template.jpa.persistence.persistence.ISubMerchantFundAccountDAO;

/**
 * 账户处理服务类
 * 
 * @author Luke
 */
@Service(value = "subMerchantService")
public class SubMerchantService implements ISubMerchantService {
	// private Log log = LogFactory.getLog(SubMerchantService.class);

	@Autowired
	private ISubMerchantDAO subMerchantDAO;

	@Autowired
	private ISubMerchantFundAccountDAO subMerchantFundAccountDAO;

	@Autowired
	private IPasswayEgw sealblockPasswayEgw;

	@Override
	// @Transactional(propagation = Propagation.REQUIRED)
	public SubMerchant createUser(SubMerchantServiceData reqData, DownMerchantData merchantData) throws MessageWooException {
		SubMerchant subMerchant = new SubMerchant();

		subMerchant.setAgentNo(merchantData.getAgentNo() != null ? merchantData.getAgentNo() : "0");
		// 机构号
		subMerchant.setMchNo(reqData.getMchNo());
		// 机构名称
		subMerchant.setMchName(reqData.getMchName());
		// 机构用户标识
		subMerchant.setMchUserNo(reqData.getMchUserNo());
		// 平台用户标识
		subMerchant.setUserNo(CommonUtils.uuid().toUpperCase());
		// TODO: TODO: 生成Sealblock地址, address用户币地址
		subMerchant.setAddress(CommonUtils.uuid());
		subMerchant.setPrikey(subMerchant.getAddress());
		// 用户姓名
		subMerchant.setUserName(reqData.getUserName());
		// 用户身份证号
		subMerchant.setUserCertId(reqData.getUserCertId());
		// 用户联系电话
		subMerchant.setUserPhone(reqData.getUserPhone());
		// 鉴权状态
		subMerchant.setAuthStatus(EnumOrderStatus.CREATE);
		// // 鉴权结果, authMessage
		// subMerchant.setAuthMessage("");
		// 鉴权日期
		subMerchant.setAuthDate(new Date());
		// 已开通币类型，多个逗号分隔开
		subMerchant.setEnabledCoin("");
		// 生成谷歌验证Key
		String googleSecret = GoogleVerifyHelper.createKey();
		subMerchant.setGoogleSecret(googleSecret);
		// 生成Sealblock钱包地址信息
		SubMerchantData blockAccount = sealblockPasswayEgw.generateAddress(googleSecret);
		subMerchant.setAddress(blockAccount.getAddress());
		subMerchant.setPrikey(blockAccount.getPrikey());

		// 用户状态
		subMerchant.setStatus(EnumDownUserStatus.ACTIVE);
		//
		this.subMerchantDAO.saveForce(subMerchant);

		try {
			subMerchant = openCoin(reqData, subMerchant, merchantData);
		}
		//
		catch (MessageWooException e) {
			this.subMerchantDAO.deleteForce(subMerchant);
			throw e;
		}

		return subMerchant;
	}

	@Override
	public SubMerchant updateUser(SubMerchantServiceData reqData, SubMerchant model, DownMerchantData merchantData) throws MessageWooException {

		// 代理商
		model.setAgentNo(merchantData.getAgentNo() != null ? merchantData.getAgentNo() : "0");
		// 机构名称
		model.setMchName(reqData.getMchName());
		// 用户姓名
		model.setUserName(reqData.getUserName());
		// 用户身份证号
		model.setUserCertId(reqData.getUserCertId());
		// 用户联系电话
		model.setUserPhone(reqData.getUserPhone());

		this.subMerchantDAO.updateForce(model);

		return openCoin(reqData, model, merchantData);
	}

	@Override
	public SubMerchant openCoin(SubMerchantServiceData reqData, SubMerchant model, DownMerchantData merchantData) throws MessageWooException {
		if (reqData.getEnabledCoinArr() == null || reqData.getEnabledCoinArr().length == 0) {
			return model;
		}

		List<String> enabledCoins = Arrays.asList(reqData.getEnabledCoinArr());

		List<String> openCoins = new ArrayList<>(16);
		List<SubMerchantFundAccount> existedCoins = this.subMerchantFundAccountDAO.findByUserNoAndCoins(reqData.getUserNo(), enabledCoins);
		for (SubMerchantFundAccount existedCoin : existedCoins) {
			openCoins.add(existedCoin.getCoin());
		}

		String curDate = SDFFactory.DATE.format(new Date());
		for (String coin : enabledCoins) {
			if (!openCoins.contains(coin)) {
				SubMerchantFundAccount fundAccount = new SubMerchantFundAccount();
				fundAccount.setAgentNo(model.getAgentNo());
				fundAccount.setMchNo(model.getMchNo());
				fundAccount.setMchName(merchantData.getName());
				fundAccount.setMchUserNo(model.getMchUserNo());
				fundAccount.setUserNo(model.getUserNo());
				fundAccount.setCoin(coin);
				fundAccount.setUserName(model.getUserName());
				fundAccount.setChangeDate(curDate);
				// 状态
				fundAccount.setStatus(EnumDownUserStatus.ACTIVE);
				// 期初余额
				fundAccount.setLastSettleAmt(new BigDecimal("0"));
				// 账户余额
				fundAccount.setSettleInAmt(new BigDecimal("0"));
				/// 消费金额
				fundAccount.setSettleOutAmt(new BigDecimal("0"));
				/// 冻结金额
				fundAccount.setFrozenAmt(new BigDecimal("0"));
				//
				fundAccount.setRealAmt(new BigDecimal("0"));

				// 生成Sealblock钱包地址信息
				String coinAddress = sealblockPasswayEgw.createCoinWallet(model.getAddress(), fundAccount.getCoin());
				fundAccount.setAddress(coinAddress);

				this.subMerchantFundAccountDAO.saveForce(fundAccount);

				openCoins.add(coin);

				//
				CoinAddrs.add(fundAccount);
			}
		}

		model.setEnabledCoin(StringUtils.join(openCoins, ","));
		this.subMerchantDAO.updateForce(model);

		return model;
	}

}
