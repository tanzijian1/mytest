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
 * ���Ʊ��ϵͳ���ù��������
 * 
 * @since 2019-09-04
 * @author lhh
 * 
 */
public class OutputinvoiceUtil extends SaleBillUtils {
	static OutputinvoiceUtil utils;
	Boolean extraBill = false;//�Ƿ�Ʊ

	public static OutputinvoiceUtil getUtils() {
		if (utils == null) {
			utils = new OutputinvoiceUtil();
		}
		return utils;
	}

	/**
	 * ���Ʊ��ϵͳ�ӿڲ������
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
		// ��������
		int actiontype = Integer.valueOf((String) value.get("actiontype"));
		Map valueMap = new HashMap<>();
		try {
			aggvos = getAggOutputTaxVo(jsonstr, actiontype);
			IOutputtaxMaintain service = NCLocator.getInstance().lookup(
					IOutputtaxMaintain.class);
			if(aggvos!=null&&existaggVO!=null&&!(extraBill)){//nc���Ѵ淢Ʊ���ݣ���ѯֱ�ӷ�����Ϣ
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
			
			// //�������Ʊ
			// aggvos =service.insert(aggvos, null);
			// ������˰
			
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
			throw new BusinessException("����ֻ��Ų���Ϊ��");
		}
		}
	}
	AggOutputTaxHVO aggvo = new AggOutputTaxHVO();
	AggOutputTaxHVO existaggVO = null;//nc���Ѵ淢Ʊ����ֱ�ӷ���
	/**
	 * ��ϵͳ���ù��ߣ����ݱ���
	 * 
	 * @param json
	 * @param actiontype
	 *            1����壬,2����Ʊ,3������ ��4����ӡ��5����ѯ��6��˰��״̬��ѯ��7����Ʊ����ѯ
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
			if(aggoJsonvo.getIsExtraBill() == null ? false : aggoJsonvo.getIsExtraBill()){//�Ƿ�Ʊ
				extraBill = true;
			}
			if (actiontype == 2 && aggVO != null ) {
				if(!(extraBill)){
				existaggVO=aggVO;
				}else{
					existaggVO=null;
				}
					return new AggOutputTaxHVO[]{aggVO};	
//				throw new BusinessException("�����Ʊ("+aggoJsonvo.getSaveSysBillid()+")��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
//						+ aggVO.getParentVO().getVbillno()+ "��,�����ظ��ϴ�!");
			}else{
				existaggVO=null;
			}
		}
		
		if (actiontype == 2 || extraBill) {
			headvo = buildNcheadVO(aggoJsonvo);
			detailvos = buildNcDetails(aggoJsonvo.getDetails());
			aggvo.setParentVO(headvo);
			aggvo.setChildrenVO(detailvos);
			//���߿�Ʊ��Ʊ
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
				throw new BusinessException("���ݲ���ʧ�ܣ�NC�����������");
			}
			Collection<AggOutputTaxHVO> col = bs.queryBillOfVOByPKs(
					AggOutputTaxHVO.class,
					new String[] { aggoJsonvo.getBillGuid() }, false);
			if (col.isEmpty()) {
				throw new BusinessException("���ݲ���ʧ�ܣ�����д��ȷ��NC����������");
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
				newAggvo.getParentVO().setTzdh(aggoJsonvo.getNegNativeNo());// ����֪ͨ����
				newAggvo.getParentVO().setMoney_fp(
						aggvo.getParentVO().getMoney_fp().multiply(-1));
				newAggvo.getParentVO().setMoney_fpout(
						aggvo.getParentVO().getMoney_fpout().multiply(-1));
				newAggvo.getParentVO().setDef18("-"+aggvo.getParentVO().getDef18());//�ϼ�˰��
				newAggvo.getParentVO().setDef14(aggoJsonvo.getSaveSysBillid());//����ϵͳID
				
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
					bvo.setMoneyouttax(bvo.getMoneyouttax().multiply(-1));// ����˰���
					bvo.setMoneytax(bvo.getMoneytax().multiply(-1));// ˰��
					bvo.setMoneyintax(bvo.getMoneyintax().multiply(-1));// ��˰�ϼ�
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
	 * ��ϵͳ������
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
		// ������Ϣ
		headvo.setKpzz(optjsonvo.getSellName());// ��������
		// ˰��״̬��ѯ
		headvo.setFplx(getPk_invoicetype(optjsonvo.getInvType()));// 0����ֵ˰ר�÷�Ʊ������Ʊ����Ϊ0ʱ������������˰�ʷ�Ʊ��
		// 2����ֵ˰��ͨ��Ʊ 51����ֵ˰������ͨƱ
		// optjsonvo.setBillType(headvo.getPk_billtype());//��Ʊ����
		// optjsonvo.setSellBankAccount(headvo.getDef6()+headvo.getDef7());//���������˺�
		// optjsonvo.setSellAddrTel(headvo.getDef8()+headvo.getDef9());//������ַ�绰
		// ������Ϣ
		headvo.setPk_kh(optjsonvo.getBuyName());// �ͻ�
		headvo.setKhh(optjsonvo.getBuyBank());// ������
		headvo.setYhzh(optjsonvo.getBuyBank());// �����˺�
		headvo.setAdress(optjsonvo.getBuyAddr());// ��ַ
		headvo.setTelphone(optjsonvo.getBuyTell());// �����绰
		headvo.setNsrsbh(optjsonvo.getBuyTaxNo());// ������˰��ʶ��ţ�ҵ�����֤����ҵͳһ���ô��룩
		
		// ������Ϣ
		BillmaintenanceVO bmVO = getSellerMsgVO(optjsonvo.getSellName(),optjsonvo.getSerialNo());
		if(bmVO == null&&!extraBill){
			throw new BusinessException("�����쳣������NCά������֯("+optjsonvo.getSellName()+")��Ʊ�����Ϣ��");
		}
		if(bmVO!=null){
		headvo.setKpzz(bmVO.getName());// �������� --����Ӧ���룬���ݱ�����NC�л�ȡ
		headvo.setDef3(bmVO.getTaxid());// ������˰��ʶ���
		headvo.setDef6(bmVO.getKhh());// ����������
		headvo.setDef7(bmVO.getYhzh());// ���������˺�
		headvo.setDef8(bmVO.getAddress());// �����˺�
		headvo.setDef9(bmVO.getTelephone());// �����绰����
		}
		headvo.setPk_jbr(optjsonvo.getPk_psndoc());// ������
		headvo.setPk_jbbm(optjsonvo.getPk_dept());// ���첿��
		headvo.setDbilldate(new UFDate(optjsonvo.getBizDate()));// ҵ������
		headvo.setRmark(optjsonvo.getNote() == null ? "" : optjsonvo.getNote());// ��ע
		headvo.setSkr(optjsonvo.getCashier());// �տ���
		headvo.setFkr(optjsonvo.getChecker());// ������
		headvo.setAttributeValue("def12", optjsonvo.getPrintType() + "");// ��ӡ����
		headvo.setAttributeValue("def13", optjsonvo.getConfirmWin() + "");// �Ƿ���ʾ��ӡ�Ի���
		// ��ϵͳ��Ϣ
		headvo.setAttributeValue("def14", optjsonvo.getSaveSysBillid());// ��ϵͳid
		headvo.setAttributeValue("def15", optjsonvo.getSaveSysBillno());// ��ϵͳ����
		headvo.setAttributeValue("def16", optjsonvo.getSysname());// ��ϵͳ����
		headvo.setDef17(optjsonvo.getInvoicer());// ��Ʊ��
		headvo.setAttributeValue("src_taxbillcode",
				optjsonvo.getSourceInvCode());
		headvo.setAttributeValue("src_taxbillno", optjsonvo.getSourceInvNo());
		
		headvo.setPk_billtype("HZ08");
		headvo.setVbillstatus(-1);//����״̬
		
		
		

		// optjsonvo.setGoodsVersion("");//˰�շ������33.0
		// optjsonvo.setSourceInvCode(headvo.getSrc_taxbillcode()==null?"":headvo.getSrc_taxbillcode());//��Ʊ����
		// optjsonvo.setSourceInvNo(headvo.getSrc_taxbillno()==null
		// ?"":headvo.getSrc_taxbillno());//��Ʊ��
		headvo.setMoney_fp(new UFDouble(optjsonvo.getTotalAmount()));// ��˰���ϼ�
		headvo.setMoney_fpout(new UFDouble(optjsonvo.getTotalAmountWithoutTax()));// ����˰���ϼ�
		headvo.setDef18(String.valueOf(optjsonvo.getTotalTaxAmount()));//�ϼ�˰��
		headvo.setDef19(optjsonvo.getDef19());//Ӱ�����
		headvo.setDef20(optjsonvo.getDef20());//Ӱ��״̬
		// optjsonvo.setTotalTaxAmount(new
		// UFDouble(optjsonvo.getTotalAmount()-optjsonvo.getTotalAmountWithoutTax()).setScale(2,
		// BigDecimal.ROUND_HALF_UP).doubleValue());//�ϼ�˰�����ʱ������ϸ˰���ۼӣ�
		return headvo;

	}

	/**
	 * ��ϵͳ���� ���Ʊ������ϸ��Ϣ����
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
				detailvo.setPk_product(bvo.getGoodsName());// ����
				detailvo.setPk_dw(bvo.getUnit());// ��λ
				detailvo.setNum(new UFDouble(bvo.getNumber()));// ����
				detailvo.setPriceintax(new UFDouble(bvo.getTaxPrice()));// ��˰����
				detailvo.setPriceouttax(new UFDouble(bvo.getWithoutTaxPrice()));// ����˰����
				detailvo.setMoneyintax(new UFDouble(bvo.getAmountWithTax()));// ��˰��������λС����
				detailvo.setMoneyouttax(new UFDouble(bvo.getAmountWithoutTax()));// ����˰��������λС����
				detailvo.setTaxrate(new UFDouble(bvo.getRate()));// ˰��
				detailvo.setPk_vatinfo(getPk_vatInfoByCode(bvo.getTaxTypeCode()));// ˰�շ������
				detailvo.setMoneytax(new UFDouble(bvo.getTaxAmount()));// ˰��
				detailvo.setDef1(String.valueOf(bvo.getDiscountFlag()));//�Ƿ������Ż�
				detailvo.setDef2(bvo.getDiscountNote());//�Ż�����˵��
				detailvo.setDef3(bvo.getDiscountType());//�Ż���������
				detailvo.setDef4(bvo.getTaxZeroRate());//��˰�ʱ�־
				detailvo.setDef5(bvo.getStandard());//����ͺ�
				taxBVOs.add(detailvo);
			}

		}
		return taxBVOs.toArray(new OutputTaxBVO[0]);

	}

}
