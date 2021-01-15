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
		// TODO 自动生成的方法存根
		return DecideWorkFlowProcessStatusUtils.getUtils()
				.queryActInsByPrceInsPK(aggvo, type, billtype);
	}

	@Override
	public JKBXVO recall_RequiresNew(JKBXVO vo) throws BusinessException {
		// TODO 自动生成的方法存根
		//新合同类费用请款单	新非合同类费用请款单	新差旅费报销单
    	IGuoXinImage image=NCLocator.getInstance().lookup(IGuoXinImage.class);
    	JKBXHeaderVO hvo = vo.getParentVO();
    	String pk_user=AppContext.getInstance().getPkUser();
    	String optype="2";//操作类型（1/作废处理(只改状态)，2/删除数据，默认1）
    	String remark=" ";//退单描述 
    	String useraccount = null;
    	String username = null;
    	Boolean isBarcodeEmpty = StringUtils.isEmpty(hvo.getZyx16());//影像编码
    	if(!isBarcodeEmpty){
    		BaseDAO dao=new BaseDAO();
    		useraccount = (String)dao.executeQuery("select user_name  from sm_user where cuserid='"
    				+pk_user+"'", new ColumnProcessor());//退单人帐号
    		username = (String)dao.executeQuery("select user_name  from sm_user where cuserid='"
    				+hvo.getOperator()+"'", new ColumnProcessor());//退单人姓名
    	}
    	if(!StringUtils.isEmpty(hvo.getZyx30())
    			&& !"UNAPPROVE".equals(hvo.getIsInterface())){//NC主动收回,BPM拒绝，BPM退回
    		if(!"UNSAVE".equals(hvo.getIsInterface())){//不为BPM拒绝；UNAPPROVE:退回，UNSAVE:拒绝
    			//调用BPM删除
    			sendBpmDelete(vo);
    		}
    		if("UNSAVE".equals(hvo.getIsInterface())){
    			hvo.setZyx29("N");//是否BPM流程中
    			hvo.setZyx30("");//BPM主键
    		}
    		//调用影像删除
    		if(!isBarcodeEmpty){
    			image.delrefund(hvo.getZyx16(), "1", useraccount, username, null, remark, null, optype);
    			hvo.setZyx16("");
    		}
    	}
    	return vo;
	}
	/**
	 * 调用BPM删除
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
		vo.setIsOperInNC("操作位置:"+billvo.getParentVO().getIsInterface());
		try {
			Map<String,Object> map  = service.pushBPMBillBackOrDelete(vo);
			if(map != null && map.size()>0 && "true".equals(map.get("flag"))){
				billvo.getParentVO().setZyx29("N");//是否BPM流程中
				billvo.getParentVO().setZyx30("");//BPM主键
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			throw new BusinessException(e.getMessage());
		}
	}

}
