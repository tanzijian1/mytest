package nc.bs.tg.outside.itf.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.bs.yypub.tools.formula.ExtFormulaParser;
import nc.itf.tg.outside.ItfConstants;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pub.itf.sort.ItfVOSort;
import nc.vo.baseapp.itfformulacfg.FormulaCfgBVO;
import nc.vo.cc.grpprotocol.AggGroupProtocolVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
/**
 * @Description: ������
 */
public class ItfUtils {
	/**
	 * ��ѯ��ʽ
	 * @param code			��ͷ����
	 * @param direction		������ϵͳ->NC ? NC->��ϵͳ��
	 * @return
	 * @throws BusinessException
	 */
	public static HashMap<String,ArrayList<String>> getFormulas(String code,int direction) throws BusinessException{
		StringBuffer sqlstr = new StringBuffer("");
		sqlstr.append("pk__formulacfg_h = (select pk__formulacfg_h").append("\r\n");
		sqlstr.append("  from ").append(ItfConstants.FORMULAR_HEAD).append("\r\n");
		sqlstr.append(" where code = '");
		sqlstr.append(code);
		sqlstr.append("'").append("\r\n");
		sqlstr.append("   and nvl(dr, 0) = 0) and nvl(dr, 0) = 0 "); 
		List<FormulaCfgBVO> syProConfigBVOs = (List<FormulaCfgBVO>)getQueryBS().retrieveByClause(FormulaCfgBVO.class, sqlstr.toString());
		if(syProConfigBVOs!=null && syProConfigBVOs.size()>0){
			HashMap<String,ArrayList<String>> formulasMap = new HashMap<String,ArrayList<String>>();
			//ת��ǰ�ǿ��ֶ�
			ArrayList<String> notNullField = new ArrayList<String>();
			//ת����ǿ��ֶ�
			ArrayList<String> notNullField2 = new ArrayList<String>();
			//���ظ��ֶ�
			ArrayList<String> notRepField = new ArrayList<String>();
			//��ϵͳ->NC �� NC->��ϵͳ ��ʽ
			ArrayList<String> formula = new ArrayList<String>();
			//NC�ֶα���
			ArrayList<String> nc_field_code = new ArrayList<String>();
			//NCת��ǰ�ֶ�����
			ArrayList<String> nc_field_name = new ArrayList<String>();
			//NCת�����ֶ�����
			ArrayList<String> nc_field_name2 = new ArrayList<String>();
			
			//NC�������ظ�����
			ArrayList<String> nc_notRep_name = new ArrayList<String>();
			//��ϵͳ�ֶα���
			ArrayList<String> wb_field_code = new ArrayList<String>();
			Collections.sort(syProConfigBVOs, new ItfVOSort(direction));
			UFBoolean isenable_nw;
			UFBoolean nullflag;
			UFBoolean nullflag2;
			UFBoolean notRepFlag;
			String field_code="";
			String field_name="";
			UFBoolean isenable_wn;
			String formula_ns="";
			String field_code_w="";
			String field_name_w="";
			String formula_wn="";
			String formula_nw="";
			String vbdef1="";
			String vbdef2="";
			for(FormulaCfgBVO syProConfigBVO:syProConfigBVOs){
				if(ItfConstants.DIRECTION_WB2NC==direction){
					//��ϵͳ->NC
					isenable_wn=(UFBoolean) syProConfigBVO.getAttributeValue("isenable_wn");
					nullflag=(UFBoolean) syProConfigBVO.getAttributeValue("nullflag");
					vbdef2=syProConfigBVO.getAttributeValue("vbdef2")==null?"N":syProConfigBVO.getAttributeValue("vbdef2")+"";
					nullflag2=new UFBoolean(vbdef2);
					vbdef1=syProConfigBVO.getAttributeValue("vbdef1")==null?"N":syProConfigBVO.getAttributeValue("vbdef1")+"";
					notRepFlag=new UFBoolean(vbdef1);
					field_code_w=(String) syProConfigBVO.getAttributeValue("field_code_w");
					field_name_w=(String) syProConfigBVO.getAttributeValue("field_name_w");
					formula_wn=(String) syProConfigBVO.getAttributeValue("formula_wn");
					if(isenable_wn.booleanValue()){
						if(!nullflag.booleanValue()){
							notNullField.add(field_code_w);
							nc_field_name.add(field_name_w);
						}
						if(notRepFlag.booleanValue()){
							notRepField.add(field_code_w);
							nc_notRep_name.add(field_name_w);
						}
						if(!nullflag2.booleanValue()){
							notNullField2.add(field_code_w);
							nc_field_name2.add(field_name_w);
						}
						formula.add(formula_wn);
						
					}
				}else if(ItfConstants.DIRECTION_NC2WB==direction){
					//NC->��ϵͳ
					isenable_nw=(UFBoolean) syProConfigBVO.getAttributeValue("isenable_nw");
					field_code=(String) syProConfigBVO.getAttributeValue("field_code");
					field_name=(String) syProConfigBVO.getAttributeValue("field_name");
					formula_nw=(String) syProConfigBVO.getAttributeValue("formula_nw");
					if(isenable_nw.booleanValue()){
						formula.add(formula_nw);
						nc_field_code.add(field_code_w);
						wb_field_code.add(field_code);
					}
				}
			}
			formulasMap.put(ItfConstants.NOTNULL_BEFORE_FIELD_KEY, notNullField);
			formulasMap.put(ItfConstants.NOTNULL_AFTER_FIELD_KEY, notNullField2);
			formulasMap.put(ItfConstants.FORMULA_KEY, formula);
			formulasMap.put(ItfConstants.NC_FIELD_CODE, nc_field_code);
			formulasMap.put(ItfConstants.WB_FIELD_CODE, wb_field_code);
			formulasMap.put(ItfConstants.NC_BEFORE_FIELD_NAME, nc_field_name);
			formulasMap.put(ItfConstants.NC_AFTER_FIELD_NAME, nc_field_name2);
			formulasMap.put(ItfConstants.NC_NOTREP_CODE, notRepField);
			formulasMap.put(ItfConstants.NC_NOTREP_NAME, nc_notRep_name);
			return formulasMap;
		}
		return null;
	}
	
	/**
	 * �ǿ�У��+ִ�й�ʽ
	 * @param hConfigCode
	 * 				��ͷ��ʽ���ñ���
	 * @param bConfigCode
	 * 				���幫ʽ���ñ���
	 * @param headVO
	 * 				��ͷVO
	 * @param bodyVOs
	 * 				����VO
	 * @throws BusinessException
	 */
	public static void notNullCheckAndExFormula(String hConfigCode,String bConfigCode,AggregatedValueObject aggVO) throws Exception {
		HashMap<String,ArrayList<String>> hFormulaone = getFormulas(hConfigCode,ItfConstants.DIRECTION_WB2NC);
		HashMap<String,ArrayList<String>> bFormulaone = getFormulas(bConfigCode,ItfConstants.DIRECTION_WB2NC);
		List<String> headKeys_before = hFormulaone.get(ItfConstants.NOTNULL_BEFORE_FIELD_KEY);
		List<String> headKeys_after = hFormulaone.get(ItfConstants.NOTNULL_AFTER_FIELD_KEY);
		List<String> notRepKeys = hFormulaone.get(ItfConstants.NOTREP_FIELD_KEY);
		List<String> headKeYsName_before = hFormulaone.get(ItfConstants.NC_BEFORE_FIELD_NAME);
		List<String> headKeYsName_after = hFormulaone.get(ItfConstants.NC_AFTER_FIELD_NAME);
		
		List<String> notNotkeys = hFormulaone.get(ItfConstants.NC_NOTREP_CODE);
		List<String> notNotNames = hFormulaone.get(ItfConstants.NC_NOTREP_NAME);
		List<String> bodyKeys_before = new ArrayList<String>();
		List<String> bodyKeys_after = new ArrayList<String>();
		List<String> bodyKeysName_before = new ArrayList<String>();
		List<String> bodyKeysName_after = new ArrayList<String>();
		if(bFormulaone!=null){
			bodyKeys_before=bFormulaone.get(ItfConstants.NOTNULL_BEFORE_FIELD_KEY);	
			bodyKeys_after=bFormulaone.get(ItfConstants.NOTNULL_AFTER_FIELD_KEY);
			bodyKeysName_before=bFormulaone.get(ItfConstants.NC_BEFORE_FIELD_NAME);	
			bodyKeysName_after=bFormulaone.get(ItfConstants.NC_AFTER_FIELD_NAME);	
		}
		
		
		ArrayList<String> hFormula = hFormulaone.get(ItfConstants.FORMULA_KEY);
		ArrayList<String> bFormula = new ArrayList<String>(); 
		if(bFormulaone!=null){
			bFormula =bFormulaone.get(ItfConstants.FORMULA_KEY);
		}	
		ExtFormulaParser extFormulaParser = new ExtFormulaParser();
		if(aggVO instanceof BXVO){//������
			
			BXVO bxvo=(BXVO)aggVO;
			notNullCheck(headKeys_before,headKeYsName_before,bodyKeysName_before,bodyKeys_before, aggVO.getParentVO(), aggVO.getChildrenVO(),1);	
			extFormulaParser.execFormulas(hFormula.toArray(new String[0]), bFormula.toArray(new String[0]), bxvo, null);
			notNullCheck(headKeys_after,headKeYsName_after,bodyKeysName_after,bodyKeys_after, aggVO.getParentVO(),aggVO.getChildrenVO(),2);
			checkRep(notNotkeys,notNotNames, aggVO.getParentVO());
			
		}else if(aggVO instanceof AggGroupProtocolVO){
			AggGroupProtocolVO aggplvo=(AggGroupProtocolVO) aggVO;
			notNullCheck(headKeys_before,headKeYsName_before,bodyKeysName_before,bodyKeys_before, aggplvo.getParentVO(),aggplvo.getTableVO("cctype"),1);
			extFormulaParser.execFormulas(hFormula.toArray(new String[0]),null,aggplvo);
			extFormulaParser.execFormulas(bFormula.toArray(new String[0]),aggplvo.getTableVO("cctype"));
			notNullCheck(headKeys_after,headKeYsName_after,bodyKeysName_after,bodyKeys_after, aggplvo.getParentVO(),aggplvo.getTableVO("cctype"),2);
			checkRep(notNotkeys,notNotNames, aggVO.getParentVO());
		}else{
			notNullCheck(headKeys_before,headKeYsName_before,bodyKeysName_before,bodyKeys_before, aggVO.getParentVO(), aggVO.getChildrenVO(),1);
			extFormulaParser.execFormulas(hFormula.toArray(new String[0]), bFormula.toArray(new String[0]),aggVO);
			notNullCheck(headKeys_after,headKeYsName_after,bodyKeysName_after,bodyKeys_after, aggVO.getParentVO(),aggVO.getChildrenVO(),2);
			checkRep(notNotkeys,notNotNames, aggVO.getParentVO());
		}		
	}
	
	/**
	 * �ǿ��ֶ�У��
	 * @param headKeys
	 * 			��ͷ�ֶ�
	 * @param bodyKeys
	 * 			�����ֶ�
	 * @param bodyKeys2 
	 * @param bodyKeysName 
	 * @param headVO
	 * 			��ͷVO
	 * @param bodyVOs
	 * 			����VO
	 * @param type
	 * 			1����һ�ηǿ�У�飬2��ת����ǿ�У��
	 * @throws Exception
	 */
	public static void notNullCheck(List<String> headKeys,List<String> headKeYsName,List<String> bodyKeysName, List<String> bodyKeys, CircularlyAccessibleValueObject headVO,CircularlyAccessibleValueObject[] bodyVOs,int type) throws Exception{
		Set<String> keySetName = new HashSet<String>();
		Object value;
		if(CollectionUtils.isNotEmpty(headKeys) && headVO!=null){
			for (int i = 0; i < headKeys.size(); i++) {
				String key=headKeys.get(i);
				value = headVO.getAttributeValue(key);
				if(value==null || StringUtil.isEmptyWithTrim(value.toString())){
					keySetName.add("��ͷ"+headKeYsName.get(i)+"("+headKeys.get(i)+")");
				}
			}
		}
		if(CollectionUtils.isNotEmpty(bodyKeys) && ArrayUtils.isNotEmpty(bodyVOs)){
		     for (int i = 0; i < bodyKeys.size(); i++) {
				for(CircularlyAccessibleValueObject bodyVO:bodyVOs){
					String key=bodyKeys.get(i);
					value = bodyVO.getAttributeValue(key);
					if(value==null || StringUtil.isEmptyWithTrim(value.toString())){
						keySetName.add("����"+bodyKeysName.get(i)+"("+bodyKeys.get(i)+")");
					}
				}
			}
		}
		if(keySetName.size()>0){
			String keyName = StringUtils.join(keySetName.toArray(), "��");
			if(type==1){
				throw new BusinessException( "�����ֶβ���Ϊ�գ�"+keyName);	
			}else{
				throw new BusinessException( "�����ֶ�ת��ʧ�ܣ�"+keyName);	
			}
		}
	}
	
	/**
	 * �Ƿ��ظ�У��
	 * @param headKeys
	 * 			��ͷ�ֶ�
	 * @param bodyKeysName 
	 * @param headVO
	 * 			��ͷVO
	 * @throws Exception
	 */
	public static void checkRep(List<String> headKeys,List<String> headKeYsName, CircularlyAccessibleValueObject headVO) throws Exception{
		String value="";
		StringBuffer condition=new StringBuffer();
		condition.append("1=1");
		StringBuffer sql=new StringBuffer();
		SuperVO superVO=(SuperVO) headVO;
		Set<String> keySetName = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(headKeys) && headVO!=null){
			for (int i = 0; i < headKeys.size(); i++) {
				String key=headKeys.get(i);
				value = headVO.getAttributeValue(key)+"";
				if(value!=null && isNotBlank(value)){
					keySetName.add(value);
					condition.append(" and ").append(key).append("='").append(value).append("'");
				}
			}
			sql.append(" select 1 from ");
			sql.append(superVO.getTableName());
			sql.append(" where ");
			sql.append(condition);
			Object obj= getQueryBS().executeQuery(sql.toString(),new ColumnProcessor());
			String keyName = StringUtils.join(keySetName.toArray(), "+");
			if(isNotBlank(obj)){
				throw new  BusinessException("NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݣ�"+keyName);
			}
		}
	}
	
	/**
	 * �ж��ַ����Ƿ�Ϊ��
	 * @param str
	 */
	public static boolean isNotBlank(Object obj){
		if (obj == null || "null".equals(obj)||"".equals(obj)||"~".equals(obj)){
			return false;
		}
		return true;
	}
	
	public static IUAPQueryBS getQueryBS() {
		return NCLocator.getInstance().lookup(IUAPQueryBS.class);
	}
	
	
	/**
	 * ��ѯNC�ֶα������ϵͳ�������
	 * @param code			��ͷ����
	 * @param direction		������ϵͳ->NC ? NC->��ϵͳ��
	 * @return ��ϵͳ->NC
	 * @throws BusinessException
	 */
	public static HashMap<String,String> getTranNCcodes(String code,int direction) throws BusinessException{
		StringBuffer sqlstr = new StringBuffer("");
		sqlstr.append("pk__formulacfg_h = (select pk__formulacfg_h").append("\r\n");
		sqlstr.append("  from itf_formulacfg_h").append("\r\n");
		sqlstr.append(" where code = '");
		sqlstr.append(code);
		sqlstr.append("'").append("\r\n");
		sqlstr.append("   and nvl(dr, 0) = 0) and nvl(dr, 0) = 0 "); 
		List<FormulaCfgBVO> syProConfigBVOs = (List<FormulaCfgBVO>)getQueryBS().retrieveByClause(FormulaCfgBVO.class, sqlstr.toString());
		if(syProConfigBVOs!=null && syProConfigBVOs.size()>0){
			HashMap<String,String> tranNCcodeMap = new HashMap<String,String>();
			UFBoolean isenable_wn;
			String field_code="";
			for(FormulaCfgBVO syProConfigBVO:syProConfigBVOs){
				if(ItfConstants.DIRECTION_WB2NC==direction){
					isenable_wn=(UFBoolean) syProConfigBVO.getAttributeValue("isenable_wn");
					//��ϵͳ->NC					
					if(isenable_wn.booleanValue()){//�Ƿ�������ϵͳ-nc
						String code_wb=(String) syProConfigBVO.getAttributeValue("field_code_w");
						if(code_wb!=null&&!"".equals(code_wb.trim())&&!"1".equals(code_wb.trim())){
							field_code=(String) syProConfigBVO.getAttributeValue("field_code");
							tranNCcodeMap.put(code_wb,field_code);
						}
					}					
				}
			}
			return tranNCcodeMap;
		}
		return null;
	}
}
