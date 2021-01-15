package nc.bs.tg.outside.tobankdoc;

import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.EBSBankdocVO;

public interface InsertBankdoc {
	
	public Map<String,String> executeTask(EBSBankdocVO docVO,String srcsystem,String dectype) throws BusinessException;

}
