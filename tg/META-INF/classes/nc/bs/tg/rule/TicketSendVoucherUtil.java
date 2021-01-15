package nc.bs.tg.rule;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

public class TicketSendVoucherUtil {

	AbstractBill aggVO = null;
	FINSendVoucher voucher = new FINSendVoucher();

	public void addVoucher(AbstractBill aggVO) throws BusinessException {
		try {
			CircularlyAccessibleValueObject hvo = aggVO.getParentVO();
			String billtype = hvo.getAttributeValue("transtype") == null ? (String) hvo
					.getAttributeValue("billtype") : (String) hvo
					.getAttributeValue("transtype");
			UFDate date = ((UFDate) hvo.getAttributeValue("billdate") != null) ? (UFDate) hvo
					.getAttributeValue("billdate") : (UFDate) hvo
					.getAttributeValue("dbilldate");
			voucher.setAggVO(aggVO);
			//String transtype =	getBillTypeCodeByPK((String) hvo.getAttributeValue("transtypepk"));
			voucher.setBilltype("RZ30");
			voucher.setBillType("RZ30");
			voucher.setBillcode((String) hvo.getAttributeValue("billno"));
			voucher.setEffectdate(date);
			voucher.setPk_group((String) hvo.getAttributeValue("pk_group"));
			voucher.setPk_org((String) hvo.getAttributeValue("pk_org"));
			voucher.setMny(new UFDouble((String)hvo.getAttributeValue("def10")));
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
			String billtype = hvo.getAttributeValue("transtype") == null ? (String) hvo
					.getAttributeValue("billtype") : (String) hvo
					.getAttributeValue("transtype");
			voucher.setAggVO(aggVO);
			//String transtype =	getBillTypeCodeByPK((String) hvo.getAttributeValue("transtypepk"));
			voucher.setBilltype("RZ30");
			voucher.setBillType("RZ30");
			voucher.setBillcode((String) hvo.getAttributeValue("billno"));
			voucher.setEffectdate((UFDate) hvo.getAttributeValue("billdate"));
			voucher.setPk_group((String) hvo.getAttributeValue("pk_group"));
			voucher.setPk_org((String) hvo.getAttributeValue("pk_org"));
			voucher.setNote("");
			voucher.setPk_currency("");
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
