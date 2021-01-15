package nc.bs.tg.approvalpro.ace.rule;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.BusinessException;
import nc.vo.tg.approvalpro.AggApprovalProVO;
/**
 * 保存批文方案单据时，附件管理添加默认文件类型
 * @author wenjie
 *
 */
public class ApprovalFileRule implements IRule<AggApprovalProVO>{

	@Override
	public void process(AggApprovalProVO[] vos) {
		// TODO 自动生成的方法存根
		String pk_appro = vos[0].getParentVO().getPrimaryKey();
		IFileSystemService file = NCLocator.getInstance().lookup(IFileSystemService.class);
		String userId = InvocationInfoProxy.getInstance().getUserId();
		try {
			/*if(file.queryNCFileNodeTree(pk_appro+"/1.发行申报文件") == null){
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
			if(file.queryNCFileNodeTree(pk_appro+"/9.进度成果文件-完成尽调") == null){
				file.createNewFileNode(pk_appro, "9.进度成果文件-完成尽调", userId);
			}
			if(file.queryNCFileNodeTree(pk_appro+"/10.进度成果文件-交易所审批通过") == null){
				file.createNewFileNode(pk_appro, "10.进度成果文件-交易所审批通过", userId);
			}
			if(file.queryNCFileNodeTree(pk_appro+"/11.进度成果文件-文件封卷") == null){
				file.createNewFileNode(pk_appro, "11.进度成果文件-文件封卷", userId);
			}
			if(file.queryNCFileNodeTree(pk_appro+"/12.进度成果文件-证监会审批通过") == null){
				file.createNewFileNode(pk_appro, "12.进度成果文件-证监会审批通过", userId);
			}
			if(file.queryNCFileNodeTree(pk_appro+"/13.进度成果文件-获取批文") == null){
				file.createNewFileNode(pk_appro, "13.进度成果文件-获取批文", userId);
			}*/
			if(file.queryNCFileNodeTree(pk_appro+"/1.提交交易所") == null){
				file.createNewFileNode(pk_appro, "1.提交交易所", userId);
			}
			if(file.queryNCFileNodeTree(pk_appro+"/2.文件封卷") == null){
				file.createNewFileNode(pk_appro, "2.文件封卷", userId);
			}
			if(file.queryNCFileNodeTree(pk_appro+"/3.获取批文") == null){
				file.createNewFileNode(pk_appro, "3.获取批文", userId);
			}
			if(file.queryNCFileNodeTree(pk_appro+"/4.中介机构协议") == null){
				file.createNewFileNode(pk_appro, "4.中介机构协议", userId);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

}
