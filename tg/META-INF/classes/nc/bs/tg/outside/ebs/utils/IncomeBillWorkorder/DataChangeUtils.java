package nc.bs.tg.outside.ebs.utils.IncomeBillWorkorder;

import java.util.Collection;

import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.vo.tg.invoicebill.InvoiceBillBVO;
import nc.vo.tg.invoicebill.InvoiceBillVO;
import nc.vo.tg.outside.incomebill.IncomeBillBodyVO;
import nc.vo.tg.outside.incomebill.IncomeBillHeadVO;

import org.apache.commons.lang.StringUtils;

public class DataChangeUtils extends IncomeBillWorkorderUtils{
	static DataChangeUtils utils;
	public  static  DataChangeUtils getUtils(){
		if(utils==null)
			utils = new  DataChangeUtils();
		return utils;
		
	}

	/**
	 * 主体信息
	 * 
	 * @param hvo
	 * @param headvo
	 */
	protected void setHeaderVO(InvoiceBillVO hvo,
			IncomeBillHeadVO headvo) throws BusinessException {
		checkHeaderNotNull(headvo);
		hvo.setPk_org(getPk_orgByCode(headvo.getOrg()));// 应付财务组织->NC业务单元编码
		//hvo.setPk_org(headvo.getOrg()); //财务组织
		hvo.setPk_deptid_v(headvo.getDept()); //经办部门
		hvo.setPk_psndoc(headvo.getPsndoc()); //经办人
		hvo.setBillno(headvo.getEbsbillno()); //单据号
		hvo.setBilldate(new UFDate( headvo.getBilldate())); //单据日期
		hvo.setBillstatus(new Integer(headvo.getBillstatus())); //单据状态
		hvo.setApprovestatus(new Integer(headvo.getApprovestatus())); //审批状态
		hvo.setEffectstatus(new Integer (headvo.getEffectstatus())); //生效状态
		hvo.setObjtype(headvo.getObjtype()); //往来对象
		hvo.setSupplier(headvo.getSupplier()); //供应商
		hvo.setChecktype(headvo.getChecktype()); //票据类型（票种）
		hvo.setPk_busitype(headvo.getBusitypeflow()); //业务流程
		hvo.setBusitype(headvo.getBusitype()); //业务类型
		hvo.setDef1(headvo.getEbsid()); //外系统主键
		hvo.setDef2(headvo.getEbsbillno()); //外系统单据号
		hvo.setDef3(headvo.getImgno()); //影像编码
		hvo.setDef4(headvo.getImgstate()); //影像状态
		hvo.setDef5(headvo.getContcode()); //合同编码
		hvo.setDef6(headvo.getContname()); //合同名称
		hvo.setDef7(headvo.getConttype()); //合同类型
		hvo.setDef9(headvo.getEmergency()); //紧急程度
		hvo.setDef10(headvo.getEbsname()); //外系统名称
		hvo.setDef11(headvo.getTotalmny_paybythisreq()); //发票总金额（价税合计）

	}

	/**
	 * 明细信息
	 * 
	 * @param parentVO
	 * @param itmevo
	 * @param bodyvo
	 * @throws BusinessException
	 */
	protected void setItemVO(InvoiceBillVO parentVO,
			InvoiceBillBVO itmevo, IncomeBillBodyVO bodyvo)
			throws BusinessException {
		checkItemNotNull(bodyvo);
		itmevo.setScomment(bodyvo.getScomment()); //摘要
		itmevo.setSupplier(bodyvo.getSupplier()); //供应商
		itmevo.setMoney_de(new UFDouble(bodyvo.getMoney_de())); //数量
		itmevo.setTaxprice(new UFDouble(bodyvo.getTaxprice())); //发票含税单价
		itmevo.setTaxrate(new UFDouble(bodyvo.getTaxrate())); //发票税率
		itmevo.setNotax_de(new UFDouble(bodyvo.getNotax_de())); //发票不含税金额
		itmevo.setLocal_tax_de(new UFDouble(bodyvo.getLocal_tax_de())); //发票税额
		itmevo.setMoney_de(new UFDouble(bodyvo.getMoney_de())); //发票价税合计
		itmevo.setDef1(bodyvo.getContractno()); //供应合同编号
		itmevo.setDef2(bodyvo.getProject()); //项目
		itmevo.setDef3(bodyvo.getProjectphase()); //项目分期
		itmevo.setDef4(bodyvo.getFinbilltype()); //财务票据类型
		itmevo.setDef5(bodyvo.getOriginalrate()); //原税率
		itmevo.setDef6(bodyvo.getOriginaltax()); //原税额
		itmevo.setDef7(bodyvo.getOriginalfreetax()); //原不含税金额
		itmevo.setDef8(bodyvo.getOriginalsumtax()); //原价税合计

	}
	@SuppressWarnings("rawtypes")
	public AggInvoiceBillVO getInvoiceBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, false);
		if (coll.size() > 0) {
			return (AggInvoiceBillVO) coll.toArray()[0];
		} else {
			return null;
		}
	}
	
	/**
	 * 检验表体必录信息
	 *  
	 * @param bodyvo
	 * @throws BusinessException
	 */
	protected void checkItemNotNull(IncomeBillBodyVO bodyvo)throws BusinessException {
		if (StringUtils.isBlank(bodyvo.getScomment())) {
			throw new BusinessException("摘要不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getSupplier())) {
			throw new BusinessException("供应商不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getQuantity_de())) {
			throw new BusinessException("数量不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getTaxprice())) {
			throw new BusinessException("发票含税单价不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getTaxrate())) {
			throw new BusinessException("发票税率不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getNotax_de())) {
			throw new BusinessException("发票不含税金额不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_tax_de())) {
			throw new BusinessException("发票税额不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getMoney_de())) {
			throw new BusinessException("发票价税合计不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getContractno())) {
			throw new BusinessException("供应合同编号不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getProject())) {
			throw new BusinessException("项目不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getProjectphase())) {
			throw new BusinessException("项目分期不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getFinbilltype())) {
			throw new BusinessException("财务票据类型不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getOriginalrate())) {
			throw new BusinessException("原税率不可为空");
		}
//		if (StringUtils.isBlank(bodyvo.getOriginaltax())) {
//			throw new BusinessException("原税额不可为空");
//		}
//		if (StringUtils.isBlank(bodyvo.getOriginalfreetax())) {
//			throw new BusinessException("原不含税金额不可为空");
//		}
		if (StringUtils.isBlank(bodyvo.getOriginalsumtax())) {
			throw new BusinessException("原价税合计不可为空");
		}

	}
	/**
	 * 检验表头必录信息
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(IncomeBillHeadVO headvo)
			throws BusinessException {
		if (StringUtils.isBlank(headvo.getOrg())) {
			throw new BusinessException("财务组织不可为空");
		}
		if (StringUtils.isBlank(headvo.getDept())) {
			throw new BusinessException("经办部门不可为空");
		}
		if (StringUtils.isBlank(headvo.getPsndoc())) {
			throw new BusinessException("经办人不可为空");
		}
		if (StringUtils.isBlank(headvo.getEbsbillno())) {
			throw new BusinessException("单据号不可为空");
		}
		if (StringUtils.isBlank(headvo.getBilldate())) {
			throw new BusinessException("单据日期不可为空");
		}
		if (StringUtils.isBlank(headvo.getBillstatus())) {
			throw new BusinessException("单据状态不可为空");
		}
		if (StringUtils.isBlank(headvo.getApprovestatus())) {
			throw new BusinessException("审批状态不可为空");
		}
		if (StringUtils.isBlank(headvo.getEffectstatus())) {
			throw new BusinessException("生效状态不可为空");
		}
		if (StringUtils.isBlank(headvo.getObjtype())) {
			throw new BusinessException("往来对象不可为空");
		}
		if (StringUtils.isBlank(headvo.getSupplier())) {
			throw new BusinessException("供应商不可为空");
		}
		if (StringUtils.isBlank(headvo.getChecktype())) {
			throw new BusinessException("票据类型（票种）不可为空");
		}
//		if (StringUtils.isBlank(headvo.getBusitypeflow())) {
//			throw new BusinessException("业务流程不可为空");
//		}
//		if (StringUtils.isBlank(headvo.getBusitype())) {
//			throw new BusinessException("业务类型不可为空");
//		}
		if (StringUtils.isBlank(headvo.getEbsid())) {
			throw new BusinessException("外系统主键不可为空");
		}
		if (StringUtils.isBlank(headvo.getEbsbillno())) {
			throw new BusinessException("外系统单据号不可为空");
		}
//		if (StringUtils.isBlank(headvo.getImgno())) {
//			throw new BusinessException("影像编码不可为空");
//		}
//		if (StringUtils.isBlank(headvo.getImgstate())) {
//			throw new BusinessException("影像状态不可为空");
//		}
		if (StringUtils.isBlank(headvo.getContcode())) {
			throw new BusinessException("合同编码不可为空");
		}
		if (StringUtils.isBlank(headvo.getContname())) {
			throw new BusinessException("合同名称不可为空");
		}
		if (StringUtils.isBlank(headvo.getConttype())) {
			throw new BusinessException("合同类型不可为空");
		}
		if (StringUtils.isBlank(headvo.getContcell())) {
			throw new BusinessException("合同细类不可为空");
		}
		if (StringUtils.isBlank(headvo.getEmergency())) {
			throw new BusinessException("紧急程度不可为空");
		}
		if (StringUtils.isBlank(headvo.getEbsname())) {
			throw new BusinessException("外系统名称不可为空");
		}
		if (StringUtils.isBlank(headvo.getTotalmny_paybythisreq())) {
			throw new BusinessException("发票总金额（价税合计）不可为空");
		}

	}
}
