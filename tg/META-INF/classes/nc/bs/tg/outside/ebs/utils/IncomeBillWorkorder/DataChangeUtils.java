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
	 * ������Ϣ
	 * 
	 * @param hvo
	 * @param headvo
	 */
	protected void setHeaderVO(InvoiceBillVO hvo,
			IncomeBillHeadVO headvo) throws BusinessException {
		checkHeaderNotNull(headvo);
		hvo.setPk_org(getPk_orgByCode(headvo.getOrg()));// Ӧ��������֯->NCҵ��Ԫ����
		//hvo.setPk_org(headvo.getOrg()); //������֯
		hvo.setPk_deptid_v(headvo.getDept()); //���첿��
		hvo.setPk_psndoc(headvo.getPsndoc()); //������
		hvo.setBillno(headvo.getEbsbillno()); //���ݺ�
		hvo.setBilldate(new UFDate( headvo.getBilldate())); //��������
		hvo.setBillstatus(new Integer(headvo.getBillstatus())); //����״̬
		hvo.setApprovestatus(new Integer(headvo.getApprovestatus())); //����״̬
		hvo.setEffectstatus(new Integer (headvo.getEffectstatus())); //��Ч״̬
		hvo.setObjtype(headvo.getObjtype()); //��������
		hvo.setSupplier(headvo.getSupplier()); //��Ӧ��
		hvo.setChecktype(headvo.getChecktype()); //Ʊ�����ͣ�Ʊ�֣�
		hvo.setPk_busitype(headvo.getBusitypeflow()); //ҵ������
		hvo.setBusitype(headvo.getBusitype()); //ҵ������
		hvo.setDef1(headvo.getEbsid()); //��ϵͳ����
		hvo.setDef2(headvo.getEbsbillno()); //��ϵͳ���ݺ�
		hvo.setDef3(headvo.getImgno()); //Ӱ�����
		hvo.setDef4(headvo.getImgstate()); //Ӱ��״̬
		hvo.setDef5(headvo.getContcode()); //��ͬ����
		hvo.setDef6(headvo.getContname()); //��ͬ����
		hvo.setDef7(headvo.getConttype()); //��ͬ����
		hvo.setDef9(headvo.getEmergency()); //�����̶�
		hvo.setDef10(headvo.getEbsname()); //��ϵͳ����
		hvo.setDef11(headvo.getTotalmny_paybythisreq()); //��Ʊ�ܽ���˰�ϼƣ�

	}

	/**
	 * ��ϸ��Ϣ
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
		itmevo.setScomment(bodyvo.getScomment()); //ժҪ
		itmevo.setSupplier(bodyvo.getSupplier()); //��Ӧ��
		itmevo.setMoney_de(new UFDouble(bodyvo.getMoney_de())); //����
		itmevo.setTaxprice(new UFDouble(bodyvo.getTaxprice())); //��Ʊ��˰����
		itmevo.setTaxrate(new UFDouble(bodyvo.getTaxrate())); //��Ʊ˰��
		itmevo.setNotax_de(new UFDouble(bodyvo.getNotax_de())); //��Ʊ����˰���
		itmevo.setLocal_tax_de(new UFDouble(bodyvo.getLocal_tax_de())); //��Ʊ˰��
		itmevo.setMoney_de(new UFDouble(bodyvo.getMoney_de())); //��Ʊ��˰�ϼ�
		itmevo.setDef1(bodyvo.getContractno()); //��Ӧ��ͬ���
		itmevo.setDef2(bodyvo.getProject()); //��Ŀ
		itmevo.setDef3(bodyvo.getProjectphase()); //��Ŀ����
		itmevo.setDef4(bodyvo.getFinbilltype()); //����Ʊ������
		itmevo.setDef5(bodyvo.getOriginalrate()); //ԭ˰��
		itmevo.setDef6(bodyvo.getOriginaltax()); //ԭ˰��
		itmevo.setDef7(bodyvo.getOriginalfreetax()); //ԭ����˰���
		itmevo.setDef8(bodyvo.getOriginalsumtax()); //ԭ��˰�ϼ�

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
	 * ��������¼��Ϣ
	 *  
	 * @param bodyvo
	 * @throws BusinessException
	 */
	protected void checkItemNotNull(IncomeBillBodyVO bodyvo)throws BusinessException {
		if (StringUtils.isBlank(bodyvo.getScomment())) {
			throw new BusinessException("ժҪ����Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getSupplier())) {
			throw new BusinessException("��Ӧ�̲���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getQuantity_de())) {
			throw new BusinessException("��������Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getTaxprice())) {
			throw new BusinessException("��Ʊ��˰���۲���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getTaxrate())) {
			throw new BusinessException("��Ʊ˰�ʲ���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getNotax_de())) {
			throw new BusinessException("��Ʊ����˰����Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_tax_de())) {
			throw new BusinessException("��Ʊ˰���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getMoney_de())) {
			throw new BusinessException("��Ʊ��˰�ϼƲ���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getContractno())) {
			throw new BusinessException("��Ӧ��ͬ��Ų���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getProject())) {
			throw new BusinessException("��Ŀ����Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getProjectphase())) {
			throw new BusinessException("��Ŀ���ڲ���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getFinbilltype())) {
			throw new BusinessException("����Ʊ�����Ͳ���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getOriginalrate())) {
			throw new BusinessException("ԭ˰�ʲ���Ϊ��");
		}
//		if (StringUtils.isBlank(bodyvo.getOriginaltax())) {
//			throw new BusinessException("ԭ˰���Ϊ��");
//		}
//		if (StringUtils.isBlank(bodyvo.getOriginalfreetax())) {
//			throw new BusinessException("ԭ����˰����Ϊ��");
//		}
		if (StringUtils.isBlank(bodyvo.getOriginalsumtax())) {
			throw new BusinessException("ԭ��˰�ϼƲ���Ϊ��");
		}

	}
	/**
	 * �����ͷ��¼��Ϣ
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(IncomeBillHeadVO headvo)
			throws BusinessException {
		if (StringUtils.isBlank(headvo.getOrg())) {
			throw new BusinessException("������֯����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getDept())) {
			throw new BusinessException("���첿�Ų���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getPsndoc())) {
			throw new BusinessException("�����˲���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getEbsbillno())) {
			throw new BusinessException("���ݺŲ���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getBilldate())) {
			throw new BusinessException("�������ڲ���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getBillstatus())) {
			throw new BusinessException("����״̬����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getApprovestatus())) {
			throw new BusinessException("����״̬����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getEffectstatus())) {
			throw new BusinessException("��Ч״̬����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getObjtype())) {
			throw new BusinessException("�������󲻿�Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getSupplier())) {
			throw new BusinessException("��Ӧ�̲���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getChecktype())) {
			throw new BusinessException("Ʊ�����ͣ�Ʊ�֣�����Ϊ��");
		}
//		if (StringUtils.isBlank(headvo.getBusitypeflow())) {
//			throw new BusinessException("ҵ�����̲���Ϊ��");
//		}
//		if (StringUtils.isBlank(headvo.getBusitype())) {
//			throw new BusinessException("ҵ�����Ͳ���Ϊ��");
//		}
		if (StringUtils.isBlank(headvo.getEbsid())) {
			throw new BusinessException("��ϵͳ��������Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getEbsbillno())) {
			throw new BusinessException("��ϵͳ���ݺŲ���Ϊ��");
		}
//		if (StringUtils.isBlank(headvo.getImgno())) {
//			throw new BusinessException("Ӱ����벻��Ϊ��");
//		}
//		if (StringUtils.isBlank(headvo.getImgstate())) {
//			throw new BusinessException("Ӱ��״̬����Ϊ��");
//		}
		if (StringUtils.isBlank(headvo.getContcode())) {
			throw new BusinessException("��ͬ���벻��Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getContname())) {
			throw new BusinessException("��ͬ���Ʋ���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getConttype())) {
			throw new BusinessException("��ͬ���Ͳ���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getContcell())) {
			throw new BusinessException("��ͬϸ�಻��Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getEmergency())) {
			throw new BusinessException("�����̶Ȳ���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getEbsname())) {
			throw new BusinessException("��ϵͳ���Ʋ���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getTotalmny_paybythisreq())) {
			throw new BusinessException("��Ʊ�ܽ���˰�ϼƣ�����Ϊ��");
		}

	}
}
