package nc.impl.tg;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.tobankdoc.InsertBankdoc;
import nc.itf.bd.bankdoc.IBankdocService;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.vo.bd.bankdoc.BankdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.tg.outside.EBSBankdocVO;

public class UpdateBankdocImpl implements InsertBankdoc {

	BaseDAO baseDAO = null;

	@Override
	public Map<String, String> executeTask(EBSBankdocVO ebsBankdocVO,
			String srcsystem, String dectype) throws BusinessException {

		IBankdocService bankdocService = NCLocator.getInstance().lookup(
				IBankdocService.class);
		Map<String, String> refMap = new HashMap<String, String>();

		String code = ebsBankdocVO.getCode();
		String areacode = null;
		String bankarea = null;
		String orgnumber = null;
		String province = null;
		String city = null;
		String combinenum = null;

		if (ebsBankdocVO.getAreacode() != null
				&& !"".equals(ebsBankdocVO.getAreacode())) {
			String codename = ebsBankdocVO.getAreacode().substring(0,
					ebsBankdocVO.getAreacode().length() - 1);
			areacode = getPkdefdocByCode(codename);
		}

		bankarea = ebsBankdocVO.getBankarea();
		orgnumber = ebsBankdocVO.getOrgnumber();

		if (("".equals(ebsBankdocVO.getProvince()) || ebsBankdocVO
				.getProvince() == null) && code == null) {
			throw new BusinessException("����ʧ�ܣ�ʡ�ݲ���Ϊ�գ������������");
		} else {
			province = ebsBankdocVO.getProvince();
		}
		// ����У��
		if (("".equals(ebsBankdocVO.getCity()) || ebsBankdocVO.getCity() == null)
				&& code == null) {
			throw new BusinessException("����ʧ�ܣ����в���Ϊ�գ������������");
		} else {
			city = ebsBankdocVO.getCity();
		}
		// ���к�У��
		if (("".equals(ebsBankdocVO.getCombinenum()) || ebsBankdocVO
				.getCombinenum() == null) && code == null) {
			throw new BusinessException("����ʧ�ܣ����кŲ���Ϊ�գ������������");
		} else {
			combinenum = ebsBankdocVO.getCombinenum();
		}

		// nameУ��
		if ("".equals(ebsBankdocVO.getName()) || ebsBankdocVO.getName() == null) {
			throw new BusinessException("����ʧ�ܣ��������Ʋ���Ϊ�գ������������");
		}

		// �������У��
		if ("".equals(ebsBankdocVO.getBanktype_code())
				|| ebsBankdocVO.getBanktype_code() == null) {
			throw new BusinessException("����ʧ�ܣ����������Ϊ�գ������������");
		}

		int enablestate = 0;
		if (ebsBankdocVO.getEnablestate() == "N") {
			enablestate = 3;
		} else {
			enablestate = 2;
		}

		if (areacode == null || "".equals(areacode)) {
			areacode = getPkdefdocByCode(city.substring(0,city.length() - 1));
		}

		if (bankarea == null || "".equals(bankarea)) {
			bankarea = province;
		}

		BankdocVO headVO = new BankdocVO();
		// ��Դϵͳ
		headVO.setDef1(srcsystem);

		headVO.setPk_group("000112100000000005FD");
		headVO.setPk_org("GLOBLE00000000000000");// ������֯
		headVO.setName(ebsBankdocVO.getName());// ��������
		headVO.setShortname(ebsBankdocVO.getShortname());// ���
		if (getPkByName(ebsBankdocVO.getBanktype_code()) == null
				|| "".equals(getPkByName(ebsBankdocVO.getBanktype_code()))) {
			throw new BusinessException("�������δ��NC����");
		}
		headVO.setPk_banktype(getPkByName(ebsBankdocVO.getBanktype_code())); // �������
		headVO.setPk_country("0001Z010000000079UJJ");// ���ң�Ĭ���й���
		headVO.setEnablestate(enablestate);// ����״̬��Ĭ�������ã�
		headVO.setAreacode(areacode);// ��������
		headVO.setBankarea(bankarea);// ��������
		headVO.setOrgnumber(orgnumber);// ������/���к�
		headVO.setProvince(province);// ʡ��
		headVO.setCity(city);// ����
		headVO.setCombinenum(combinenum);// ���к�
		headVO.setDef2(ebsBankdocVO.getDef2());
		headVO.setDef3(ebsBankdocVO.getDef3());
		headVO.setDef4(ebsBankdocVO.getDef4());
		headVO.setDef5(ebsBankdocVO.getDef5());
		headVO.setDr(0);
		headVO.setStatus(VOStatus.NEW);
		String billqueue = EBSCont.getDocNameMap().get(dectype) + ":"
				+ headVO.getCode();
		String billkey = EBSCont.getDocNameMap().get(dectype) + ":"
				+ headVO.getName();

		String creatcode = null;
		try {
			EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
			Collection<BankdocVO> docVO = getBaseDAO().retrieveByClause(
					BankdocVO.class,
					"isnull(dr,0)=0 and name = '" + ebsBankdocVO.getName()
							+ "'");
			if (code == null || "".equals(code)) {
				if (docVO != null && docVO.size() > 0) {

					if (getCodeByName(ebsBankdocVO.getName()) != null) {
						creatcode = getCodeByName(ebsBankdocVO.getName());
					}
					refMap.put("msg", "�����е����Ѵ���!");
				} else {
					IBillcodeManage codeManage = NCLocator.getInstance()
							.lookup(IBillcodeManage.class);
					creatcode = codeManage.getBillCode_RequiresNew("bankdoc",
							"000112100000000005FD", "GLOBLE00000000000000",
							headVO);
					headVO.setCode(creatcode);
					BankdocVO bankvo = bankdocService.insertBankdocVO(headVO,
							false);
					creatcode = bankvo.getCode();
					refMap.put("msg", "�����е�����," + "�������!");
				}
			} else {
				Collection<BankdocVO> upadteVO = getBaseDAO().retrieveByClause(
						BankdocVO.class,
						"isnull(dr,0)=0 and code = '" + code + "' and name = '"
								+ ebsBankdocVO.getName() + "'");
				if (upadteVO != null && upadteVO.size() > 0) {
					BankdocVO[] bankdocVO = docVO.toArray(new BankdocVO[0]);
					bankdocVO[0].setDef1(srcsystem);// ��Դϵͳ
					bankdocVO[0].setStatus(VOStatus.UPDATED);
					bankdocVO[0].setPk_org("GLOBLE00000000000000");// ������֯
					bankdocVO[0].setName(ebsBankdocVO.getName());// ��������
					bankdocVO[0].setShortname(ebsBankdocVO.getShortname());// ���
					bankdocVO[0].setPk_banktype(getPkByName(ebsBankdocVO
							.getBanktype_code())); // �������
					bankdocVO[0].setEnablestate(enablestate);// ����״̬
					bankdocVO[0].setAreacode(areacode);// ��������
					bankdocVO[0].setBankarea(bankarea);// ��������
					bankdocVO[0].setOrgnumber(orgnumber);// ������/���к�
					bankdocVO[0].setProvince(province);// ʡ��
					bankdocVO[0].setCity(city);// ����
					bankdocVO[0].setCombinenum(combinenum);// ���к�
					bankdocVO[0].setDef2(ebsBankdocVO.getDef2());
					bankdocVO[0].setDef3(ebsBankdocVO.getDef3());
					bankdocVO[0].setDef4(ebsBankdocVO.getDef4());
					bankdocVO[0].setDef5(ebsBankdocVO.getDef5());
					bankdocService.updateBankdocVO(bankdocVO[0]);
					refMap.put("msg", "�����е����޸����!");
				} else {
					throw new BusinessException("����ʧ�ܣ�δ�鵽������ݣ������������");
				}
			}
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}
		refMap.put("data", creatcode);
		return refMap;
	}

	// private String getBnakTypeID(String name) throws BusinessException {
	// Map<String, String> infoMap = getBankTypeMap();
	// String pk_banktype = null;
	// if (infoMap.size() > 0) {
	// for (String key : infoMap.keySet()) {
	// if ("�й�����".equals(name.substring(0, 4))) {
	// pk_banktype = infoMap.get("�й�����");
	// break;
	// }
	// if (name.contains(key) && !"�й�����".equals(key)) {
	// pk_banktype = infoMap.get(key);
	// break;
	// }
	// }
	// }
	//
	// if (pk_banktype == null) {
	// throw new BusinessException(name + "�޷��ҵ���Ӧ��������������������");
	// }
	//
	// return pk_banktype;
	// }
	//
	// private Map<String, String> getBankTypeMap() throws DAOException {
	// String sql =
	// "select bd_banktype.pk_banktype,bd_banktype.name from bd_banktype where dr =0 ";
	// List<Map<String, String>> list = (List<Map<String, String>>) getBaseDAO()
	// .executeQuery(sql, new MapListProcessor());
	// Map<String, String> infoMap = new HashMap<String, String>();
	// if (list != null && list.size() > 0) {
	// for (Map<String, String> map : list) {
	// infoMap.put(map.get("name"), map.get("pk_banktype"));
	//
	// }
	// }
	// return infoMap;
	//
	// }

	private String getCodeByName(String name) throws DAOException {
		String sql = "select distinct bd_bankdoc.code from bd_bankdoc where dr = 0 and name ='"
				+ name + "'";

		Map<String, String> infoMap = new HashMap<String, String>();

		List<Object[]> list = (List<Object[]>) getBaseDAO().executeQuery(sql,
				new ArrayListProcessor());
		String code = null;
		if (list.size() > 0) {
			code = (String) list.get(0)[0];
		}
		return code;

	}

	private String getPkByName(String name) throws DAOException {
		String sql = "select bd_banktype.pk_banktype from bd_banktype where dr =0  and name ='"
				+ name + "'";

		String list = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return list;

	}

	private String getPkdefdocByCode(String name) throws DAOException {
		String sql = "select pk_defdoc from bd_defdoc where name = '"
				+ name
				+ "' and dr = 0 "
				+ "and enablestate =2 and pk_defdoclist = (select pk_defdoclist from bd_defdoclist where code = 'BD001_0xx')";

		String pk_defdoc = null;

		pk_defdoc = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());

		return pk_defdoc;

	}

	/**
	 * ���ݿ�־û�
	 * 
	 * @return
	 */
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

}
