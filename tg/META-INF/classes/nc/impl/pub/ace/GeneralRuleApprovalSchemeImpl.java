package nc.impl.pub.ace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.wfengine.impl.ActionEnvironment;
import nc.bs.wfengine.engine.ext.PfParticipantHandlerContext;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.general.rule.IGeneralRule;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.sm.UserVO;

public class GeneralRuleApprovalSchemeImpl implements IGeneralRule {
  
	@Override
	public ArrayList<String> getuser(PfParticipantHandlerContext context)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		PfParameterVO paraVo = ActionEnvironment.getInstance().getParaVo(
				context.getWftask().getBillID());
		String percharge=(String)paraVo.m_preValueVo.getParentVO().getAttributeValue("percharge");
		if(percharge==null){
			throw new BusinessException("��ǰ���ݷ����������ֶ�Ϊ��");
		}
		 BaseDAO dao=new BaseDAO();
		String sql="select * from bd_psndoc where pk_psndoc='"+percharge+"'";
		Map map=(Map)dao.executeQuery(sql, new MapProcessor());
		if(map.get("def1")==null){
			throw new BusinessException("��ǰ���ݷ����������ϼ���ԱΪ�գ���ǰ����Ա����ά��");
		}
		Collection<?> c = (Collection<?>) dao.retrieveByClause(UserVO.class,SQLUtil.buildSqlForIn(UserVO.PK_PSNDOC, new String[]{map.get("def1").toString()}));
		if (c == null || c.size() == 0) {
			throw new BusinessException("û���ҵ���ǰ��������������Ӧ���ϼ���Ա���û����е���Ϣ ");
		}
		ArrayList<String> user=new ArrayList<String>();
for(UserVO vo:c.toArray(new UserVO[0])){
			user.add(vo.getCuserid());
		}
		return user;
	}
     
}
