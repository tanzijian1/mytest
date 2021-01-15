package nc.bs.tg.singleissue.ace.rule;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.tg.singleissue.GroupCreditVO;
/**
 * 保存批文方案单据时，附件管理添加默认文件类型
 * @author wenjie
 *
 */
public class SingleIssueFileRule implements IRule<AggSingleIssueVO>{

	@Override
	public void process(AggSingleIssueVO[] vos) {
		for (AggSingleIssueVO aggVO : vos) {
			GroupCreditVO[] gvos = (GroupCreditVO[])aggVO.getChildren(GroupCreditVO.class);
			if(gvos != null){
				for (GroupCreditVO gvo : gvos) {
					if("Y".equals(gvo.getDef3()) && gvo.getDef4()==null){
						ExceptionUtils.wrappBusinessException("【集团授信】选择占用授信时，必须填写占用授信金额");
					}
				}
			}
		}
		
		
		// TODO 自动生成的方法存根
		String pk_appro = vos[0].getParentVO().getPrimaryKey();
		IFileSystemService file = NCLocator.getInstance().lookup(IFileSystemService.class);
		String userId = InvocationInfoProxy.getInstance().getUserId();
		try {
			if(file.queryNCFileNodeTree(pk_appro+"/1.发行申报文件") == null){
				file.createNewFileNode(pk_appro, "1.发行申报文件", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/2.发行公告文件") == null){
				file.createNewFileNode(pk_appro, "2.发行公告文件", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/3.中介机构协议") == null){
				file.createNewFileNode(pk_appro, "3.中介机构协议", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/4.财务顾问协议") == null){
				file.createNewFileNode(pk_appro, "4.财务顾问协议", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/5.债券销售协议") == null){
				file.createNewFileNode(pk_appro, "5.债券销售协议", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/6.债券付息文件") == null){
				file.createNewFileNode(pk_appro, "6.债券付息文件", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/7.债券回售文件") == null){
				file.createNewFileNode(pk_appro, "7.债券回售文件", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/8.债券到期兑付文件") == null){
				file.createNewFileNode(pk_appro, "8.债券到期兑付文件", userId);
			};
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

}
