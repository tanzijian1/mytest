package nc.itf.tg.outside;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public interface IImageFileUploadService {
	
	/**
	 * ���͸�����Ӱ�񸽼�ϵͳ
	 * 
	 * @param urlstr Ӱ�񸽼���ַ
	 * @param fromData Я����Ϣ
	 * @param files �ļ�
	 * @return �������
	 * @throws BusinessException
	 */
	public String uploadFile(String urlstr,Map<String, String> fromData,File[] files) throws BusinessException;
	
	
	public String uploadFileByByte(String urlstr,Map<String, String> fromData,byte[] fileBuffer,String filname) throws BusinessException;
	
	public String uploadFileByStream(String urlstr,Map<String, String> fromData, InputStream inputStream, String filname) throws BusinessException;
	
	/**
	 * ����ƴ�Ӳ�ѯ������ѯ��������
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	public AggregatedValueObject getBillVo(Class c, String whereCondStr)  throws BusinessException ;
	
	public String deleteFile(String urlstr, Map<String, String> fromData) throws BusinessException;
}
