package nc.bs.tg.fund.rz.report.cost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.tg.fund.rz.report.ReportConts;
import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.AppContext;
import nc.vo.tgfn.report.cost.CostLedgerVO;
import nc.vo.tgfp.pub.common.FPConst;
import nc.vo.tgfp.report.ReportOrgVO;

import org.apache.commons.lang.StringUtils;

import com.ufida.dataset.IContext;

/**
 * Ѻ��֤��̨�ˣ��ɱ�����
 * 
 * @author ASUS
 * 
 */
public class CostLedgerUtils extends ReportUtils {
	static CostLedgerUtils utils;

	public static CostLedgerUtils getUtils() throws BusinessException {
		if (utils == null) {
			utils = new CostLedgerUtils();
		}
		return utils;
	}

	public CostLedgerUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance()
				.getPropertiesAry(new CostLedgerVO()));
	}

	@Override
	public DataSet getProcessData(IContext context) throws BusinessException {
		CmpReportResultVO[] cmpreportresults = null;

		try {
			ConditionVO[] conditionVOs = (ConditionVO[]) context
					.getAttribute(FPConst.KEY_QueryScheme);
			if (conditionVOs == null || conditionVOs.length == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			initQuery(conditionVOs);
			List<CostLedgerVO> list = getCostLedgerVOs();
			if(list != null && list.size() > 0){
				cmpreportresults = transReportResult(list);
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

	/**
	 * ��ѯѺ��֤��̨�˱������ݷ���
	 * @param travellingTrader 
	 * @param area2 
	 * @param documentationDate 
	 * @param travellingTrader2 
	 * @throws BusinessException 
	 * */
	@SuppressWarnings("unchecked")
	private List<CostLedgerVO> getCostLedgerVOs() throws BusinessException{
		String documentationDate_begin = queryValueMap.get("documentationDate_begin");
		String documentationDate_end = queryValueMap.get("documentationDate_end");
		String area = queryValueMap.get("area");
		String travellingTrader = queryValueMap.get("travellingTrader");
		String companyName = queryValueMap.get("companyName");
		String transactionPerson = queryValueMap.get("transactionPerson");
		String receiptNum = queryValueMap.get("receiptNum");
		String contractCode = queryValueMap.get("contractCode");
		String outBillNo = queryValueMap.get("outBillNo");
		String tradeType = queryValueMap.get("tradeType");
		//add by �ƹڻ� SDYC-56 Ѻ��֤��̨�ˣ��ɱ�����20200818 begin
		List<Map<String, Object>> lists=getAviBalOrg(companyName);
		if(lists==null||lists.isEmpty()){//û�����ֱ�ӷ��ؿ�
			return null;
		}
		//add by �ƹڻ� SDYC-56 Ѻ��֤��̨�ˣ��ɱ�����20200818 end
		//��ȡ���й�˾��μ���VO
		List<ReportOrgVO> orgVOs = getOrgVOs();
		//���pk_org��pk_region�Ķ�Ӧ��ϵ
		Map<String, String> regionMap = new HashMap<String,String>();
		Map<String, ArrayList<String>> orgsMap = new HashMap<String,ArrayList<String>>();
		for (ReportOrgVO reportOrgVO : orgVOs) {
			String pk_org = reportOrgVO.getPk_org();
			String pk_region = reportOrgVO.getPk_region();
			//���pk_org����pk_region
			regionMap.put(pk_org, pk_region);
			/**
			 * ��orgsMap�л�ȡpk_region��Ӧ��list���ϣ��������Ϊnull��
			 * orgsMapҪ�洢�Ե�ǰpk_regionΪkey������˵�ǰpk_org��listΪvalue���¼�ֵ��
			 * �����Ϊnull��˵����ǰorgsMap���Ѵ���pk_regionΪkey�ļ�ֵ�ԣ�ֻ��Ҫ��list
			 * �д��뵱ǰpk_org����
			 */
			ArrayList<String> arrayList = orgsMap.get(pk_region);
			if(arrayList != null){
				arrayList.add(pk_org);
			}else{
				ArrayList<String> arrayList2 = new ArrayList<String>();
				arrayList2.add(pk_org);
				orgsMap.put(pk_region, arrayList2);
			}
		}
		StringBuilder sql = new StringBuilder();
		//���
		sql.append("select distinct * from ( ");
		sql.append("select  * from ( ");
		sql.append("select ");
		sql.append("gl_voucher.pk_voucher as pkVoucher, ");//ƾ֤����
		sql.append("org_orgs.pk_org as area, ");//����
		sql.append("org_orgs.name as companyName, ");//��˾����
		sql.append("bd_accasoa.name as accountingSubject, ");//��ƿ�Ŀ
		sql.append("bd_cust_supplier.name as travellingTrader, ");//����
		sql.append("gl_voucher.explanation as voucherAbstract , ");//��֤���Ѻ�����ݣ�ƾ֤ժҪ��
		sql.append("gl_detail.localdebitamount as paymentAmount, ");//֧�����
		sql.append("SUBSTR(ap_paybill.paydate,1,10) as paymentDate, ");//֧������
		sql.append("gl.user_name as proofMaker, ");//ƾ֤�Ƶ���
		sql.append("SUBSTR(gl_voucher.prepareddate,1,10) as documentationDate, ");//�Ƶ�����
		sql.append("to_char(gl_voucher.num) as proofNum, ");//ƾ֤��
		sql.append("org_dept.name as transactionDepartment, ");//���첿��
		sql.append("ap.user_name as transactionPerson, ");//�����ˣ��˿������ˣ�
		sql.append("fct_ap.actualinvalidate as refundDate, ");//�˿����
//		sql.append("dap_dapsystem.systypename as sourceSystem, ");//��Դϵͳ
		sql.append("case when nvl(dap_dapsystem.systypename,'~')='~' then '����' else dap_dapsystem.systypename end  as sourceSystem, ");//��Դϵͳ
		sql.append("bd_billtype.billtypename as tradeType, ");//��������
		sql.append("ap_paybill.billno as receiptNum, ");//���ݱ��
		sql.append("ap_paybill.def3 as imageCode, ");//Ӱ�����
		sql.append("fct_ap.vbillcode as contractCode, ");//��ͬ����
		sql.append("fct_ap.ctname as contractName, ");//��ͬ����
		sql.append("ap_paybill.def2 as outBillNo, ");//��ϵͳ���ݺ�
		sql.append("'' as sourceRemark ");//��Դ��ע
		sql.append(" ,gl_detail.pk_org ");//��֯pk
		sql.append(" ,bd_accasoa.pk_accasoa ");//��Ŀ
		sql.append(" ,gl_detail.assid ");//��Դ��ע
		sql.append(" ,case when  length(nvl(fct_ap.vbillcode,'0'))>1 and fct_ap.blatest = 'Y' then 'Y' else (case when length(nvl(fct_ap.vbillcode,'0'))<=1 then 'Y' else 'N' end)end isneed ");
		sql.append("from gl_voucher ");//ƾ֤
		//ƾ֤������¼
		sql.append("left join gl_detail on gl_detail.pk_voucher = gl_voucher.pk_voucher and nvl(gl_detail.dr,0) = 0 ");
		//��¼����ģ����Ϣ
//		sql.append("left join dap_dapsystem on RTRIM(gl_detail.pk_systemv) = dap_dapsystem.systypecode and nvl(dap_dapsystem.dr,0) = 0 ");
		//��¼������ƿ�Ŀ
		sql.append("left join bd_accasoa on bd_accasoa.pk_accasoa = gl_detail.pk_accasoa and nvl(bd_accasoa.dr,0) = 0  ");
		//��ƿ�Ŀ������ƿ�Ŀ������Ϣ��
		sql.append("left join bd_account on bd_accasoa.pk_account = bd_account.pk_account and nvl(bd_account.dr,0) = 0 ");
		//ƾ֤�����м��
		sql.append("left join fip_relation on fip_relation.des_relationid = gl_voucher.pk_voucher and nvl(fip_relation.dr,0) = 0 ");
		//��¼����ģ����Ϣ
		sql.append("left join dap_dapsystem on RTRIM(fip_relation.src_system) = dap_dapsystem.systypecode and nvl(dap_dapsystem.dr,0) = 0 ");
		//�м�������Ӧ�̸��
		sql.append("left join ap_paybill on ap_paybill.pk_paybill = fip_relation.src_relationid and nvl(ap_paybill.dr,0) = 0 ");
		//������������
		sql.append("left join bd_billtype on bd_billtype.pk_billtypeid = ap_paybill.pk_tradetypeid and (nvl(bd_billtype.dr,0) = 0 "
				+ "or bd_billtype.dr is null)  ");
		//��Ӧ�̸�����������ͬ
		sql.append("left join fct_ap on fct_ap.vbillcode = ap_paybill.def5 and nvl(fct_ap.dr,0) = 0 ");
		//ƾ֤�����û�
		sql.append("left join sm_user gl on gl_voucher.pk_prepared = gl.cuserid and nvl(gl.dr,0) = 0 ");
		//ƾ֤������֯
		sql.append("left join org_orgs on org_orgs.pk_org = gl_voucher.pk_org and nvl(org_orgs.dr,0) = 0 ");
		//��¼����������Ŀ
		sql.append("left join gl_docfree1 on gl_detail.assid = gl_docfree1.assid and nvl(gl_docfree1.dr,0) = 0 ");
		//������Ŀ��������
		sql.append("left join bd_cust_supplier on bd_cust_supplier.pk_cust_sup = gl_docfree1.f4 and nvl(bd_cust_supplier.dr,0) = 0 ");
		//��������û�
		sql.append("left join sm_user ap on ap_paybill.creator = ap.cuserid and nvl(ap.dr,0) = 0 ");
		//�û�������Ա������Ϣ
		sql.append("left join bd_psndoc on bd_psndoc.pk_psndoc = ap.pk_psndoc and nvl(bd_psndoc.dr,0) = 0 ");
		//��Ա������Ϣ������Ա������Ϣ
		sql.append("left join bd_psnjob on bd_psnjob.pk_psndoc = bd_psndoc.pk_psndoc and bd_psnjob.ismainjob = 'Y' and nvl(bd_psnjob.dr,0) = 0 ");
		//��Ա������Ϣ ���� HR���� 
		sql.append("left join org_dept on org_dept.pk_dept = bd_psnjob.pk_dept and nvl(org_dept.dr,0) = 0 ");
		sql.append("where nvl(gl_voucher.dr,0) = 0 ");
		//sql.append("and fct_ap.blatest = 'Y' ");  
		sql.append("and gl_voucher.period <> '00' ");
		sql.append("and bd_account.code in ('12210201','12210202','22410401','22410402') ");
		//sql.append("and gl_detail.localdebitamount <> 0 ");
		
		//��Ӳ�ѯ����������ת������Ӧ�Ĺ�˾pk
		if(area != null){
			String[] pk_regionArr = area.split(",");
			//new һ��list���ڴ�����е�pk_region(����)��Ӧ��pk_org(��˾)
			List<String> pk_org = new ArrayList<String>();
			//ѭ���������pk_region(����)
			for (String pk_region : pk_regionArr) {
				//���get pk_region��Ӧ��org list��Ϊnull��������pk_org���list��
				if(orgsMap.get(pk_region) != null){
					pk_org.addAll(orgsMap.get(pk_region));
				}
			}
			if(pk_org.size()>0){
				sql.append(" and "
						+ SQLUtil.buildSqlForIn(
								" org_orgs.pk_org ",
								pk_org.toArray(new String[pk_org.size()])));
			}
		}
		//��Ӳ�ѯ����������pk
		if(travellingTrader != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_cust_supplier.pk_cust_sup ",
							travellingTrader.split(",")));
		}
		//��Ӳ�ѯ�������Ƶ����ڽ���date��date֮��
		if(documentationDate_begin != null && documentationDate_end != null){
			sql.append("and gl_voucher.prepareddate between '"+documentationDate_begin+"' and '"+documentationDate_end+"' ");
		}
		//��Ӳ�ѯ��������˾PK
		if(companyName != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" org_orgs.pk_org ",
							companyName.split(",")));
		}
		//��Ӳ�ѯ������������PK
		if(transactionPerson != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" ap.cuserid ",
							transactionPerson.split(",")));
		}
		//��Ӳ�ѯ���������ݱ��
		if(receiptNum != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" ap_paybill.billno ",
							receiptNum.split(",")));
		}
		//��Ӳ�ѯ��������ͬ����
		if(contractCode != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" fct_ap.vbillcode ",
							contractCode.split(",")));
		}
		//��Ӳ�ѯ��������ϵͳ���ݱ��
		if(outBillNo != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" ap_paybill.def2 ",
							outBillNo.split(",")));
		}
		//��Ӳ�ѯ��������������
		if(tradeType != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_billtype.pk_billtypeid ",
							tradeType.split(",")));
		}
		sql.append(" ) ");
		sql.append("where isneed= 'Y' ");
		
		sql.append("union all ");
		//������
		sql.append("select * from ( ");
		sql.append("select ");
		sql.append("gl_voucher.pk_voucher as pkVoucher, ");//ƾ֤����
		sql.append("org_orgs.pk_org as area, ");//����
		sql.append("org_orgs.name as companyName, ");//��˾����
		sql.append("bd_accasoa.name as accountingSubject, ");
		sql.append("bd_cust_supplier.name as travellingTrader, ");
		sql.append("gl_voucher.explanation as voucherAbstract, ");
		sql.append("gl_detail.localdebitamount as paymentAmount, ");
		sql.append("SUBSTR(er_bxzb.paydate,1,10) as paymentDate, ");
		sql.append("gl.user_name as proofMaker, ");
		sql.append("SUBSTR(gl_voucher.prepareddate,1,10) as documentationDate, ");
		sql.append("to_char(gl_voucher.num) as proofNum, ");
		sql.append("org_dept.name as transactionDepartment, ");
		sql.append("er.user_name as transactionPerson, ");
		sql.append("fct_ap.actualinvalidate as refundDate, ");
//		sql.append("dap_dapsystem.systypename as sourceSystem, ");//��Դϵͳ
		sql.append("case when nvl(dap_dapsystem.systypename,'~')='~' then '����' else dap_dapsystem.systypename end  as sourceSystem, ");//��Դϵͳ
		sql.append("bd_billtype.billtypename as tradeType, ");
		sql.append("er_bxzb.djbh as receiptNum, ");
		sql.append("er_bxzb.zyx16 as imageCode, ");
		sql.append("fct_ap.vbillcode as contractCode, ");
		sql.append("fct_ap.ctname as contractName, ");
		sql.append("'' as outBillNo, ");//����ϵͳ���ݺ�
		sql.append("'' as sourceRemark ");
		sql.append(" ,gl_detail.pk_org ");//��֯pk
		sql.append(" ,bd_accasoa.pk_accasoa ");//��Ŀ
		sql.append(" ,gl_detail.assid ");//��Դ��ע
	    sql.append(" ,case when  length(nvl(fct_ap.vbillcode,'0'))>1 and fct_ap.blatest = 'Y' then 'Y' else (case when length(nvl(fct_ap.vbillcode,'0'))<=1 then 'Y' else 'N' end)end isneed ");
		sql.append("from gl_voucher ");
		//ƾ֤������¼
		sql.append("left join gl_detail on gl_detail.pk_voucher = gl_voucher.pk_voucher and nvl(gl_detail.dr,0) = 0 ");
		//��¼����ģ����Ϣ
//		sql.append("left join dap_dapsystem on RTRIM(gl_detail.pk_systemv) = dap_dapsystem.systypecode and nvl(dap_dapsystem.dr,0) = 0 ");
		//��¼������ƿ�Ŀ
		sql.append("left join bd_accasoa on bd_accasoa.pk_accasoa = gl_detail.pk_accasoa and nvl(bd_accasoa.dr,0) = 0 ");
		//��ƿ�Ŀ������ƿ�Ŀ������Ϣ��
		sql.append("left join bd_account on bd_accasoa.pk_account = bd_account.pk_account and nvl(bd_account.dr,0) = 0 ");
		//ƾ֤�����м��
		sql.append("left join fip_relation on fip_relation.des_relationid = gl_voucher.pk_voucher and nvl(fip_relation.dr,0) = 0 ");
		//��¼����ģ����Ϣ
		sql.append("left join dap_dapsystem on RTRIM(fip_relation.src_system) = dap_dapsystem.systypecode and nvl(dap_dapsystem.dr,0) = 0 ");
		//�м�����������
		sql.append("left join er_bxzb on er_bxzb.pk_jkbx = fip_relation.src_relationid and nvl(er_bxzb.dr,0) = 0 ");
		//������������
		sql.append("left join bd_billtype on bd_billtype.pk_billtypeid = er_bxzb.pk_tradetypeid and (nvl(bd_billtype.dr,0) = 0 "
				+ "or bd_billtype.dr is null) ");
		//������������ͬ
		sql.append("left join fct_ap on fct_ap.vbillcode = er_bxzb.zyx2 and nvl(fct_ap.dr,0) = 0 ");
		//�����������û�
		sql.append("left join sm_user er on er_bxzb.creator = er.cuserid and nvl(er.dr,0) = 0 ");
		//�û�������Ա������Ϣ
		sql.append("left join bd_psndoc on bd_psndoc.pk_psndoc = er.pk_psndoc and nvl(bd_psndoc.dr,0) = 0 ");
		//��Ա������Ϣ������Ա������Ϣ
		sql.append("left join bd_psnjob on bd_psnjob.pk_psndoc = bd_psndoc.pk_psndoc and bd_psnjob.ismainjob = 'Y' and nvl(bd_psnjob.dr,0) = 0 ");
		//��Ա������Ϣ ���� HR���� 
		sql.append("left join org_dept on org_dept.pk_dept = bd_psnjob.pk_dept and nvl(org_dept.dr,0) = 0 ");
		//ƾ֤�����û�
		sql.append("left join sm_user gl on gl_voucher.pk_prepared = gl.cuserid and nvl(gl.dr,0) = 0 ");
		//ƾ֤������֯
		sql.append("left join org_orgs on org_orgs.pk_org = gl_voucher.pk_org and nvl(org_orgs.dr,0) = 0 ");
		//��¼����������Ŀ
		sql.append("left join gl_docfree1 on gl_detail.assid = gl_docfree1.assid  and nvl(gl_docfree1.dr,0) = 0 ");
		//������Ŀ��������
		sql.append("left join bd_cust_supplier on bd_cust_supplier.pk_cust_sup = gl_docfree1.f4 and nvl(bd_cust_supplier.dr,0) = 0 ");
		sql.append("where nvl(gl_voucher.dr,0) = 0 ");
		//sql.append("and fct_ap.blatest = 'Y' ");
		sql.append("and gl_voucher.period <> '00' ");
		sql.append("and bd_account.code in ('12210201','12210202','22410401','22410402') ");
		//sql.append("and gl_detail.localdebitamount <> 0 ");

		if(area != null){
			String[] pk_regionArr = area.split(",");
			List<String> pk_org = new ArrayList<String>();
			for (String pk_region : pk_regionArr) {
				if(orgsMap.get(pk_region) != null){
					pk_org.addAll(orgsMap.get(pk_region));
				}
			}
			if(pk_org.size()>0){
				sql.append(" and "
						+ SQLUtil.buildSqlForIn(
								" org_orgs.pk_org ",
								pk_org.toArray(new String[pk_org.size()])));
			}
		}
		if(travellingTrader != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_cust_supplier.pk_cust_sup ",
							travellingTrader.split(",")));
		}
		if(documentationDate_begin != null && documentationDate_end != null){
			sql.append("and gl_voucher.prepareddate between '"+documentationDate_begin+"' and '"+documentationDate_end+"'");
		}
		//��Ӳ�ѯ��������˾PK
		if(companyName != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" org_orgs.pk_org ",
							companyName.split(",")));
		}
		//��Ӳ�ѯ������������PK
		if(transactionPerson != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" er.cuserid ",
							transactionPerson.split(",")));
		}
		//��Ӳ�ѯ���������ݱ��
		if(receiptNum != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" er_bxzb.djbh ",
							receiptNum.split(",")));
		}
		//��Ӳ�ѯ��������ͬ����
		if(contractCode != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" fct_ap.vbillcode ",
							contractCode.split(",")));
		}
		//��Ӳ�ѯ��������ϵͳ���ݱ�� 
		if(outBillNo != null){
			sql.append(" and 1=2");//add by �ƹڻ� ������û����ϵͳ���ݺţ�����ϵͳ���ݱ�Ź�������ʱ����������ʾ 20200901
		}
		//��Ӳ�ѯ��������������
		if(tradeType != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_billtype.pk_billtypeid ",
							tradeType.split(",")));
		}
		sql.append(" ) ");
		sql.append("where isneed= 'Y' ");
		
		sql.append(" union all ");
		//�տ
		sql.append("select * from (");
		sql.append("select ");
		sql.append("gl_voucher.pk_voucher as pkVoucher, ");//ƾ֤����
		sql.append("org_orgs.pk_org as area, ");//����
		sql.append("org_orgs.name as companyName, ");//��˾����
		sql.append("bd_accasoa.name as accountingSubject, ");//��ƿ�Ŀ
		sql.append("bd_cust_supplier.name as travellingTrader, ");//����
		sql.append("gl_voucher.explanation as voucherAbstract , ");//��֤���Ѻ�����ݣ�ƾ֤ժҪ��
		sql.append("gl_detail.localdebitamount as paymentAmount, ");//֧�����
		sql.append("SUBSTR(ar_gatherbill.paydate,1,10) as paymentDate, ");//֧������
		sql.append("gl.user_name as proofMaker, ");//ƾ֤�Ƶ���
		sql.append("SUBSTR(gl_voucher.prepareddate,1,10) as documentationDate, ");//�Ƶ�����
		sql.append("to_char(gl_voucher.num) as proofNum, ");//ƾ֤��
		sql.append("org_dept.name as transactionDepartment, ");//���첿��
		sql.append("ap.user_name as transactionPerson, ");//�����ˣ��˿������ˣ�
		sql.append("fct_ap.actualinvalidate as refundDate, ");//�˿����
//		sql.append("dap_dapsystem.systypename as sourceSystem, ");//��Դϵͳ
		sql.append("case when nvl(dap_dapsystem.systypename,'~')='~' then '����' else dap_dapsystem.systypename end  as sourceSystem, ");//��Դϵͳ
		sql.append("bd_billtype.billtypename as tradeType, ");//��������
		sql.append("ar_gatherbill.billno as receiptNum, ");//���ݱ��
		sql.append("ar_gatherbill.def3 as imageCode, ");//Ӱ�����
		sql.append("fct_ap.vbillcode as contractCode, ");//��ͬ����
		sql.append("fct_ap.ctname as contractName, ");//��ͬ����
		sql.append("ar_gatherbill.def2 as outBillNo, ");//��ϵͳ���ݺ�
		sql.append("'' as sourceRemark ");//��Դ��ע
		sql.append(" ,gl_detail.pk_org ");//��֯pk
		sql.append(" ,bd_accasoa.pk_accasoa ");//��Ŀ
		sql.append(" ,gl_detail.assid ");//��Դ��ע
	    sql.append(" ,case when length(nvl(fct_ap.vbillcode,'0'))>1 and fct_ap.blatest = 'Y' then 'Y' else (case when length(nvl(fct_ap.vbillcode,'0'))<=1 then 'Y' else 'N' end)end isneed ");
		sql.append("from gl_voucher ");//ƾ֤
		//ƾ֤������¼
		sql.append("left join gl_detail on gl_detail.pk_voucher = gl_voucher.pk_voucher and nvl(gl_detail.dr,0) = 0 ");
		//��¼����ģ����Ϣ
//		sql.append("left join dap_dapsystem on RTRIM(gl_detail.pk_systemv) = dap_dapsystem.systypecode and nvl(dap_dapsystem.dr,0) = 0 ");
		//��¼������ƿ�Ŀ
		sql.append("left join bd_accasoa on bd_accasoa.pk_accasoa = gl_detail.pk_accasoa and nvl(bd_accasoa.dr,0) = 0  ");
		//��ƿ�Ŀ������ƿ�Ŀ������Ϣ��
		sql.append("left join bd_account on bd_accasoa.pk_account = bd_account.pk_account and nvl(bd_account.dr,0) = 0 ");
		//ƾ֤�����м��
		sql.append("left join fip_relation on fip_relation.des_relationid = gl_voucher.pk_voucher and nvl(fip_relation.dr,0) = 0 ");
		//��¼����ģ����Ϣ
		sql.append("left join dap_dapsystem on RTRIM(fip_relation.src_system) = dap_dapsystem.systypecode and nvl(dap_dapsystem.dr,0) = 0 ");
		//�м�������Ӧ���տ
		sql.append("left join ar_gatherbill on ar_gatherbill.pk_gatherbill = fip_relation.src_relationid and nvl(ar_gatherbill.dr,0) = 0 ");
		//������������
		sql.append("left join bd_billtype on bd_billtype.pk_billtypeid = ar_gatherbill.pk_tradetypeid and (nvl(bd_billtype.dr,0) = 0 "
				+ "or bd_billtype.dr is null)  ");
		//��Ӧ���տ�����տ��ͬ
        sql.append("left join fct_ap on fct_ap.vbillcode = ar_gatherbill.def5 and nvl(fct_ap.dr,0) = 0 ");
		//ƾ֤�����û�
		sql.append("left join sm_user gl on gl_voucher.pk_prepared = gl.cuserid and nvl(gl.dr,0) = 0 ");
		//ƾ֤������֯
		sql.append("left join org_orgs on org_orgs.pk_org = gl_voucher.pk_org and nvl(org_orgs.dr,0) = 0 ");
		//��¼����������Ŀ
		sql.append("left join gl_docfree1 on gl_detail.assid = gl_docfree1.assid and nvl(gl_docfree1.dr,0) = 0 ");
		//������Ŀ��������
		sql.append("left join bd_cust_supplier on bd_cust_supplier.pk_cust_sup = gl_docfree1.f4 and nvl(bd_cust_supplier.dr,0) = 0 ");
		//��������û�
		sql.append("left join sm_user ap on ar_gatherbill.creator = ap.cuserid and nvl(ap.dr,0) = 0 ");
		//�û�������Ա������Ϣ
		sql.append("left join bd_psndoc on bd_psndoc.pk_psndoc = ap.pk_psndoc and nvl(bd_psndoc.dr,0) = 0 ");
		//��Ա������Ϣ������Ա������Ϣ
		sql.append("left join bd_psnjob on bd_psnjob.pk_psndoc = bd_psndoc.pk_psndoc and bd_psnjob.ismainjob = 'Y' and nvl(bd_psnjob.dr,0) = 0 ");
		//��Ա������Ϣ ���� HR���� 
		sql.append("left join org_dept on org_dept.pk_dept = bd_psnjob.pk_dept and nvl(org_dept.dr,0) = 0 ");
		sql.append("where nvl(gl_voucher.dr,0) = 0 ");
		//sql.append("and fct_ap.blatest = 'Y' "); 
		sql.append("and gl_voucher.period <> '00' ");
		sql.append("and bd_account.code in ('12210201','12210202','22410401','22410402') ");
		//sql.append("and gl_detail.localdebitamount <> 0 ");

		//��Ӳ�ѯ����������ת������Ӧ�Ĺ�˾pk
		if(area != null){
			String[] pk_regionArr = area.split(",");
			//new һ��list���ڴ�����е�pk_region(����)��Ӧ��pk_org(��˾)
			List<String> pk_org = new ArrayList<String>();
			//ѭ���������pk_region(����)
			for (String pk_region : pk_regionArr) {
				//���get pk_region��Ӧ��org list��Ϊnull��������pk_org���list��
				if(orgsMap.get(pk_region) != null){
					pk_org.addAll(orgsMap.get(pk_region));
				}
			}
			if(pk_org.size()>0){
				sql.append(" and "
						+ SQLUtil.buildSqlForIn(
								" org_orgs.pk_org ",
								pk_org.toArray(new String[pk_org.size()])));
			}
		}
		//��Ӳ�ѯ����������pk
		if(travellingTrader != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_cust_supplier.pk_cust_sup ",
							travellingTrader.split(",")));
		}
		//��Ӳ�ѯ�������Ƶ����ڽ���date��date֮��
		if(documentationDate_begin != null && documentationDate_end != null){
			sql.append("and gl_voucher.prepareddate between '"+documentationDate_begin+"' and '"+documentationDate_end+"' ");
		}
		//��Ӳ�ѯ��������˾PK
		if(companyName != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" org_orgs.pk_org ",
							companyName.split(",")));
		}
		//��Ӳ�ѯ������������PK
		if(transactionPerson != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" ap.cuserid ",
							transactionPerson.split(",")));
		}
		//��Ӳ�ѯ���������ݱ��
		if(receiptNum != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" ar_gatherbill.billno ",
							receiptNum.split(",")));
		}
		//��Ӳ�ѯ��������ͬ����
		if(contractCode != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" fct_ap.vbillcode ",
							contractCode.split(",")));
		}
		//��Ӳ�ѯ��������ϵͳ���ݱ��
		if(outBillNo != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" ar_gatherbill.def2 ",
							outBillNo.split(",")));
		}
		//��Ӳ�ѯ��������������
		if(tradeType != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_billtype.pk_billtypeid ",
							tradeType.split(",")));
		}
		
		
		
		sql.append(") ");
	
		sql.append(") ");

		
		
		
		List<CostLedgerVO> costLedgerVOs = (List<CostLedgerVO>)getBaseDAO().executeQuery(sql.toString(), new BeanListProcessor(CostLedgerVO.class));
		// �����ѯ������area������Ϊnull�Ļ���nameMap��û��ֵ�ģ���������newһ��map�����pk_region�����򣩶�Ӧname�����ƣ�
		Map<String,String> region_nameMap = new HashMap<String,String>();
		List<CostLedgerVO> useLessVO= new ArrayList<CostLedgerVO>();
		//��������area���ƺ����
		if(null != costLedgerVOs && costLedgerVOs.size()>0){
			//��ʼ�����Ϊ0
			int sequeNumGlobal = 0;
			Iterator<CostLedgerVO> iterator = costLedgerVOs.iterator();
			String pk_org2="";
			String pk_accasoa="";
			String assid="";
			String uniStr="";
			while(iterator.hasNext()){
				CostLedgerVO costLedgerVO = iterator.next();
				//��VO�л�ȡ��˾��pk_org
				String pk_org = costLedgerVO.getArea();
				//���ݹ�˾��pk_org��regionMap�л�ȡ�����pk_region
				String pk_region = regionMap.get(pk_org);
				
				//add by�ƹڻ� SDYC-56 Ѻ��֤��̨�ˣ��ɱ�����20200818 begin
				pk_org2=costLedgerVO.getPk_org();
				pk_accasoa=costLedgerVO.getPk_accasoa();
				assid=costLedgerVO.getAssid();
//				uniStr=pk_org2+pk_accasoa+assid;
//				if(!lists.contains(uniStr)){
//					useLessVO.add(costLedgerVO);
//				}
				for(Map map:lists){
					if(pk_org2.equals(map.get("pk_org"))&&pk_accasoa.equals(map.get("pk_accasoa"))&&assid.equals(map.get("assid"))){
						costLedgerVO.setBalMny(map.get("balmny")+"");
					}
				}
				//add by�ƹڻ� SDYC-56 Ѻ��֤��̨�ˣ��ɱ�����20200818 end
				//�ж�����PK�Ƿ���ڣ����������Ƴ�
				if(null ==pk_region || pk_region.equals("~")){
					iterator.remove();
				}else{
					//ÿ����һ��Ԫ�أ���ż�1
					++sequeNumGlobal;
					//��VO��д�뵱ǰ���
					costLedgerVO.setSequeNumGlobal(new UFDouble(sequeNumGlobal));
					/**�Ӳ�ѯ���costLedgerVOs�в�����Ӧ������pk��������pk����ѯ��������name��
					 * �������set��costLedgerVO�����Ҵ���region_nameMap�У�
					�����ظ���ѯ�������ƣ������˾��pk_org���ܶ�Ӧһ������pk��*/
						if(region_nameMap.get(pk_region) != null){
							costLedgerVO.setArea(region_nameMap.get(pk_region));
						}else{
							String sql_region = "select name,pk_org from org_orgs where org_orgs.dr = 0 and pk_org = '"+pk_region+"'";
							Map<String, String> value = (Map<String, String>) getBaseDAO()
									.executeQuery(sql_region, new MapProcessor());
							costLedgerVO.setArea(value.get("name"));
							region_nameMap.put(value.get("pk_org"), value.get("name"));
						}
				}
			}
			for(CostLedgerVO vo:costLedgerVOs){
				if(null==vo.getBalMny()){
					useLessVO.add(vo);
				}
			}
			if(useLessVO!=null&&useLessVO.size()>0){
				costLedgerVOs.removeAll(useLessVO);
			}
			//add by  �ƹڻ� SDYC-56 Ѻ��֤��̨�ˣ��ɱ����� ����ڳ����� 20200824 begin
			if(costLedgerVOs!=null&&costLedgerVOs.size()>0){
				List<CostLedgerVO> initVOs=getInitDetails();
				if(initVOs!=null&&initVOs.size()>0){
					initVOs.addAll(costLedgerVOs);
					costLedgerVOs=initVOs;		
				}
				sequeNumGlobal=0;
				for(CostLedgerVO costLedgerVO:costLedgerVOs){
					++sequeNumGlobal;
					costLedgerVO.setSequeNumGlobal(new UFDouble(sequeNumGlobal));
				}
			}
			//add by  �ƹڻ� SDYC-56 Ѻ��֤��̨�ˣ��ɱ����� ����ڳ����� 20200824 end
		}
		return costLedgerVOs;
	}
	
	/**
	 * @author �ƹڻ�
	 * ��ѯ����ǰ����
	 * 20200824
	 * @throws DAOException 
	 */
	private List<CostLedgerVO> getInitDetails() throws DAOException{
		List<CostLedgerVO> initVOs =null;
		String documentationDate_begin = queryValueMap.get("documentationDate_begin");
		String documentationDate_end = queryValueMap.get("documentationDate_end");
		String area = queryValueMap.get("area");
		String travellingTrader = queryValueMap.get("travellingTrader");
		String companyName = queryValueMap.get("companyName");
		String transactionPerson = queryValueMap.get("transactionPerson");
		String receiptNum = queryValueMap.get("receiptNum");
		String contractCode = queryValueMap.get("contractCode");
		String outBillNo = queryValueMap.get("outBillNo");
		String tradeType = queryValueMap.get("tradeType");
		StringBuffer sb=new StringBuffer();
		//ȡ��ϵͳ�ڳ�
		sb.append(" SELECT '' pkVoucher, ");                                                                        //ƾ֤����                   
		sb.append(" zb_3 as area,                                                                                ");//����                     
		sb.append("    zb_4 as companyName,                                                                      ");//��˾����                   
		sb.append(" zb_7 as accountingSubject,                                                                   ");//��ƿ�Ŀ                   
		sb.append(" zb_11 as travellingTrader,                                                                   ");//����                     
		sb.append(" zb_1 as voucherAbstract,                                                                     ");//��֤���Ѻ�����ݣ�ƾ֤ժҪ��         
		sb.append(" zb_18 as paymentAmount,                                                                      ");//֧�����                   
		sb.append(" zb_19 as paymentDate,                                                                        ");//֧������                   
		sb.append(" '' as proofMaker,                                                                            ");//ƾ֤�Ƶ���                  
		sb.append(" zb_20 as documentationDate,                                                                  ");//�Ƶ�����                   
		sb.append(" zb_14 as proofNum,                                                                           ");//ƾ֤��                    
	    sb.append("    zb_9 as transactionDepartment,                                                            ");//���첿��                   
	    sb.append("    zb_10 as transactionPerson,                                                               ");//�����ˣ��˿������ˣ�             
	    sb.append("    zb_16 as refundDate,                                                                      ");//�˿����                  
	    sb.append("    zb_13 as sourceSystem,                                                                    ");//��Դϵͳ                   
	    sb.append("    '' as tradeType,                                                                          ");//��������                   
	    sb.append("    '' as receiptNum,                                                                         ");//���ݱ��                   
	    sb.append("    '' as imageCode,                                                                          ");//Ӱ�����                   
	    sb.append("    '' as contractCode,                                                                       ");//��ͬ����                   
	    sb.append("    '' as contractName,                                                                       ");//��ͬ����                   
	    sb.append("    '' as outBillNo,                                                                          ");//��ϵͳ���ݺ�                 
	    sb.append("    '' as sourceRemark,                                                                       ");//��Դ��ע                   
	    sb.append("    '' pk_org,                                                                                ");//��֯pk                   
	    sb.append("    '' pk_accasoa,                                                                            ");//��Ŀ                     
	    sb.append("    '' assid,                                                                                 ");//��Դ��ע                   
	    sb.append("    'Y' isneed                                                                                ");
		sb.append(" FROM (SELECT pubdata_table.alone_id              alone_id,                                 ");
		sb.append("              org_table.code                      org_code,                                 ");
		sb.append("              org_table.name                      org_name,                                 ");
		sb.append("              org_table.pk_reportorg              pk_org,                                   ");
		sb.append("              org_table.pk_group                  pk_group,                                 ");
		sb.append("              pubdata_table.keyword1              key1,                                     ");
		sb.append("              pubdata_table.keyword2              key2,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10008   zb_1,                                     ");
		sb.append("              iufo_measure_data_jgldhtdh.m10001   zb_2,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10012   zb_3,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10011   zb_4,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10015   zb_5,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10014   zb_6,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10010   zb_7,                                     ");
		sb.append("              iufo_measure_data_jgldhtdh.m10002   zb_8,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10002   zb_9,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10001   zb_10,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.m10009   zb_11,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.m10013   zb_12,                                    ");
		sb.append("              iufo_measure_data_jgldhtdh.m10003   zb_13,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.m10003   zb_14,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.m10005   zb_15,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.m10000   zb_16,                                    ");
		sb.append("              iufo_measure_data_jgldhtdh.m10000   zb_17,                                    ");
		sb.append("              iufo_measure_data_jgldhtdh.mn6ik8a  zb_18,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.m10006   zb_19,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.m10004   zb_20,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.alone_id alone_id1,                                ");
		sb.append("              iufo_measure_data_jgldhtdh.alone_id alone_id2,                                ");
		sb.append("              pubdata_table.ver                   ver                                       ");
		sb.append("         FROM iufo_measpub_GS7T pubdata_table                                               ");
		sb.append("         LEFT OUTER JOIN iufo_measure_data_80kfvpy2 ON pubdata_table.alone_id =             ");
		sb.append("                                                       iufo_measure_data_80kfvpy2.alone_id  ");
		sb.append("         LEFT OUTER JOIN iufo_measure_data_jgldhtdh ON pubdata_table.alone_id =             ");
		sb.append("                                                       iufo_measure_data_jgldhtdh.alone_id  ");
		sb.append("        INNER JOIN org_reportorg org_table ON pubdata_table.keyword1 =                      ");
		sb.append("                                              org_table.pk_reportorg                        ");
		sb.append("        WHERE (iufo_measure_data_80kfvpy2.alone_id IS NOT NULL OR                           ");
		sb.append("              iufo_measure_data_jgldhtdh.alone_id IS NOT NULL)             ");
		//��Ӳ�ѯ��������֯pk
		if(companyName != null){
			sb.append(" and "
					+ SQLUtil.buildSqlForIn(
							" pubdata_table.keyword2 ",
							companyName.split(",")));
		}
		//��Ӳ�ѯ�������Ƶ���ʼ����
		if(documentationDate_begin != null){
			sb.append("and iufo_measure_data_80kfvpy2.m10006 >= '"+documentationDate_begin+"'  ");
		}
		//��Ӳ�ѯ�������Ƶ���������
		if(documentationDate_end != null){
			sb.append("and iufo_measure_data_80kfvpy2.m10006 <= '"+documentationDate_end+"'  ");
		}		
		//�ڳ����ݺܶ�����Ϊ�ջ���һЩ���ݲ��淶��������֯��������ѯ������������ѯ������Ϊ�յ��������ʾ�ڳ�����
		if(travellingTrader != null||transactionPerson!=null||receiptNum != null
				||contractCode != null||outBillNo != null||tradeType != null){
			sb.append(" and 1=2 ");
		}
		sb.append(" ) repdataprovider ");
		initVOs = (List<CostLedgerVO>)getBaseDAO().executeQuery(sb.toString(), new BeanListProcessor(CostLedgerVO.class));
		return initVOs;
	}
	
	
	/**@author �ƹڻ�
	 * ��ѯ��������֯+��Ŀ
	 * 20200810
	 */
	private  List<Map<String,Object>> getAviBalOrg(String companyName) throws BusinessException {
		String[] accArr={"12210201","12210202","22410401","22410402"};
		StringBuffer sb=new StringBuffer();
		String endDate = queryValueMap.get("endDate_end");
		if(StringUtils.isBlank(endDate)){
			endDate=AppContext.getInstance().getServerTime().toLocalString();
		}
		String where_org="";
		if (StringUtils.isNotBlank(companyName)) {// ������֯
			String[] orgstr = companyName.split(",");
			where_org=("and " + SQLUtil.buildSqlForIn("gl_balan.pk_org", orgstr));
		}
		String where_acc=("and " + SQLUtil.buildSqlForIn("account.code", accArr));
		String year=endDate.substring(0,4);
		String begMth="00";
		String endMth=endDate.substring(5, 7);
		sb.append("select pk_org,pk_accasoa,assid,balmny from ( ");
		sb.append("select sum(debitinitsum)-sum(creditinitsum)+sum(debitamountsum)-sum(creditamountsum) balmny,pk_org,pk_accasoa,assid from ( ");
	    sb.append("select                                                                                                  ");
	    sb.append("distinct                                                                                               ");
	    sb.append("         sum(gl_balan.debitamount) debitinitsum,                                                        ");
	    sb.append("         sum(gl_balan.creditamount ) creditinitsum,                                                   ");
	    sb.append("         0 debitamountsum,                                                                               ");
	    sb.append("         0 creditamountsum,                                                                              ");
	    sb.append("         gl_balan.pk_org , gl_balan.pk_accasoa,gl_balan.assid                                                                                                ");
	    sb.append("  from gl_balance gl_balan                                                                              ");
	    sb.append("  left join bd_accasoa accoa on accoa.pk_accasoa=gl_balan.pk_accasoa ");
	    sb.append("  left join bd_account account on accoa.pk_account=account.pk_account ");
	    sb.append(" where  gl_balan.year = '"+year+"' " )  ;  
	    if (StringUtils.isNotBlank(where_org)) {// ������֯
	    	sb.append(where_org);
		}                                                                       
	    sb.append("   and gl_balan.adjustperiod >= '00'                                                                     ");
	    sb.append("   and gl_balan.adjustperiod < '01'                                                                      ");
	    sb.append("   and gl_balan.voucherkind <> 5                                                                         ");
	    sb.append(where_acc);
	    sb.append(" group by gl_balan.pk_org,gl_balan.pk_accasoa,gl_balan.assid                                                                                          ");
	    sb.append(" union                                                                                                   ");
	    sb.append("select                                                                                                  ");
	    sb.append("distinct                                                                                                 ");
	    sb.append("         0 debitinitsum,                                                                                 ");
	    sb.append("         0 creditinitsum,                                                                                ");
	    sb.append("         sum(gl_balan.debitamount) debitamountsum,                                                       ");
	    sb.append("         sum(gl_balan.creditamount ) creditamountsum,                                                    ");
	    sb.append("         gl_balan.pk_org, gl_balan.pk_accasoa,gl_balan.assid                                                                                 ");
	    sb.append("  from gl_balance gl_balan                                                                              ");
	    sb.append("  left join bd_accasoa accoa on accoa.pk_accasoa=gl_balan.pk_accasoa                                                                              ");
	    sb.append("  left join bd_account account on accoa.pk_account=account.pk_account ");
	    sb.append(" where  gl_balan.year = '"+year+"' " )  ;  
	    if (StringUtils.isNotBlank(where_org)) {// ������֯
	    	sb.append(where_org);
		}  
	    sb.append("   and gl_balan.adjustperiod >= '01'                                                                     ");
	    sb.append("   and gl_balan.adjustperiod <= '"+endMth+"'                                                                    ");
	    sb.append("   and gl_balan.voucherkind <> 5                                                                         ");
	    sb.append(where_acc);
	    sb.append(" group by gl_balan.pk_org, gl_balan.pk_accasoa,gl_balan.assid )                                                                               ");
	    sb.append("group by pk_org,pk_accasoa,assid                                       ");
	    sb.append(") where balmny<>0 ");
	    List<Map<String,Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sb.toString(), new MapListProcessor());
		return list;
		
	}

	/**
	 * ���ʱ����˾��μ���VO,��ҵ��˾���ڵ�����
	 * @return
	 * @throws BusinessException
	 */
	private List<ReportOrgVO> getOrgVOs() throws BusinessException{
		ReportConts reportUtils = ReportConts.getUtils();
		String pk_group = reportUtils.getorg("JT0001");// ��������
		Map<String, String> fatherMap = reportUtils.getFinanecMap();//��ȡҵ��Ԫ����Ӧ�ĸ�����Ԫ
		List<String> regionList = reportUtils.getRegionOrgList();// �������˾
		Map<String, List<String>> orgMap = reportUtils.getSecondaryOrgList(regionList);// ��Ŀ��˾��Ϣ
		ReportOrgVO orgVO = new ReportOrgVO();
		orgVO.setPk_clique(pk_group);
		List<ReportOrgVO> volist = new ArrayList<ReportOrgVO>();
		for (String regionOrg : regionList) {
			String pk_plate = fatherMap.get(regionOrg);
			if(pk_plate != null){
				orgVO = (ReportOrgVO) orgVO.clone();
				orgVO.setPk_plate(pk_plate.equals(pk_group) ? regionOrg
						: pk_plate);// ��鹫˾
				orgVO.setPk_region(regionOrg.equals(orgVO.getPk_plate()) ? "~"
						: regionOrg);// ����˾
				List<String> orgList = orgMap.get(regionOrg);
				if (orgList != null && orgList.size() > 0) {
					for (String org : orgList) {
						ReportOrgVO repOrgVO = (ReportOrgVO) orgVO.clone();
						repOrgVO.setPk_org(org);// ��Ŀ��˾
						volist.add(repOrgVO);
					}
				} else {
					ReportOrgVO repOrgVO = (ReportOrgVO) orgVO.clone();
					repOrgVO.setPk_org(regionOrg);// ��Ŀ��˾
					volist.add(repOrgVO);
				}
			}
		}
		return volist;
	}

}
