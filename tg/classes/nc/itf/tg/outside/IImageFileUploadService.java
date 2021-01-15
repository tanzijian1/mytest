package nc.itf.tg.outside;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public interface IImageFileUploadService {
	
	/**
	 * 推送附件到影像附件系统
	 * 
	 * @param urlstr 影像附件地址
	 * @param fromData 携带信息
	 * @param files 文件
	 * @return 结果报文
	 * @throws BusinessException
	 */
	public String uploadFile(String urlstr,Map<String, String> fromData,File[] files) throws BusinessException;
	
	
	public String uploadFileByByte(String urlstr,Map<String, String> fromData,byte[] fileBuffer,String filname) throws BusinessException;
	
	public String uploadFileByStream(String urlstr,Map<String, String> fromData, InputStream inputStream, String filname) throws BusinessException;
	
	/**
	 * 根据拼接查询条件查询单据数据
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	public AggregatedValueObject getBillVo(Class c, String whereCondStr)  throws BusinessException ;
	
	public String deleteFile(String urlstr, Map<String, String> fromData) throws BusinessException;
}
