package nc.vo.tg.salaryfundaccure;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 *   �˴�����۵�������Ϣ
 * </p>
 *  ��������:2020-8-28
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class SalaryFundAccureItem extends SuperVO {
	
/**
*�ϲ㵥������
*/
public static final String PK_SALARYFUNDACCURE="pk_salaryfundaccure";
/**
*ʱ���
*/
public static final String TS="ts";;
    
    
/**
* ���� �����ϲ�������Getter����.���������ϲ�����
*  ��������:2020-8-28
* @return String
*/
public String getPk_salaryfundaccure(){
return (String)this.getAttributeValue(SalaryFundAccureItem.PK_SALARYFUNDACCURE);
}
/**
* ���������ϲ�������Setter����.���������ϲ�����
* ��������:2020-8-28
* @param newPk_salaryfundaccure String
*/
public void setPk_salaryfundaccure(String pk_salaryfundaccure){
this.setAttributeValue(SalaryFundAccureItem.PK_SALARYFUNDACCURE,pk_salaryfundaccure);
} 
/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2020-8-28
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return (UFDateTime)this.getAttributeValue(SalaryFundAccureItem.TS);
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2020-8-28
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.setAttributeValue(SalaryFundAccureItem.TS,ts);
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.salaryfundaccure_b");
    }
   }
    