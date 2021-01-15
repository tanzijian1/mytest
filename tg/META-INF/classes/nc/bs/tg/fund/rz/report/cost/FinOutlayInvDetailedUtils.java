package nc.bs.tg.fund.rz.report.cost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.cost.FinOutlayInvDetailedVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * �Ŵ����ʷ��÷�Ʊ��ϸ���ɱ�����
 * 
 * @author ASUS
 * 
 */
public class FinOutlayInvDetailedUtils extends ReportUtils {
	static FinOutlayInvDetailedUtils utils;

	public static FinOutlayInvDetailedUtils getUtils() throws BusinessException {
		if (utils == null) {
			utils = new FinOutlayInvDetailedUtils();
		}
		return utils;
	}

	public FinOutlayInvDetailedUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new FinOutlayInvDetailedVO()));
	}

	@Override
	public DataSet getProcessData(IContext context) throws BusinessException {
		CmpReportResultVO[] cmpreportresults = null;

		try {
			ConditionVO[] conditionVOs = (ConditionVO[]) context.getAttribute(FPConst.KEY_QueryScheme);
			if (conditionVOs == null || conditionVOs.length == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			initQuery(conditionVOs);

			List<FinOutlayInvDetailedVO> list = null;
			//��������list��ֵ�ķ���
			list = getFinOutlayInvDetailedVO();
			
			if(list != null && list.size() > 0){
				cmpreportresults = transReportResult(list);
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}
	
	/**
	 * ��ѯ�Ŵ����ʷ��÷�Ʊ��ϸ����
	 * @return FinOutlayInvDetailedVO����
	 * @throws BusinessException
	 */
	public List<FinOutlayInvDetailedVO> getFinOutlayInvDetailedVO() throws BusinessException{
		
		List<FinOutlayInvDetailedVO> allList = getFinOutlayInvDetailed();
		return allList == null || allList.size() == 0 ? new ArrayList<FinOutlayInvDetailedVO>() : allList;
	}

	@SuppressWarnings("unchecked")
	private List<FinOutlayInvDetailedVO> getFinOutlayInvDetailed() throws DAOException{

		String approveDate = null;
		String costtype = null;
		String loanContractNames = null;
		String oweTicketAmount = null;
		
		//��ȡ��ѯģ�崦���õĲ�ѯ����
		String approvedates = queryWhereMap.get("approvedate");//��������
		String costtypes = queryWhereMap.get("costtype");//��������
		String oweTicketAmounts = queryWhereMap.get("oweticketamount");//ǷƱ���
		if(queryWhereMap.get("loancontractname") != null){
			loanContractNames = queryWhereMap.get("loancontractname").replace("loancontractname", "cdm.htmc");//�����ͬ����
		}

		StringBuffer sql = new StringBuffer();
		

		// �ƹ˷�/���ʷ���� tgrz_financexpense�Ĳ�ѯsql ��������Ʊ����ͨ����״̬�������������ʾ
		sql.append(" select tg.billno billno, ").append("\r\n"); // ���ݺ�
		sql.append(" ora.name region, ").append("\r\n"); // ����
		sql.append(" org.name companyName, ").append("\r\n"); // ��˾����
		sql.append(" cdm.htmc loancontractname, ").append("\r\n"); // �����ͬ����
		sql.append(" bank.name loanBank, ").append("\r\n"); // ��������
		sql.append(" gl.prepareddate makebilldate, ").append("\r\n"); // �Ƶ�����
		sql.append(" tg.approvedate approvedate, ").append("\r\n"); // ��������
		sql.append(" gl.num certificateno, ").append("\r\n"); // ƾ֤��
		sql.append(" gl.explanation  digest, ").append("\r\n"); // ƾ֤ժҪ gl.explanation
		sql.append(" byh.billno ticketno, ").append("\r\n");//��Ʊ������
		sql.append(" to_number((case tg.approvestatus when 1 then (case cmph.paystatus when 2 then to_number(cmph.primal_money) else (to_number(tg.applyamount)) end) else 0 end)) as payamount, ").append("\r\n"); // ֧�����
		sql.append(" (select t.billtypename from bd_billtype t where t.pk_billtypecode = tg.transtype and tg.transtype <>'~' ) as costtype, ").append("\r\n"); // ��������
		sql.append(" smu.user_name fundcentermanager, "
				+ "to_char((case when tg.def18 is null and tg.def18 <> '~' then (case tg.def18 when '100112100000000005KE' then '��' else '��' end) else '��' end))  isreceiptinvoicevdef4, "
				+ "to_char((case when tg.def31 is null and tg.def31 <> '~' then (case tg.def31 when '100112100000000005KE' then '��' else '��' end) else '��' end)) isreceiptinvoicedef31,").append("\r\n"); // �ʽ����ľ�����
		sql.append(" (case when tg.def27 is not null and tg.def27 <> '~' and tg.def27 > 0 then '��'  else '��' end) isreceiptinvoice, ").append("\r\n"); // �Ƿ��յ���Ʊ
		// �յ���Ʊ���
		sql.append("  to_number(case when byb.def5 is not null and byb.def5 <> '~' then to_number(byb.def5) else 0 end)+ to_number((case when tg.def27 is not null and tg.def27 <> '~' then tg.def27 else (case when tg.def23 is not null and tg.def23<>'~' then tg.def23 else '0' end) end)) as receiptinvoiceamount, ").append("\r\n");
		// ǷƱ��� =ǷƱ���-�յ���Ʊ���
		sql.append(" to_number((to_number((case tg.approvestatus when 1 then (case cmph.paystatus when 2 then to_number(cmph.primal_money) else (to_number(tg.applyamount)) end) else 0 end)) - to_number(to_number(case when byb.def5 is not null and byb.def5 <> '~' then to_number(byb.def5) else 0 end)+ to_number((case when tg.def27 is not null and tg.def27 <> '~' then tg.def27 else (case when tg.def23 is not null and tg.def23<>'~' then tg.def23 else '0' end) end))))) as oweticketamount,  ").append("\r\n");
		sql.append(" tg.big_text_a accountnote ").append("\r\n"); // ��Ʊ�ע
		sql.append(" from tgrz_financexpense tg ").append("\r\n");
		sql.append(" left join org_orgs org on org.pk_org = tg.pk_org and nvl(org.dr,0) = 0 ").append("\r\n");
		sql.append(" left join org_orgs ora on ora.pk_org = org.pk_fatherorg and nvl(ora.dr,0) = 0 ").append("\r\n");
		sql.append(" left join cdm_contract cdm on cdm.pk_contract = tg.def4 and nvl(cdm.dr,0) = 0 ").append("\r\n");
		sql.append(" left join bd_bankdoc bank on bank.pk_bankdoc = cdm.pk_creditbank and nvl(bank.dr,0) = 0 ").append("\r\n");
		sql.append(" left join cmp_paybill cmph on cmph.pk_upbill = tg.pk_finexpense and nvl(cmph.dr,0) = 0 ").append("\r\n");
		sql.append(" left join cmp_paybilldetail cmpb on cmpb.pk_paybill = cmph.pk_paybill and nvl(cmpb.dr,0) = 0 ").append("\r\n");
		sql.append(" left join fip_relation fip on fip.src_relationid =  cmph.pk_paybill and nvl(fip.dr,0) = 0 ").append("\r\n");
		sql.append(" left join gl_voucher gl on gl.pk_voucher = fip.des_relationid and gl.num is not null and nvl(gl.dr,0) = 0 ").append("\r\n");
		sql.append(" left join bd_billtype bd on bd.pk_billtypeid = tg.transtypepk and nvl(bd.dr,0) = 0 ").append("\r\n");
		sql.append(" left join sm_user smu on smu.cuserid = tg.creator and nvl(smu.dr,0) = 0 ").append("\r\n");
		sql.append(" left join bd_defdoc def on def.pk_defdoc = tg.def5 and nvl(def.dr,0) = 0 "
				+ "  inner join tg_ticketbody byb on byb.def1 = tg.pk_finexpense and nvl(byb.dr,0) = 0 "
				+ "  inner join tg_addticket byh on byh.pk_ticket = byb.pk_ticket and byh.approvestatus =1 and nvl(byh.dr,0) = 0 ").append("\r\n");
		
		sql.append(" where 1 = 1 and tg.dr = 0 and tg.approvestatus = 1 and nvl(tg.dr,0) = 0 ").append("\r\n");
		//����ѯ������Ϊ�գ�����׷��
		//��������
		if(approvedates != null){
			approveDate = approvedates.replace("approvedate", " tg.approvedate ");
			sql.append(approveDate).append("\r\n");
		}
		//��������
		if (costtypes != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("tg.transtype",
							queryValueMap.get("costtype").split(",")));
		}
		//�����ͬ����
		if(queryWhereMap.get("loancontractname") != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("cdm.pk_contract",
							queryValueMap.get("loancontractname").split(",")));
		}
		//ǷƱ���
		if(oweTicketAmounts != null){
			
			String pk= queryValueMap.get("oweticketamount");
			int result1 = pk.indexOf(",");
			String po = pk.substring(0,result1 );
			String pp = pk.substring(result1+1,pk.length()-1);
			if(!"ISNULL".equals(po) ){				
			sql.append(" and to_number((to_number((case tg.approvestatus when 1 then (case cmph.paystatus when 2 then to_number(cmph.primal_money) else (to_number(tg.applyamount)) end) else 0 end)) - to_number(to_number(case when byb.def5 is not null and byb.def5 <> '~' then to_number(byb.def5) else 0 end)+ to_number((case when tg.def27 is not null and tg.def27 <> '~' then tg.def27 else (case when tg.def23 is not null and tg.def23<>'~' then tg.def23 else '0' end) end))))) >="+po+" ").append("\r\n");				
			}
			if(!"ISNULL".equals(pp) ){
				sql.append( "and to_number((to_number((case tg.approvestatus when 1 then (case cmph.paystatus when 2 then to_number(cmph.primal_money) else (to_number(tg.applyamount)) end) else 0 end)) - to_number(to_number(case when byb.def5 is not null and byb.def5 <> '~' then to_number(byb.def5) else 0 end)+ to_number((case when tg.def27 is not null and tg.def27 <> '~' then tg.def27 else (case when tg.def23 is not null and tg.def23<>'~' then tg.def23 else '0' end) end))))) <= "+pp+" ").append("\r\n");
			}
			//sql.append(oweTicketAmounts).append("\r\n");
		}
		// ��˾����
				if (queryValueMap.get("companyName") != null) {
							sql.append(" and "
									+ SQLUtil.buildSqlForIn("tg.pk_org",
											queryValueMap.get("companyName").split(",")));
				}
		
		//union all cdm_repayrcpt��Ĳ�ѯsql
		sql.append(" union all ").append("\r\n");
		sql.append(" select cmd.vbillno billno, ").append("\r\n");
		sql.append(" ora.name region, ").append("\r\n");
		sql.append(" org.name companyName, ").append("\r\n");
		sql.append(" cdm.htmc loancontractname, ").append("\r\n");
		sql.append(" bank.name loanBank, ").append("\r\n");
		sql.append(" gl.prepareddate makebilldate, ").append("\r\n");
		sql.append(" cmd.approvedate approvedate, ").append("\r\n");
		sql.append(" gl.num certificateno, ").append("\r\n");
		sql.append(" gl.explanation  digest, ").append("\r\n"); //gl.explanation 
		sql.append(" byh.billno ticketno, ").append("\r\n");//��Ʊ������
		// �����������״̬Ϊͨ�����鸶����㵥��ȡ������㵥���廹�����def1��ûֵ��ȡ��Ϣ���def2
		//	��û�и�����㵥��ȡ����ı���repayamount����Ϣ���interest
		sql.append("  to_number(case cmph.paystatus when 2 then to_number(cmph.primal_money) "
				+ "else (to_number(case when cmd.interest is not null then cmd.interest "
				+ "else 0 end))  end) as payamount, ").append("\r\n");
		sql.append(" (select t.billtypename from bd_billtype t where t.pk_billtypecode = cmd.pk_billtypecode) as costtype, ").append("\r\n");
		sql.append(" smu.user_name fundcentermanager, "
				+ "to_char((case when cmd.vdef4 is null and cmd.vdef4 <> '~' then (case cmd.vdef4 when '100112100000000005KE' then '��' else '��' end) else '��' end))  isreceiptinvoicevdef4,"
				+ "to_char((case when cmd.def31 is null and cmd.def31 <> '~' then (case cmd.def31 when '100112100000000005KE' then '��' else '��' end) else '��' end)) isreceiptinvoicedef31,").append("\r\n");
		sql.append(" (case when cmd.def22 is not null and cmd.def22 <> '~' and cmd.def22 > 0 then '��' else '��' end) isreceiptinvoice, ").append("\r\n");
		sql.append(" to_number(case when byb.def5 is not null and byb.def5 <> '~' then to_number(byb.def5) else 0 end)+to_number((case when cmd.def22 is not null and cmd.def22 <> '~' then cmd.def22 else '0' end)) receiptinvoiceamount, ").append("\r\n");
		sql.append(" to_number(to_number(case cmph.paystatus when 2 then to_number(cmph.primal_money)  else (to_number(case when cmd.interest is not null then cmd.interest else 0 end)) end) "
				+ "- to_number(to_number(case when byb.def5 is not null and byb.def5 <> '~' then to_number(byb.def5) else 0 end)+to_number((case when cmd.def22 is not null and cmd.def22 <> '~' then cmd.def22 else '0' end)))) as oweticketamount, ").append("\r\n");
		sql.append(" cmd.big_text_a accountnote ").append("\r\n");
		sql.append(" from cdm_repayrcpt cmd ").append("\r\n");
		sql.append(" left join org_orgs org on org.pk_org = cmd.pk_org and nvl(org.dr,0) = 0 " ).append("\r\n");
		sql.append(" left join org_orgs ora on ora.pk_org = org.pk_fatherorg and nvl(ora.dr,0) = 0 ").append("\r\n");
		sql.append(" left join cdm_contract cdm on cdm.pk_contract = cmd.pk_contract and nvl(cdm.dr,0) = 0 ").append("\r\n");
		sql.append(" left join bd_bankdoc bank on bank.pk_bankdoc = cdm.pk_creditbank and nvl(cdm.dr,0) = 0 ").append("\r\n");
		sql.append(" left join cmp_paybill cmph on cmph.pk_upbill = cmd.pk_repayrcpt and nvl(cmph.dr,0) = 0 ").append("\r\n");
		sql.append(" left join cmp_paybilldetail cmpb on cmpb.pk_paybill = cmph.pk_paybill and nvl(cmpb.dr,0) = 0 ").append("\r\n");
		sql.append(" left join fip_relation fip on fip.src_relationid = cmph.pk_paybill and nvl(fip.dr,0) = 0 ").append("\r\n");
		sql.append(" left join gl_voucher gl on gl.pk_voucher = fip.des_relationid and gl.num is not null and nvl(gl.dr,0) = 0 ").append("\r\n");
		sql.append(" left join bd_billtype bd on bd.pk_billtypeid = cmd.pk_billtypeid and nvl(bd.dr,0) = 0 ").append("\r\n");
		sql.append(" left join sm_user smu on smu.cuserid = cmd.creator and nvl(smu.dr,0) = 0 "
				+ " inner join tg_ticketbody byb on byb.def1 = cmd.pk_repayrcpt and nvl(byb.dr,0) = 0 "
				+ " inner join tg_addticket byh on byh.pk_ticket = byb.pk_ticket and byh.approvestatus =1 and nvl(byh.dr,0) = 0 ").append("\r\n");
		sql.append(" where 1 = 1 and cmd.dr = 0 and cmd.vbillstatus = 1 and nvl(cmd.dr,0) = 0 ").append("\r\n");
		//����ѯ������Ϊ�գ�����׷��
		//��������
		if(approvedates != null){
			approveDate = approvedates.replace("approvedate", " cmd.approvedate ");
			sql.append(approveDate).append("\r\n");
		}
		//��������
		if (costtypes != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("cmd.pk_billtypecode",
							queryValueMap.get("costtype").split(",")));
		}
		//����ͬ����
		if(queryWhereMap.get("loancontractname") != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("cdm.pk_contract",
							queryValueMap.get("loancontractname").split(",")));
		}
		//ǷƱ���
		if(oweTicketAmounts != null){
			String pk= queryValueMap.get("oweticketamount");
			int result1 = pk.indexOf(",");
			String po = pk.substring(0,result1 );
			String pp = pk.substring(result1+1,pk.length()-1);
			if(!"ISNULL".equals(po) ){
			sql.append(" and to_number((to_number((case cmd.vbillstatus when 1 then (case cmph.paystatus when 2 then to_number(cmph.primal_money) else (to_number(case when cmd.interest is not null then cmd.interest else to_number(case when cmd.repayamount is not null then cmd.repayamount else 0 end) end) - to_number(case when cmd.repayamount is not null then cmd.repayamount else 0 end) ) end) else (to_number(case when cmd.interest is not null then cmd.interest else to_number(case when cmd.repayamount is not null then cmd.repayamount else 0 end) end)+to_number(case when cmd.repayamount is not null then cmd.repayamount else 0 end) ) end)) - to_number(to_number(case when byb.def5 is not null and byb.def5 <> '~' then to_number(byb.def5) else 0 end)+to_number((case when cmd.def22 is not null and cmd.def22 <> '~' then cmd.def22 else '0' end))))) >= "+po+" ").append("\r\n");
			}
			if(!"ISNULL".equals(pp) ){
				sql.append(" and to_number((to_number((case cmd.vbillstatus when 1 then (case cmph.paystatus when 2 then to_number(cmph.primal_money) else (to_number(case when cmd.interest is not null then cmd.interest else to_number(case when cmd.repayamount is not null then cmd.repayamount else 0 end) end) - to_number(case when cmd.repayamount is not null then cmd.repayamount else 0 end) ) end) else (to_number(case when cmd.interest is not null then cmd.interest else to_number(case when cmd.repayamount is not null then cmd.repayamount else 0 end) end)+to_number(case when cmd.repayamount is not null then cmd.repayamount else 0 end) ) end)) - to_number(to_number(case when byb.def5 is not null and byb.def5 <> '~' then to_number(byb.def5) else 0 end)+to_number((case when cmd.def22 is not null and cmd.def22 <> '~' then cmd.def22 else '0' end))))) <= "+pp+" ").append("\r\n");	
			}
		}
		// ��˾����
		if (queryValueMap.get("companyName") != null) {
						sql.append(" and "
								+ SQLUtil.buildSqlForIn("cmd.pk_org",
										queryValueMap.get("companyName").split(",")));
		}
//		//union all ��Ʊ�� tg_addticket��Ĳ�ѯsql
//		sql.append(" union all ").append("\r\n");
//		sql.append(" select tga.billno billno, ").append("\r\n");
//		sql.append(" ora.name region, ").append("\r\n");
//		sql.append(" org.name companyName, ").append("\r\n");
//		sql.append(" cdm.htmc loancontractname, ").append("\r\n");
//		sql.append(" bank.name loanBank, ").append("\r\n");
//		sql.append(" gl.prepareddate makebilldate, ").append("\r\n");
//		sql.append(" tga.approvedate approvedate, ").append("\r\n");
//		sql.append(" gl.num certificateno, ").append("\r\n");
//		sql.append(" gl.explanation  digest, ").append("\r\n");//gl.explanation 
//		sql.append(" to_number(0) as payamount, ").append("\r\n");
//		sql.append(" (select t.billtypename from bd_billtype t where t.pk_billtypecode = tga.billtype) as costtype, ").append("\r\n");
//		sql.append(" smu.user_name fundcentermanager, ").append("\r\n");
//		sql.append("  to_char('') isreceiptinvoicevdef4, to_char('') isreceiptinvoicedef31,'��' isreceiptinvoice, ").append("\r\n");
//		sql.append(" to_number((case when tga.def10 is not null and tga.def10 <> '~' then tga.def10 else (case when tga.def37 is not null and tga.def37 <> '~' then tga.def37 else '0' end) end)) receiptinvoiceamount, ").append("\r\n");
//		sql.append(" to_number((to_number(0)-to_number((case when tga.def10 is not null and tga.def10 <> '~' then tga.def10 else (case when tga.def37 is not null and tga.def37 <> '~' then tga.def37 else '0' end) end)))) oweticketamount, ").append("\r\n");
//		sql.append(" tga.big_text_b accountnote ").append("\r\n");
//		sql.append(" from tg_addticket tga ").append("\r\n");
//		sql.append(" left join org_orgs org on org.pk_org = tga.pk_org ").append("\r\n");
//		sql.append(" left join org_orgs ora on ora.pk_org = org.pk_fatherorg ").append("\r\n");
//		sql.append(" left join cdm_contract cdm on cdm.pk_contract = tga.def2 ").append("\r\n");
//		sql.append(" left join bd_bankdoc bank on bank.pk_bankdoc = cdm.pk_creditbank ").append("\r\n");
//		sql.append(" left join cmp_paybill cmph on cmph.pk_upbill = tga.pk_ticket ").append("\r\n");
//		sql.append(" left join cmp_paybilldetail cmpb on cmpb.pk_paybill = cmph.pk_paybill ").append("\r\n");
//		sql.append(" left join fip_relation fip on fip.src_relationid = cmph.pk_paybill ").append("\r\n");
//		sql.append(" left join gl_voucher gl on gl.pk_voucher = fip.des_relationid and gl.num is not null ").append("\r\n");
//		sql.append(" left join bd_billtype bd on bd.pk_billtypeid = tga.transtypepk ").append("\r\n");
//		sql.append(" left join sm_user smu on smu.cuserid = tga.creator ").append("\r\n");
//		//��ӹ����������߼�ɾ��
//		sql.append(" where tga.dr = '0' and tga.approvestatus = 1 ").append("\r\n");
//
//		//����ѯ������Ϊ�գ�����׷��
//		//��������
//		if(approvedates != null){
//			approveDate = approvedates.replace("approvedate", " tga.approvedate ");
//			sql.append(approveDate).append("\r\n");
//		}
//		//��������
//		if (costtypes != null) {
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn("tga.billtype",
//							queryValueMap.get("costtype").split(",")));
//		}
		//ǷƱ���
//				if(oweTicketAmounts != null){
//					String pk= queryValueMap.get("oweticketamount");
//					int result1 = pk.indexOf(",");
//					String po = pk.substring(0,result1 );
//					String pp = pk.substring(result1+1,pk.length()-1);
//					if(!"ISNULL".equals(po) ){
//					sql.append(" and to_number((to_number(0)-to_number((case when tga.def10 is not null and tga.def10 <> '~' then tga.def10 else (case when tga.def37 is not null and tga.def37 <> '~' then tga.def37 else '0' end) end)))) >= "+po+" ").append("\r\n");									
//					}
//					if(!"ISNULL".equals(pp) ){
//						sql.append( " and to_number((to_number(0)-to_number((case when tga.def10 is not null and tga.def10 <> '~' then tga.def10 else (case when tga.def37 is not null and tga.def37 <> '~' then tga.def37 else '0' end) end)))) <= "+pp+" ").append("\r\n");
//					}
//				}
//		//�����ͬ����
//		if(queryWhereMap.get("loancontractname") != null){
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn("cdm.pk_contract",
//							queryValueMap.get("loancontractname").split(",")));
//		}
//		// ��˾����
//		if (queryValueMap.get("companyName") != null) {
//					sql.append(" and "
//							+ SQLUtil.buildSqlForIn("tga.pk_org",
//									queryValueMap.get("companyName").split(",")));
//		}
		//���÷�װ��API�������ݿ��ѯ����ȡ��������Ĳ�ѯ���
		List<FinOutlayInvDetailedVO> listVO = (List<FinOutlayInvDetailedVO>) getBaseDAO().executeQuery(sql.toString(), new BeanListProcessor(FinOutlayInvDetailedVO.class));
		for(FinOutlayInvDetailedVO vo:listVO){
//			vo.setOweticketamount(vo.getPayamount().sub(vo.getReceiptinvoiceamount()));//ǷƱ���
			//����ֵ˰��Ʊ�ܽ�� ��ֵ��ʱ�� �Ƿ����յ���Ʊ ��Ĭ����    �� Ȼ���������ֶ� û��ֵ  �ͻ��ǰ���֮ǰ���ж�ȥ�ж�:4����������Ƿ��з�Ʊ���ǣ��Ƿ��ȸ����Ʊ�����Ƿ����յ���Ʊ����3����������Ƿ��з�Ʊ����  �Ƿ����յ���Ʊ���� 2����������Ƿ��з�Ʊ���Ƿ��ȸ����Ʊ ��Ϊ��  �Ƿ����յ���Ʊ--�� 1�� ��������Ƿ��з�Ʊ����   �Ƿ��ȸ����Ʊ����  �Ƿ����յ���Ʊ--��
			if("��".equals(vo.getIsreceiptinvoice()) && vo.getIsreceiptinvoice() != null){
				vo.setIsreceiptinvoice("��");
			}else{
				if(vo.getIsreceiptinvoicedef31() != null && vo.getIsreceiptinvoicevdef4() != null){
					if("��".equals(vo.getIsreceiptinvoicedef31()) && "��".equals(vo.getIsreceiptinvoicevdef4())){
						vo.setIsreceiptinvoice("��");
					}else{
						vo.setIsreceiptinvoice("��");
					}
				}else{
					vo.setIsreceiptinvoice("��");
				}
			}
			Double ppu = vo.getReceiptinvoiceamount().toDouble();
			if(ppu>0){
				vo.setIsreceiptinvoice("��");
			}
		}
		//ǷƱ����߼�  ������ - �ۼ��յ���Ʊ��ͬһ���ݺţ�add by huangxj 2020��9��16��
		List<String> billnos = new ArrayList<String>();
		for (FinOutlayInvDetailedVO vos : listVO) {
			
			if(billnos.indexOf(vos.getBillno()) == -1){
				billnos.add(vos.getBillno());
			}
		}
		Map<String, UFDouble> mnyMap = new HashMap<String, UFDouble>();
		for (String billno : billnos) {
			UFDouble summny = new UFDouble(0);
			for (FinOutlayInvDetailedVO vos : listVO) {
				if(billno.equals(vos.getBillno())){
					summny = summny.add(vos.getReceiptinvoiceamount());
				}
			}
			mnyMap.put(billno, summny);
		}
		
		for (FinOutlayInvDetailedVO list : listVO) {
			list.setOweticketamount(list.getPayamount().sub(mnyMap.get(list.getBillno())));
		}
		//end
		
		return listVO;
	}	
}