package nc.bs.tg.singleissue.ace.rule;

import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.tg.singleissue.GroupCreditVO;
import nc.vo.tg.singleissue.SingleIssueVO;

public class UnApproveRule implements IRule<AggSingleIssueVO>{

	@Override
	public void process(AggSingleIssueVO[] vos) {
		for (AggSingleIssueVO aggvo : vos) {
			SingleIssueVO parentVO = aggvo.getParentVO();
			String pk_appro = parentVO.getDef1();
			String sql = "select a1.def1 from sdfn_issuescale a1 inner join sdfn_approvalpro a2  "
					+ " on a1.pk_appro=a2.pk_appro and nvl(a2.dr,0)=0"
					+ " inner join sdfn_singleissue a3 on a2.pk_appro = a3.def1 and nvl(a3.dr,0)=0"
					+ " where nvl(a1.dr,0)=0 and a3.def1 = '"+pk_appro+"'";
			BaseDAO baseDAO = new BaseDAO();
			try {
				List<Map<String,String>> maplist = (List<Map<String,String>>)baseDAO.executeQuery(sql, new MapListProcessor());
				if(maplist!=null && maplist.size()>0){
					for (Map<String, String> map : maplist) {
						String billno = map.get("def1");
						if(billno.equals(parentVO.getBillno())){
							ExceptionUtils.wrappBusinessException("�ѻ�д���ķ���������ȡ����ˣ�ֻ�ܱ��������");
						}
					}
				}
				GroupCreditVO[] gvos = (GroupCreditVO[])aggvo.getChildren(GroupCreditVO.class);
				for (GroupCreditVO gvo : gvos) {
					if(gvo.getDef1()!=null && "Y".equals(gvo.getDef3())){
						ExceptionUtils.wrappBusinessException("�ѻ�д�������ţ�����ȡ����ˣ�ֻ�ܱ��������");
					}
				}
			} catch (DAOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		
	}

}
