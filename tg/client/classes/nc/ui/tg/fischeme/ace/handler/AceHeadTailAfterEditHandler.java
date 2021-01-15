package nc.ui.tg.fischeme.ace.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

public class AceHeadTailAfterEditHandler implements
		IAppEventHandler<CardHeadTailAfterEditEvent> {

	IUAPQueryBS query=null;
	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		if ("pk_organization".equals(e.getKey())) {
			UIRefPane ref = (UIRefPane) e.getBillCardPanel()
					.getHeadTailItem(e.getKey()).getComponent();
			
			UFDate dbilldate = (UFDate) e.getBillCardPanel()
			.getHeadTailItem("dbilldate").getValueObject();
			if(dbilldate==null){
				nc.vo.pubapp.pattern.exception.ExceptionUtils
			      .wrappBusinessException("�õ�������Ϊ��,������д�������ں��ٲ���");
			}
			String code = "";
			String pk_defdoc = "";
			String fischemeTimeTypePk = "";
			if(getCode(ref)!=null){
				code = (String) getCode(ref).get("code");
				pk_defdoc = (String) getCode(ref).get("pk_defdoc");
			}
			if(StringUtils.isBlank(code)){
				nc.vo.pubapp.pattern.exception.ExceptionUtils
			      .wrappBusinessException("�����ʻ�������δά��,��ά��NC�Զ��嵵��");
			}
			//����
			if ("001".equals(code)) {
				e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b")
						.clearBodyData();
				e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b")
						.clearBodyData();
				e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b")
						.clearBodyData();
				if(e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").delLine(new int[e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").getRowCount()]);
				}
				if(e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").delLine(new int[e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").getRowCount()]);
				}
				if(e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").delLine(new int[e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").getRowCount()]);
				}
				e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b")
						.addLine();
				e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b")
						.addLine();
				e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b")
						.addLine();
				e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").setValueAt("�ƻ����", 0,
						"def13");
				e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").setValueAt("ʵ�����", 1,
						"def13");
				e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").setValueAt("�ƻ�����", 2,
						"def13");
				//���ݱ�����pk��ȡ���ʷ����ƽ���׼����
				Map<String, Object> bMap = getRow("tg_fischemepushstandard_b",pk_defdoc);
				//��ȡ���ʷ����ƽ���׼��ͷ�����ʷ���ʱ�����
				if(getHeadVoForPk((String)bMap.get("pk_fispushstandard"))!=null){
					fischemeTimeTypePk = (String) getHeadVoForPk((String)bMap.get("pk_fispushstandard")).get("def2");
				}
				if(StringUtils.isNotBlank(getDefdocCode(fischemeTimeTypePk))&&"001".equals(getDefdocCode(fischemeTimeTypePk))){
					if(bMap!=null){
						e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def1")==null?0:Integer.valueOf((String) bMap.get("def1")))).toString().substring(0, 11), 0,
								"vbdef1");
						e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def2")==null?0:Integer.valueOf((String) bMap.get("def2")))).toString().substring(0, 11), 0,
								"vbdef2");
						e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def3")==null?0:Integer.valueOf((String) bMap.get("def3")))).toString().substring(0, 11), 0,
								"vbdef3");
						e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def4")==null?0:Integer.valueOf((String) bMap.get("def4")))).toString().substring(0, 11), 0,
								"vbdef4");
						e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def5")==null?0:Integer.valueOf((String) bMap.get("def5")))).toString().substring(0, 11), 0,
								"vbdef5");
						e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def6")==null?0:Integer.valueOf((String) bMap.get("def6")))).toString().substring(0, 11), 0,
								"vbdef6");
						e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def7")==null?0:Integer.valueOf((String) bMap.get("def7")))).toString().substring(0, 11), 0,
								"vbdef7");
					}
				}
				e.getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
			}
			//����
			if ("002".equals(code)) {
				e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b")
						.clearBodyData();
				e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b")
						.clearBodyData();
				e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b")
				.clearBodyData();
				if(e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").delLine(new int[e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").getRowCount()]);
				}
				if(e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").delLine(new int[e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").getRowCount()]);
				}
				if(e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").delLine(new int[e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").getRowCount()]);
				}
				e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b")
						.addLine();
				e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b")
						.addLine();
				e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b")
						.addLine();
				e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").setValueAt("�ƻ����", 0,
						"def13");
				e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").setValueAt("ʵ�����", 1,
						"def13");
				e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").setValueAt("�ƻ�����", 2,
						"def13");
				//���ݱ�����pk��ȡ���ʷ����ƽ���׼����
				Map<String, Object> bMap = getRow("tg_fischemepushstandard_n",pk_defdoc);
				//��ȡ���ʷ����ƽ���׼��ͷ�����ʷ���ʱ�����
				if(getHeadVoForPk((String)bMap.get("pk_fispushstandard"))!=null){
					fischemeTimeTypePk = (String) getHeadVoForPk((String)bMap.get("pk_fispushstandard")).get("def2");
				}
				if(StringUtils.isNotBlank(getDefdocCode(fischemeTimeTypePk))&&"001".equals(getDefdocCode(fischemeTimeTypePk))){
					if(bMap!=null){
						e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def1")==null?0:Integer.valueOf((String) bMap.get("def1")))).toString().substring(0, 11), 0,
								"def1");
						e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def2")==null?0:Integer.valueOf((String) bMap.get("def2")))).toString().substring(0, 11), 0,
								"def2");
						e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def3")==null?0:Integer.valueOf((String) bMap.get("def3")))).toString().substring(0, 11), 0,
								"def3");
						e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def4")==null?0:Integer.valueOf((String) bMap.get("def4")))).toString().substring(0, 11), 0,
								"def4");
						e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def5")==null?0:Integer.valueOf((String) bMap.get("def5")))).toString().substring(0, 11), 0,
								"def5");
						e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def6")==null?0:Integer.valueOf((String) bMap.get("def6")))).toString().substring(0, 11), 0,
								"def6");
						e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def7")==null?0:Integer.valueOf((String) bMap.get("def7")))).toString().substring(0, 11), 0,
								"def7");
					}
				}
				e.getBillCardPanel().getBodyTabbedPane().setSelectedIndex(1);
			}
			//�ʱ��г�
			if ("003".equals(code)) {
				e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b")
						.clearBodyData();
				e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b")
						.clearBodyData();
				e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b")
						.clearBodyData();
				if(e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").delLine(new int[e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").getRowCount()]);
				}
				if(e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").delLine(new int[e.getBillCardPanel().getBillModel("id_tgrz_fischeme_b").getRowCount()]);
				}
				if(e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").delLine(new int[e.getBillCardPanel().getBillModel("id_tgrz_nfischeme_b").getRowCount()]);
				}
				e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b")
						.addLine();
				e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b")
						.addLine();
				e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b")
						.addLine();
				e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").setValueAt("�ƻ����", 0,
						"def13");
				e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").setValueAt("ʵ�����", 1,
						"def13");
				e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").setValueAt("�ƻ�����", 2,
						"def13");
				//���ݱ�����pk��ȡ���ʷ����ƽ���׼����
				Map<String, Object> bMap = getRow("tg_fischemepushstandard_c",pk_defdoc);
				if(StringUtils.isNotBlank(getDefdocCode(fischemeTimeTypePk))&&"001".equals(getDefdocCode(fischemeTimeTypePk))){
					if(bMap!=null){
						e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def1")==null?0:Integer.valueOf((String) bMap.get("def1")))).toString().substring(0, 11), 0,
								"def1");
						e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def2")==null?0:Integer.valueOf((String) bMap.get("def2")))).toString().substring(0, 11), 0,
								"def2");
						e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def3")==null?0:Integer.valueOf((String) bMap.get("def3")))).toString().substring(0, 11), 0,
								"def3");
						e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def4")==null?0:Integer.valueOf((String) bMap.get("def4")))).toString().substring(0, 11), 0,
								"def4");
						e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def5")==null?0:Integer.valueOf((String) bMap.get("def5")))).toString().substring(0, 11), 0,
								"def5");
						e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def6")==null?0:Integer.valueOf((String) bMap.get("def6")))).toString().substring(0, 11), 0,
								"def6");
						e.getBillCardPanel().getBillModel("id_tgrz_capmarket_b").setValueAt((dbilldate==null?null:dbilldate.getDateAfter(bMap.get("def7")==null?0:Integer.valueOf((String) bMap.get("def7")))).toString().substring(0, 11), 0,
								"def7");
					}
				}
				e.getBillCardPanel().getBodyTabbedPane().setSelectedIndex(2);
			}
		}

	}
	
	private Map<String, Object> getHeadVoForPk(String pk_fispushstandard) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from tg_fischemepushstandard_h where pk_fispushstandard = '"+pk_fispushstandard+"'  ");
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			result = (Map<String, Object>) getQuery().executeQuery(sql.toString(), new MapProcessor());
		} catch (BusinessException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return result;
	}

	private Map<String, Object> getRow(String tableName,String pk) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from "+tableName+" where pk_fispushstandard in( ");
		sql.append(" select pk_fispushstandard from tg_fischemepushstandard_h where def1 = '"+pk+"' and def2 <> '~' and def2 is not null and nvl(dr,0) = 0 )   and rownum = 1 order by ts desc ");
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			result = (Map<String, Object>) getQuery().executeQuery(sql.toString(), new MapProcessor());
//			pk_defdoc =  (String) getQuery().executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}
		return result;
	}

	public IUAPQueryBS getQuery(){
		if(query==null){
			query = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		}
		return query;
	}
	
	/**
	 * ��ȡ���ñ����Ӧ���������ͱ���
	 * @param ref
	 * @return
	 */
	private Map<String, Object> getCode(UIRefPane ref){
		StringBuffer sql = new StringBuffer();
		sql.append("select code,pk_defdoc from bd_defdoc where pk_defdoc in( ");
		sql.append(" select def1 from tgrz_OrganizationType otype where  otype.pk_organizationtype in  ");
		sql.append(" (select pk_organizationtype from tgrz_organization where code = '"+ref.getRefCode()+"' and nvl(dr,0) = 0 )  ");
		sql.append(" and nvl(dr,0) = 0) and nvl(dr,0)= 0 and enablestate = '2' ");
		Map<String,Object> result = new HashMap<String,Object>();
		
		try {
			result = (Map<String, Object>) getQuery().executeQuery(sql.toString(), new MapProcessor());
//			pk_defdoc =  (String) getQuery().executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}
		return result;
	}
	
	private String getDefdocCode(String pk_defdoc) {
		DefdocVO defdocvo = null;
		String code = "";
		try {
			if(StringUtils.isNotBlank(pk_defdoc)){
				defdocvo = (DefdocVO) getQuery().retrieveByPK(DefdocVO.class, pk_defdoc);
				if(defdocvo!=null){
					code  = defdocvo.getCode();
				}
			}
		} catch (BusinessException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return code;
	}

}
