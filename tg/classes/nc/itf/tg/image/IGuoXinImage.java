package nc.itf.tg.image;

import java.util.HashMap;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public interface IGuoXinImage {
	/**
	 * �������ʹ���ӿ�
	 * @param vbilltype ��������
	 * @param billid ����id
	 * @param pk_org ��˾����
	 * @param scanUser ɨ�������
	 * @param billMny ���ݽ��
	 * @param scantype ɨ������
	 * @param billno ���ݺ�
	 * @param billInfo
	 * @throws DAOException 
	 * @throws BusinessException
	 */
	public void startWorkFlow(String vbilltype, String billid, String pk_org,
			String scanUser, UFDouble billMny, String scantype, String billno,HashMap<String, String> map) throws BusinessException;
	
	
	/**
	 * ���Ų鿴Ӱ��ӿ�
	 * @param barcode Ӱ�����
	 * @return URL
	 * @throws BusinessException
	 */
	public String createImagePath(String barcode) throws BusinessException;
	
	/**
	 *  ���ݹ���Ӱ����ҷ�Ʊ
	 * @param barcode
	 * @return VO
	 * @throws BusinessException
	 */
	public String createImageInv(String barcode) throws BusinessException;
	
	/**
	 *  ncɾ����������Ӱ���˵�
	 * @param barcode Ӱ�������
	 * @param type �������ͣ�(1/���˵���2/�ʽ𵥾�)
	 * @param useraccount �˵����ʺ�
	 * @param username �˵�������
	 * @param phone �˵��˵绰
	 * @param remark �˵�����
	 * @param billtype �Ƿ�����(0������ 1 ������ Ĭ��1)
	 * @param optype �������ͣ�(1/���ϴ���(ֻ��״̬)��2/ɾ�����ݣ�Ĭ��1)
	 * @return
	 * @throws BusinessException
	 */
	public String delrefund(String barcode,String type,String useraccount,String username,String phone,String remark,String billtype,String optype ) throws BusinessException;
}
