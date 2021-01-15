package nc.bs.tg.financingexpense.rule;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.pf.IPfExchangeService;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.itf.uap.pf.IplatFormEntry;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.cdm.contractbankcredit.CwgwfzxqkBVO;
import nc.vo.cdm.contractbankcredit.FinancexpenseMapping;
import nc.vo.cmp.bill.BillAggVO;
import nc.vo.org.FinanceOrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.financingexpense.FinancexpenseVO;
import nc.vo.tg.singleissue.ConstateExeVO;
import nc.vo.trade.pub.IBillStatus;

public class ApproveAfterRule implements IRule<AggFinancexpenseVO> {
	private IPfExchangeService ips = null;
	private String pk_cwBusiclass = null;// ��֧��Ŀ��������ʷ�pk
	private String pk_rzBusiclass = null;// ��֧��Ŀ������������
	private BaseDAO dao = null;

	@Override
	public void process(AggFinancexpenseVO[] vos) {
		// TODO �Զ����ɵķ������
		FinancexpenseVO vo = vos[0].getParentVO();
		String isPush = (String) vo.getAttributeValue("def17");
		if (IBillStatus.CHECKPASS == Integer.parseInt(String.valueOf(vo
				.getAttributeValue("approvestatus")))) {
			if ("Y".equals(isPush)) {

				try {
					afterPush(vos);
				} catch (BusinessException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
					ExceptionUtils.wrappBusinessException(e.getMessage());
				}

			} else if(vo.getDef12()==null){// add swh 20200114
					// ��def17�����й�ѡʱ�����Ƹ�����㵥ֱ�ӻ�д�����д����ͬ��ϸ�Ĳƹ���ִ���������
				for (AggFinancexpenseVO finVO : vos) {
					// ��ѯ��ǰ���õĶ�Ӧ�����ͬ��ϸpk

					// ���������
					UFDouble applyamount = (UFDouble) finVO.getParentVO()
							.getAttributeValue("applyamount");
					// ���λ
					String contract = (String) finVO.getParentVO()
							.getAttributeValue("def4");
					String pk_payer = (String) finVO.getParentVO()
							.getAttributeValue("pk_payer");
					String pk_finexpense = (String) finVO.getParentVO()
							.getAttributeValue("pk_finexpense");
					String payer = getContractPK(pk_payer);
					// �Ƿ��з�Ʊ
					String def31 = (String) finVO.getParentVO()
							.getAttributeValue("def31");
					// ��Ʊ���
					String def27 = (String) finVO.getParentVO()
							.getAttributeValue("def27");
					StringBuffer sql = new StringBuffer();
					List<CwgwfzxqkBVO> CwgwVOs = null;
					sql.append(
							"  cdm_cwgwfzxqk.pk_contract ='" + contract + "' ")
					// .append("(select pk_contract  from cdm_contract where cdm_contract.contractcode='"+contract+"') ")
							.append("and  cdm_cwgwfzxqk.rowno='"
									+ pk_finexpense + "' ");
					try {
						CwgwVOs = (List<CwgwfzxqkBVO>) getBaseDao()
								.retrieveByClause(CwgwfzxqkBVO.class,
										sql.toString());
					} catch (DAOException e1) {
						// TODO �Զ����ɵ� catch ��
						e1.printStackTrace();
					}
					if (CwgwVOs.size() > 0) {
						for (CwgwfzxqkBVO cvo : CwgwVOs) {
							cvo.setAttributeValue("pk_contract", contract);
							cvo.setAttributeValue("rowno", pk_finexpense);
							cvo.setAttributeValue("pk_zfgs", payer);
							cvo.setAttributeValue("m_zfje", applyamount);
							cvo.setAttributeValue("d_zfsj", new UFDate());
							cvo.setAttributeValue("def1", def31);// �Ƿ�Ʊ
							cvo.setAttributeValue("def2", def27);// ��Ʊ���
							cvo.setAttributeValue("dr", 0);
							try {
								getBaseDao().updateObject(cvo,
										new FinancexpenseMapping());
							} catch (DAOException e) {
								// TODO �Զ����ɵ� catch ��
								e.printStackTrace();
							}
						}
					} else {
						try {
							CwgwfzxqkBVO inserVO = new CwgwfzxqkBVO();
							inserVO.setAttributeValue("rowno", pk_finexpense);
							inserVO.setAttributeValue("pk_zfgs", payer);
							inserVO.setAttributeValue("m_zfje", applyamount);
							inserVO.setAttributeValue("pk_contract", contract);
							inserVO.setAttributeValue("d_zfsj", new UFDate());
							inserVO.setAttributeValue("def1", def31);
							inserVO.setAttributeValue("dr", 0);
							getBaseDao().insertObject(inserVO,
									new FinancexpenseMapping());
						} catch (DAOException e) {
							// TODO �Զ����ɵ� catch ��
							e.printStackTrace();
						}
					}

					/*
					 * try { dao.updateObject(cvo,new FinancexpenseMapping(),
					 * " and where pk_contract='10016A1000000024I50S'"); } catch
					 * (DAOException e) { // TODO �Զ����ɵ� catch ��
					 * e.printStackTrace(); }
					 */
				}
			}else{
				//���ڷ��������Ϊ�գ���д�����ڷ������
				if("RZ06-Cxx-001".equals(vo.getTranstype())){
					//��д���ݵ����ڷ��й����ִ�����
					String pk_singleissue = vo.getDef12();
					ConstateExeVO cvo = new ConstateExeVO();
					cvo.setDr(0);
					cvo.setPk_singleissue(pk_singleissue);
					cvo.setDef1(vo.getPk_payer());
					cvo.setDef2(vo.getApprovedate().toString());
					cvo.setDef3(vo.getApplyamount().toString());
					cvo.setDef5(vo.getBillno());
					//����Ʊ�ݷ�Ʊ���
					UFDouble a = vo.getDef23()==null?UFDouble.ZERO_DBL:new UFDouble(vo.getDef23());
					//������Ʊ��˰�ܽ���ֵ˰��
					UFDouble b = vo.getDef27()==null?UFDouble.ZERO_DBL:new UFDouble(vo.getDef27());
					cvo.setDef4(a.add(b,2).toString());
					try {
						getBaseDao().insertVO(cvo);
					} catch (DAOException e) {
						e.printStackTrace();
						ExceptionUtils.wrappBusinessException(e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * ���ʷ�������ͨ���Ƹ�����㵥
	 * 
	 * @param vos
	 * @throws BusinessException
	 */
	private void afterPush(AggFinancexpenseVO[] vos) throws BusinessException {
		// TODO Auto-generated method stub
		try {
			AggFinancexpenseVO aggvo = (AggFinancexpenseVO) vos[0];
			FinancexpenseVO headvo = (FinancexpenseVO) aggvo.getParentVO();
			InvocationInfoProxy.getInstance().setGroupId(
					(String) headvo.getAttributeValue("pk_group"));
			BillAggVO[] vo = (BillAggVO[]) getPfExchangeService()
					.runChangeDataAryNeedClassify("RZ06", "F5",
							new AggFinancexpenseVO[] { aggvo }, null, 2);
			Boolean istrue = vo == null || vo.length == 0;

			if (istrue) {
				throw new BusinessException("���ʷ���["
						+ headvo.getAttributeValue("pk_billtype")
						+ "]ת��������㵥�쳣 [δת���ɹ�]!");
			}

			// TODO ���󣺵�ѡ�������ʷ�ʱ��֧��Ŀ��Ӧ������ʷ� ��ѡ���������ʷ���ʱ��֧��Ŀ��Ӧ����������
			if (headvo.getAttributeValue("def2") != null) {// ��֧��Ŀ
				getpk_cwBusiclass();// ��ȡ������ʷ�pk
				getpk_rzBusiclass();// ��ȡ����������pk

				CircularlyAccessibleValueObject[] bodys = vo[0].getChildrenVO();
				for (CircularlyAccessibleValueObject bodyVO : bodys) {
					if (pk_cwBusiclass != null
							&& pk_cwBusiclass.equals(headvo
									.getAttributeValue("def2"))) {
						bodyVO.setAttributeValue("pk_recproject",
								pk_cwBusiclass);
					} else {
						bodyVO.setAttributeValue("pk_recproject",
								pk_rzBusiclass);
					}
				}

			}

			IplatFormEntry plat = (IplatFormEntry) NCLocator.getInstance()
					.lookup(IplatFormEntry.class);
			BillAggVO[] savedBill = null;
			// ��ʽ���ɸ�����㵥
			IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);
			WorkflownoteVO worknoteVO = iWorkflowMachine.checkWorkFlow("SAVE",
					"F5", vo[0], null);
			savedBill = (BillAggVO[]) plat.processAction("SAVE", "F5",
					worknoteVO, vo[0], null, null);
			// --add by huangdq 2020-04-16 ҵ��һ�廯�����߼�����,�������Ƹ�����㵥����ֱ����� -start-
			// �ز����ݣ���ʱ�򱣴���򷵻ص�vo����׼ȷ�����ݣ��п�����ȱʧ
			// ICmpPayBillPubQueryService cmpquery = NCLocator.getInstance()
			// .lookup(ICmpPayBillPubQueryService.class);
			// // �Ը�����㵥����������
			// BillAggVO[] billaggvo = cmpquery
			// .findBillByPrimaryKey(new String[] { (String) savedBill[0]
			// .getParentVO().getAttributeValue("pk_paybill") });
			// plat.processAction("APPROVE", "F5", null, billaggvo[0], null,
			// null);
			// --add by huangdq 2020-04-16 ҵ��һ�廯�����߼�����,�������Ƹ�����㵥����ֱ����� -end-

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}

	}

	/**
	 * VOת��������
	 * 
	 * @return
	 */
	public IPfExchangeService getPfExchangeService() {
		if (ips == null) {
			ips = NCLocator.getInstance().lookup(IPfExchangeService.class);
		}
		return ips;
	}

	/**
	 * @return pk_cwBusiclass ��֧��ĿΪ0406 ������ʷ�
	 * @throws DAOException
	 */
	public void getpk_cwBusiclass() throws DAOException {
		if (pk_cwBusiclass == null) {
			String sql = "select pk_inoutbusiclass from bd_inoutbusiclass where code ='0406' and dr =0";
			pk_cwBusiclass = (String) new BaseDAO().executeQuery(sql,
					new ColumnProcessor());
		}
	}

	/**
	 * @return pk_rzBusiclass ��֧��ĿΪ0405 ����������
	 * @throws DAOException
	 */
	public void getpk_rzBusiclass() throws DAOException {
		if (pk_rzBusiclass == null) {
			String sql = "select pk_inoutbusiclass from bd_inoutbusiclass where code ='0405' and dr =0";
			pk_rzBusiclass = (String) new BaseDAO().executeQuery(sql,
					new ColumnProcessor());
		}
	}

	public String getContractPK(String pk_financeorg) {
		List<FinanceOrgVO> vos = null;
		try {
			vos = (List<FinanceOrgVO>) getBaseDao().retrieveByClause(
					FinanceOrgVO.class,
					"pk_financeorg  ='" + pk_financeorg + "'");
			// dao.retrieveByClause(ContractVO.class,"contractcode ='"+contractCode+"'"
			// );
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}

		// add by tjl 2020-04-11
		if (vos == null || vos.size() <= 0) {
			return null;
		}
		// end
		return vos.get(0).getName();

	}

	private BaseDAO getBaseDao() {
		if (dao == null)
			dao = new BaseDAO();
		return dao;

	}

}
