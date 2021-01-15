package nc.impl.tg.workflowstatus;

import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.tg.image.IGuoXinImage;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.tg.workflowstatus.IWorkFlowStatus;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.er.util.StringUtils;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.outside.bpm.NcToBpmVO;
import nc.workfolw.util.DecideWorkFlowProcessStatusUtils;

public class WorkFlowStatusImpl implements IWorkFlowStatus {

	@Override
	public boolean queryActInsByPrceInsPK_RequiresNew(
			AggregatedValueObject aggvo, String type, String billtype)
			throws Exception {
		// TODO �Զ����ɵķ������
		return DecideWorkFlowProcessStatusUtils.getUtils()
				.queryActInsByPrceInsPK(aggvo, type, billtype);
	}

	@Override
	public JKBXVO recall_RequiresNew(JKBXVO vo) throws BusinessException {
		// TODO �Զ����ɵķ������
		//�º�ͬ�������	�·Ǻ�ͬ�������	�²��÷ѱ�����
    	IGuoXinImage image=NCLocator.getInstance().lookup(IGuoXinImage.class);
    	JKBXHeaderVO hvo = vo.getParentVO();
    	String pk_user=AppContext.getInstance().getPkUser();
    	String optype="2";//�������ͣ�1/���ϴ���(ֻ��״̬)��2/ɾ�����ݣ�Ĭ��1��
    	String remark=" ";//�˵����� 
    	String useraccount = null;
    	String username = null;
    	Boolean isBarcodeEmpty = StringUtils.isEmpty(hvo.getZyx16());//Ӱ�����
    	if(!isBarcodeEmpty){
    		BaseDAO dao=new BaseDAO();
    		useraccount = (String)dao.executeQuery("select user_name  from sm_user where cuserid='"
    				+pk_user+"'", new ColumnProcessor());//�˵����ʺ�
    		username = (String)dao.executeQuery("select user_name  from sm_user where cuserid='"
    				+hvo.getOperator()+"'", new ColumnProcessor());//�˵�������
    	}
    	if(!StringUtils.isEmpty(hvo.getZyx30())
    			&& !"UNAPPROVE".equals(hvo.getIsInterface())){//NC�����ջ�,BPM�ܾ���BPM�˻�
    		if(!"UNSAVE".equals(hvo.getIsInterface())){//��ΪBPM�ܾ���UNAPPROVE:�˻أ�UNSAVE:�ܾ�
    			//����BPMɾ��
    			sendBpmDelete(vo);
    		}
    		if("UNSAVE".equals(hvo.getIsInterface())){
    			hvo.setZyx29("N");//�Ƿ�BPM������
    			hvo.setZyx30("");//BPM����
    		}
    		//����Ӱ��ɾ��
    		if(!isBarcodeEmpty){
    			image.delrefund(hvo.getZyx16(), "1", useraccount, username, null, remark, null, optype);
    			hvo.setZyx16("");
    		}
    	}
    	return vo;
	}
	/**
	 * ����BPMɾ��
	 * @param billvo
	 * @throws BusinessException
	 */
	private void sendBpmDelete(JKBXVO billvo) throws BusinessException{
		NcToBpmVO vo=new NcToBpmVO();
		IPushBPMBillFileService service=NCLocator.getInstance().lookup(IPushBPMBillFileService.class);
		vo.setPrimaryKey(billvo.getParentVO().getPrimaryKey());
		vo.setApprovaltype("delete");
		vo.setDesbill(billvo.getParentVO().getZyx10());
		vo.setTaskid(billvo.getParentVO().getZyx30());
		vo.setIsOperInNC("����λ��:"+billvo.getParentVO().getIsInterface());
		try {
			Map<String,Object> map  = service.pushBPMBillBackOrDelete(vo);
			if(map != null && map.size()>0 && "true".equals(map.get("flag"))){
				billvo.getParentVO().setZyx29("N");//�Ƿ�BPM������
				billvo.getParentVO().setZyx30("");//BPM����
			}
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new BusinessException(e.getMessage());
		}
	}

}
