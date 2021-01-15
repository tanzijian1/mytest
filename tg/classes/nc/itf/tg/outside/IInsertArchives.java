package nc.itf.tg.outside;

import java.util.HashMap;

import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSONObject;

public interface IInsertArchives {
	/**
	 * 档案插入业务类型和业务细类档案-2020-08-17-tzj
	 * 
	 * @param vo
	 * @throws BusinessException
	 */
	public void insertArchives_RequiresNew(JSONObject data) throws BusinessException;

	/**
	 * 档案插入收费档案档案-2020-09-16-tzj
	 * 
	 * @param vo
	 * @throws BusinessException
	 */
	public void insertChaProjectArchives_RequiresNew(JSONObject data,
			HashMap<String, String> parentData) throws BusinessException;

}
