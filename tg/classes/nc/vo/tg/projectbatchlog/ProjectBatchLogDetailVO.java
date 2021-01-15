package nc.vo.tg.projectbatchlog;

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
 * �˴�����۵�������Ϣ
 * </p>
 * ��������:2020-3-30
 * 
 * @author YONYOU NC
 * @version NCPrj ??
 */

public class ProjectBatchLogDetailVO extends SuperVO {

	/**
	 * �ϲ㵥������
	 */
	public static final String PK_PROJECTBATCHLOG = "pk_projectbatchlog";
	/**
	 * ʱ���
	 */
	public static final String TS = "ts";;

	/**
	 * ���� �����ϲ�������Getter����.���������ϲ����� ��������:2020-3-30
	 * 
	 * @return String
	 */
	public String getPk_projectbatchlog() {
		return (String) this
				.getAttributeValue(ProjectBatchLogDetailVO.PK_PROJECTBATCHLOG);
	}

	/**
	 * ���������ϲ�������Setter����.���������ϲ����� ��������:2020-3-30
	 * 
	 * @param newPk_projectbatchlog
	 *            String
	 */
	public void setPk_projectbatchlog(String pk_projectbatchlog) {
		this.setAttributeValue(ProjectBatchLogDetailVO.PK_PROJECTBATCHLOG,
				pk_projectbatchlog);
	}

	/**
	 * ���� ����ʱ�����Getter����.��������ʱ��� ��������:2020-3-30
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(ProjectBatchLogDetailVO.TS);
	}

	/**
	 * ��������ʱ�����Setter����.��������ʱ��� ��������:2020-3-30
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(ProjectBatchLogDetailVO.TS, ts);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta(
				"tg.ProjectBatchLogDetailVO");
	}
}
