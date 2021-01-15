package nc.bs.tg.invoicing.ace.rule;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.tg.rule.FINSendVoucher;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

public class TGSendVoucherUtil {

	AbstractBill aggVO = null;
	FINSendVoucher voucher = new FINSendVoucher();

	public void addVoucher(AbstractBill aggVO) throws BusinessException {
		try {
			CircularlyAccessibleValueObject hvo = aggVO.getParentVO();
			String billtype = "FN23";
			voucher.setAggVO(aggVO);
//			String transtype = getBillTypeCodeByPK((String) hvo
//					.getAttributeValue("transtypepk"));
			voucher.setBilltype(billtype);
			voucher.setBillType(billtype);
			voucher.setBillcode((String) hvo.getAttributeValue("billno"));
			voucher.setEffectdate((UFDate) hvo.getAttributeValue("billdate"));
			voucher.setPk_group((String) hvo.getAttributeValue("pk_group"));
			voucher.setPk_org((String) hvo.getAttributeValue("pk_org"));
			voucher.setMny((UFDouble) (hvo.getAttributeValue("def29") == null
					|| "~".equals(hvo.getAttributeValue("def29")) ? UFDouble.ZERO_DBL
					: new UFDouble(String.valueOf(hvo
							.getAttributeValue("def29")))));
			voucher.setNote("");
			voucher.setPk_currency("");
			voucher.setId((String) hvo.getPrimaryKey());
			voucher.sendDAPAddMessge(voucher.getMessageVO());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public void delVoucher(AbstractBill aggVO) throws BusinessException {
		try {
			CircularlyAccessibleValueObject hvo = aggVO.getParentVO();
			String billtype = "FN23";
			voucher.setAggVO(aggVO);
			// String transtype = getBillTypeCodeByPK((String) hvo
			// .getAttributeValue("transtypepk"));
			voucher.setBilltype(billtype);
			voucher.setBillType(billtype);
			voucher.setBillcode((String) hvo.getAttributeValue("billno"));
			voucher.setEffectdate((UFDate) hvo.getAttributeValue("billdate"));
			voucher.setPk_group((String) hvo.getAttributeValue("pk_group"));
			voucher.setPk_org((String) hvo.getAttributeValue("pk_org"));
			voucher.setNote("");
			voucher.setPk_currency("");
			voucher.setMny((UFDouble) (hvo.getAttributeValue("def29") == null
					|| "~".equals(hvo.getAttributeValue("def29")) ? UFDouble.ZERO_DBL
					: new UFDouble(String.valueOf(hvo
							.getAttributeValue("def29")))));
			voucher.setId((String) hvo.getPrimaryKey());
			voucher.sendDAPDelMessge(voucher.getMessageVO());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private String getBillTypeCodeByPK(String billtypeid) throws DAOException {
		BaseDAO dao = new BaseDAO();
		String sql = "SELECT pk_billtypecode FROM bd_billtype WHERE pk_billtypeid = '"
				+ billtypeid + "';";
		String billtypecode = (String) dao.executeQuery(sql,
				new ColumnProcessor());
		return billtypecode;
	}
}
