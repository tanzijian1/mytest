package nc.impl.tg.pf;

import java.util.ArrayList;
import java.util.Collection;

import nc.bs.dao.BaseDAO;
import nc.bs.wfengine.engine.ext.PfParticipantHandlerContext;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.general.rule.IGeneralRule;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

/**
 * 批文方案  提交制单人上级作为审核人
 * @author wenjie
 *
 */
public class ApprovalproApproveRuleImpl implements IGeneralRule {
	BaseDAO baseDAO;

	/**
	 * 持久化类
	 * 
	 * @return
	 */
	protected BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;

	}
	@Override
	public ArrayList<String> getuser(PfParticipantHandlerContext context)
			throws BusinessException {
		// TODO 自动生成的方法存根
		String percharge = null;
		if(context.getWftask().getBillID() != null){
			String sqlpk = "select def1 from sm_user where sm_user.pk_psndoc = "
							+ "	(select def22 from sdfn_approvalpro where pk_appro='"+context.getWftask().getBillID()+"') "
							+ "	and def1 <> '~' and def1 is not null";
			//跟进人人员主键
			percharge = (String) getBaseDAO().executeQuery(sqlpk, new ColumnProcessor()) ;
		}
		if(percharge==null){
			throw new BusinessException("没有获取当前业务单据对应的上级领导");
		}
		Collection<?> c = (Collection<?>) getBaseDAO().retrieveByClause(UserVO.class,SQLUtil.buildSqlForIn(UserVO.PK_PSNDOC, new String[]{percharge}));
		if (c == null || c.size() == 0) {
			throw new BusinessException("没有找到当前方案负责人所对应的上级人员在用户表中的信息 ");
		}
		ArrayList<String> user=new ArrayList<String>();

		for(UserVO vo:c.toArray(new UserVO[0])){
			user.add(vo.getCuserid());
		}
		return user;
	}

}
