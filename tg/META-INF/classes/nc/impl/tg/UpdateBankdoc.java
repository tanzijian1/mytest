package nc.impl.tg;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.pub.doc.DefdocUtils;
import nc.bs.tg.outside.tobankdoc.InsertBankdoc;
import nc.itf.bd.bankdoc.IBankdocService;
import nc.itf.tg.outside.EBSCont;
import nc.vo.bd.bankdoc.BankdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.tg.outside.EBSBankdocVO;

public class UpdateBankdoc implements InsertBankdoc {
	
	BaseDAO baseDAO = null;

	@Override
	public Map<String,String> executeTask(EBSBankdocVO ebsBankdocVO,String srcsystem,String dectype) throws BusinessException {
		
		IBankdocService bankdocService = NCLocator.getInstance().lookup(
				IBankdocService.class);
		Map<String,String> refMap = new HashMap<String,String>();
		DefdocUtils defdocUtils = new DefdocUtils();
		// 查询所属组织PK及其校验
		String pk_org = null;
		if ("".equals(ebsBankdocVO.getOrg_code())
				|| ebsBankdocVO.getOrg_code() == null) {
			throw new BusinessException("操作失败，组织不能为空，请检查参数设置");
		} else {
			pk_org = defdocUtils.getPk_org((String) ebsBankdocVO.getOrg_code());
			if (pk_org == null) {
				throw new BusinessException("操作失败，组织输入有误，请检查参数设置");
			}
		}
		// 查询银行类别名称及其校验
		String pk_banktype = null;
		if ("".equals(ebsBankdocVO.getBanktype_code())
				|| ebsBankdocVO.getBanktype_code() == null) {
			throw new BusinessException("操作失败，银行类别不能为空，请检查参数设置");
		} else {
			pk_banktype = defdocUtils.getBanktype((String) ebsBankdocVO.getBanktype_code());
			if (pk_banktype == null) {
				throw new BusinessException("操作失败，银行类别输入有误，请检查参数设置");
			}
		}
		// code校验
		if ("".equals(ebsBankdocVO.getCode()) || ebsBankdocVO.getCode() == null) {
			throw new BusinessException("操作失败，银行编码不能为空，请检查参数设置");
		}
		// name校验
		if ("".equals(ebsBankdocVO.getName()) || ebsBankdocVO.getName() == null) {
			throw new BusinessException("操作失败，银行名称不能为空，请检查参数设置");
		}
		int enablestate = 0;
		if (ebsBankdocVO.getEnablestate() == null) {
			enablestate = 2;
		} else {
			enablestate = (Integer) ebsBankdocVO.getEnablestate();
		}

		BankdocVO headVO = new BankdocVO();
		// 来源系统
		headVO.setDef1(srcsystem);

		headVO.setPk_group("000112100000000005FD");
		headVO.setPk_org(pk_org);// 所属组织
		headVO.setName(ebsBankdocVO.getName());// 银行名称
		headVO.setCode(ebsBankdocVO.getCode());// 银行编码
		headVO.setShortname(ebsBankdocVO.getShortname());// 简称
		headVO.setPk_banktype(pk_banktype); // 银行类别
		headVO.setPk_country("0001Z010000000079UJJ");// 国家（默认中国）
		headVO.setEnablestate(enablestate);// 启用状态（默认已启用）
		headVO.setDef2(ebsBankdocVO.getDef2());
		headVO.setDef3(ebsBankdocVO.getDef3());
		headVO.setDef4(ebsBankdocVO.getDef4());
		headVO.setDef5(ebsBankdocVO.getDef5());
		headVO.setDr(0);
		headVO.setStatus(VOStatus.NEW);
		String billqueue = EBSCont.getDocNameMap().get(dectype) + ":"
				+ headVO.getCode();
		String billkey = EBSCont.getDocNameMap().get(dectype) + ":"
				+ headVO.getName();
		try {
			EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
			Collection<BankdocVO> docVO = getBaseDAO().retrieveByClause(
					BankdocVO.class,
					"isnull(dr,0)=0 and code = '" + headVO.getCode() + "'");
			if (docVO != null && docVO.size() > 0) {
				BankdocVO[] bankdocVO = docVO.toArray(new BankdocVO[0]);
				// add by tjl 2019-09-09
				// 来源系统
				bankdocVO[0].setDef1(srcsystem);

				bankdocVO[0].setStatus(VOStatus.UPDATED);
				bankdocVO[0].setPk_org(pk_org);// 所属组织
				bankdocVO[0].setName(ebsBankdocVO.getName());// 银行名称
				bankdocVO[0].setShortname(ebsBankdocVO.getShortname());// 简称
				bankdocVO[0].setPk_banktype(pk_banktype); // 银行类别
				bankdocVO[0].setEnablestate(enablestate);//启用状态
				bankdocVO[0].setDef2(ebsBankdocVO.getDef2());
				bankdocVO[0].setDef3(ebsBankdocVO.getDef3());
				bankdocVO[0].setDef4(ebsBankdocVO.getDef4());
				bankdocVO[0].setDef5(ebsBankdocVO.getDef5());
				bankdocService.updateBankdocVO(bankdocVO[0]);
			} else {
				bankdocService.insertBankdocVO(headVO, false);
			}
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】,"
					+ e.getMessage(), e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}
		refMap.put("msg", "【银行档案】," + "操作完成!");
		refMap.put("data", "");
		return refMap;
	}

	/**
	 * 数据库持久化
	 * 
	 * @return
	 */
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	

}
