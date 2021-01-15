package nc.bs.tg.alter.plugin.ebs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.outside.bpm.utils.SyncBPMBillStatesUtils;
import nc.itf.tg.ISaveLog;
import nc.itf.tg.outside.ISyncEBSServcie;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.ExecutionBVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.NcToEbsLogVO;

/**
 * ebsϵͳ��ʱͬ��ִ�������Ϣ��NCϵͳ
 * 
 * @author ASUS
 * 
 */
public class AoutSysncDataToNCInExecution implements IBackgroundWorkPlugin {
	BaseDAO baseDAO = null;
	IMDPersistenceQueryService queryServcie = null;
	String dblinkName = "@APPSTEST";// dblinke @APPSTEST

	ISyncEBSServcie ebsService = null;

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		String title = bgwc.getAlertTypeName();
		getExecutionData(title);
		return null;
	}

	/**
	 * ��ȡEBSִ�������ͼ
	 * 
	 * @param title
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<Object>> getExecutionData(String title)
			throws BusinessException {
		try {
			StringBuffer sql = new StringBuffer();
			Object fapplynumber = null;
			sql.append("select * from CUX_OKC_EXPLAIN_PLAN_V"
					+ dblinkName
					+ " where last_update_date is not null and nc_number is not null and fapplynumber is not null");

			List<Map<String, Object>> value = (List<Map<String, Object>>) getBaseDAO()
					.executeQuery(sql.toString(), new MapListProcessor());
			if (value != null && value.size() > 0) {
				for (Map<String, Object> map : value) {

					// ��ͬ��
					String fnumber = (String) map.get("fnumber");
					AggCtApVO aggVO = (AggCtApVO) getBillVO(AggCtApVO.class,
							"isnull(dr,0)=0 and vbillcode = '" + fnumber + "'");

					if (aggVO != null) {
						List<ExecutionBVO> list = new ArrayList<ExecutionBVO>();
						// ִ�����vo
						ExecutionBVO executionBVO = new ExecutionBVO();
						// �������
						executionBVO.setDef8((String) map.get("amount_type"));
						// ����
						fapplynumber = map.get("fapplynumber");
						if (fapplynumber != null) {
							executionBVO.setDef9(fapplynumber.toString());
						}
						// �������
						Object fapplydate = map.get("fapplydate");
						if (fapplydate != null) {
							executionBVO.setDef10(fapplydate.toString());
						}
						// �����
						Object fapplyamount = map.get("fapplyamount");
						if (fapplyamount != null) {
							executionBVO.setDef11(fapplyamount.toString());
						}
						// ���ݽ��
						Object nc_pay_amount = map.get("nc_pay_amount");
						if (nc_pay_amount != null) {
							executionBVO.setDef12(nc_pay_amount.toString());
						}
						// ������ʽ
						executionBVO.setDef13((String) map.get("type_code"));
						// ��������
						Object nc_pat_date = map.get("nc_pat_date");
						if (nc_pat_date != null) {
							executionBVO.setDef14(nc_pat_date.toString());
						}
						// ���ݱ��
						Object nc_number = map.get("nc_number");
						if (nc_number != null) {
							executionBVO.setDef15(nc_number.toString());
						}
						// ��ͷ����
						executionBVO.setPk_fct_ap(aggVO.getPrimaryKey());

						executionBVO.setDr(0);
						
						executionBVO.setStatus(VOStatus.NEW);

						list.add(executionBVO);
						aggVO.setChildren(ExecutionBVO.class,
								list.toArray(new ExecutionBVO[0]));
						getEBSServcie().syncExecutionData_RequiresNew(aggVO,
								title);
					}

				}
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return null;
	}

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	public IMDPersistenceQueryService getQueryServcie() {
		if (queryServcie == null) {
			queryServcie = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return queryServcie;
	}

	public ISyncEBSServcie getEBSServcie() {
		if (ebsService == null) {
			ebsService = NCLocator.getInstance().lookup(ISyncEBSServcie.class);
		}
		return ebsService;
	}

	/**
	 * ��ȡҵ�񵥾ݾۺ�VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public AggregatedValueObject getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getQueryServcie().queryBillOfVOByCond(c,
				whereCondStr, false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}
}
