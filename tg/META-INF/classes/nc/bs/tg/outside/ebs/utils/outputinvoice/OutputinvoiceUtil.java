package nc.bs.tg.outside.ebs.utils.outputinvoice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.itf.hzvat.IOutputtaxMaintain;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.hzvat.billmaintenance.BillmaintenanceVO;
import nc.vo.hzvat.outputtax.AggOutputTaxHVO;
import nc.vo.hzvat.outputtax.OutputTaxBVO;
import nc.vo.hzvat.outputtax.OutputTaxHVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outputtax.NCOutputtaxDetailsJsonVO;
import nc.vo.tg.outputtax.NcOutputtaxJsonVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 销项发票外系统调用工具类入口
 * 
 * @since 2019-09-04
 * @author lhh
 * 
 */
public class OutputinvoiceUtil extends SaleBillUtils {
	static OutputinvoiceUtil utils;
	Boolean extraBill = false;//是否补票

	public static OutputinvoiceUtil getUtils() {
		if (utils == null) {
			utils = new OutputinvoiceUtil();
		}
		return utils;
	}

	/**
	 * 销项发票外系统接口操作入口
	 * 
	 * @param value
	 * @param dectype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srctype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);
		AggOutputTaxHVO[] aggvos = null;
		extraBill = false;
		String jsonstr = JSONObject.toJSONString(value.get("data"));
		NcOutputtaxJsonVO aggoJsonvo = JSONObject.parseObject(jsonstr,
				NcOutputtaxJsonVO.class);
		// 操作类型
		int actiontype = Integer.valueOf((String) value.get("actiontype"));
		Map valueMap = new HashMap<>();
		try {
			aggvos = getAggOutputTaxVo(jsonstr, actiontype);
			IOutputtaxMaintain service = NCLocator.getInstance().lookup(
					IOutputtaxMaintain.class);
			if(aggvos!=null&&existaggVO!=null&&!(extraBill)){//nc中已存发票单据，查询直接返回信息
				existaggVO.getParentVO().setSellMachineNo(aggoJsonvo.getSerialNo());
				String invouceMsg = service.invoiceOpenForOutSys(new AggOutputTaxHVO[]{existaggVO},
						5, 0);
				valueMap.put("billno", existaggVO.getParentVO().getVbillno());
				valueMap.put("billid", existaggVO.getParentVO().getPrimaryKey());
				if(invouceMsg!=null){
				JSONObject jobj=JSON.parseObject(invouceMsg);
				if("200".equals(jobj.getString("code"))){
				JSONArray jarry=jobj.getJSONArray("objects");
				if(jarry!=null){
					valueMap.put("body",JSONObject.toJSONString(jarry.get(0)));
				}
				}
				}
//				valueMap.put("body", invouceMsg);
				return JSONObject.toJSON(valueMap).toString();
			}
			
			// //保存销项发票
			// aggvos =service.insert(aggvos, null);
			// 推送乐税
			
			if(!(aggoJsonvo.getIsExtraBill() == null ? false : aggoJsonvo.getIsExtraBill())){
				aggvos[0].getParentVO().setSellMachineNo(aggoJsonvo.getSerialNo());
				String invouceMsg = service.invoiceOpenForOutSys(aggvos,
						actiontype, 0);
				valueMap.put("billno", aggvos[0].getParentVO().getVbillno());
				valueMap.put("billid", aggvos[0].getParentVO().getPrimaryKey());
				valueMap.put("body", invouceMsg);
			}else{
				valueMap.put("billno", aggvos[0].getParentVO().getVbillno());
				valueMap.put("billid", aggvos[0].getParentVO().getPrimaryKey());
			}
			return JSONObject.toJSON(valueMap).toString();
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		} finally {
			EBSBillUtils.removeBillQueue("billqueue");
		}

	}

	public void checkrule(NcOutputtaxJsonVO vo,int actiontype) throws BusinessException{
		if(actiontype==1||actiontype==2){
		if(vo.getSerialNo()==null||vo.getSerialNo().length()<1){
			throw new BusinessException("传入分机号不能为空");
		}
		}
	}
	AggOutputTaxHVO aggvo = new AggOutputTaxHVO();
	AggOutputTaxHVO existaggVO = null;//nc中已存发票单据直接返回
	/**
	 * 外系统调用工具，单据保存
	 * 
	 * @param json
	 * @param actiontype
	 *            1：红冲，,2：开票,3：作废 ，4：打印，5：查询，6：税盆状态查询，7：发票库存查询
	 * @return
	 * @throws BusinessException
	 */
	public AggOutputTaxHVO[] getAggOutputTaxVo(String json, int actiontype)
			throws BusinessException {
		IOutputtaxMaintain service = NCLocator.getInstance().lookup(
				IOutputtaxMaintain.class);
		IMDPersistenceQueryService bs = NCLocator.getInstance().lookup(
				IMDPersistenceQueryService.class);
		NcOutputtaxJsonVO aggoJsonvo = JSONObject.parseObject(json,
				NcOutputtaxJsonVO.class);
		AggOutputTaxHVO[] aggvos = null;
		OutputTaxHVO headvo = null;
		OutputTaxBVO[] detailvos = null;
		if(aggoJsonvo.getSaveSysBillid() != null){
			checkrule( aggoJsonvo, actiontype);
			AggOutputTaxHVO aggVO = (AggOutputTaxHVO) getBillVO(
					AggOutputTaxHVO.class, "isnull(dr,0)=0 and def14 = '" + aggoJsonvo.getSaveSysBillid()
					+ "'");
			if(aggoJsonvo.getIsExtraBill() == null ? false : aggoJsonvo.getIsExtraBill()){//是否补票
				extraBill = true;
			}
			if (actiontype == 2 && aggVO != null ) {
				if(!(extraBill)){
				existaggVO=aggVO;
				}else{
					existaggVO=null;
				}
					return new AggOutputTaxHVO[]{aggVO};	
//				throw new BusinessException("【销项发票("+aggoJsonvo.getSaveSysBillid()+")】,NC已存在对应的业务单据【"
//						+ aggVO.getParentVO().getVbillno()+ "】,请勿重复上传!");
			}else{
				existaggVO=null;
			}
		}
		
		if (actiontype == 2 || extraBill) {
			headvo = buildNcheadVO(aggoJsonvo);
			detailvos = buildNcDetails(aggoJsonvo.getDetails());
			aggvo.setParentVO(headvo);
			aggvo.setChildrenVO(detailvos);
			//离线开票后补票
			if(extraBill){
				aggvo.getParentVO().setFpdm(aggoJsonvo.getInvCode());
				aggvo.getParentVO().setFph(aggoJsonvo.getInvNo());
				aggvo.getParentVO().setKprq(new UFDate(aggoJsonvo.getInvDate()));
			}
			aggvos = service.insert(new AggOutputTaxHVO[] { aggvo }, null);
		} else if (actiontype == 1 || actiontype == 3 || actiontype == 4
				|| actiontype == 5 || actiontype == 6 || actiontype == 7) {
			if (aggoJsonvo.getBillGuid() == null
					|| "".equals(aggoJsonvo.getBillGuid())) {
				throw new BusinessException("单据操作失败，NC单据主键必填！");
			}
			Collection<AggOutputTaxHVO> col = bs.queryBillOfVOByPKs(
					AggOutputTaxHVO.class,
					new String[] { aggoJsonvo.getBillGuid() }, false);
			if (col.isEmpty()) {
				throw new BusinessException("单据操作失败，请填写正确的NC单据主键！");
			}
			for (AggOutputTaxHVO agg : col) {
				aggvo = agg;
			}
			if (actiontype == 1) {
				AggOutputTaxHVO newAggvo = (AggOutputTaxHVO) aggvo.clone();
				newAggvo.getParentVO().setIsred(UFBoolean.TRUE);
				newAggvo.getParentVO().setIsredby(UFBoolean.TRUE);
				newAggvo.getParentVO().setSrc_taxbillcode(
						aggvo.getParentVO().getFpdm());
				newAggvo.getParentVO().setSrc_taxbillno(
						aggvo.getParentVO().getFph());
				newAggvo.getParentVO().setDbilldate(
						new UFDate());
				newAggvo.getParentVO().setTzdh(aggoJsonvo.getNegNativeNo());// 红字通知单号
				newAggvo.getParentVO().setMoney_fp(
						aggvo.getParentVO().getMoney_fp().multiply(-1));
				newAggvo.getParentVO().setMoney_fpout(
						aggvo.getParentVO().getMoney_fpout().multiply(-1));
				newAggvo.getParentVO().setDef18("-"+aggvo.getParentVO().getDef18());//合计税额
				newAggvo.getParentVO().setDef14(aggoJsonvo.getSaveSysBillid());//销售系统ID
				
				newAggvo.getParentVO().setFpdm(null);
				newAggvo.getParentVO().setFph(null);
				newAggvo.getParentVO().setPk_outputtax(null);
				newAggvo.getParentVO().setVbillno(null);
				OutputTaxBVO[] bvos = (OutputTaxBVO[]) newAggvo.getChildrenVO();
				for (OutputTaxBVO bvo : bvos) {
					bvo.setPk_outputtax(null);
					bvo.setPk_outputtax_b(null);
					bvo.setIsred(UFBoolean.TRUE);
					bvo.setNum(bvo.getNum().multiply(-1));
					bvo.setMoneyouttax(bvo.getMoneyouttax().multiply(-1));// 不含税金额
					bvo.setMoneytax(bvo.getMoneytax().multiply(-1));// 税额
					bvo.setMoneyintax(bvo.getMoneyintax().multiply(-1));// 价税合计
				}
				aggvos = service.insert(new AggOutputTaxHVO[] { newAggvo },
						null);
			} else if (actiontype == 3 || actiontype == 4 || actiontype == 5
					|| actiontype == 6 || actiontype == 7) {
				aggvos = col.toArray(new AggOutputTaxHVO[0]);
			}
		}
		return aggvos;
	}

	/**
	 * 外系统工具类
	 * 
	 * @since 2019-09-23
	 * @param optjsonvo
	 * @version NC6.5
	 * @return
	 * @throws BusinessException
	 */
	private OutputTaxHVO buildNcheadVO(NcOutputtaxJsonVO optjsonvo)
			throws BusinessException {
		OutputTaxHVO headvo = new OutputTaxHVO();
		OrgVO orgvo = (OrgVO) super.getBaseDAO().executeQuery(
				"select * from org_orgs where code ='" + optjsonvo.getPk_org()
						+ "'", new BeanProcessor(OrgVO.class));
		headvo.setPk_org(orgvo.getPk_org());
		headvo.setPk_org_v(orgvo.getPk_vid());
		headvo.setPk_group(orgvo.getPk_group());
		// 销方信息
		headvo.setKpzz(optjsonvo.getSellName());// 销方名称
		// 税盆状态查询
		headvo.setFplx(getPk_invoicetype(optjsonvo.getInvType()));// 0：增值税专用发票（当发票类型为0时，不允许开具零税率发票）
		// 2：增值税普通发票 51：增值税电子普通票
		// optjsonvo.setBillType(headvo.getPk_billtype());//发票类型
		// optjsonvo.setSellBankAccount(headvo.getDef6()+headvo.getDef7());//购方银行账号
		// optjsonvo.setSellAddrTel(headvo.getDef8()+headvo.getDef9());//购方地址电话
		// 购方信息
		headvo.setPk_kh(optjsonvo.getBuyName());// 客户
		headvo.setKhh(optjsonvo.getBuyBank());// 开户行
		headvo.setYhzh(optjsonvo.getBuyBank());// 银行账号
		headvo.setAdress(optjsonvo.getBuyAddr());// 地址
		headvo.setTelphone(optjsonvo.getBuyTell());// 购方电话
		headvo.setNsrsbh(optjsonvo.getBuyTaxNo());// 购方纳税人识别号（业主身份证，企业统一信用代码）
		
		// 销方信息
		BillmaintenanceVO bmVO = getSellerMsgVO(optjsonvo.getSellName(),optjsonvo.getSerialNo());
		if(bmVO == null&&!extraBill){
			throw new BusinessException("数据异常，请在NC维护该组织("+optjsonvo.getSellName()+")开票相关信息！");
		}
		if(bmVO!=null){
		headvo.setKpzz(bmVO.getName());// 销方名称 --传相应编码，根据编码在NC中获取
		headvo.setDef3(bmVO.getTaxid());// 销方纳税人识别号
		headvo.setDef6(bmVO.getKhh());// 销方开户行
		headvo.setDef7(bmVO.getYhzh());// 销方银行账号
		headvo.setDef8(bmVO.getAddress());// 销方账号
		headvo.setDef9(bmVO.getTelephone());// 销方电话号码
		}
		headvo.setPk_jbr(optjsonvo.getPk_psndoc());// 经办人
		headvo.setPk_jbbm(optjsonvo.getPk_dept());// 经办部门
		headvo.setDbilldate(new UFDate(optjsonvo.getBizDate()));// 业务日期
		headvo.setRmark(optjsonvo.getNote() == null ? "" : optjsonvo.getNote());// 备注
		headvo.setSkr(optjsonvo.getCashier());// 收款人
		headvo.setFkr(optjsonvo.getChecker());// 复核人
		headvo.setAttributeValue("def12", optjsonvo.getPrintType() + "");// 打印类型
		headvo.setAttributeValue("def13", optjsonvo.getConfirmWin() + "");// 是否显示打印对话框
		// 外系统信息
		headvo.setAttributeValue("def14", optjsonvo.getSaveSysBillid());// 外系统id
		headvo.setAttributeValue("def15", optjsonvo.getSaveSysBillno());// 外系统单号
		headvo.setAttributeValue("def16", optjsonvo.getSysname());// 外系统名称
		headvo.setDef17(optjsonvo.getInvoicer());// 开票人
		headvo.setAttributeValue("src_taxbillcode",
				optjsonvo.getSourceInvCode());
		headvo.setAttributeValue("src_taxbillno", optjsonvo.getSourceInvNo());
		
		headvo.setPk_billtype("HZ08");
		headvo.setVbillstatus(-1);//单据状态
		
		
		

		// optjsonvo.setGoodsVersion("");//税收分类编码33.0
		// optjsonvo.setSourceInvCode(headvo.getSrc_taxbillcode()==null?"":headvo.getSrc_taxbillcode());//发票代码
		// optjsonvo.setSourceInvNo(headvo.getSrc_taxbillno()==null
		// ?"":headvo.getSrc_taxbillno());//发票号
		headvo.setMoney_fp(new UFDouble(optjsonvo.getTotalAmount()));// 含税金额合计
		headvo.setMoney_fpout(new UFDouble(optjsonvo.getTotalAmountWithoutTax()));// 不含税金额合计
		headvo.setDef18(String.valueOf(optjsonvo.getTotalTaxAmount()));//合计税额
		headvo.setDef19(optjsonvo.getDef19());//影像编码
		headvo.setDef20(optjsonvo.getDef20());//影像状态
		// optjsonvo.setTotalTaxAmount(new
		// UFDouble(optjsonvo.getTotalAmount()-optjsonvo.getTotalAmountWithoutTax()).setScale(2,
		// BigDecimal.ROUND_HALF_UP).doubleValue());//合计税额（不传时，按明细税额累加）
		return headvo;

	}

	/**
	 * 外系统调用 销项发票表体明细信息构建
	 * 
	 * @param details
	 * @return
	 * @throws BusinessException 
	 */
	private OutputTaxBVO[] buildNcDetails(NCOutputtaxDetailsJsonVO[] details) throws BusinessException {

		List<OutputTaxBVO> taxBVOs = new ArrayList<>();
		if (details != null && details.length > 0) {

			for (NCOutputtaxDetailsJsonVO bvo : details) {
				OutputTaxBVO detailvo = new OutputTaxBVO();
				detailvo.setPk_product(bvo.getGoodsName());// 货物
				detailvo.setPk_dw(bvo.getUnit());// 单位
				detailvo.setNum(new UFDouble(bvo.getNumber()));// 数量
				detailvo.setPriceintax(new UFDouble(bvo.getTaxPrice()));// 含税单价
				detailvo.setPriceouttax(new UFDouble(bvo.getWithoutTaxPrice()));// 不含税单价
				detailvo.setMoneyintax(new UFDouble(bvo.getAmountWithTax()));// 含税金额（保留两位小数）
				detailvo.setMoneyouttax(new UFDouble(bvo.getAmountWithoutTax()));// 不含税金额（保留两位小数）
				detailvo.setTaxrate(new UFDouble(bvo.getRate()));// 税率
				detailvo.setPk_vatinfo(getPk_vatInfoByCode(bvo.getTaxTypeCode()));// 税收分类编码
				detailvo.setMoneytax(new UFDouble(bvo.getTaxAmount()));// 税额
				detailvo.setDef1(String.valueOf(bvo.getDiscountFlag()));//是否享受优惠
				detailvo.setDef2(bvo.getDiscountNote());//优惠政策说明
				detailvo.setDef3(bvo.getDiscountType());//优惠政策类型
				detailvo.setDef4(bvo.getTaxZeroRate());//零税率标志
				detailvo.setDef5(bvo.getStandard());//规格型号
				taxBVOs.add(detailvo);
			}

		}
		return taxBVOs.toArray(new OutputTaxBVO[0]);

	}

}
