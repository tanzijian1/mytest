package nc.itf.tg;

import java.util.List;
import java.util.Map;

import nc.vo.arap.agiotage.ArapBusiDataVO;
import nc.vo.erm.accruedexpense.AggAccruedBillVO;
import nc.vo.fip.operatinglogs.OperatingLogVO;
import nc.vo.hzvat.outputtax.AggOutputTaxHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

public interface ISqlThread {
	public AggAccruedBillVO ytinsert_RequiresNew(AggAccruedBillVO vo)
			throws BusinessException;

	public AggAccruedBillVO ytupdate_RequiresNew(AggAccruedBillVO vo)
			throws BusinessException;

	public AggAccruedBillVO ytunapprove_RequiresNew(AggAccruedBillVO vo)
			throws BusinessException;

	public void billInsert_RequiresNew(SuperVO vo) throws BusinessException;

	public void billupdate_RequiresNew(SuperVO vo) throws BusinessException;

	/**
	 * 单据推影像待办
	 * 
	 * @param billvo
	 * @throws BusinessException
	 */
	public void pushImage(AggregatedValueObject billvo) throws BusinessException;

	public void pushImageAction(AggregatedValueObject billvo)
			throws BusinessException;

	public void update__RequiresNew(SuperVO vo, String[] fileds)
			throws BusinessException;

	public void insertsql_RequiresNew(String sql) throws BusinessException;

	/**
	 * 销售系统回写凭证
	 * 
	 * @param mapdata
	 * @throws BusinessException
	 */
	public void saleSendGL_RequiresNew(Map<String, Object> mapdata)
			throws BusinessException;

	 /**
     * 销售系统退回
     * @param mapdata
     * @throws BusinessException
     */
    public void saleBack(Map<String, Object> mapdata) throws BusinessException;
    
    /**
     * 审批流单据收回
     * @param billType 单据类型
     * @param billId 单据id
     * @param checkResult 审批结果
     * @param checkNote审批意见
     * @param checkman审批人
     * @param actionName操作类型
     * @throws Exception
     */
    public void returnPfAction(String billType, String billId,
			String checkResult, String checkNote, String checkman,String actionName) throws Exception;
 
    /**
     * 批量开票分单开票(控制同一单统一回滚)
     * @param jsonstr
     * @param actiontype
     * @throws Exception
     */
    public AggOutputTaxHVO[] moreOutputinvoice_RequiresNew(String jsonstr, int actiontype) throws Exception;
    
    /**
     * 销售系统定时出账
     * @param mapdata
     * @param type (1为回写出账，2位回写凭证)
     * @param pk_settlement(结算单主键防止定时任务重复上传)
     * @throws BusinessException
     */
    public void salecz_RequiresNew(Map<String, Object> mapdata,int type,String pk) throws BusinessException;
    
    /**
     * 单据退回Bpm
     * @param aggvo
     * @throws BusinessException
     */
    public void addworkbill_RequiresNew(AggregatedValueObject billvo,String returnmsg) throws BusinessException;
    
    /**
     *生成凭证合并规则
     * @param volist
     * @param pk_sumrule
     * @throws BusinessException
     */
    public void genMergeGL_RequiresNew(List<OperatingLogVO> volist,
			String pk_sumrule,String user) throws BusinessException;
    /**
     *  转备案收付款单定时退回及回写
     * @param listmap
     * @throws Exception
     */
    public void sendzpayrec__RequiresNew(Map<String, Object> map) throws BusinessException; 

    public void sendBackpayrec__RequiresNew(Map<String, Object> map)throws BusinessException;

    
    /**
     * 商业系统调用接口方法
     * @param map
     * @throws BusinessException
     */
    public String sendBusiness_RequiresNew(String url,Map<String, Object> map,String pk_settlement)throws BusinessException;
    
    /**
     * 商业收款应收核销
     * @param dlist  (借方数据，应收)
     * @param clist  (贷方数据，收款)
     * @throws BusinessException
     */
    public void insertVerfy_RequiresNew(List<ArapBusiDataVO> dlist,List<ArapBusiDataVO> clist,String user )throws BusinessException;
}
