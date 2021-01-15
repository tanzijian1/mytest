package nc.bs.tg.pub.voucher;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.pubitf.bbd.CurrtypeQuery;
import nc.pubitf.fip.service.IFipMessageService;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.cmp.pub.CmpPublicUtil;
import nc.vo.fip.service.FipMessageVO;
import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tgfp.pub.common.FPPublicUtil;

public  class SendVoucher {
	private String billType = null;

	private String billcode;// ���ݺ�

	private UFDate effectdate;// ҵ������

	private String pk_group;// ��������

	private String billtype;// �������ͱ���

	private String pk_org;// ҵ����֯

	private String note;// ��ע

	private UFDouble mny;// ���

	private String pk_currency;// ����

	private String id;// ����

	private AggregatedValueObject aggVO = null;// �ۺ�VO

	/**
	 * 
	 * ƾ֤����
	 * 
	 * @param aggvo
	 * @param messagetype
	 * @throws BusinessException
	 */
	public void sendDAPAddMessge(FipMessageVO messageVO)
			throws BusinessException {
		messageVO.setMessagetype(FipMessageVO.MESSAGETYPE_ADD);
		getFipMessageService().sendMessage(messageVO);

	}

	/**
	 * ƾ֤ɾ��
	 * 
	 * @param messageVO
	 * @throws BusinessException
	 */
	public void sendDAPDelMessge(FipMessageVO messageVO)
			throws BusinessException {
		messageVO.setMessagetype(FipMessageVO.MESSAGETYPE_DEL);
		getFipMessageService().sendMessage(messageVO);

	}

	/**
	 * ҵ�񵥾�����ƾ֤��ת��
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public FipMessageVO getMessageVO() throws BusinessException {
		FPPublicUtil.lockBillNO(new String[] { getBillcode() });
		UFDate effectdate = getEffectdate();
		// �жϻ��ƽ̨�Ƿ�����
		if (CmpPublicUtil.isFIPEnable(getPk_group())) {
			String pk_tradetype = getBillType();
			FipRelationInfoVO reVO = new FipRelationInfoVO();
			reVO.setPk_group(getPk_group());
			reVO.setPk_org((String) getPk_org());
			reVO.setRelationID(getId());
			/*reVO.setPk_system(PfDataCache.getBillType(pk_tradetype)
					.getSystemcode());*/
			reVO.setBusidate(effectdate == null ? AppContext.getInstance()
					.getBusiDate() : effectdate);
			reVO.setPk_billtype(pk_tradetype);
			reVO.setPk_operator(InvocationInfoProxy.getInstance().getUserId());
			reVO.setFreedef1(getBillcode());
			reVO.setPk_billtype(getBilltype());
			reVO.setPk_system("TGFN");
			String note = getNote();
			if (!nc.vo.jcom.lang.StringUtil.isEmptyWithTrim(note)
					&& note.length() > 50) {
				reVO.setFreedef2(note.substring(0, 50));
			}

			UFDouble money = getMny();

			if (money == null) {
				money = UFDouble.ZERO_DBL;
			}

			String pk_curr = getPk_currency();
			if (pk_curr == null) {
				pk_curr = CurrencyRateUtilHelper.getInstance()
						.getLocalCurrtypeByOrgID(reVO.getPk_org());

			}
			if (pk_curr == null) {
				pk_curr = CurrencyRateUtilHelper.getInstance()
						.getLocalCurrtypeByOrgID(reVO.getPk_group());
			}
			int currdigit = 2;
			try {
				currdigit = CurrtypeQuery.getInstance().getCurrdigit(pk_curr);
			} catch (Exception e) {

			}

			money = money.setScale(currdigit, UFDouble.ROUND_HALF_UP);

			reVO.setFreedef3(money.toString());
			reVO.setFreedef4("vuserdef4");
			reVO.setFreedef5("vuserdef5");
			FipMessageVO messageVO = new FipMessageVO();
			if (getAggVO().getChildrenVO() == null
					|| getAggVO().getChildrenVO().length == 0) {
				messageVO.setBillVO(getAggVO().getParentVO());
			} else {
				messageVO.setBillVO(getAggVO());
			}
			messageVO.setMessageinfo(reVO);
			return messageVO;
		} else {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("3607cash_0", "03607cash-0086")/*
																			 * @res
																			 * "���ƽ̨û�����ã��޷������Ƶ���ز���"
																			 */);
		}
	}

	/**
	 * ���ͻ��ƽ̨��ط���
	 */
	public IFipMessageService getFipMessageService() {
		return (IFipMessageService) NCLocator.getInstance().lookup(
				IFipMessageService.class.getName());
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	/**
	 * ͨ���ۺ�VO������������ѯ��Ӧ��VO��¼
	 * 
	 * @param aggvo
	 * @return
	 * @throws BusinessException
	 */
	public static AggregatedValueObject queryAggvoByPK(Class<?> c, String key)
			throws BusinessException {
		nc.itf.pubapp.pub.smart.IBillQueryService qry = NCLocator.getInstance()
				.lookup(nc.itf.pubapp.pub.smart.IBillQueryService.class);
		AbstractBill[] bills = qry.queryAbstractBillsByPks(c,
				new String[] { key });
		return bills != null && bills.length > 0 ? bills[0] : null;
	}

	public String getBillcode() {
		return billcode;
	}

	public void setBillcode(String billcode) {
		this.billcode = billcode;
	}

	public UFDate getEffectdate() {
		return effectdate;
	}

	public void setEffectdate(UFDate effectdate) {
		this.effectdate = effectdate;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public UFDouble getMny() {
		return mny;
	}

	public void setMny(UFDouble mny) {
		this.mny = mny;
	}

	public String getPk_currency() {
		return pk_currency;
	}

	public void setPk_currency(String pk_currency) {
		this.pk_currency = pk_currency;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public AggregatedValueObject getAggVO() {
		return aggVO;
	}

	public void setAggVO(AggregatedValueObject aggVO) {
		this.aggVO = aggVO;
	}

}
