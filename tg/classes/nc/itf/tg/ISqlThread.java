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
	 * ������Ӱ�����
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
	 * ����ϵͳ��дƾ֤
	 * 
	 * @param mapdata
	 * @throws BusinessException
	 */
	public void saleSendGL_RequiresNew(Map<String, Object> mapdata)
			throws BusinessException;

	 /**
     * ����ϵͳ�˻�
     * @param mapdata
     * @throws BusinessException
     */
    public void saleBack(Map<String, Object> mapdata) throws BusinessException;
    
    /**
     * �����������ջ�
     * @param billType ��������
     * @param billId ����id
     * @param checkResult �������
     * @param checkNote�������
     * @param checkman������
     * @param actionName��������
     * @throws Exception
     */
    public void returnPfAction(String billType, String billId,
			String checkResult, String checkNote, String checkman,String actionName) throws Exception;
 
    /**
     * ������Ʊ�ֵ���Ʊ(����ͬһ��ͳһ�ع�)
     * @param jsonstr
     * @param actiontype
     * @throws Exception
     */
    public AggOutputTaxHVO[] moreOutputinvoice_RequiresNew(String jsonstr, int actiontype) throws Exception;
    
    /**
     * ����ϵͳ��ʱ����
     * @param mapdata
     * @param type (1Ϊ��д���ˣ�2λ��дƾ֤)
     * @param pk_settlement(���㵥������ֹ��ʱ�����ظ��ϴ�)
     * @throws BusinessException
     */
    public void salecz_RequiresNew(Map<String, Object> mapdata,int type,String pk) throws BusinessException;
    
    /**
     * �����˻�Bpm
     * @param aggvo
     * @throws BusinessException
     */
    public void addworkbill_RequiresNew(AggregatedValueObject billvo,String returnmsg) throws BusinessException;
    
    /**
     *����ƾ֤�ϲ�����
     * @param volist
     * @param pk_sumrule
     * @throws BusinessException
     */
    public void genMergeGL_RequiresNew(List<OperatingLogVO> volist,
			String pk_sumrule,String user) throws BusinessException;
    /**
     *  ת�����ո����ʱ�˻ؼ���д
     * @param listmap
     * @throws Exception
     */
    public void sendzpayrec__RequiresNew(Map<String, Object> map) throws BusinessException; 

    public void sendBackpayrec__RequiresNew(Map<String, Object> map)throws BusinessException;

    
    /**
     * ��ҵϵͳ���ýӿڷ���
     * @param map
     * @throws BusinessException
     */
    public String sendBusiness_RequiresNew(String url,Map<String, Object> map,String pk_settlement)throws BusinessException;
    
    /**
     * ��ҵ�տ�Ӧ�պ���
     * @param dlist  (�跽���ݣ�Ӧ��)
     * @param clist  (�������ݣ��տ�)
     * @throws BusinessException
     */
    public void insertVerfy_RequiresNew(List<ArapBusiDataVO> dlist,List<ArapBusiDataVO> clist,String user )throws BusinessException;
}
