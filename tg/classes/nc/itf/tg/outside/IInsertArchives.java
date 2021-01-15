package nc.itf.tg.outside;

import java.util.HashMap;

import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSONObject;

public interface IInsertArchives {
	/**
	 * ��������ҵ�����ͺ�ҵ��ϸ�൵��-2020-08-17-tzj
	 * 
	 * @param vo
	 * @throws BusinessException
	 */
	public void insertArchives_RequiresNew(JSONObject data) throws BusinessException;

	/**
	 * ���������շѵ�������-2020-09-16-tzj
	 * 
	 * @param vo
	 * @throws BusinessException
	 */
	public void insertChaProjectArchives_RequiresNew(JSONObject data,
			HashMap<String, String> parentData) throws BusinessException;

}
