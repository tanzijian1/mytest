package nc.bs.tg.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.itf.bd.material.marbasclass.IMaterialBasClassService;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.bd.material.marbasclass.MarBasClassVO;
import nc.vo.pub.BusinessException;

/**
 * SRM���Ϸ���ͬ����NC���ϻ������൵��
 * @author zhaozhiying
 *
 */
public class AoutSysncSRMMaterialToNCTask implements IBackgroundWorkPlugin {
	
	BaseDAO baseDAO = null;
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc) throws BusinessException {
		//��ȡSRM������Ϣ
		try{
			PreAlertObject retObj = new PreAlertObject();
			String lLCode = getCatagoryPkByCode("LL");
			
			int start = 0;
			int end = 500;
			//��ҳ��ȡ
			while(true){
				List<Map<String,Object>> result = getSRMMaterialInfo(start,end);
				start = start+500;
				end = end+500;
				if(result != null){
					//������
					for (Map<String, Object> map : result) {
						//���
						convertAndInsertMaterial(map,lLCode);
					}
				}else {
					break;
				}
			}
			
			retObj.setReturnObj("ҵ�����ɹ�ִ�����.");
			//retObj.setReturnObj("ҵ�����ɹ�ִ�����,������Ҫͬ�������ϻ�������.");
			retObj.setReturnType(PreAlertReturnType.RETURNMESSAGE);
			return retObj;
			
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		//return null;
	}
	
	
	/**
	 * ��ȡͬ������
	 * @return
	 */
	private List<Map<String,Object>> getSRMMaterialInfo(Integer start,Integer end){
		List<Map<String,Object>> result = null;
		//String sql = "select * from cux_material_items_desc_v@link_sie";
		
		String page_sql = "SELECT * FROM ("
				+ "	SELECT ROWNUM AS rowno, t.* "
				+ "FROM cux_material_items_desc_v@link_sie t "
				+ "WHERE ROWNUM < "+end+") table_alias "
				+ "WHERE table_alias.rowno >= "+ start;
		
		String disctpage_sql = "SELECT * FROM ("
				+ "	SELECT ROWNUM AS rowno, t.* ,row_number() over(partition by categoryNumber order by ROWNUM asc,itemid asc) rn"
				+ "	FROM cux_material_items_desc_v@link_sie t "
				+ "	WHERE ROWNUM < "+end+") table_alias "
				+ "	WHERE table_alias.rowno >= "+ start +" and table_alias.rn = 1";
		
		String successpage_sql = "SELECT * FROM ("
				+ "	select ROWNUM AS rowno,a.* from ("
				+ "	SELECT t.* ,row_number() over(partition by categoryNumber order by ROWNUM asc,itemid asc) rn"
				+ "	FROM cux_material_items_desc_v@appstest t ) a where rn=1  )table_alias WHERE rowno >= "+ start +"  and rowno < "+end;
		
		try{
			result = (List<Map<String,Object>>) getBaseDAO().executeQuery(successpage_sql, new MapListProcessor());
			if (result != null && result.size() > 0) {
				return result;
			}
		} catch (BusinessException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	private void convertAndInsertMaterial(Map<String, Object> map, String lLCode) throws BusinessException{
		MarBasClassVO classVo = new MarBasClassVO();
		classVo.setPk_group("000112100000000005FD");
		classVo.setPk_org("000112100000000005FD");
		
		//��Ź��������һ�����ļ�����ʹ��SRM�����ϣ�categorynumber���ֶε�ֵ��ȥ����.��,�ڱ��ǰ�ӡ�LL����
		//�硰categorynumber=91.09.10.123������һ����LL91��������LL9109��������LL910910
		
		//------��������Ҳһ����� start ------
		//String categoryNumber = (String)map.get("categorynumber");
		
		//���ݸ÷���ı���ж����Ƿ����
		//String m_code = "LL"+categoryNumber.replace(".", "");
		//String itemid = String.valueOf(map.get("itemid"));
		//�÷���ı��
		//String class_code = m_code+itemid.substring(0, 6);
		//MarBasClassVO vo = getMaterialClassPKBycategoryName(class_code);
		//String categoryName = (String)map.get("longdescription");
		//------��������Ҳһ����� end ------
		
		
		//if(vo == null){
			//�ж����ϼ��������
		//------ֻ������ start--------
			String categoryNumber = (String)map.get("categorynumber");
		//------ֻ������ end--------
		
			if(StringUtils.isNotBlank(categoryNumber)){
				String m_code = "LL"+categoryNumber.replace(".", "");
				//�ϼ�����PK
				String parent_pk = getCatagoryPkByCode(m_code);
				
				if(parent_pk == null){
					//�ļ���������
					String fourthCategory = (String)map.get("fourthcategory");
					//������������
					String thirdCategory  = (String)map.get("thirdcategory");
					//������������
					String secondCategory  = (String)map.get("secondcategory");
					//һ����������
					String firstCategory  = (String)map.get("firstcategory");
					
					//�жϸò㼶�Ƿ����
					String first = "LL"+categoryNumber.replace(".", "").substring(0,2);
					String second = "LL"+categoryNumber.replace(".", "").substring(0,4);
					String third = "LL"+categoryNumber.replace(".", "").substring(0,6);
					String four = "LL"+categoryNumber.replace(".", "");
					
					String pk_parent = null;
					
					//����һ������
					//String pk_first = getCatagoryPkByCode(first);
					MarBasClassVO first_vo = getMaterialClassPKBycategoryName(first);
					if(first_vo == null){
						//����һ��
						pk_parent = createClassVo(first,firstCategory,lLCode);
					}else {
						if(!firstCategory.equals(first_vo.getName())){
							//�޸�����
							updateClassVO(firstCategory,first);
						}
						pk_parent = first_vo.getPk_marbasclass();
					}
					
					//��������
					String child_second = null;
					if(StringUtils.isNotBlank(secondCategory)){
						if(first_vo == null){
							child_second = createClassVo(second,secondCategory,pk_parent);
							pk_parent = child_second;
						}else {
							//��ȡ֮ǰ�ķ���
							MarBasClassVO second_vo = getMaterialClassPKBycategoryName(second);
							if(second_vo != null && !secondCategory.equals(second_vo.getName())){
								//�޸�����
								updateClassVO(secondCategory,second);
								pk_parent = second_vo.getPk_marbasclass();
							}else if(second_vo == null){
								child_second = createClassVo(second,secondCategory,pk_parent);
								pk_parent = child_second;
							}else {
								pk_parent = second_vo.getPk_marbasclass();
							}
						}	
					}
					
					//��������
					String child_third = null;
					if(StringUtils.isNotBlank(thirdCategory)){
						if(first_vo == null){
							child_third = createClassVo(third,thirdCategory,pk_parent);
							pk_parent = child_third;
						}else{
							//��ȡ֮ǰ�ķ���
							MarBasClassVO third_vo = getMaterialClassPKBycategoryName(third);
							if(third_vo != null && !thirdCategory.equals(third_vo.getName())){
								//�޸�����
								updateClassVO(thirdCategory,third);
								pk_parent = third_vo.getPk_marbasclass();
							}else if(third_vo == null){
								child_third = createClassVo(third,thirdCategory,pk_parent);
								pk_parent = child_third;
							}else {
								pk_parent = third_vo.getPk_marbasclass();
							}
						}
					}
					
					//�����ļ�
					if(StringUtils.isNotBlank(fourthCategory)){
						if(first_vo == null){
							String pk_four = createClassVo(four,fourthCategory,pk_parent);
							pk_parent = pk_four;
						}else{
							//��ȡ֮ǰ�ķ���
							MarBasClassVO four_vo = getMaterialClassPKBycategoryName(four);
							if(four_vo != null && !thirdCategory.equals(four_vo.getName())){
								//�޸�����
								updateClassVO(fourthCategory,four);
								pk_parent = four_vo.getPk_marbasclass();
							}else if(four_vo == null){
								String pk_four = createClassVo(four,fourthCategory,pk_parent);
								pk_parent = pk_four;
							}else {
								pk_parent = four_vo.getPk_marbasclass();
							}
						}
					}		
					
					//���
					//createClassVo(class_code,categoryName,pk_parent);
					
				}/*else{
					//ֱ�����
					createClassVo(class_code,categoryName,parent_pk);
				}*/
			}
			/*}else if(!categoryName.equals(vo.getName())){
			updateClassVO(categoryName,class_code);
		}*/
		
	}
	
	
	/**
	 * ���ݱ����ѯ���ϻ�����Ϣ
	 * @param categoryName
	 * @return
	 */
	private MarBasClassVO getMaterialClassPKBycategoryName(String code){
		String sql = "select * from bd_marbasclass where code ='"+ code + "' and enablestate = '2'";
		//String result = null;
		MarBasClassVO classVO = null;
		try{
			//result = (String) getBaseDAO().executeQuery(sql,new ColumnProcessor());
			classVO = (MarBasClassVO)getBaseDAO().executeQuery(sql,new BeanProcessor(MarBasClassVO.class));
		}catch(Exception e){
			e.printStackTrace();
		}
		return classVO;
	}
	
	/**
	 * ���ݷ�������ȡID
	 * @return
	 */
	private String getCatagoryPkByCode(String code){
		String sql = "select pk_marbasclass from bd_marbasclass where code ='"+ code + "' and enablestate = '2'";
		String result = null;
		try{
			result = (String) getBaseDAO().executeQuery(sql,new ColumnProcessor());
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ���
	 * @param code
	 * @param categoryName
	 * @param pk_parent
	 * @return
	 * @throws BusinessException
	 */
	public String createClassVo(String code,String categoryName,String pk_parent) throws BusinessException{
		MarBasClassVO classVo = new MarBasClassVO();
		classVo.setPk_group("000112100000000005FD");
		classVo.setPk_org("000112100000000005FD");
		classVo.setCode(code);
		classVo.setDataoriginflag(0);
		classVo.setEnablestate(2);
		classVo.setName(categoryName);
		classVo.setPk_parent(pk_parent);
		try{
			//MarBasClassVO returnMarBasClssVO = new MaterialBasClassServiceImpl().insertMaterialBasClass(classVo);
			MarBasClassVO returnMarBasClssVO = NCLocator.getInstance().lookup(IMaterialBasClassService.class).insertMaterialBasClass(classVo);
			//boolean success = (boolean) data.get("success");
			return returnMarBasClssVO.getPk_marbasclass();
		}catch(BusinessException e){
			throw new BusinessException(e.getMessage());
		}
	}
	
	/**
	 * �޸ķ��������
	 * @param resultMap
	 * @param key
	 * @throws DAOException
	 */
	public void updateClassVO(String categoryName,String code) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append("update bd_marbasclass set name = '"+categoryName+"' ");
		sql.append("where code = '"+code+"' and enablestate = '2'");
		getBaseDAO().executeUpdate(sql.toString());
	}

}
