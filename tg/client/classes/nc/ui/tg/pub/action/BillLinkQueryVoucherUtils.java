package nc.ui.tg.pub.action;

import java.awt.Component;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.fip.service.IFipRelationQueryService;
import nc.ui.pub.link.FipBillLinkQueryCenter;
import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.pub.BusinessException;

public class BillLinkQueryVoucherUtils {
	private static BillLinkQueryVoucherUtils utils = null;

	public static BillLinkQueryVoucherUtils getUtils() {
		if (utils == null) {
			utils = new BillLinkQueryVoucherUtils();
		}
		return utils;
	}

	/**
	 * 联查凭证
	 * 
	 * @param comp
	 * @param billtype
	 * @param pk_group
	 * @param pk_org
	 * @param primarykey
	 * @throws BusinessException
	 */
	public void onLinkQryVoucher(Component comp, String billtype,
			String pk_group, String pk_org, String primarykey)
			throws BusinessException {
		try {
			FipRelationInfoVO infovo = new FipRelationInfoVO();

			infovo.setPk_billtype(billtype);
			infovo.setPk_group(pk_group);
			infovo.setPk_org(pk_org);
			infovo.setRelationID(primarykey);
			IFipRelationQueryService ip1 = NCLocator.getInstance().lookup(
					IFipRelationQueryService.class);
			FipRelationInfoVO[] desvos = ip1.queryDesBill(infovo);
			if (desvos == null)
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("3607mng_0", "03607mng-0017")/*
																 * @res
																 * "没有相应的凭证信息"
																 */);
			// 来源方查目标方
			FipBillLinkQueryCenter.queryDesBillBySrcInfoInDlg(comp, infovo);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

	}

}
