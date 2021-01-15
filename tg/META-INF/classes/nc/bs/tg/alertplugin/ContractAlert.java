package nc.bs.tg.alertplugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.generator.IdGenerator;
import nc.jdbc.framework.generator.SequenceGenerator;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.timesgroup.yer.ebs.synchro.ContractVO;
import nc.vo.tg.contractfiles.ContractFile;
import net.sf.mpxj.utility.Sequence;

/**
 * ��ͬͬ��Ԥ��
 * 
 * @author lyq
 * 
 */
public class ContractAlert implements IBackgroundWorkPlugin {
	IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
	BaseDAO dao = new BaseDAO();

	@Override
	public PreAlertObject executeTask(BgWorkingContext arg0)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		// Ԥ����Ϣ����
		List<Object[]> msglist = new ArrayList<Object[]>();

		// ����pk
		IdGenerator id = new SequenceGenerator();
		String pk_defdoc = null;
		// ��ȡ�ͻ��˼���
		String pk_group = AppContext.getInstance().getPkGroup();

//		String sql = "select * from commoncontract_to_nc_v@APPSTEST ";// commoncontract_to_nc_v
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT fconid,fnumber,fcontractmoney,fname,invoice_type,invoice_type_name,tax_rate,");
		sb.append("       tax_rate_code,fcontracttype,fcontracttype_name,organization_id,org_name,ncid,");
		sb.append("       vender1_id,vender1_number,vender1_name,vender2_id,vender2_number,vender2_name,");
		sb.append("       vender3_id,vender3_number,vender3_name,vender4_id,vender4_number,vender4_name,");
		sb.append("       foperatecorp,foperatecorp_name,foperateadmin,foperateadmin_code,foperateadmin_name,");
		sb.append("       foperator,user_name,fapplyamount,fpay_amount,sum_boud_amount,contract_Operator,");
		sb.append("       contract_User_Name,contract_Foperateadmin,Contract_Foperatecorp,");
		sb.append("       Contract_Foperateadmin_Name,Contract_Foperatecorp_Name,contracted_amount,budgets_amount,supplementary_amount,sum_dynamic_amount,Sum_Invoice");
		sb.append(" from commoncontract_to_nc_v@APPSTEST;");
		String sql = sb.toString();
		List<ContractVO> list = (List<ContractVO>) bs.executeQuery(sql,
				new BeanListProcessor(ContractVO.class));

		sql = "select pk_defdoclist from bd_defdoclist where name='��ͬ'";
		Object pk_defdoclist = bs.executeQuery(sql, new ColumnProcessor());
		// �����������
		// sql =
		// "delete from bd_defdoc where pk_defdoclist='"+pk_defdoclist+"'";
		// dao.executeUpdate(sql);
		// List<DefdocVO> deflist=new ArrayList<DefdocVO>();
		// Map<String, String> cusmap = new HashMap<>();
		for (ContractVO contractVO : list) {
			List<String> cuslist = new ArrayList<>();
			if (contractVO.getVender1_number() != null
					&& contractVO.getVender1_name() != null
					&& contractVO.getOrg_name() != null) {
				if (!contractVO.getOrg_name().equals(
						contractVO.getVender1_name())) {
					cuslist.add(contractVO.getVender1_number() + ","
							+ contractVO.getVender1_name());
				}
			}
			if (contractVO.getVender2_number() != null
					&& contractVO.getVender2_name() != null
					&& contractVO.getOrg_name() != null) {
				if (!contractVO.getOrg_name().equals(
						contractVO.getVender2_name())) {
					cuslist.add(contractVO.getVender2_number() + ","
							+ contractVO.getVender2_name());
				}
			}
			if (contractVO.getVender3_number() != null
					&& contractVO.getVender3_name() != null
					&& contractVO.getOrg_name() != null) {
				if (!contractVO.getOrg_name().equals(
						contractVO.getVender3_name())) {
					cuslist.add(contractVO.getVender3_number() + ","
							+ contractVO.getVender3_name());
				}
			}
			if (contractVO.getVender4_number() != null
					&& contractVO.getVender4_name() != null
					&& contractVO.getOrg_name() != null) {
				if (!contractVO.getOrg_name().equals(
						contractVO.getVender4_name())) {
					cuslist.add(contractVO.getVender4_number() + ","
							+ contractVO.getVender4_name());
				}
			}

			List<ContractFile> exitVO = queryNcExit(contractVO.getFnumber());
			if (exitVO != null && exitVO.size() > 0) {
				for (int i = 0; i < exitVO.size(); i++) {
					exitVO.get(i).setCode(contractVO.getFnumber());
					exitVO.get(i).setName(contractVO.getFname());
					exitVO.get(i).setDef1(contractVO.getInvoice_type());// ��Ʊ����
					exitVO.get(i).setDef2(contractVO.getTax_rate_code());// ˰��
					exitVO.get(i).setDef3(contractVO.getFpay_amount());// �Ѹ���ۼ�֧����
					exitVO.get(i).setDef4(contractVO.getFapplyamount());// �������
					exitVO.get(i).setDef5(contractVO.getOrganization_id());// ���˹�˾id
					exitVO.get(i).setDef6(contractVO.getInvoice_type_name());// ��ͬ��Ʊ��������
					exitVO.get(i).setDef7(contractVO.getNcid());// ����
					exitVO.get(i).setDef8(contractVO.getFconid());// ��ͬid
					exitVO.get(i).setDef11(contractVO.getFcontractmoney());// ��ͬ���
					exitVO.get(i).setDef12(contractVO.getFoperateadmin());// ���첿��id
					exitVO.get(i).setDef13(contractVO.getFcontracttype());// ��ͬ����
					exitVO.get(i).setDef14(contractVO.getFcontracttype_name());// ��ͬ������
					
					//TODO addBy ln 2020��1��8��11:31:26
					exitVO.get(i).setDef16(contractVO.getSum_boud_amount());// ��֤��/Ѻ��/�����/�����ʽ�
					exitVO.get(i).setDef17(contractVO.getContracted_amount());// ǩԼ���
					exitVO.get(i).setDef18(contractVO.getBudgets_amount());// ��ռԤ��(����Ԥ����)
					exitVO.get(i).setDef19(contractVO.getSupplementary_amount());// ����Э����
					exitVO.get(i).setDef20(contractVO.getSum_dynamic_amount());// ��̬���
					exitVO.get(i).setDef21(contractVO.getContract_foperateadmin());// ��ͬ�����˲���ID
					exitVO.get(i).setDef22(contractVO.getSum_invoice());// �ۼƷ�Ʊ���
					// if(){
					//
					// }
					if (cuslist != null && cuslist.size() > i) {
						String[] cust = cuslist.get(i).split(",");
						exitVO.get(i).setDef9(cust[0]);// ��Ӧ�̱���
						exitVO.get(i).setDef10(cust[1]);// ��Ӧ������
						exitVO.get(i).setStatus(VOStatus.UPDATED);
						exitVO.get(i).setEnablestate("2");
						dao.updateVO(exitVO.get(i));
						// } else if(cuslist != null && cuslist.size() > i){
						// exitVO.get(i).setStatus(VOStatus.UPDATED);
						// exitVO.get(i).setEnablestate(3);
						// dao.updateVO(exitVO.get(i));
						// }
					} else {
						exitVO.get(i).setStatus(VOStatus.UPDATED);
						exitVO.get(i).setEnablestate("3");
						dao.updateVO(exitVO.get(i));
					}
				}

				// vo.setEnablestate(2);
				// vo.setStatus(VOStatus.UPDATED);
				// dao.updateVO(vo);
			} else {
				try {
					ContractFile vo = new ContractFile();
					vo.setCode(contractVO.getFnumber());
					vo.setName(contractVO.getFname());
					vo.setDef1(contractVO.getInvoice_type());// ��Ʊ����
					vo.setDef2(contractVO.getTax_rate_code());// ˰��
					vo.setDef3(contractVO.getFpay_amount());// �Ѹ���ۼ�֧����
					vo.setDef4(contractVO.getFapplyamount());// �������
					vo.setDef5(contractVO.getOrganization_id());// ���˹�˾id
					vo.setDef6(contractVO.getInvoice_type_name());// ��ͬ��Ʊ��������
					vo.setDef7(contractVO.getNcid());// ����
					vo.setDef8(contractVO.getFconid());// ��ͬid
					
					UFDouble fcontractmoney = new UFDouble(contractVO.getFcontractmoney());
					vo.setDef11(fcontractmoney.toString());// ��ͬ���
					
					vo.setDef12(contractVO.getFoperateadmin());// ���첿��id
					vo.setDef13(contractVO.getFcontracttype());// ��ͬ����
					vo.setDef14(contractVO.getFcontracttype_name());// ��ͬ������
					
					vo.setDef16(contractVO.getSum_boud_amount());// ��֤��/Ѻ��/�����/�����ʽ�
					vo.setDef17(contractVO.getContracted_amount());// ǩԼ���
					vo.setDef18(contractVO.getBudgets_amount());// ��ռԤ��(����Ԥ����)
					vo.setDef19(contractVO.getSupplementary_amount());// ����Э����
					vo.setDef20(contractVO.getSum_dynamic_amount());// ��̬���
					vo.setDef21(contractVO.getContract_foperateadmin());// ��ͬ�����˲���ID
					vo.setDef22(contractVO.getSum_invoice());// �ۼƷ�Ʊ���
					
					vo.setPk_org(pk_group);
					vo.setPk_group(pk_group);
					vo.setEnablestate("2");
					vo.setPk_defdoclist((String) pk_defdoclist);
					if (contractVO.getVender1_number() != null
							&& contractVO.getVender1_name() != null
							&& contractVO.getOrg_name() != null
							&& !contractVO.getOrg_name().equals(
									contractVO.getVender1_name())) {
						vo.setDef9(contractVO.getVender1_number());// ��Ӧ��2����
						vo.setDef10(contractVO.getVender1_name());// ��Ӧ������
						dao.insertVO(vo);
					} else if (contractVO.getVender2_number() != null
							&& contractVO.getVender2_name() != null
							&& contractVO.getOrg_name() != null
							&& !contractVO.getOrg_name().equals(
									contractVO.getVender2_name())) {
						vo.setDef9(contractVO.getVender2_number());// ��Ӧ��2����
						vo.setDef10(contractVO.getVender2_name());// ��Ӧ������
						dao.insertVO(vo);
					} else if (contractVO.getVender3_number() != null
							&& contractVO.getVender3_name() != null
							&& contractVO.getOrg_name() != null
							&& !contractVO.getOrg_name().equals(
									contractVO.getVender3_name())) {
						vo.setDef9(contractVO.getVender3_number());// ��Ӧ��2����
						vo.setDef10(contractVO.getVender3_name());// ��Ӧ������
						dao.insertVO(vo);
					} else if (contractVO.getVender4_number() != null
							&& contractVO.getVender4_name() != null
							&& contractVO.getOrg_name() != null
							&& !contractVO.getOrg_name().equals(
									contractVO.getVender4_name())) {
						vo.setDef9(contractVO.getVender4_number());// ��Ӧ��2����
						vo.setDef10(contractVO.getVender4_name());// ��Ӧ������
						dao.insertVO(vo);
					}

				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
					Logger.error(e.getMessage() + "��ͬ���Ϊ:"
							+ contractVO.getFnumber() + "");
				}
			}

		}
		updateDeleteFlag(pk_defdoclist);
		return null;
	}

	private void updateDeleteFlag(Object pk_defdoclist) throws DAOException {
		// TODO �Զ����ɵķ������
		String sql = "update yer_contractfile set def15='0' where pk_defdoclist ='"
				+ pk_defdoclist
				+ "' AND NOT exists (select 1 from commoncontract_to_nc_v@APPSTEST WHERE code =fnumber AND fnumber IS NOT NULL);";
		dao.executeUpdate(sql);
	}

	/**
	 * ��ѯ�Ƿ���nc�д���
	 * 
	 * @throws BusinessException
	 */
	private List<ContractFile> queryNcExit(String fnumber) throws BusinessException {
		String sql = "select * from yer_contractfile where code='" + fnumber
				+ "' and def15!='0' and enablestate = 2";
		List<ContractFile> list = (List<ContractFile>) bs.executeQuery(sql,
				new BeanListProcessor(ContractFile.class));

		return list;
		// TODO �Զ����ɵķ������
	}

	// public String
}
