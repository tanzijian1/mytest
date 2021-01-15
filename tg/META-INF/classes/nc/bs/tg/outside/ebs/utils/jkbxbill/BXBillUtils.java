
package nc.bs.tg.outside.ebs.utils.jkbxbill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.ebs.utils.costadvance.CostAdvanceBillUtil;
import nc.bs.tg.outside.salebpm.utils.SyncSaleBPMBillStatesUtils;
import nc.itf.arap.pub.IBXBillPublic;
import nc.itf.tg.ISaveLog;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.EBSCont;
import nc.itf.tg.outside.ISaveLogService;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.erm.accruedexpense.IErmAccruedBillManage;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.ep.bx.BXBusItemVO;
import nc.vo.ep.bx.BXHeaderVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.er.exception.BugetAlarmBusinessException;
import nc.vo.erm.accruedexpense.AccruedVO;
import nc.vo.erm.accruedexpense.AccruedVerifyVO;
import nc.vo.erm.accruedexpense.AggAccruedBillVO;
import nc.vo.erm.common.MessageVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.tg.costadvance.CostBodyVO;
import nc.vo.tg.costadvance.CostHeadVO;
import nc.vo.tg.jkbx.OutsideJKBXAccruedVerifyBodyVO;
import nc.vo.tg.jkbx.OutsideJKBXBodyItemVO;
import nc.vo.tg.jkbx.OutsideJKBXHeadVO;
import nc.vo.tg.outside.OutsideLogVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 报销单接口
 * @author acer
 *
 */
public class BXBillUtils extends EBSBillUtils {
	
	static BXBillUtils utils;
	IBXBillPublic BXBillPublicService = null;
	private IErmAccruedBillManage billManagerService = null;
	
	public static BXBillUtils getUtils() {
		if (utils == null) {
			utils = new BXBillUtils();
		}
		return utils;
	}
	
	public String onSyncBill(HashMap<String, Object> value, String dectype,String srctype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("EBS");
		logVO.setDesbill("EBS推预算占用单和预提核销推报销单");
		logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());
		logVO.setSrcparm(value.toString());
		ISaveLogService service = NCLocator.getInstance().lookup(
				ISaveLogService.class);
		// 外系统信息
		JSONObject jsonData1 = (JSONObject) value.get("datayz");// 表单数据
		// 处理预占单信息
		StringBuffer operation=new StringBuffer();//nc进行操作详情
		String yzdcode = EBSCont.BILL_01;//预占单
		AggAccruedBillVO ServiceaggVO=null;
		ISqlThread iser= NCLocator.getInstance().lookup(ISqlThread.class);//预提服务
		ISaveLog iappover= NCLocator.getInstance().lookup(ISaveLog.class);//预提服务
//		String yzdname = EBSCont.getSrcBillNameMap().get("01");
//		AggAccruedBillVO accruedAggvo=null;
		HashMap<String, String> dataMap = new HashMap<String, String>();
		if(jsonData1!=null){
			String jsonhead1 = jsonData1.getString("accrualHeadVO");// 外系统来源表头数据
			String jsonbody1 = jsonData1.getString("accrualBodyVOs");// 外系统来源表体数据
			// 转换json
			CostHeadVO headVO = JSONObject.parseObject(jsonhead1, CostHeadVO.class);
			headVO.setOperator(headVO.getDef29());
			headVO.setOperatordept(headVO.getDef30());
			List<CostBodyVO> bodyVOs = JSONObject.parseArray(jsonbody1,
					CostBodyVO.class);
			if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
				throw new BusinessException("表单数据转换失败，请检查！json:" + jsonData1);
			}
			
			
			String srcid = headVO.getEbsid();// EBS业务单据ID
			String srcno = headVO.getEbsbillcode();// EBS业务单据单据号
			String billqueue = "预算占用单" + ":" + srcid;
			String billkey = "预算占用单" + ":" + srcno;
			// TODO ebsid 按实际存入信息位置进行变更
			AggAccruedBillVO aggVO = (AggAccruedBillVO) getBillVO(
					AggAccruedBillVO.class, "isnull(dr,0)=0 and defitem1 = '"
							+ srcid + "'");
			if (aggVO != null && (headVO.getNcid()==null || headVO.getNcid()=="")&& !("Y".equals(headVO.getIsapprove()))) {
				throw new BusinessException("【"
						+ billkey
						+ "】,NC已存在对应的业务单据【"
						+ aggVO.getParentVO().getAttributeValue(
								PayableBillVO.BILLNO) + "】,请勿重复上传!");
			}
			
			EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
			try {
				//删除单据
				if("Y".equals(headVO.getIsdelete())&&!(StringUtils.isEmpty(headVO.getNcid()))){
					AggAccruedBillVO prebillvo=new AggAccruedBillVO();
					IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
					operation.append("删除操作");
					if("Y".equals(headVO.getIsdelete())){
						AggAccruedBillVO deleaggvo = (AggAccruedBillVO) getBillVO(
								AggAccruedBillVO.class, "isnull(dr,0)=0 and  pk_accrued_bill = '"
										+ headVO.getNcid()+ "'");
						if(deleaggvo==null){
							throw new BusinessException("传入ncid无效，找不到对应单据");
						}
						if(deleaggvo.getParentVO().getBillstatus()==3){
							Object returnobj_uapp= pfaction.processAction(IPFActionName.UNAPPROVE, "262X", null,deleaggvo , null, null);
							if(((Object[])returnobj_uapp)[0] instanceof nc.vo.erm.common.MessageVO){
								prebillvo=(AggAccruedBillVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj_uapp)[0]).getSuccessVO();
								if(prebillvo!=null){
									deleaggvo=prebillvo;
								}
							}
						}
						Object returnobj=null;
						if(deleaggvo.getParentVO().getApprstatus()!=-1){
							returnobj= pfaction.processAction(IPFActionName.UNSAVE, "262X", null,deleaggvo , null, null);
						}
						if(returnobj!=null){
							if(((Object[])returnobj)[0] instanceof nc.vo.erm.accruedexpense.AggAccruedBillVO){
								deleaggvo=(AggAccruedBillVO)((Object[])returnobj)[0];
							}
						}
						getAppService().deleteVOs(new AggAccruedBillVO[]{deleaggvo});
						ServiceaggVO=deleaggvo;
					}
				}else{
					if(!(StringUtils.isEmpty(headVO.getNcid()))&&(!"Y".equals(headVO.getIsdelete()))){//更新单据
						AggAccruedBillVO prebillvo=new AggAccruedBillVO();
						AggAccruedBillVO updedbillvo=new AggAccruedBillVO();
						AggAccruedBillVO preupdateAggvo = (AggAccruedBillVO) getBillVO(
								AggAccruedBillVO.class, "isnull(dr,0)=0 and  pk_accrued_bill = '"
										+ headVO.getNcid()+ "'");
						if(preupdateAggvo==null){
							throw new BusinessException("传入ncid无效，找不到对应单据");
						}
						if(preupdateAggvo.getParentVO().getApprstatus()==1){//更新前如果已审批先弃审
							operation.append("弃审操作");
							IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
							Object returnobj= pfaction.processAction(IPFActionName.UNAPPROVE, "262X", null,preupdateAggvo , null, null);
							if(((Object[])returnobj)[0] instanceof nc.vo.erm.common.MessageVO){
								prebillvo=(AggAccruedBillVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj)[0]).getSuccessVO();
							}
						}else{
							prebillvo=preupdateAggvo;
						}
						prebillvo=(AggAccruedBillVO) getBillVO(
	      	     				AggAccruedBillVO.class, "isnull(dr,0)=0 and  pk_accrued_bill = '"
	      	     						+ headVO.getNcid()+ "'");
						operation.append("更新操作");
						updedbillvo =CostAdvanceBillUtil.getUtils().updateVO(headVO, prebillvo,yzdcode,bodyVOs);
//						accruedAggvo=getAppService().updateVO(updedbillvo);
						try{
							IErmAccruedBillManage billManagerSer = NCLocator.getInstance().lookup(
									IErmAccruedBillManage.class);
							billManagerSer.updateVO(updedbillvo);
//		        			 updedbillvo.getParentVO().setHasntbcheck(new UFBoolean(flag));
//							ServiceaggVO=iser.ytupdate_RequiresNew(updedbillvo);
		        		 }catch (BugetAlarmBusinessException e){//预警性处理
//		        			 updedbillvo.getParentVO().setBillno(null);
//		        			 updedbillvo.getParentVO().setPk_accrued_bill(null);
//	     					 for(AccruedDetailVO bvo:updedbillvo.getChildrenVO()){
//	     						 bvo.setPk_accrued_detail(null);
//	     					 }
		        			 updedbillvo=(AggAccruedBillVO) getBillVO(
			      	     				AggAccruedBillVO.class, "isnull(dr,0)=0 and  pk_accrued_bill = '"
			      	     						+ headVO.getNcid()+ "'");
	     					updedbillvo.getParentVO().setHasntbcheck(UFBoolean.TRUE);
	     					ServiceaggVO= iser.ytupdate_RequiresNew(updedbillvo);
	     				    }
//						if("Y".equals(headVO.getIsapprove())){//单据审批
							AggAccruedBillVO billvo=(AggAccruedBillVO) getBillVO(
		      	     				AggAccruedBillVO.class, "isnull(dr,0)=0 and  pk_accrued_bill = '"
		      	     						+ headVO.getNcid()+ "'");
		     				if(billvo==null){
		     					throw new BusinessException("传入ncid无效，找不到对应单据");
		     				}
		     				billvo.getParentVO().setApprover("000112100000000001IN");
		     				billvo.getParentVO().setApprovetime(new UFDateTime());
							operation.append("&审批操作");
//							prebillvo=iappover.ytapprove_RequiresNew(preupdateAggvo);
							IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
							Object returnobj= pfaction.processAction(IPFActionName.APPROVE, "262X", null,billvo , null, null);
							if(((Object[])returnobj)[0] instanceof nc.vo.erm.common.MessageVO){
								ServiceaggVO = (AggAccruedBillVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj)[0]).getSuccessVO();
							}
//						}
					} else{//新增单据
						AggAccruedBillVO billvo = CostAdvanceBillUtil.getUtils().onTranBill(headVO, bodyVOs, yzdcode);
						operation.append("新增操作");
//						accruedAggvo=getAppService().insertVO(billvo);
						try{
//		        			 updedbillvo.getParentVO().setHasntbcheck(new UFBoolean(flag));
							ServiceaggVO=iser.ytinsert_RequiresNew(billvo);
		        		 }catch (BugetAlarmBusinessException e){//预警性处理
//		        			 updedbillvo.getParentVO().setBillno(null);
//		        			 updedbillvo.getParentVO().setPk_accrued_bill(null);
//	     					 for(AccruedDetailVO bvo:updedbillvo.getChildrenVO()){
//	     						 bvo.setPk_accrued_detail(null);
//	     					 }
		        			 AggAccruedBillVO ybillvo =CostAdvanceBillUtil.getUtils().onTranBill(headVO, bodyVOs, srctype);
		     					ybillvo.getParentVO().setHasntbcheck(UFBoolean.TRUE);
		     					ServiceaggVO= iser.ytinsert_RequiresNew(ybillvo);
	     				    }
//						if("Y".equals(headVO.getIsapprove())){//单据审批
//							AggAccruedBillVO bill=(AggAccruedBillVO) getBillVO(
//		      	     				AggAccruedBillVO.class, "isnull(dr,0)=0 and  pk_accrued_bill = '"
//		      	     						+ headVO.getNcid()+ "'");
//		     				if(billvo==null){
//		     					throw new BusinessException("传入ncid无效，找不到对应单据");
//		     				}
//							ServiceaggVO.getParentVO().setApprover("000112100000000001IN");
//							ServiceaggVO.getParentVO().setApprovetime(new UFDateTime());
							ServiceaggVO.getParentVO().setHasntbcheck(UFBoolean.TRUE);
							operation.append("&审批操作");
							IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
							Object returnobj= pfaction.processAction(IPFActionName.APPROVE, "262X", null,ServiceaggVO , null, null);
							if(((Object[])returnobj)[0] instanceof nc.vo.erm.common.MessageVO){
								ServiceaggVO = (AggAccruedBillVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj)[0]).getSuccessVO();
							}
//						}
					}
				}	
			} catch (Exception e) {
				logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
				logVO.setErrmsg(org.apache.commons.lang.exception.ExceptionUtils
						.getFullStackTrace(e));
				logVO.setOperator(srcno);//key
				throw new BusinessException("【" + billkey + "】," +e.getMessage(),e);
			} finally{
				EBSBillUtils.removeBillQueue(billqueue);
				try {
					service.saveLog_RequiresNew(logVO);
				} catch (Exception e1) {
					throw new BusinessException("插入日志失败:"+e1.getMessage());
				}
			}
			
			dataMap.put("yzbillid", ServiceaggVO.getPrimaryKey());
			dataMap.put("yzoperation",operation.toString());
			dataMap.put("yzbillno", (String) ServiceaggVO.getParentVO()
					.getAttributeValue(AccruedVO.BILLNO));
		}
		
		
		
		//预算推报销单
		JSONObject jsonData2 = (JSONObject) value.get("datayt");// 表单数据
		if(jsonData2!=null){
			
			String jsonhead2 = jsonData2.getString("OutBXHeadVO");// 外系统来源表头数据
			String jsonItemBody2 = jsonData2.getString("OutsideJKBXBodyItemVO");// 外系统来源报销单表体数据
			String jsonVerifyBody2 = jsonData2.getString("OutsideJKBXAccruedVerifyBodyVO");// 外系统来源报销单表体数据
			OutsideJKBXHeadVO  headVO = JSONObject.parseObject(jsonhead2, OutsideJKBXHeadVO.class);
			List<OutsideJKBXBodyItemVO> itemBodyVOs = JSONObject.parseArray(jsonItemBody2,
					OutsideJKBXBodyItemVO.class);
			List<OutsideJKBXAccruedVerifyBodyVO> verifyBodyVOs = JSONObject.parseArray(jsonVerifyBody2,
					OutsideJKBXAccruedVerifyBodyVO.class);
			if(headVO!=null&&itemBodyVOs!=null&&verifyBodyVOs!=null)checkNull(headVO,itemBodyVOs,verifyBodyVOs);//空值校验
			String actionFlag2 =  headVO.getZyx35();// NC单据动作标识 枚举 1:新增 2:修改 3:删除 
			{
				String srcid = "预算预提->报销单"+headVO.getZyx32();// EBS合同单据ID
				String srcno = "预算预提->报销单"+headVO.getZyx2();// EBS合同单据单据号
				String billqueue = EBSCont.getSrcBillNameMap().get(srctype) + ":"
						+ srcid;
				String billkey = EBSCont.getSrcBillNameMap().get(srctype) + ":" + srcno;
				EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
				
				try {
					BXVO aggvo = (BXVO) getBillVO(
							BXVO.class, "isnull(dr,0)=0 and pk_jkbx = '" + headVO.getZyx44()
							+ "'");
					MessageVO[] messagevos = null;
					StringBuffer title=new StringBuffer();//nc进行操作详情
					if(aggvo!=null&&"2".equals(actionFlag2)){
						aggvo = (BXVO) onUpdateBill(aggvo,headVO,itemBodyVOs,verifyBodyVOs);//ebs会根据预提单的视图传NC预提单的单据编码过来,如果有此单据编码则更新,没有则新增
						title.append("更新操作");
						if("Y".equals(headVO.getZyx43())){
							title.append("&审批操作");
						}
					}else if(aggvo!=null&&"3".equals(actionFlag2)){
						messagevos = onDeleteBill(aggvo);
						title.append("删除操作");
					}else if(aggvo==null&&"1".equals(actionFlag2)){
						aggvo = (BXVO) onInsertBill(null,null,headVO,itemBodyVOs,verifyBodyVOs);
						title.append("新增操作");
						if("Y".equals(headVO.getZyx43())){
							title.append("&审批操作");
						}
					}
					//审核动作
					if("Y".equals(headVO.getZyx43())&&!"3".equals(actionFlag2)){
						HashMap eParam = new HashMap();
						eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
								PfUtilBaseTools.PARAM_NOTE_CHECKED);
						BXVO commitVO = (BXVO) getPfBusiAction().processAction("SAVE", "264X", null,
								aggvo, null, eParam);//提交
						getPfBusiAction().processAction("APPROVE", "264X", null,
								commitVO, null, eParam);
					}
					if(aggvo!=null){
						if(messagevos==null){
							dataMap.put("ytbillid", aggvo.getParentVO().getPrimaryKey());
							dataMap.put("yttitle",title.toString());
							dataMap.put("ytbillno", aggvo.getParentVO().getDjbh());
						}else{
							dataMap.put("yttitle",title.toString());
							dataMap.put("ytbillno", aggvo.getParentVO().getDjbh()+"已删除");
						}
					}else{
						throw new BusinessException("接口出现异常，请重新再试");
					}
					
				} catch (Exception e) {
					logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
					logVO.setErrmsg(org.apache.commons.lang.exception.ExceptionUtils
							.getFullStackTrace(e));
					logVO.setOperator(srcno);//key
					throw new BusinessException("【" + billkey + "】," + e.getMessage(),
							e);
				}finally{
					EBSBillUtils.removeBillQueue(billqueue);
					try {
						service.saveLog_RequiresNew(logVO);
					} catch (Exception e) {
						throw new BusinessException("插入日志失败:"+e.getMessage());
					}
				}
			}
		}
		
		return JSON.toJSONString(dataMap);
	}
	
	/**
	 * 删除报销单,报销单行和核销预提明细
	 * @param aggvo
	 * @return
	 * @throws BusinessException
	 */
	private MessageVO[] onDeleteBill(JKBXVO aggvo) throws BusinessException {
		List<JKBXVO> list = new ArrayList<JKBXVO>();
		list.add(aggvo);
		MessageVO[] message = null;
		if(aggvo!=null){
//			HashMap eParam = new HashMap();
//			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
//					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			if(aggvo.getParentVO().getSpzt()==1){
//				getPfBusiAction().processAction(IPFActionName.UNAPPROVE, "264X", null,
//						oldaggvo, null, null);
//				BXVO aggvo = (BXVO) getBillVO(
//						BXVO.class, "isnull(dr,0)=0 and pk_jkbx = '" + headVO.getZyx44()
//						+ "'");
//				getBXBillPublicService().deleteBills(new JKBXVO[]{aggvo});
				BXVO prebillvo=new BXVO();
				IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
				Object returnobj= pfaction.processAction(IPFActionName.UNAPPROVE, "264X", null,aggvo , null, null);
				if(((Object[])returnobj)[0] instanceof nc.vo.erm.common.MessageVO){
					prebillvo=(BXVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj)[0]).getSuccessVO();
				}
				if(prebillvo!=null){
					prebillvo = (BXVO) pfaction.processAction(IPFActionName.UNSAVE, "264X", null,prebillvo , null, null);
				}
//				if(((Object[])returnobj)[0] instanceof nc.vo.erm.common.MessageVO){
//					prebillvo=(BXVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj)[0]).getSuccessVO();
//				}
				message = getBXBillPublicService().deleteBills(new BXVO[]{prebillvo});
			}else{
				message = getBXBillPublicService().deleteBills(list.toArray(new JKBXVO[0]));
			}
		}
		return message;
	}
	
	/**
	 * 新增报销单,报销单行和核销预提明细
	 * @param oldPk 
	 * @param oldBillNo 
	 * @param headVO
	 * @param itemBodyVOs
	 * @param verifyBodyVOs 
	 * @return
	 * @throws BusinessException
	 */
	private JKBXVO onInsertBill(String oldPk, String oldBillNo, OutsideJKBXHeadVO headVO, List<OutsideJKBXBodyItemVO> itemBodyVOs, List<OutsideJKBXAccruedVerifyBodyVO> verifyBodyVOs) throws BusinessException {
		
		if(headVO==null||"null".equals(headVO)){
			throw new BusinessException("报销单表头为空,请检查参数设置");
		}
		if(itemBodyVOs==null||"null".equals(itemBodyVOs)){
			throw new BusinessException("报销单表体单据信息为空,请检查参数设置");
		}
		if(verifyBodyVOs==null||"null".equals(verifyBodyVOs)){
			throw new BusinessException("预提明细信息为空,请检查参数设置");
		}
//		BXBusItemVO[] itemvoArray = itemvos.toArray(new BXBusItemVO[0]);
//		AccruedDetailVO[] verifyArray = detailvos.toArray(new AccruedDetailVO[0]);
		JKBXVO aggvo = new BXVO(); 
		List<JKBXVO> list = new ArrayList<JKBXVO>();
		BXHeaderVO  headvo = new BXHeaderVO();//表头数据
		List<BXBusItemVO> busitemvos = new LinkedList<BXBusItemVO>();//单据明细vo
		BXBusItemVO busitemvo = null;
		List<AccruedVerifyVO> verifyvos = new LinkedList<AccruedVerifyVO>();//核销预提明细
		AccruedVerifyVO verifyvo = null;
//		BXBusItemVO[] busitemvos = new BXBusItemVO[itemBodyVOs.size()];//单据明细vo
//		AccruedVerifyVO[] verifyvos= new AccruedVerifyVO[verifyBodyVOs.size()];//核销预提明细
		
		//表头信息(由于外系统表头字段与nc报销单表头字段不匹配,故此处表头需要额外新增一个外系统作为javabean来翻译)
		headvo.setDr(0);
		headvo.setStatus(VOStatus.NEW);
		headvo.setDjdl("bx");//单据大类
		headvo.setDjlxbm("264X-Cxx-006");//交易类型编码
		headvo.setPk_tradetypeid(getBillTypePkByCode("264X-Cxx-006",getPk_orgByCode("0001")));//交易类型pk
		headvo.setCreationtime(new UFDateTime());//创建时间
		headvo.setCreator(getUserIDByCode("EBS"));//创建人
		headvo.setDjrq(new UFDate(headVO.getDjrq()));//单据日期
		headvo.setDjzt(1);//单据状态
		headvo.setFlexible_flag(UFBoolean.FALSE);//是否柔性控制
		headvo.setIscheck(UFBoolean.FALSE);//是否限额
		headvo.setIscostshare(UFBoolean.FALSE);//是否分摊
		headvo.setIsexpamt(UFBoolean.FALSE);//是否待摊
		headvo.setIsmashare(UFBoolean.FALSE);//申请单是否分摊
		headvo.setIsneedimag(UFBoolean.FALSE);//是否影像扫描
		headvo.setIsexpedited(UFBoolean.FALSE);//是否紧急
		headvo.setKjnd(headVO.getDjrq().substring(0, 4));//会计年度
		headvo.setKjqj(headVO.getDjrq().substring(5, 7));//会计期间
		headvo.setPayflag(1);//支付状态
		headvo.setPk_billtype("264X");//单据类型
		headvo.setPk_group(getPk_orgByCode("0001"));//集团
		headvo.setPk_pcorg("pk_pcorg");//原利润中心
//		headvo.setPk_tradetypeid(headVO.getPk_tradetypeid());//交易类型pk
		headvo.setQcbz(UFBoolean.FALSE);//期初标识
		headvo.setQzzt(0);
		headvo.setSpzt(-1);//审批状态
		headvo.setSxbz(0);//生效状态
		headvo.setPaytarget(1);//收款对象
		headvo.setBbhl(new UFDouble(1));//本币汇率
		headvo.setOperator(getUserPkByCode("TY01"));//经办人1001ZZ100000001MR73Z
//		headvo.setOperator("1001ZZ100000001MR73Z");
		if(StringUtils.isNotBlank(oldPk)&&StringUtils.isNotBlank(oldBillNo)){
			headvo.setPk_jkbx(oldPk);
			headvo.setDjbh(oldBillNo);
		}
		
		
//		headvo.setBzbm(getPkcurrtypeByCode("CNY"));//币种
		headvo.setBzbm(headVO.getBzbm());//币种
		headvo.setPk_org(getPk_orgByCode(headVO.getPk_org()));//出账公司
		headvo.setPk_org_v(getPk_org_vByCode(headVO.getPk_org()));//原报销人单位组织版本
		headvo.setPk_payorg(getPk_orgByCode(headVO.getPk_org()));//原支付组织
		headvo.setPk_payorg_v(getPk_org_vByCode(headVO.getPk_org()));//原支付组织版本
		headvo.setPk_fiorg(getPk_orgByCode(headVO.getPk_org()));//财务组织(作废)
//		headvo.setJkbxr(headVO.getJkbxr());//经办人getPsndocPkByCode
		headvo.setJkbxr(getUserPkByCode("TY01"));//经办人
		headvo.setZyx6(headVO.getZyx6());//板块
		headvo.setTotal(new UFDouble(headVO.getTotal()));//本次冲预提金额金额
		headvo.setYbje(new UFDouble(headVO.getTotal()));//原币金额
		headvo.setHbbm(getCustPkByCode(headVO.getHbbm()));//供应商
		headvo.setFydwbm(getPk_orgByCode(headVO.getFydwbm()));//原费用承担单位
		headvo.setFydwbm_v(getPk_org_vByCode(headVO.getFydwbm()));//原费用承担部门版本(经办单位)
		headvo.setDwbm(getPk_orgByCode(headVO.getFydwbm()));//原报销人单位
		headvo.setDwbm_v(getPk_org_vByCode(headVO.getFydwbm()));//原报销人单位组织版本
//		headvo.setDeptid(headVO.getDeptid());//经办部门
		headvo.setDeptid(getPsnjobMainDeptByPk(getPsnPkByCode("TY01")).get("pk_dept"));//经办部门
		headvo.setDeptid_v(getDept_v_pk(getPsnjobMainDeptByPk(getPsnPkByCode("TY01")).get("pk_dept")).get("pk_vid"));//原经办部门版本
//		headvo.setDeptid_v(getDeptpksByCode("00002122").get("pk_vid"));
		headvo.setFydeptid(getPsnjobMainDeptByPk(getPsnPkByCode("TY01")).get("pk_dept"));//原费用承担部门
		headvo.setFydeptid_v(getDept_v_pk(getPsnjobMainDeptByPk(getPsnPkByCode("TY01")).get("pk_dept")).get("pk_vid"));//费用承担部门
		headvo.setZyx2(changeNull(headVO.getZyx2()));//EBS合同编码
		headvo.setZyx9(changeNull(headVO.getZyx9()));//EBS合同名称
		headvo.setZyx32(changeNull(headVO.getZyx32()));//EBS合同ID
		headvo.setZyx33(changeNull(headVO.getZyx33()));//EBS合同类型
		headvo.setZyx34(changeNull(headVO.getZyx34()));//EBS合同细类
//		headvo.setReceiver(receiver);//收款人
		
		//金额
		UFDouble amount = UFDouble.ZERO_DBL;
		//单据明细信息
		for(int i=0;i<itemBodyVOs.size();i++){
			busitemvo = new BXBusItemVO();
			busitemvo.setDr(0);
			busitemvo.setStatus(VOStatus.NEW);
			busitemvo.setAmount(new UFDouble(itemBodyVOs.get(i).getAmount()));//金额
			busitemvo.setYbje(new UFDouble(itemBodyVOs.get(i).getAmount()));//原币金额
			busitemvo.setBbje(new UFDouble(itemBodyVOs.get(i).getAmount()));//本币金额
			busitemvo.setZfybje(new UFDouble(itemBodyVOs.get(i).getAmount()));//支付原币金额
			busitemvo.setZfbbje(new UFDouble(itemBodyVOs.get(i).getAmount()));//支付本币金额
			busitemvo.setTablecode("arap_bxbusitem");//页签编码
			busitemvo.setDefitem12(changeNull(itemBodyVOs.get(i).getDefitem12()));//预算科目
			busitemvo.setJobid(changeNull(itemBodyVOs.get(i).getJobid()));//项目
			busitemvo.setDefitem22(changeNull(itemBodyVOs.get(i).getDefitem22()));//业态
			busitemvo.setDefitem26(changeNull(itemBodyVOs.get(i).getDefitem26()));//车牌部门
			busitemvo.setDefitem24(changeNull(itemBodyVOs.get(i).getDefitem24()));//部门楼层
			busitemvo.setJkbxr(itemBodyVOs.get(i).getJkbxr());//用款人
			busitemvo.setDefitem49(changeNull(itemBodyVOs.get(i).getDefitem49()));//比例
			busitemvo.setDefitem30(changeNull(itemBodyVOs.get(i).getDefitem30()));//说明
			busitemvo.setHbbm(getCustPkByCode(headVO.getHbbm()));//供应商(这里只会有一个供应商,取表头)
			amount = amount.add(busitemvo.getAmount());
			busitemvos.add(busitemvo);
		}
		headvo.setTotal(amount);
		
		//核销金额
		UFDouble verifyAmount = UFDouble.ZERO_DBL;
		//核销预提明细
		for(int i=0;i<verifyBodyVOs.size();i++){
			verifyvo = new AccruedVerifyVO();
			verifyvo.setDr(0);
			verifyvo.setStatus(VOStatus.NEW);
			verifyvo.setVerify_amount(new UFDouble(verifyBodyVOs.get(i).getVerify_amount()));//核销金额
			verifyvo.setOrg_verify_amount(new UFDouble(verifyBodyVOs.get(i).getVerify_amount()));//组织本币核销金额
			verifyvo.setPk_accrued_detail(verifyBodyVOs.get(i).getPk_accrued_detail());//预提明细行
			verifyvo.setAccrued_billno(verifyBodyVOs.get(i).getAccrued_billno());//预提单据编号
			verifyvo.setPk_accrued_bill(getAccruedPkByCode(verifyBodyVOs.get(i).getAccrued_billno()));//费用预提单pk
			verifyvo.setVerify_date(new UFDate(verifyBodyVOs.get(i).getVerify_date()));//核销日期
			verifyvo.setPk_group(getPk_orgByCode("0001"));//默认时代集团
			verifyvo.setPk_org(getPk_orgByCode(headVO.getPk_org()));//组织
			verifyvo.setVerify_man("#EBS#");
			verifyAmount = verifyAmount.add(new UFDouble(verifyBodyVOs.get(i).getVerify_amount()));//核销金额
			verifyvos.add(verifyvo);
		}
		
		aggvo.setParentVO(headvo);
		aggvo.setChildrenVO(busitemvos.toArray(new BXBusItemVO[0]));
		aggvo.setTableVO("er_accrued_verify", verifyvos.toArray(new AccruedVerifyVO[0]));
		
		checkBxVOValid(aggvo);// 校验是否可进行核销预提
		list.add(aggvo);
		JKBXVO[] jkbxAggvo = getBXBillPublicService().save(list.toArray(new JKBXVO[0]));
		
		return jkbxAggvo[0];
		
	}
	
	/**
	 * 更新报销单,报销单行和核销预提明细
	 * @param aggvo
	 * @param headVO
	 * @param itemBodyVOs
	 * @param verifyBodyVOs
	 * @return
	 * @throws BusinessException
	 */
	private JKBXVO onUpdateBill(JKBXVO oldaggvo, OutsideJKBXHeadVO headVO, List<OutsideJKBXBodyItemVO> itemBodyVOs, List<OutsideJKBXAccruedVerifyBodyVO> verifyBodyVOs) throws BusinessException {
		
		String oldPk = oldaggvo.getParentVO().getPrimaryKey();
		String oldBillNo = oldaggvo.getParentVO().getDjbh();
		onDeleteBill(oldaggvo);//更新前先把之前的数据清掉
		return onInsertBill(oldPk,oldBillNo,headVO,itemBodyVOs,verifyBodyVOs);//根据旧的pk和单据编号重新插入
	}
	
	
	private void checkBxVOValid(JKBXVO vo) throws ValidationException {
		StringBuffer bf = new StringBuffer();
		UFBoolean iscostshare = vo.getParentVO().getIscostshare();
		if (iscostshare != null && iscostshare.booleanValue()) {
			bf.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("2011v61013_0", "02011v61013-0120")/*
			 * @
			 * res
			 * "报销单已经设置分摊，不可进行核销预提"
			 */);
		}
		
		UFBoolean isexp = vo.getParentVO().getIsexpamt();
		if (isexp != null && isexp.booleanValue()) {
			bf.append("\n" + NCLangRes4VoTransl.getNCLangRes().getStrByID("2011v61013_0", "02011v61013-0121")/*
			 * @
			 * res
			 * "报销单已经设置待摊，不可进行核销预提"
			 */);
		}
		
		if(bf.length() != 0){
			throw new ValidationException(bf.toString());
		}
	}
	
	/**
	 * 空值校验
	 * @param headvo
	 * @param itemBodyVOs
	 * @param verifyBodyVOs
	 * @throws BusinessException
	 */
	public void checkNull(OutsideJKBXHeadVO  headvo,List<OutsideJKBXBodyItemVO> itemBodyVOs,List<OutsideJKBXAccruedVerifyBodyVO> verifyBodyVOs) throws BusinessException{
		if(StringUtils.isBlank(headvo.getBzbm()))throw new BusinessException("币种不可为空");
		if(StringUtils.isBlank(headvo.getPk_org()))throw new BusinessException("出账公司不可为空");
//		if(StringUtils.isBlank(headvo.getZyx6()))throw new BusinessException("板块不可为空");
		if(StringUtils.isBlank(headvo.getDjrq()))throw new BusinessException("单据日期不可为空");
		if(StringUtils.isBlank(headvo.getTotal()))throw new BusinessException("本次冲预提金额不可为空");
		if(StringUtils.isBlank(headvo.getFydwbm()))throw new BusinessException("费用承担单位不可为空");
		if(StringUtils.isBlank(headvo.getFydeptid()))throw new BusinessException("费用承担部门不可为空");
//		if(StringUtils.isBlank(headvo.getPk_tradetypeid()))throw new BusinessException("交易类型不可为空");
		if(StringUtils.isBlank(headvo.getZyx2()))throw new BusinessException("合同编码不可为空");
		if(StringUtils.isBlank(headvo.getZyx9()))throw new BusinessException("合同名称不可为空");
		if(StringUtils.isBlank(headvo.getZyx32()))throw new BusinessException("合同ID不可为空");
		if(StringUtils.isBlank(headvo.getZyx33()))throw new BusinessException("合同类型不可为空");
		if(StringUtils.isBlank(headvo.getZyx34()))throw new BusinessException("合同细类不可为空");
//		if(StringUtils.isBlank(headvo.getJkbxr()))throw new BusinessException("经办人不可为空");
//		if(StringUtils.isBlank(headvo.getDeptid()))throw new BusinessException("经办部门不可为空");
//		if(StringUtils.isBlank(headvo.getDwbm_v()))throw new BusinessException("经办单位不可为空");
//		if(StringUtils.isBlank(headvo.getPaytarget()))throw new BusinessException("收款对象不可为空");
		if(StringUtils.isBlank(headvo.getHbbm()))throw new BusinessException("供应商不可为空");
		if(StringUtils.isBlank(headvo.getZyx35()))throw new BusinessException("状态不可为空");
		if(StringUtils.isBlank(headvo.getZyx43()))throw new BusinessException("是否审批不可为空");
		int i = 1;
		for (OutsideJKBXBodyItemVO outsideJKBXBodyItemVO : itemBodyVOs) {
			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getDefitem12()))throw new BusinessException("第"+i+"行,预算科目不可为空");
//			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getJobid()))throw new BusinessException("第"+i+"行,项目不可为空");
//			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getDefitem22()))throw new BusinessException("第"+i+"行,业态不可为空");
//			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getDefitem26()))throw new BusinessException("第"+i+"行,车牌部门不可为空");
//			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getDefitem24()))throw new BusinessException("第"+i+"行,部门楼层不可为空");
//			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getJkbxr()))throw new BusinessException("第"+i+"行,用款人不可为空");
			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getAmount()))throw new BusinessException("第"+i+"行,金额不可为空");
			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getDefitem49()))throw new BusinessException("第"+i+"比例不可为空");
//			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getDefitem30()))throw new BusinessException("第"+i+"说明不可为空");
			i++;
		}
		int j = 1;
		for (OutsideJKBXAccruedVerifyBodyVO outsideJKBXAccruedVerifyBodyVO : verifyBodyVOs) {
			if(StringUtils.isBlank(outsideJKBXAccruedVerifyBodyVO.getAccrued_billno()))throw new BusinessException("第"+j+"行,预提单号不可为空");
			if(StringUtils.isBlank(outsideJKBXAccruedVerifyBodyVO.getPk_accrued_detail()))throw new BusinessException("第"+j+"行,预提单行pk不可为空");
			if(StringUtils.isBlank(outsideJKBXAccruedVerifyBodyVO.getVerify_amount()))throw new BusinessException("第"+j+"行,核销金额不可为空");
			if(StringUtils.isBlank(outsideJKBXAccruedVerifyBodyVO.getVerify_date()))throw new BusinessException("第"+j+"行,核销日期不可为空");
			j++;
		}
	}
	
	public IBXBillPublic getBXBillPublicService() {
		if (BXBillPublicService == null) {
			BXBillPublicService = NCLocator.getInstance().lookup(
					IBXBillPublic.class);
		}
		return BXBillPublicService;
	}
	
	private IErmAccruedBillManage getAppService() {
		if (billManagerService == null) {
			billManagerService = NCLocator.getInstance().lookup(
					IErmAccruedBillManage.class);
		}
		return billManagerService;
	}
	
	private String changeNull(String arg){
		if("".equals(arg)||"~".equals(arg)||null==arg){
			return null;
		}
		return arg;
		
	}
	
	BaseDAO baseDAO = null;
	/**
	 * 数据库持久化
	 * 
	 * @return
	 */
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	
	private String getUserIDByCode(String code) throws BusinessException{
		StringBuffer sql = new StringBuffer();
		sql.append("select sm_user.cuserid from sm_user where sm_user.user_code = '"+code+"' and dr = 0 and sm_user.enablestate = 2 ");
		String userid = (String) getBaseDAO().executeQuery(sql.toString(), new ColumnProcessor());
		return userid;
	}
}