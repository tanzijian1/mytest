package nc.bs.tg.approvalpro.ace.rule;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.BusinessException;
import nc.vo.tg.approvalpro.AggApprovalProVO;
/**
 * �������ķ�������ʱ�������������Ĭ���ļ�����
 * @author wenjie
 *
 */
public class ApprovalFileRule implements IRule<AggApprovalProVO>{

	@Override
	public void process(AggApprovalProVO[] vos) {
		// TODO �Զ����ɵķ������
		String pk_appro = vos[0].getParentVO().getPrimaryKey();
		IFileSystemService file = NCLocator.getInstance().lookup(IFileSystemService.class);
		String userId = InvocationInfoProxy.getInstance().getUserId();
		try {
			/*if(file.queryNCFileNodeTree(pk_appro+"/1.�����걨�ļ�") == null){
				file.createNewFileNode(pk_appro, "1.�����걨�ļ�", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/2.���й����ļ�") == null){
				file.createNewFileNode(pk_appro, "2.���й����ļ�", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/3.�н����Э��") == null){
				file.createNewFileNode(pk_appro, "3.�н����Э��", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/4.�������Э��") == null){
				file.createNewFileNode(pk_appro, "4.�������Э��", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/5.ծȯ����Э��") == null){
				file.createNewFileNode(pk_appro, "5.ծȯ����Э��", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/6.ծȯ��Ϣ�ļ�") == null){
				file.createNewFileNode(pk_appro, "6.ծȯ��Ϣ�ļ�", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/7.ծȯ�����ļ�") == null){
				file.createNewFileNode(pk_appro, "7.ծȯ�����ļ�", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/8.ծȯ���ڶҸ��ļ�") == null){
				file.createNewFileNode(pk_appro, "8.ծȯ���ڶҸ��ļ�", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/9.���ȳɹ��ļ�-��ɾ���") == null){
				file.createNewFileNode(pk_appro, "9.���ȳɹ��ļ�-��ɾ���", userId);
			}
			if(file.queryNCFileNodeTree(pk_appro+"/10.���ȳɹ��ļ�-����������ͨ��") == null){
				file.createNewFileNode(pk_appro, "10.���ȳɹ��ļ�-����������ͨ��", userId);
			}
			if(file.queryNCFileNodeTree(pk_appro+"/11.���ȳɹ��ļ�-�ļ����") == null){
				file.createNewFileNode(pk_appro, "11.���ȳɹ��ļ�-�ļ����", userId);
			}
			if(file.queryNCFileNodeTree(pk_appro+"/12.���ȳɹ��ļ�-֤�������ͨ��") == null){
				file.createNewFileNode(pk_appro, "12.���ȳɹ��ļ�-֤�������ͨ��", userId);
			}
			if(file.queryNCFileNodeTree(pk_appro+"/13.���ȳɹ��ļ�-��ȡ����") == null){
				file.createNewFileNode(pk_appro, "13.���ȳɹ��ļ�-��ȡ����", userId);
			}*/
			if(file.queryNCFileNodeTree(pk_appro+"/1.�ύ������") == null){
				file.createNewFileNode(pk_appro, "1.�ύ������", userId);
			}
			if(file.queryNCFileNodeTree(pk_appro+"/2.�ļ����") == null){
				file.createNewFileNode(pk_appro, "2.�ļ����", userId);
			}
			if(file.queryNCFileNodeTree(pk_appro+"/3.��ȡ����") == null){
				file.createNewFileNode(pk_appro, "3.��ȡ����", userId);
			}
			if(file.queryNCFileNodeTree(pk_appro+"/4.�н����Э��") == null){
				file.createNewFileNode(pk_appro, "4.�н����Э��", userId);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

}
