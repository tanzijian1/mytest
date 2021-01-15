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
 * SRM物料分类同步至NC物料基本分类档案
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
		//获取SRM物料信息
		try{
			PreAlertObject retObj = new PreAlertObject();
			String lLCode = getCatagoryPkByCode("LL");
			
			int start = 0;
			int end = 500;
			//分页获取
			while(true){
				List<Map<String,Object>> result = getSRMMaterialInfo(start,end);
				start = start+500;
				end = end+500;
				if(result != null){
					//入库分类
					for (Map<String, Object> map : result) {
						//入库
						convertAndInsertMaterial(map,lLCode);
					}
				}else {
					break;
				}
			}
			
			retObj.setReturnObj("业务插件成功执行完毕.");
			//retObj.setReturnObj("业务插件成功执行完毕,暂无需要同步的物料基本分类.");
			retObj.setReturnType(PreAlertReturnType.RETURNMESSAGE);
			return retObj;
			
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		//return null;
	}
	
	
	/**
	 * 获取同步数据
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
		
		//编号规则：如果是一二三四级，则使用SRM的物料（categorynumber）字段的值，去除“.”,在编号前加“LL”，
		//如“categorynumber=91.09.10.123”，则一级：LL91，二级：LL9109，三级：LL910910
		
		//------将其物料也一并入库 start ------
		//String categoryNumber = (String)map.get("categorynumber");
		
		//根据该分类的编号判断其是否存在
		//String m_code = "LL"+categoryNumber.replace(".", "");
		//String itemid = String.valueOf(map.get("itemid"));
		//该分类的编号
		//String class_code = m_code+itemid.substring(0, 6);
		//MarBasClassVO vo = getMaterialClassPKBycategoryName(class_code);
		//String categoryName = (String)map.get("longdescription");
		//------将其物料也一并入库 end ------
		
		
		//if(vo == null){
			//判断其上级分类编码
		//------只入库分类 start--------
			String categoryNumber = (String)map.get("categorynumber");
		//------只入库分类 end--------
		
			if(StringUtils.isNotBlank(categoryNumber)){
				String m_code = "LL"+categoryNumber.replace(".", "");
				//上级分类PK
				String parent_pk = getCatagoryPkByCode(m_code);
				
				if(parent_pk == null){
					//四级分类名称
					String fourthCategory = (String)map.get("fourthcategory");
					//三级分类名称
					String thirdCategory  = (String)map.get("thirdcategory");
					//二级分类名称
					String secondCategory  = (String)map.get("secondcategory");
					//一级分类名称
					String firstCategory  = (String)map.get("firstcategory");
					
					//判断该层级是否存在
					String first = "LL"+categoryNumber.replace(".", "").substring(0,2);
					String second = "LL"+categoryNumber.replace(".", "").substring(0,4);
					String third = "LL"+categoryNumber.replace(".", "").substring(0,6);
					String four = "LL"+categoryNumber.replace(".", "");
					
					String pk_parent = null;
					
					//创建一级分类
					//String pk_first = getCatagoryPkByCode(first);
					MarBasClassVO first_vo = getMaterialClassPKBycategoryName(first);
					if(first_vo == null){
						//创建一级
						pk_parent = createClassVo(first,firstCategory,lLCode);
					}else {
						if(!firstCategory.equals(first_vo.getName())){
							//修改名称
							updateClassVO(firstCategory,first);
						}
						pk_parent = first_vo.getPk_marbasclass();
					}
					
					//创建二级
					String child_second = null;
					if(StringUtils.isNotBlank(secondCategory)){
						if(first_vo == null){
							child_second = createClassVo(second,secondCategory,pk_parent);
							pk_parent = child_second;
						}else {
							//获取之前的分类
							MarBasClassVO second_vo = getMaterialClassPKBycategoryName(second);
							if(second_vo != null && !secondCategory.equals(second_vo.getName())){
								//修改名称
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
					
					//创建三级
					String child_third = null;
					if(StringUtils.isNotBlank(thirdCategory)){
						if(first_vo == null){
							child_third = createClassVo(third,thirdCategory,pk_parent);
							pk_parent = child_third;
						}else{
							//获取之前的分类
							MarBasClassVO third_vo = getMaterialClassPKBycategoryName(third);
							if(third_vo != null && !thirdCategory.equals(third_vo.getName())){
								//修改名称
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
					
					//创建四级
					if(StringUtils.isNotBlank(fourthCategory)){
						if(first_vo == null){
							String pk_four = createClassVo(four,fourthCategory,pk_parent);
							pk_parent = pk_four;
						}else{
							//获取之前的分类
							MarBasClassVO four_vo = getMaterialClassPKBycategoryName(four);
							if(four_vo != null && !thirdCategory.equals(four_vo.getName())){
								//修改名称
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
					
					//入库
					//createClassVo(class_code,categoryName,pk_parent);
					
				}/*else{
					//直接入库
					createClassVo(class_code,categoryName,parent_pk);
				}*/
			}
			/*}else if(!categoryName.equals(vo.getName())){
			updateClassVO(categoryName,class_code);
		}*/
		
	}
	
	
	/**
	 * 根据编码查询物料基本信息
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
	 * 根据分类编码获取ID
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
	 * 入库
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
	 * 修改分类的名称
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
