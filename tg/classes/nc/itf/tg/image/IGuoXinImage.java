package nc.itf.tg.image;

import java.util.HashMap;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public interface IGuoXinImage {
	/**
	 * 国信推送代办接口
	 * @param vbilltype 单据类型
	 * @param billid 单据id
	 * @param pk_org 公司主键
	 * @param scanUser 扫描根据人
	 * @param billMny 单据金额
	 * @param scantype 扫描类型
	 * @param billno 单据号
	 * @param billInfo
	 * @throws DAOException 
	 * @throws BusinessException
	 */
	public void startWorkFlow(String vbilltype, String billid, String pk_org,
			String scanUser, UFDouble billMny, String scantype, String billno,HashMap<String, String> map) throws BusinessException;
	
	
	/**
	 * 国信查看影像接口
	 * @param barcode 影像编码
	 * @return URL
	 * @throws BusinessException
	 */
	public String createImagePath(String barcode) throws BusinessException;
	
	/**
	 *  根据国信影像查找发票
	 * @param barcode
	 * @return VO
	 * @throws BusinessException
	 */
	public String createImageInv(String barcode) throws BusinessException;
	
	/**
	 *  nc删除报销单推影像退单
	 * @param barcode 影像条码号
	 * @param type 单据类型，(1/报账单，2/资金单据)
	 * @param useraccount 退单人帐号
	 * @param username 退单人姓名
	 * @param phone 退单人电话
	 * @param remark 退单描述
	 * @param billtype 是否重提(0可重提 1 不重提 默认1)
	 * @param optype 操作类型，(1/作废处理(只改状态)，2/删除数据，默认1)
	 * @return
	 * @throws BusinessException
	 */
	public String delrefund(String barcode,String type,String useraccount,String username,String phone,String remark,String billtype,String optype ) throws BusinessException;
}
