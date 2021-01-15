package nc.bs.tg.fund.rz.report.cost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.cmp.utils.UFDoubleUtils;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pub.smart.data.DataSet;
import nc.ui.pubapp.gantt.ui.event.AppgantModelEvent.APP_ID;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.AppContext;
import nc.vo.tgfn.report.cost.MinAndLongProjectCostLedgerVO;
import nc.vo.tgfp.pub.common.FPConst;
import nc.vo.uap.rbac.FuncSubInfo;

import com.ufida.dataset.IContext;
import com.ufida.zior.context.ComContextKey;

/**
 * �г�����Ŀ�ɱ�̨�ˣ��ɱ����� ljf
 * 
 * @author ASUS
 * 
 *         ���������:R ˰�� L ��̬����˰�� O �ۼ�Ӧ���� M ��ͬδ���� ����˰�� N ��ͬδ���� ������˰�� A �ۼ�ʵ����-NC B
 *         �ۼ�ʵ����-NC������˰�� C �ۼ�ʵ����-NC��˰� D nc���˷�Ʊ����˰�� E ����ɱ�������˰�� F δ��ɱ�����˰�� G
 *         δ��ɱ�������˰�� H �ۼƲ�ֵ����˰�� I ���ղ�ֵ J �ۼ� ��ֵδ���� ����˰�� K �ۼƲ�ֵδ���� ������˰��
 * 
 * 
 */
public class MinAndLongProjectCostLedgerUtils extends ReportUtils {
	static MinAndLongProjectCostLedgerUtils utils;
	private Map<String, Map<String, String>> balatypeMap = null;
	private Map<String, String> contTypeMap = null;
	private final static String FINACODE="500106";
	private final static String DEVACODE="500107";
	private  static String FUNCODE="";
	private boolean isGenCons = false;

	public static MinAndLongProjectCostLedgerUtils getUtils()
			throws BusinessException {
		if (utils == null) {
			utils = new MinAndLongProjectCostLedgerUtils();
		}
		return utils;
	}

	public MinAndLongProjectCostLedgerUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new MinAndLongProjectCostLedgerVO()));
	}

	@Override
	public DataSet getProcessData(IContext context) throws BusinessException {
		CmpReportResultVO[] cmpreportresults = null;

		try {
			ConditionVO[] conditionVOs = (ConditionVO[]) context
					.getAttribute(FPConst.KEY_QueryScheme);
			if (conditionVOs == null || conditionVOs.length == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			initQuery(conditionVOs);
			FUNCODE = context.getAttribute("key_report_funcode")+"";
			List<MinAndLongProjectCostLedgerVO> list = getCosttLedgerDatas(false);

			cmpreportresults = transReportResult(list);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

	/**
	 * �ɱ���ͬ��Ϣ
	 * 
	 * @param istotal
	 *            �Ƿ��ܰ�
	 * @return
	 * @throws BusinessException
	 */
	protected List<MinAndLongProjectCostLedgerVO> getCosttLedgerDatas(
			boolean istotal) throws BusinessException {
		initBalatypeMap();
		initContType();
		// 1.��ʼ��ȡ�ܰ�/�ְ��ɱ���ͬ��Ϣ
		//add by �ƹڻ� SDYC-53 �ɱ�̨�ˣ��ɱ����� 20200813 begin
		isGenCons=istotal;
//		List<MinAndLongProjectCostLedgerVO> totallist = getCostDatas(true);// �ܰ���ͬ��Ϣ
		List<MinAndLongProjectCostLedgerVO> totallist = getCostDatas2(true);// �ܰ���ͬ��Ϣ
//		List<MinAndLongProjectCostLedgerVO> list = getCostDatas(false);// �ְ���ͬ��Ϣ
		List<MinAndLongProjectCostLedgerVO> list = getCostDatas2(false);// �ְ���ͬ��Ϣ
		//add by �ƹڻ� SDYC-53 �ɱ�̨�ˣ��ɱ����� 20200813 end
		List<MinAndLongProjectCostLedgerVO> integrationList = new ArrayList<MinAndLongProjectCostLedgerVO>();
		Map<String, List<MinAndLongProjectCostLedgerVO>> detailedMap = new HashMap<String, List<MinAndLongProjectCostLedgerVO>>();// �ְ�������Ϣ����
		Map<String, List<MinAndLongProjectCostLedgerVO>> detailedMap2 = new HashMap<String, List<MinAndLongProjectCostLedgerVO>>();// �ְ�������Ϣ����(����ҵ̬)

		// 2.��ȡ�ۼ�ʵ����-NC
		// ���̨���ڳ�ʵ�����˰����NC�ڳ�ʵ�����˰��def98�����ֶ���������+112301�跽���������ݸ��ƾ֤�һص��ݣ�ƥ���ͬ��A
		Map<String, UFDouble> actualMap = null;
		// 3.��ȡ��������
		// ���ݺ�ͬ�����ȡ��Ӧ�ĸ��ap_paybill.pk_balatype�������ͬ�ĺ�ͬ���롢����ҵ�񵥾ݣ����ݹ���ƾ֤��ƾ֤��ϸ��ȡ����֯���ң��跽����
		Map<String, UFDouble> factorPayMap = null;// ȡ���ʽΪABS�����
		// 4.��ȡ�ۼƲ�ֵ
		// ȡ��ͬ��ֵ���ܲ�ѯʱ���������ƣ�ȡ���ֵ��
		Map<String, Map<String, UFDouble>> outputMap = null;
		// 5.��ȡҵ̬��Ϣ
		Map<String, List<Map<String, Object>>> formatMap = null;
		// 6.��ȡnc���˷�Ʊ����˰��
		// ��ʼ����ȥ�ķ�Ʊ����NC�ڳ����ݣ�+112301�������������Ӧ����ƾ֤�һص��ݣ�ƥ���ͬ��
		Map<String, Map<String, UFDouble>> accInvMap = null;
		// 7.��ȡ����ɱ�������˰��
		// �ڳ���Ʊ������˰��+5001�跽���������Ӧ����ƾ֤�һص��ݣ�ƥ���ͬ��
		Map<String, Map<String, UFDouble>> costMap = null;
		// 8.��ȡӦ������صļ�˰�����Ϣ���ۼ�ʵ�����˰�������Ϣ���ڴ�������
		Map<String, Map<String, UFDouble>> invMnyMap = null;
		
		//add by �ƹڻ� ������ʷ��úͿ�����ӷѵ�������� 20200813 begin
		Map<String, UFDouble> finaMnyMap = null;
		Map<String, UFDouble> devMnyMap = null;
		//add by �ƹڻ� ������ʷ��úͿ�����ӷѵ�������� 20200813 end

		// ������Ҫ���˵����ݡ���ͬ��������ͬ���롿
		Set<String> contSet = new HashSet<String>();
		Set<String> keySet = new HashSet<String>();

		if (list != null && list.size() > 0) {
			for (MinAndLongProjectCostLedgerVO vo : list) {
				if (!detailedMap.containsKey(vo.getTatalcontcode())) {
					detailedMap.put(vo.getTatalcontcode(),
							new ArrayList<MinAndLongProjectCostLedgerVO>());
				}
				detailedMap.get(vo.getTatalcontcode()).add(vo);
//				contSet.add(vo.getContcode());
				//add by �ƹڻ� ��ӿ�����ӷѵȺ��ͬ����Ϊ�գ�����Ƿ�Ϊ���ж� 20200915 begin
//				keySet.add(vo.getPk_fct_ap());
				String pk_fct_ap=vo.getPk_fct_ap();
				if(pk_fct_ap!=null){
					contSet.add(vo.getContcode());
					keySet.add(pk_fct_ap);
				}
				//add by �ƹڻ� ��ӿ�����ӷѵȺ��ͬ����Ϊ�գ�����Ƿ�Ϊ���ж� 20200915 end
			}

		}

		if (totallist != null && totallist.size() > 0) {
			for (MinAndLongProjectCostLedgerVO vo : totallist) {
				//add by �ƹڻ� ��ӿ�����ӷѵȺ��ͬ����Ϊ�գ�����Ƿ�Ϊ���ж� 20200915 begin
//				keySet.add(vo.getPk_fct_ap());
//				contSet.add(vo.getContcode());
				String pk_fct_ap=vo.getPk_fct_ap();
				if(pk_fct_ap!=null){
					keySet.add(pk_fct_ap);
					contSet.add(vo.getContcode());
				}
				//add by �ƹڻ� ��ӿ�����ӷѵȺ��ͬ����Ϊ�գ�����Ƿ�Ϊ���ж� 20200915 end
				
			}
		}
		if (keySet != null && keySet.size() > 0){
			// 2.��ȡ�ۼ�ʵ����-NC
			// ���̨���ڳ�ʵ�����˰����NC�ڳ�ʵ�����˰��def98�����ֶ���������+112301�跽���������ݸ��ƾ֤�һص��ݣ�ƥ���ͬ��A
			actualMap = getPayVoucherMap(contSet, null, "112301");
			// 3.��ȡ��������
			// ���ݺ�ͬ�����ȡ��Ӧ�ĸ��ap_paybill.pk_balatype�������ͬ�ĺ�ͬ���롢����ҵ�񵥾ݣ����ݹ���ƾ֤��ƾ֤��ϸ��ȡ����֯���ң��跽����
			factorPayMap = getPayVoucherMap(contSet,
					balatypeMap.get("9").get("pk_balatype"), "112301");// ȡ���ʽΪABS�����
			// 4.��ȡ�ۼƲ�ֵ
			// ȡ��ͬ��ֵ���ܲ�ѯʱ���������ƣ�ȡ���ֵ��
			outputMap = getOutputMap(keySet);
			// 5.��ȡҵ̬��Ϣ
			//add by �ƹڻ� SDYC-388 ����������ɱ�̨�� ����Ƿ�ҵ̬�������� 20200817 begin
			if("36HA0201".equals(FUNCODE)){
				formatMap = getFormatMap(keySet);
			}else {
				formatMap=new HashMap<String, List<Map<String,Object>>>();
			}
			//add by �ƹڻ� SDYC-388 ����������ɱ�̨�� ����Ƿ�ҵ̬�������� 20200817 end
			// 6.��ȡnc���˷�Ʊ����˰��
			// ��ʼ����ȥ�ķ�Ʊ����NC�ڳ����ݣ�+112301�������������Ӧ����ƾ֤�һص��ݣ�ƥ���ͬ��
			accInvMap = getPayableVoucherMap(contSet, "112301");
			// 7.��ȡ����ɱ�������˰��
			// �ڳ���Ʊ������˰��+5001�跽���������Ӧ����ƾ֤�һص��ݣ�ƥ���ͬ��
			costMap = getPayableVoucherMap(contSet, "5001");
			// 8.��ȡӦ������صļ�˰�����Ϣ���ۼ�ʵ�����˰�������Ϣ���ڴ�������
			invMnyMap = getPayableMnyMap(keySet);
			
			//add by �ƹڻ� ������ʷ��úͿ�����ӷѵ�������� 20200813 begin
//			finaMnyMap=getBalByCode(FINACODE);
//			devMnyMap = getBalByCode(DEVACODE);
			//add by �ƹڻ� ������ʷ��úͿ�����ӷѵ�������� 20200813 end
		}
		if (totallist != null && totallist.size() > 0) {
			int firstrow = 1;
			for (MinAndLongProjectCostLedgerVO vo : totallist) {
				vo.setTatalcontcode("~");
				vo.setTatalcontname("~");
				vo.setRate1(vo.getRate1());// ˰��
				UFDouble rate = UFDoubleUtils.add(UFDouble.ONE_DBL,
						UFDoubleUtils.div(vo.getRate1(), new UFDouble(100)));
				vo.setVclass(contTypeMap.get(vo.getConttype()));
				vo.setInnercode(String.format("%04d", firstrow));
				vo.setFormatname("����");
				vo.setRate2(new UFDouble(1));
				vo = getHandleContMainData2(vo, actualMap, factorPayMap,
						outputMap, accInvMap, costMap, invMnyMap, firstrow,null);
				firstrow++;
				integrationList.add(vo);
				if (!detailedMap2.containsKey(vo.getContcode())) {
					detailedMap2.put(vo.getContcode(),
							new ArrayList<MinAndLongProjectCostLedgerVO>());
				}
				String[] attNames = vo.getAttributeNames();
				UFDouble signTotalMny = UFDouble.ZERO_DBL;// ǩԼ����ܼ�
				Map<String, UFDouble> formatSignMnyMap = new HashMap<String, UFDouble>();
				if(isGenCons)vo.setRate2(UFDouble.ZERO_DBL);//add by �ƹڻ� �ְܷ�̨��ҵ̬���������� ���������ְܷ���ͬ 20200917

				if (detailedMap.containsKey(vo.getContcode())) {// �ܰ���ͬ���ڷְ���Ϣ
					for (String attName : attNames) {
						if (attName.contains("amount")) {
							if (!"amount1".equals(attName)) {
								vo.setAttributeValue(attName, UFDouble.ZERO_DBL);
							}
						}

					}

					List<MinAndLongProjectCostLedgerVO> detailedlists = detailedMap
							.get(vo.getContcode());
					int secondrow = 200;// �ְ���Ϣ��200��ʼ,�Ա����ܰ���ͬҵ̬��Ϣ����
					for (MinAndLongProjectCostLedgerVO detvo : detailedlists) {
						detvo.setVclass(contTypeMap.get(vo.getConttype()));
//						detvo = getHandleContMainData(detvo, actualMap,
//								factorPayMap, outputMap, accInvMap, costMap,
//								invMnyMap, secondrow, vo.getInnercode());
						detvo = getHandleContMainData2(detvo, actualMap,
						factorPayMap, outputMap, accInvMap, costMap,
						invMnyMap, secondrow, vo.getInnercode());
						// ����ǩԼ����ֶ�,�����漰�������Ϣ����ϸ���ܵ���ͷ
						for (String attName : attNames) {
							//if (attName.contains("amount")||attName.contains("rate2")) {
							//add by �ƹڻ� �ܰ���ͬ��ҵ̬����Ҳ��Ҫ���ְܷ���ͬ 20200917 begin
							if (attName.contains("amount")||attName.contains("rate2")) {

								if ("amount1".equals(attName)) {
									signTotalMny = UFDoubleUtils.add(
											signTotalMny, detvo.getAmount1());// ��ȡǩԼ���
								}else if(isGenCons&&"rate2".equals(attName)){
									vo.setAttributeValue(
											attName,
											UFDoubleUtils.add((UFDouble) vo
													.getAttributeValue(attName),
													(UFDouble) detvo
															.getAttributeValue(attName)));
								}else {
									vo.setAttributeValue(
											attName,
											UFDoubleUtils.add(
													(UFDouble) vo
															.getAttributeValue(attName),
													(UFDouble) detvo
															.getAttributeValue(attName)));
								}
							}
							//add by �ƹڻ� �ܰ���ͬ��ҵ̬����Ҳ��Ҫ���ְܷ���ͬ 20200917 end

						}

						secondrow++;
						if (istotal) {
							integrationList.add(detvo);
						}
//						if (formatMap.containsKey(detvo.getPk_fct_ap())) {
						if (formatMap!=null&&formatMap.containsKey(detvo.getPk_fct_ap())) {//add by �ƹڻ� ��ӿ�����ӷѵȺ��ͬ����Ϊ�գ�����Ƿ�Ϊ���ж� 20200915
							List<MinAndLongProjectCostLedgerVO> foramtList = getForamtDataList(
									detvo, formatMap.get(detvo.getPk_fct_ap()));
							for (MinAndLongProjectCostLedgerVO foramtvo : foramtList) {
								if (!formatSignMnyMap.containsKey(foramtvo
										.getFormatname())) {
									formatSignMnyMap.put(
											foramtvo.getFormatname(),
											UFDouble.ZERO_DBL);
								}
								UFDouble mny = UFDoubleUtils.add(
										formatSignMnyMap.get(foramtvo
												.getFormatname()), foramtvo
												.getAmount1());// ���ҵ̬����ǩԼ���

								formatSignMnyMap.put(foramtvo.getFormatname(),
										mny);
							}

							if (istotal) {
								integrationList.addAll(foramtList);
							}
						}

					}

					// ���ڷְ���Ϣ����,�벻��˰�����˰���ؼ�
					// ��̬���
					vo.setAmount6(UFDoubleUtils.div(vo.getAmount6(), rate));
					//add by �ƹڻ� ��̬��˰�=��̬����˰��-��̬������˰��20200917 begin
//					vo.setAmount7(UFDoubleUtils.sub(vo.getAmount6(),
//							vo.getAmount7()));
					vo.setAmount7(UFDoubleUtils.sub(vo.getAmount5(),
					vo.getAmount6()));
					//add by �ƹڻ� ��̬��˰�=��̬����˰��-��̬������˰��20200917 end

					// �ۼ�ʵ����
					vo.setAmount12(UFDoubleUtils.div(vo.getAmount11(), rate));
					vo.setAmount14(UFDoubleUtils.sub(vo.getAmount11(),
							vo.getAmount12()));

					// ��ͬδ����
					vo.setAmount16(UFDoubleUtils.div(vo.getAmount15(), rate));
					vo.setAmount17(UFDoubleUtils.sub(vo.getAmount15(),
							vo.getAmount16()));
					// �ۼ�ʵ����-NC
					vo.setAmount20(UFDoubleUtils.div(vo.getAmount19(), rate));
					vo.setAmount21(UFDoubleUtils.sub(vo.getAmount19(),
							vo.getAmount20()));

					// �ۼ�ʵ����-NC
					vo.setAmount20(UFDoubleUtils.div(vo.getAmount19(), rate));
					vo.setAmount21(UFDoubleUtils.sub(vo.getAmount19(),
							vo.getAmount20()));

					// δ��ɱ�

					vo.setAmount27(UFDoubleUtils.div(vo.getAmount26(), rate));
					vo.setAmount28(UFDoubleUtils.sub(vo.getAmount26(),
							vo.getAmount27()));

					// �ۼƲ�ֵ

					vo.setAmount29(UFDoubleUtils.div(vo.getAmount29(), rate));
//					vo.setAmount30(UFDoubleUtils.sub(vo.getAmount29(),
//							vo.getAmount30()));
					//add by �ƹڻ� �ۼƲ�ֵ������˰��=�ۼƲ�ֵ����˰��-�ۼƲ�ֵ��˰� 20200917 begin
					vo.setAmount30(UFDoubleUtils.sub(vo.getAmount29(),
					vo.getAmount31()));
					//add by �ƹڻ� �ۼƲ�ֵ������˰��=�ۼƲ�ֵ����˰��-�ۼƲ�ֵ��˰� 20200917 end

					// �ۼ� ��ֵδ����
					vo.setAmount34(UFDoubleUtils.div(vo.getAmount33(), rate));
					vo.setAmount35(UFDoubleUtils.sub(vo.getAmount33(),
							vo.getAmount34()));

				}

				// �����ְܷ�ҵ̬������Ϣ
				if (formatSignMnyMap != null && formatSignMnyMap.size() > 0) {
					int secondrow = 1;
					for (String key : formatSignMnyMap.keySet()) {
						MinAndLongProjectCostLedgerVO coloneVO = (MinAndLongProjectCostLedgerVO) vo
								.clone();
						coloneVO.setInnercode(vo.getInnercode()
								+ String.format("%04d", secondrow));
						coloneVO.setFormatname(key);
						coloneVO.setRate2(UFDoubleUtils
								.isNullOrZero(signTotalMny) ? UFDouble.ZERO_DBL
								: UFDoubleUtils.div(formatSignMnyMap.get(key),
										signTotalMny));
						String[] attributeNames = coloneVO.getAttributeNames();
						for (String attributeName : attributeNames) {
							if (attributeName.contains("amount")) {
								coloneVO.setAttributeValue(
										attributeName,
										UFDoubleUtils.multiply(
												(UFDouble) coloneVO
														.getAttributeValue(attributeName),
												coloneVO.getRate2()));
							}

						}
						integrationList.add(coloneVO);
						secondrow++;
					}

				} else {
//					if (formatMap.containsKey(vo.getPk_fct_ap())) {
					if (formatMap!=null&&formatMap.containsKey(vo.getPk_fct_ap())) {//add by �ƹڻ� ��ӿ�����ӷѵȺ��ͬ����Ϊ�գ�����Ƿ�Ϊ���ж�
						List<MinAndLongProjectCostLedgerVO> foramtList = getForamtDataList(
								vo, formatMap.get(vo.getPk_fct_ap()));
						integrationList.addAll(foramtList);
					}
				}
			}	
		}

		return integrationList;
	}

	private void initContType() throws BusinessException {
		contTypeMap = new HashMap<String, String>();
		String sql = "select v1.name ,v2.name pname from bd_defdoc v1  "
				+ " left join bd_defdoc v2 on v1.pid = v2.pk_defdoc "
				+ " where v1.dr =0 and exists (select l.pk_defdoclist from bd_defdoclist l where l.code = 'tgyt01' and v1.pk_defdoclist = l.pk_defdoclist)";
		List<Map<String, String>> list = (List<Map<String, String>>) getBaseDAO()
				.executeQuery(sql, new MapListProcessor());
		if (list != null && list.size() > 0) {
			for (Map<String, String> info : list) {
				contTypeMap.put(info.get("name"), info.get("pname"));
			}
		}

	}

	/**
	 * ��ú�ͬ��Ӧ��ҵ̬������Ϣ
	 * 
	 * @param vo
	 * 
	 * @param formatInfoList
	 * @return
	 */
	private List<MinAndLongProjectCostLedgerVO> getForamtDataList(
			MinAndLongProjectCostLedgerVO vo,
			List<Map<String, Object>> formatInfoList) {
		List<MinAndLongProjectCostLedgerVO> list = new ArrayList<MinAndLongProjectCostLedgerVO>();
		int secondrow = 1;
		for (Map<String, Object> formatInfoMap : formatInfoList) {
			String format = (String) formatInfoMap.get("vbdef22");// ҵ̬
			String formatname = (String) formatInfoMap.get("name");// ҵ̬

			UFDouble formatrate = (String) formatInfoMap.get("vbdef13") != null
					&& !"~".equals(formatInfoMap.get("vbdef13")) ? new UFDouble(
					formatInfoMap.get("vbdef13").toString())
					: UFDouble.ZERO_DBL;// ҵ̬����

			MinAndLongProjectCostLedgerVO coloneVO = (MinAndLongProjectCostLedgerVO) vo
					.clone();
			coloneVO.setInnercode(vo.getInnercode()
					+ String.format("%04d", secondrow));
			coloneVO.setFormatname(formatname + "����" + format);
			UFDouble rate = UFDoubleUtils.div(formatrate, new UFDouble(100));// ҵ̬����
			coloneVO.setRate2(rate);
			String[] attributeNames = coloneVO.getAttributeNames();
			for (String attributeName : attributeNames) {
				if (attributeName.contains("amount")) {
					coloneVO.setAttributeValue(attributeName, UFDoubleUtils
							.multiply((UFDouble) coloneVO
									.getAttributeValue(attributeName), rate));
				}

			}
			list.add(coloneVO);
			secondrow++;
		}
		return list;
	}
	
	
	
	/**
	 * �����ͬ����Ϣ
	 * @author �ƹڻ�
	 * @param vo
	 * @param actualMap
	 * @param factorPayMap
	 * @param outputMap
	 * @param accInvMap
	 * @param costMap
	 * @param invMnyMap
	 * @param firstrow
	 * @param innercode
	 * @return
	 */
	private MinAndLongProjectCostLedgerVO getHandleContMainData2(
			MinAndLongProjectCostLedgerVO vo, Map<String, UFDouble> actualMap,
			Map<String, UFDouble> factorPayMap,
			Map<String, Map<String, UFDouble>> outputMap,
			Map<String, Map<String, UFDouble>> accInvMap,
			Map<String, Map<String, UFDouble>> costMap,
			Map<String, Map<String, UFDouble>> invMnyMap, int row,
			String innercode) {
		String contcode = vo.getContcode();// ��ͬ����
		String key = vo.getPk_fct_ap();// ��ͬ����
		// vo.setInnercode(RandomSeqUtil.getRandomSeq(4));
		UFDouble rate = UFDoubleUtils.div(vo.getRate1(), new UFDouble(100));
		vo.setRate1(rate);
//		if (invMnyMap.containsKey(key)) {
		if (invMnyMap!=null&&invMnyMap.containsKey(key)) {//add by �ƹڻ� ��ӿ�����ӷѵȺ��ͬ����Ϊ�գ�����Ƿ�Ϊ���ж� 20200915
			vo.setAmount11(invMnyMap.get(key).get("local_money")); // �ۼ�ʵ����
			vo.setAmount12(UFDoubleUtils.div(vo.getAmount11(),
					UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// �ۼ�ʵ��(����˰)
																// �ۼ�ʵ����/(1+˰��)
			vo.setAmount14(UFDoubleUtils.sub(vo.getAmount11(), vo.getAmount12()));// �ۼ�ʵ���˰��
																					// �ۼ�ʵ����-�ۼ�ʵ�������˰��
		}
		//add by �ƹڻ� ��ӿ�����ӷѵȺ��ͬ����Ϊ�գ�����Ƿ�Ϊ���ж� 20200915 begin
		UFDouble tempAmount=UFDouble.ZERO_DBL;
		if(actualMap!=null&&actualMap.get(contcode) != null){
			tempAmount=actualMap.get(contcode);
		}
//		vo.setAmount19(UFDoubleUtils.add(
//				vo.getAmount19(),
//				actualMap.get(contcode) == null ? UFDouble.ZERO_DBL : actualMap
//						.get(contcode))); // �ۼ�ʵ����-NC
		vo.setAmount19(UFDoubleUtils.add(
		vo.getAmount19(),tempAmount)); // �ۼ�ʵ����-NC
		//add by �ƹڻ� ��ӿ�����ӷѵȺ��ͬ����Ϊ�գ�����Ƿ�Ϊ���ж� 20200915 end
		vo.setAmount20(UFDoubleUtils.div(vo.getAmount19(),
				UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// �ۼ�ʵ����-NC������˰��
															// B=A/(1+R)
															// �ۼ�ʵ����-NC/(1+˰��)
		vo.setAmount21(UFDoubleUtils.sub(vo.getAmount19(), vo.getAmount20()));// �ۼ�ʵ����-NC��˰�
																				// C=A-B
																				// �ۼ�ʵ����-NC-�ۼ�ʵ����-NC������˰��

		vo.setAmount10(UFDoubleUtils.sub(vo.getAmount9(), vo.getAmount19()));// �ۼ�Ӧ��δ����
																				// O-A
																				// �ۼ�Ӧ����-�ۼ�ʵ����-NC

		vo.setAmount15(UFDoubleUtils.sub(vo.getAmount5(), vo.getAmount19()));// ��ͬδ�����˰��M=L-A
																				// ��̬���(˰��)-�ۼ�ʵ����-NC
		vo.setAmount16(UFDoubleUtils.div(vo.getAmount15(),
				UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// ��ͬδ�������˰��
															// M/(1+R)
															// ��ͬδ�����˰��/(1+˰��)
		vo.setAmount17(UFDoubleUtils.sub(vo.getAmount15(), vo.getAmount16()));// ��ͬδ�����˰��
																				// M-N
																				// ��ͬδ�����˰��-��ͬδ�������˰��

//		vo.setAmount22(factorPayMap.get(contcode));// ��������
		vo.setAmount22(factorPayMap==null?UFDouble.ZERO_DBL:factorPayMap.get(contcode));// �������� //add by �ƹڻ� ��ӿ�����ӷѵȺ��ͬ����Ϊ�գ�����Ƿ�Ϊ���ж� 20200915 

		// nc���˷�Ʊ����˰��
//		if (accInvMap.containsKey(contcode)) {
		if (accInvMap!=null&&accInvMap.containsKey(contcode)) { //add by �ƹڻ� ��ӿ�����ӷѵȺ��ͬ����Ϊ�գ�����Ƿ�Ϊ���ж� 20200915 
			vo.setAmount23(UFDoubleUtils.add(vo.getAmount23(),
					accInvMap.get(contcode).get("local_cr")));
		}
		// ����ɱ�������˰��
		if (costMap!=null&&costMap.containsKey(contcode)) {//add by �ƹڻ� ��ӿ�����ӷѵȺ��ͬ����Ϊ�գ�����Ƿ�Ϊ���ж� 20200915 
			vo.setAmount24(UFDoubleUtils.add(vo.getAmount24(),
					costMap.get(contcode).get("local_de")));
		}

		vo.setAmount25(UFDoubleUtils.sub(vo.getAmount23(), vo.getAmount24()));// �ɵֿ۽���˰��˰�D-E
																				// nc���˷�Ʊ����˰��-����ɱ�������˰��

		vo.setAmount26(UFDoubleUtils.sub(vo.getAmount19(), vo.getAmount23()));// δ��ɱ�����˰��A-D
																				// �ۼ�ʵ����-NC-nc���˷�Ʊ����˰��
		vo.setAmount27(UFDoubleUtils.sub(vo.getAmount20(), vo.getAmount24()));// B-E
																				// �ۼ�ʵ����-NC������˰��
																				// -����ɱ�������˰��
		vo.setAmount28(UFDoubleUtils.sub(vo.getAmount21(), vo.getAmount25()));// δ��ɱ���˰�F-G
																				// δ��ɱ�����˰��
																				// -
																				// δ��ɱ�������˰��

		if (outputMap!=null&&outputMap.containsKey(key)) {//add by �ƹڻ� ��ӿ�����ӷѵȺ��ͬ����Ϊ�գ�����Ƿ�Ϊ���ж� 20200915
			vo.setAmount29(outputMap.get(key).get("local_money"));// �ۼƲ�ֵ����˰��
			vo.setAmount30(outputMap.get(key).get("local_notax"));// �ۼƲ�ֵ������˰��
			vo.setAmount31(outputMap.get(key).get("local_tax"));// �ۼƲ�ֵ��˰�
		}

		// ���ղ�ֵ A��H�ϴ�ֵ �ۼ�ʵ����-NC or �ۼƲ�ֵ����˰��
		if (UFDoubleUtils.isGreaterThan(vo.getAmount19(), vo.getAmount29())) {
			vo.setAmount32(vo.getAmount19());
		} else {
			vo.setAmount32(vo.getAmount29());
		}

		vo.setAmount33(UFDoubleUtils.sub(vo.getAmount32(), vo.getAmount19()));// �ۼ�
																				// ��ֵδ����
																				// ����˰��I-A
																				// ���ղ�ֵ
																				// -
																				// �ۼ�ʵ����-NC
		vo.setAmount34(UFDoubleUtils.div(vo.getAmount33(),
				UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// �ۼƲ�ֵδ�������˰��J/(1+R)
															// �ۼƲ�ֵδ�����˰/(1+˰��)
		vo.setAmount35(UFDoubleUtils.sub(vo.getAmount33(), vo.getAmount34()));// �ۼƲ�ֵδ����
																				// ��˰�
																				// J-K
																				// �ۼ�
																				// ��ֵδ����
																				// ����˰��-�ۼƲ�ֵδ����
																				// ������˰��

		vo.setInnercode((innercode != null && !"".equals(innercode) ? innercode
				: "") + String.format("%04d", row));
		vo.setFormatname("����");
		vo.setRate2(new UFDouble(1));
		
		//add by �ƹڻ� �ɱ�̨�� ������ʷ��úͿ�����ӷѵ�������� 20200813 begin
		if(!isGenCons){
			String contCode=vo.getContcode();
			if("������ӷ�".equals(contCode)||"���ʷ���".equals(contCode)){
				vo.setAmount10(new UFDouble(0));
				vo.setAmount11(vo.getAmount5());
				vo.setAmount12(vo.getAmount5());
				vo.setPk_format("");
				vo.setRate2(new UFDouble(0));
				vo.setRate1(new UFDouble(0));
			}
		}
		
		//add by �ƹڻ� �ɱ�̨�� ������ʷ��úͿ�����ӷѵ�������� 20200813 end
		
		return vo;
	}

	/**
	 * �����ͬ����Ϣ
	 * 
	 * @param vo
	 * @param actualMap
	 * @param factorPayMap
	 * @param outputMap
	 * @param accInvMap
	 * @param costMap
	 * @param invMnyMap
	 * @param firstrow
	 * @param innercode
	 * @return
	 */
	private MinAndLongProjectCostLedgerVO getHandleContMainData(
			MinAndLongProjectCostLedgerVO vo, Map<String, UFDouble> actualMap,
			Map<String, UFDouble> factorPayMap,
			Map<String, Map<String, UFDouble>> outputMap,
			Map<String, Map<String, UFDouble>> accInvMap,
			Map<String, Map<String, UFDouble>> costMap,
			Map<String, Map<String, UFDouble>> invMnyMap, int row,
			String innercode) {
		String contcode = vo.getContcode();// ��ͬ����
		String key = vo.getPk_fct_ap();// ��ͬ����
		// vo.setInnercode(RandomSeqUtil.getRandomSeq(4));
		UFDouble rate = UFDoubleUtils.div(vo.getRate1(), new UFDouble(100));
		vo.setRate1(rate);
		if (invMnyMap.containsKey(key)) {
			vo.setAmount11(invMnyMap.get(key).get("local_money")); // �ۼ�ʵ����
			vo.setAmount12(UFDoubleUtils.div(vo.getAmount11(),
					UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// �ۼ�ʵ��(����˰)
																// �ۼ�ʵ����/(1+˰��)
			vo.setAmount14(UFDoubleUtils.sub(vo.getAmount11(), vo.getAmount12()));// �ۼ�ʵ���˰��
																					// �ۼ�ʵ����-�ۼ�ʵ�������˰��
		}

		vo.setAmount19(UFDoubleUtils.add(
				vo.getAmount19(),
				actualMap.get(contcode) == null ? UFDouble.ZERO_DBL : actualMap
						.get(contcode))); // �ۼ�ʵ����-NC
		vo.setAmount20(UFDoubleUtils.div(vo.getAmount19(),
				UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// �ۼ�ʵ����-NC������˰��
															// B=A/(1+R)
															// �ۼ�ʵ����-NC/(1+˰��)
		vo.setAmount21(UFDoubleUtils.sub(vo.getAmount19(), vo.getAmount20()));// �ۼ�ʵ����-NC��˰�
																				// C=A-B
																				// �ۼ�ʵ����-NC-�ۼ�ʵ����-NC������˰��

		vo.setAmount10(UFDoubleUtils.sub(vo.getAmount9(), vo.getAmount19()));// �ۼ�Ӧ��δ����
																				// O-A
																				// �ۼ�Ӧ����-�ۼ�ʵ����-NC

		vo.setAmount15(UFDoubleUtils.sub(vo.getAmount5(), vo.getAmount19()));// ��ͬδ�����˰��M=L-A
																				// ��̬���(˰��)-�ۼ�ʵ����-NC
		vo.setAmount16(UFDoubleUtils.div(vo.getAmount15(),
				UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// ��ͬδ�����˰��
															// M/(1+R)
															// ��ͬδ�����˰��/(1+˰��)
		vo.setAmount17(UFDoubleUtils.sub(vo.getAmount15(), vo.getAmount16()));// ��ͬδ�����˰��
																				// M-N
																				// ��ͬδ�����˰��-��ͬδ�������˰��

		vo.setAmount22(factorPayMap.get(contcode));// ��������

		// nc���˷�Ʊ����˰��
		if (accInvMap.containsKey(contcode)) {
			vo.setAmount23(UFDoubleUtils.add(vo.getAmount23(),
					accInvMap.get(contcode).get("local_cr")));
		}
		// ����ɱ�������˰��
		if (costMap.containsKey(contcode)) {
			vo.setAmount24(UFDoubleUtils.add(vo.getAmount24(),
					costMap.get(contcode).get("local_de")));
		}

		vo.setAmount25(UFDoubleUtils.sub(vo.getAmount23(), vo.getAmount24()));// �ɵֿ۽���˰��˰�D-E
																				// nc���˷�Ʊ����˰��-����ɱ�������˰��

		vo.setAmount26(UFDoubleUtils.sub(vo.getAmount19(), vo.getAmount23()));// δ��ɱ�����˰��A-D
																				// �ۼ�ʵ����-NC-nc���˷�Ʊ����˰��
		vo.setAmount27(UFDoubleUtils.sub(vo.getAmount20(), vo.getAmount24()));// B-E
																				// �ۼ�ʵ����-NC������˰��
																				// -����ɱ�������˰��
		vo.setAmount28(UFDoubleUtils.sub(vo.getAmount21(), vo.getAmount25()));// δ��ɱ���˰�F-G
																				// δ��ɱ�����˰��
																				// -
																				// δ��ɱ�������˰��

		if (outputMap.containsKey(key)) {
			vo.setAmount29(outputMap.get(key).get("local_money"));// �ۼƲ�ֵ����˰��
			vo.setAmount30(outputMap.get(key).get("local_notax"));// �ۼƲ�ֵ������˰��
			vo.setAmount31(outputMap.get(key).get("local_tax"));// �ۼƲ�ֵ��˰�
		}

		// ���ղ�ֵ A��H�ϴ�ֵ �ۼ�ʵ����-NC or �ۼƲ�ֵ����˰��
		if (UFDoubleUtils.isGreaterThan(vo.getAmount19(), vo.getAmount29())) {
			vo.setAmount32(vo.getAmount19());
		} else {
			vo.setAmount32(vo.getAmount29());
		}

		vo.setAmount33(UFDoubleUtils.sub(vo.getAmount32(), vo.getAmount19()));// �ۼ�
																				// ��ֵδ����
																				// ����˰��I-A
																				// ���ղ�ֵ
																				// -
																				// �ۼ�ʵ����-NC
		vo.setAmount34(UFDoubleUtils.div(vo.getAmount33(),
				UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// �ۼƲ�ֵδ�������˰��J/(1+R)
															// �ۼƲ�ֵδ�����˰/(1+˰��)
		vo.setAmount35(UFDoubleUtils.sub(vo.getAmount33(), vo.getAmount34()));// �ۼƲ�ֵδ����
																				// ��˰�
																				// J-K
																				// �ۼ�
																				// ��ֵδ����
																				// ����˰��-�ۼƲ�ֵδ����
																				// ������˰��

		vo.setInnercode((innercode != null && !"".equals(innercode) ? innercode
				: "") + String.format("%04d", row));
		vo.setFormatname("����");
		vo.setRate2(new UFDouble(1));
		return vo;
	}

	/**
	 * ��ȡ��ͬ��ӦӦ����Ʊ��Ϣ
	 * 
	 * @param contSet
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Map<String, UFDouble>> getPayableMnyMap(
			Set<String> keySet) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select b.pk_fct_ap,sum(nvl(b.mpay,0)) local_money from  fct_execution_b b  ")
				.append(" where nvl(b.dr,0) = 0 ")
				.append(" and "
						+ SQLUtil.buildSqlForIn("b.pk_fct_ap",
								keySet.toArray(new String[0])));
		sql.append(" group by b.pk_fct_ap");
		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		Map<String, Map<String, UFDouble>> infoMap = new HashMap<String, Map<String, UFDouble>>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				String pk_fct_ap = (String) map.get("pk_fct_ap");
				if (!infoMap.containsKey(pk_fct_ap)) {
					infoMap.put(pk_fct_ap, new HashMap<String, UFDouble>());
				}
				UFDouble local_money = new UFDouble(String.valueOf(map
						.get("local_money")));
				infoMap.get(pk_fct_ap).put("local_money", local_money);
			}
		}

		return infoMap;
	}

	/**
	 * ��ȡ��ͬ����Ӧ������ص�ƾ֤�����Ϣ
	 * 
	 * @param contSet
	 * @param string
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Map<String, UFDouble>> getPayableVoucherMap(
			Set<String> contSet, String acccode) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select v1.def5 contcode,sum(nvl(v3.localdebitamount,0)) local_de,sum(nvl(v3.localcreditamount ,0)) local_cr from ap_payablebill v1  ")
				.append(" inner join fip_relation v2 on v2.src_relationid = v1.pk_payablebill and v2.src_billtype = v1.pk_tradetype and nvl(v2.dr,0)=0 ")
				.append(" inner join gl_detail v3 on v3.pk_voucher = v2.des_relationid and nvl(v3.dr,0) = 0  ")
				.append(" inner join bd_account v4  on v4.pk_account = v3.pk_account and nvl(v4.dr,0) = 0 ")
				.append(" where v1.pk_tradetype ='F1-Cxx-001'  ")
				.append(" and nvl(v1.dr,0) = 0 ")
				.append(" and v3.discardflagv <> 'Y' and v3.voucherkindv <> 255  and nvl(v3.errmessage2, '~') = '~' and v3.tempsaveflag <> 'Y' and v3.voucherkindv <> 5 ")
				.append(" and v4.code like '" + acccode + "%' ")
				.append(" and "
						+ SQLUtil.buildSqlForIn("v1.def5",
								contSet.toArray(new String[0])));
		sql.append(" group by v1.def5");
		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		Map<String, Map<String, UFDouble>> infoMap = new HashMap<String, Map<String, UFDouble>>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				String contcode = (String) map.get("contcode");
				if (!infoMap.containsKey(contcode)) {
					infoMap.put(contcode, new HashMap<String, UFDouble>());
				}
				infoMap.get(contcode).put("local_de",
						new UFDouble(String.valueOf(map.get("local_de"))));
				infoMap.get(contcode).put("local_cr",
						new UFDouble(String.valueOf(map.get("local_cr"))));
			}
		}

		return infoMap;
	}

	/**
	 * ��ȡҵ̬��Ϣ
	 * 
	 * @param keySet
	 * @return
	 * @throws BusinessException
	 */
	private java.util.Map<String, List<java.util.Map<String, Object>>> getFormatMap(
			Set<String> keySet) throws BusinessException {
		String sql = "select b.pk_fct_ap, vbdef22,vbdef13,bd_defdoc.name  from fct_ap_b b "
				+ " left join bd_defdoc on bd_defdoc.pk_defdoc = b.vbdef11 "
				+ "  where nvl(b.dr,0)=0 and  "
				+ SQLUtil.buildSqlForIn("b.pk_fct_ap",
						keySet.toArray(new String[0]));
		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql, new MapListProcessor());
		Map<String, List<java.util.Map<String, Object>>> infoMap = new HashMap<String, List<java.util.Map<String, Object>>>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> info : list) {
				String pk_fct_ap = (String) info.get("pk_fct_ap");
				if (!infoMap.containsKey(pk_fct_ap)) {
					infoMap.put(pk_fct_ap, new ArrayList<Map<String, Object>>());
				}
				infoMap.get(pk_fct_ap).add(info);
			}

		}

		return infoMap;
	}

	/**
	 * �ۼƲ�ֵ
	 * 
	 * @param keySet
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Map<String, UFDouble>> getOutputMap(Set<String> keySet)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select b.pk_fct_ap,sum(nvl(b.mcz,0)) local_money,sum(nvl(b.mczbhs,0)) local_notax,sum(nvl(b.mczse,0)) local_tax   ")
				.append(" from fct_output_b  b ")
				.append(" where nvl(b.dr,0)=0  ")
				.append(" and "
						+ SQLUtil.buildSqlForIn("b.pk_fct_ap",
								keySet.toArray(new String[0])));

		if (queryWhereMap.get("cdate") != null) {
			sql.append(queryWhereMap.get("cdate")
					.replace("cdate", "outputtime"));
		}

		sql.append(" group by b.pk_fct_ap");
		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		Map<String, Map<String, UFDouble>> infoMap = new HashMap<String, Map<String, UFDouble>>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				String pk_key = (String) map.get("pk_fct_ap");
				if (!infoMap.containsKey(pk_key)) {
					infoMap.put(pk_key, new HashMap<String, UFDouble>());
				}
				infoMap.get(pk_key).put("local_money",
						new UFDouble(String.valueOf(map.get("local_money"))));
				infoMap.get(pk_key).put("local_notax",
						new UFDouble(String.valueOf(map.get("local_notax"))));
				infoMap.get(pk_key).put("local_tax",
						new UFDouble(String.valueOf(map.get("local_tax"))));
			}
		}

		return infoMap;
	}

	/**
	 * ��ȡ��ͬ���������ص�ƾ֤�����Ϣ
	 * 
	 * @param contSet
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, UFDouble> getPayVoucherMap(Set<String> contSet,
			String pk_balatype, String acccode) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select v1.def5 contcode,sum(nvl(v3.localdebitamount,0)) nmny from ap_paybill v1  ")
				.append(" inner join ap_payitem item on v1.pk_paybill=item.pk_paybill  ")
				.append(" inner join fip_relation v2 on v2.src_relationid = v1.pk_paybill and v2.src_billtype = v1.pk_tradetype and nvl(v2.dr,0)=0 ")
				.append(" inner join gl_detail v3 on v3.pk_voucher = v2.des_relationid and nvl(v3.dr,0) = 0  ")
				.append(" inner join bd_account v4  on v4.pk_account = v3.pk_account and nvl(v4.dr,0) = 0 ")
				.append(" where v1.pk_tradetype ='F3-Cxx-004'  ")
				.append(" and nvl(v1.dr,0) = 0 ")
				.append(" and v3.discardflagv <> 'Y' and v3.voucherkindv <> 255  and nvl(v3.errmessage2, '~') = '~' and v3.tempsaveflag <> 'Y' and v3.voucherkindv <> 5 ")
				.append(" and v4.code like '" + acccode + "%' ")
				.append(" and "
						+ SQLUtil.buildSqlForIn("v1.def5",
								contSet.toArray(new String[0])));
		if (pk_balatype != null && !"".equals(pk_balatype)) {
			sql.append(" and item.pk_balatype ='" + pk_balatype + "'");//add by�ƹڻ� ���㷽ʽ�ڱ�����д��Ҫ�ñ�������ѯ���� 20200904
		}

		sql.append(" group by v1.def5");
		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		Map<String, UFDouble> infoMap = new HashMap<String, UFDouble>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				infoMap.put((String) map.get("contcode"),
						new UFDouble(String.valueOf(map.get("nmny"))));
			}
		}

		return infoMap;
	}
	
	
	/**
	 * @author �ƹڻ�
	 * @param acccode
	 * ��ȡ��Ŀ���
	 */
	private  Map<String, UFDouble> getBalByCode(String acccode) throws BusinessException {
		
		// ������֯
		String pk_org = queryValueMap.get("pk_org");
		//��������
		String endDate =queryValueMap.get("cdate_end");
		StringBuffer sb=new StringBuffer();
		String year=endDate.substring(0,4);
		String begMth="00";
		String endMth=endDate.substring(5, 7);
		sb.append("select sum(debitinitsum)-sum(creditinitsum)+sum(debitamountsum)-sum(creditamountsum) balmny,pk_org from ( ");
	    sb.append("select                                                                                                  ");
	    sb.append("distinct                                                                                               ");
	    sb.append("         sum(gl_balan.debitamount) debitinitsum,                                                        ");
	    sb.append("         sum(gl_balan.creditamount ) creditinitsum,                                                   ");
	    sb.append("         0 debitamountsum,                                                                               ");
	    sb.append("         0 creditamountsum,                                                                              ");
	    sb.append("         gl_balan.pk_org                                                                                 ");
	    sb.append("  from gl_balance gl_balan                                                                              ");
	    sb.append("  left join bd_accasoa accoa on accoa.pk_accasoa=gl_balan.pk_accasoa ");
	    sb.append(" where  gl_balan.year = '"+year+"' " )  ;  
	    if (pk_org != null && !"".equals(pk_org)) {// ������֯
			String[] orgstr = pk_org.split(",");
			sb.append("and " + SQLUtil.buildSqlForIn("gl_balan.pk_org", orgstr))
					.append("\r\n");
		}                                                                       
	    sb.append("   and gl_balan.adjustperiod >= '00'                                                                     ");
	    sb.append("   and gl_balan.adjustperiod < '01'                                                                      ");
	    sb.append("   and gl_balan.voucherkind <> 5                                                                         ");
	    sb.append("   and accoa.dispname like  '"+acccode+"%'                                                      ");
	    sb.append(" group by gl_balan.pk_org                                                                               ");
	    sb.append(" union                                                                                                   ");
	    sb.append("select                                                                                                  ");
	    sb.append("distinct                                                                                                 ");
	    sb.append("         0 debitinitsum,                                                                                 ");
	    sb.append("         0 creditinitsum,                                                                                ");
	    sb.append("         sum(gl_balan.debitamount) debitamountsum,                                                       ");
	    sb.append("         sum(gl_balan.creditamount ) creditamountsum,                                                    ");
	    sb.append("         gl_balan.pk_org                                                                                 ");
	    sb.append("  from gl_balance gl_balan                                                                              ");
	    sb.append("  left join bd_accasoa accoa on accoa.pk_accasoa=gl_balan.pk_accasoa                                                                              ");
	    sb.append(" where  gl_balan.year = '"+year+"' " )  ;  
	    if (pk_org != null && !"".equals(pk_org)) {// ������֯
			String[] orgstr = pk_org.split(",");
			sb.append("and " + SQLUtil.buildSqlForIn("gl_balan.pk_org", orgstr))
					.append("\r\n");
		}  
	    sb.append("   and gl_balan.adjustperiod >= '01'                                                                     ");
	    sb.append("   and gl_balan.adjustperiod <= '"+endMth+"'                                                                    ");
	    sb.append("   and gl_balan.voucherkind <> 5                                                                         ");
	    sb.append("   and accoa.dispname like  '"+acccode+"%'                                                				 ");
	    sb.append(" group by gl_balan.pk_org )                                                                               ");
//	    sb.append(") )                                                                                                       ");
	    sb.append("group by pk_org                                       ");

		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sb.toString(), new MapListProcessor());
		Map<String, UFDouble> infoMap = new HashMap<String, UFDouble>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				infoMap.put((String) map.get("pk_org"),
						new UFDouble(String.valueOf(map.get("balmny"))));
			}
		}
		return infoMap;
		
	}
	
	
	/**
	 * @author �ƹڻ�
	 * @param acccode
	 * ��ȡ��Ŀ���sql
	 */
	private  String getBalByCodeSql(String acccode) throws BusinessException {
		
		// ������֯
		String pk_org = queryValueMap.get("pk_org");
		//��������
		String endDate =queryValueMap.get("cdate_end");
		String contCode="";
		StringBuffer sb=new StringBuffer();
		String year=endDate.substring(0,4);
		String begMth="00";
		String endMth=endDate.substring(5, 7);
		sb.append(" select '' pk_fct_ap,           ");
	    sb.append("    '' pk_project,              ");
	    sb.append("    '' projectname,             ");
	    sb.append("    '' vclass,                  ");
	    if(FINACODE.equals(acccode)){
	    	contCode="������ӷ�";
	    }else if(DEVACODE.equals(acccode)){
	    	contCode="���ʷ���";
	    }
	    sb.append("'").append(contCode).append("'").append(" contcode,");
	    sb.append("'").append(contCode).append("'").append(" contname,");
	    sb.append("    '' tatalcontcode,           ");
	    sb.append("    '' tatalcontname,           ");
	    sb.append("    '' conttype,                ");
	    sb.append("    '' contattribute,           ");
	    sb.append("    pk_org pk_first,            ");
	    sb.append("    orgname firstname,               ");
	    sb.append("    '' pk_second,               ");
	    sb.append("    '' secondname,              ");
	    sb.append("    '' signdate,                ");
	    sb.append("    '' settstate,               ");
	    sb.append("    0 amount1,                 ");
	    sb.append("    0 amount2,                 ");
	    sb.append("    0 amount3,                 ");
	    sb.append("    0 amount4,                 ");
	    sb.append("    balmny amount5,             ");
	    sb.append("    0 rate1,                   ");
	    sb.append("    balmny amount6,             ");
	    sb.append("    0 amount7,                 ");
	    sb.append("    balmny amount8,             ");
	    sb.append("    0 amount9,                 ");
	    sb.append("    0 amount10,                ");
	    sb.append("    0 amount15,                ");
	    sb.append("    0 amount16,                ");
	    sb.append("    0 amount17,                ");
	    sb.append("    0 amount18,                ");
	    sb.append("    balmny amount19,            ");
	    sb.append("    balmny amount20,            ");
	    sb.append("    0 amount21,                ");
	    sb.append("    0 amount22,                ");
	    sb.append("    balmny amount23,            ");
	    sb.append("    balmny amount24,            ");
	    sb.append("    0 amount25,                ");
	    sb.append("    0 amount26,                ");
	    sb.append("    0 amount27,                ");
	    sb.append("    0 amount28,                ");
	    sb.append("    balmny amount29,            ");
	    sb.append("    balmny amount30,            ");
	    sb.append("    0 amount31,                ");
	    sb.append("    balmny amount32,            ");
	    sb.append("    0 amount33,                ");
	    sb.append("    0 amount34,                ");
	    sb.append("    0 amount35,                ");
	    sb.append("    '' pk_dept,                 ");
	    sb.append("    '' pk_psndoc,               ");
	    sb.append("    '' modename,                ");
	    sb.append("    '' pk_format,               ");
	    sb.append("    0 rate2,                   ");
	    sb.append("    'Y' isneed                  ");
	    sb.append("                                ");
	    sb.append("  from (                        ");
		sb.append("select sum(debitinitsum)-sum(creditinitsum)+sum(debitamountsum)-sum(creditamountsum) balmny,pk_org,orgname from ( ");
	    sb.append("select                                                                                                  ");
	    sb.append("distinct                                                                                               ");
	    sb.append("         sum(gl_balan.debitamount) debitinitsum,                                                        ");
	    sb.append("         sum(gl_balan.creditamount ) creditinitsum,                                                   ");
	    sb.append("         0 debitamountsum,                                                                               ");
	    sb.append("         0 creditamountsum,                                                                              ");
	    sb.append("         gl_balan.pk_org ,orgs.name orgname                                                                                ");
	    sb.append("  from gl_balance gl_balan                                                                              ");
	    sb.append("  left join bd_accasoa accoa on accoa.pk_accasoa=gl_balan.pk_accasoa ");
	    sb.append("  left join org_orgs orgs on gl_balan.pk_org= orgs.pk_org ");
	    sb.append(" where  gl_balan.year = '"+year+"' " )  ;  
	    if (pk_org != null && !"".equals(pk_org)) {// ������֯
			String[] orgstr = pk_org.split(",");
			sb.append("and " + SQLUtil.buildSqlForIn("gl_balan.pk_org", orgstr))
					.append("\r\n");
		}                                                                       
	    sb.append("   and gl_balan.adjustperiod >= '00'                                                                     ");
	    sb.append("   and gl_balan.adjustperiod < '01'                                                                      ");
	    sb.append("   and gl_balan.voucherkind <> 5                                                                         ");
	    sb.append("   and accoa.dispname like  '"+acccode+"%'                                                      ");
	    sb.append(" group by gl_balan.pk_org ,orgs.name                                                                              ");
	    sb.append(" union                                                                                                   ");
	    sb.append("select                                                                                                  ");
	    sb.append("distinct                                                                                                 ");
	    sb.append("         0 debitinitsum,                                                                                 ");
	    sb.append("         0 creditinitsum,                                                                                ");
	    sb.append("         sum(gl_balan.debitamount) debitamountsum,                                                       ");
	    sb.append("         sum(gl_balan.creditamount ) creditamountsum,                                                    ");
	    sb.append("         gl_balan.pk_org ,orgs.name orgname                                                                                  ");
	    sb.append("  from gl_balance gl_balan                                                                              ");
	    sb.append("  left join org_orgs orgs on gl_balan.pk_org= orgs.pk_org  ");
	    sb.append("  left join bd_accasoa accoa on accoa.pk_accasoa=gl_balan.pk_accasoa                                                                              ");
	    sb.append(" where  gl_balan.year = '"+year+"' " )  ;  
	    if (pk_org != null && !"".equals(pk_org)) {// ������֯
			String[] orgstr = pk_org.split(",");
			sb.append("and " + SQLUtil.buildSqlForIn("gl_balan.pk_org", orgstr))
					.append("\r\n");
		}  
	    sb.append("   and gl_balan.adjustperiod >= '01'                                                                     ");
	    sb.append("   and gl_balan.adjustperiod <= '"+endMth+"'                                                                    ");
	    sb.append("   and gl_balan.voucherkind <> 5                                                                         ");
	    sb.append("   and accoa.dispname like  '"+acccode+"%'                                                				 ");
	    sb.append(" group by gl_balan.pk_org,orgs.name )                                                                               ");
//	    sb.append(") )                                                                                                       ");
	    sb.append("group by pk_org ,orgname                                      ");
	    sb.append(" )                                     ");
		return sb.toString();
		
	}
	

	
	
	/**
	 * ��ȡ��ͬ��¼
	 * @author �ƹڻ�
	 * @param istotal
	 * @return
	 * @throws BusinessException
	 */
	private List<MinAndLongProjectCostLedgerVO> getCostDatas2(boolean istotal)
			throws BusinessException {
		// ��Ŀ����
		  String cfinancialo = queryValueMap.get("pk_project");
		    // ������֯
		    String pk_org = queryValueMap.get("pk_org");
		    
		 // ����
		    String pk_cust = queryValueMap.get("pk_cust");

		    StringBuffer sql = new StringBuffer();
		    //add by �ƹڻ� SDYC-388 ����������ɱ�̨�� 20200812 begin
		    StringBuffer sb = new StringBuffer();
//		    sql.append(
		    sb.append(
		        "select fct_ap.pk_fct_ap                                  pk_fct_ap ")
		        // ��ͬ����
		        .append("   ,fct_ap.proname                               pk_project")
		        .append("   ,b.project_name                               projectname")

		        // ��Ŀ����
		        .append("   , ''                                                  vclass")
		        // ����
		        .append("   ,fct_ap.vbillcode                                     contcode")
		        // ��ͬ����
		        .append("   ,fct_ap.ctname                                       contname")
		        // ��ͬ����
		        .append("   ,nvl(decode(fct_ap.def54,'~','',fct_ap.def54),'')    tatalcontcode")
		        // �ܰ���ͬ����
		        .append("   ,fct_ap.def55                                        tatalcontname")
		        // �ܰ���ͬ����
		        .append("   ,fct_ap.contype                                      conttype")
		        // ��ͬ���
		        .append("   ,nvl(decode(fct_ap.vdef3,'~','N',fct_ap.vdef3),'N')   contattribute")
		        // ��ͬ����
		        .append("   ,fct_ap.first                                         pk_first ")
		        .append("   ,t.name                                               firstname ")

		        // �׷���λ
		        .append("   ,fct_ap.second                                        pk_second")
		        .append("   ,w.name                                               secondname ")
		        // �ҷ���λ
		        .append("   ,fct_ap.d_sign                                         signdate")
		        // ǩԼ����
		        .append("   ,fct_ap.vdef15                                          settstate")
		        // ����״̬
		        .append("   ,fct_ap.msign                                          amount1")
		        // ǩԼ���
		        .append("   ,nvl(decode(fct_ap.vdef6,'~',0,fct_ap.vdef6),0)     amount2")
		        // �ۼƱ�����
		        .append("   ,nvl(decode(fct_ap.vdef7,'~',0,fct_ap.vdef7),0)     amount3")
		        // �ۼƲ���Э����
		        .append("   ,nvl(decode(fct_ap.vdef8,'~',0,fct_ap.vdef8),0)     amount4   ")
		        // ����������
		        .append("   ,nvl(decode(fct_ap.vdef9,'~',0,fct_ap.vdef9),0)   amount5 ")
		        // ��̬����˰��
		        .append("   ,fct_ap.rate                                           rate1")
		        // ˰��
		        .append("   ,nvl(decode(fct_ap.vdef10,'~',0,fct_ap.vdef10),0)   amount6       ")
		        // ��̬������˰��
		        .append("   ,nvl(decode(fct_ap.vdef11,'~',0,fct_ap.vdef11),0)   amount7 ")
		        // ��̬��˰�
		        .append("   ,nvl(decode(fct_ap.vdef16,'~',0,fct_ap.vdef16),0)   amount8")
		        // ������
		        .append("   ,nvl(decode(fct_ap.def31,'~',0,fct_ap.def31),0)     amount9")
		        // �ۼ�Ӧ����
		        .append("   ,0                                                      amount10")
		        // �ۼ�Ӧ��δ����
		        /*
		         * �ۼ���Ϣ������ȡ��ϸ���� .append(
		         * "   ,nvl(decode(fct_ap.vdef21,'~',0,fct_ap.vdef21),0)     amount11"
		         * ) // �ۼ�ʵ�����˰�� .append(
		         * "   ,nvl(decode(fct_ap.def42,'~',0,fct_ap.def42),0)     amount12"
		         * ) // �ۼ�ʵ�������˰�� // .append(
		         * "   ,nvl(decode(fct_ap.def43,'~',0,fct_ap.def43),0)     amount13"
		         * ) // // �ۼ�ʵ�������˰�� .append(
		         * "   ,nvl(decode(fct_ap.def43,'~',0,fct_ap.def43),0)     amount14"
		         * )
		         */
		        // �ۼ�ʵ���˰�
		        .append("   ,0                                                      amount15")
		        // ?��ͬδ�����˰��
		        .append("   ,0                                                      amount16")
		        // ?��ͬδ�������˰��
		        .append("   ,0                                                      amount17")
		        // ��ͬδ���� ��˰�
		        .append("   ,nvl(decode(fct_ap.vdef18,'~',0,fct_ap.vdef18),0)   amount18")
		        // EBS��Ʊ����ۼƣ���˰��
		        //.append("   ,nvl(decode(fct_ap.def98,'~',0,fct_ap.def98),0)   amount19")
		        .append("   ,nvl(decode(fct_ap.vdef21,'~',0,fct_ap.vdef21),0)   amount19")//add by �ƹڻ� �ۼ�ʵ����-NCӦ����fct_ap.vdef21 20200904
		        // �ۼ�ʵ����-NC
		        .append("   ,0                                                      amount20")
		        // �ۼ�ʵ����-NC������˰��
		        .append("   ,0                                                      amount21")
		        // �ۼ�ʵ����-NC��˰�
		        .append("   ,0                                                      amount22")
		        // ��������
		        .append("   , nvl(decode(fct_ap.def61,'~',0,decode(billtype.pk_billtypecode,'FCT1-Cxx-003',0,fct_ap.def61)),0)    amount23")
		        // nc���˷�Ʊ����˰��
		        .append("   ,nvl(decode(fct_ap.def62,'~',0,fct_ap.def62),0)     amount24")
		        // ����ɱ�������˰��
		        .append("   ,0                                                      amount25")
		        // �ɵֿ۽���˰��˰�
		        .append("   ,0                                                      amount26")
		        // δ��ɱ�����˰��
		        .append("   ,0                                                      amount27")
		        // δ��ɱ�������˰��
		        .append("   ,0                                                      amount28")
		        // δ��ɱ���˰�
		        .append("   ,0                                                      amount29")
		        // �ۼƲ�ֵ����˰��
		        .append("   ,0                                                      amount30")
		        // �ۼƲ�ֵ������˰��
		        .append("   ,0                                                      amount31")
		        // �ۼƲ�ֵ��˰�
		        .append("   ,0                                                      amount32")
		        // ���ղ�ֵ
		        .append("   ,0                                                      amount33")
		        // �ۼ� ��ֵδ���� ����˰��
		        .append("   ,0                                                      amount34")
		        // �ۼƲ�ֵδ���� ������˰��
		        .append("   ,0                                                      amount35")
		        // �ۼƲ�ֵδ���� ��˰�
		        .append("   ,fct_ap.def50                                           pk_dept")
		        // ����
		        .append("   ,fct_ap.vdef12                                          pk_psndoc")
		        // ������
		        .append("   ,fct_ap.vdef4                                           modename")
		        // �а���ʽ
		        .append("   ,''                                                     pk_format")
		        // ҵ̬
		        .append("   ,0                                                      rate2")
		        // ҵ̬����
		        .append("   ,case when billtype.pk_billtypecode ='FCT1-Cxx-001' and nvl(fct_ap.contype,'~')<>'Ӫ����' and nvl(fct_ap.def103,'~')<>'Y' then 'Y' else (")
		        .append(" case when billtype.pk_billtypecode ='FCT1-Cxx-001' and nvl(fct_ap.contype,'~')='Ӫ����' and nvl(fct_ap.def104,'~')='Y' then 'Y' else (")
		        .append(" case when billtype.pk_billtypecode ='FCT1-Cxx-003' and nvl(fct_ap.def105,'~')='Y' then 'Y' else 'N' end)end)end isneed")//�ж��Ƿ���Ҫ����(1��Ӫ����ȡdef103Ϊ������ݣ�2Ӫ����ȡdef104Ϊ�ǵ�����3ͨ�ú�ͬdef105��ѡ��ȡ��)
		        .append("  from fct_ap ")
		        .append(" inner join bd_billtype billtype  on billtype.pk_billtypeid = fct_ap.ctrantypeid ")
		        .append(" left join bd_project b on b.pk_project = fct_ap.proname and b.dr = 0  ")
		        .append(" left join org_orgs t on t.pk_org = fct_ap.first and t.dr = 0  ")
		        .append(" left join bd_cust_supplier w on w.pk_cust_sup = fct_ap.second and t.dr = 0  ")
		        .append(" where   nvl(fct_ap.dr,0) = 0 ")
		        .append(" and fct_ap.blatest = 'Y' ")// �Ƿ�����
		        .append("and fct_ap.def52 = '����ͨ��' ").append("\r\n")// --��ͬ״̬
//		        .append("and fct_ap.vdef5 = 'Y' ").append("\r\n");// --����ɱ����
		        .append("and billtype.pk_billtypecode in ('FCT1-Cxx-003','FCT1-Cxx-001') ").append("\r\n");//EBS�ɱ������ͬ��ͨ�ú�ͬ
		    if (istotal) {
		      sb.append(
		          "and (fct_ap.def54 is null or fct_ap.def54 = '~' or fct_ap.def54 = fct_ap.vbillcode) ")
//		                    "and ( fct_ap.def54 = fct_ap.vbillcode) ")

		          .append("\r\n");// --�ܰ���ͬ����
		      // sql.append("and (fct_ap.def55 is null or fct_ap.def55 = '~') ")
		      // .append("\r\n");// --�ܰ���ͬ����
		      // sql.append("and fct_ap.vbillcode = '100201003.01-���ϲɹ���-2019-0001'")
		      // .append("\r\n");

		    } else {
		      sb.append(
		          "and (   fct_ap.def54 <> fct_ap.vbillcode) ")
//		          "and (fct_ap.def54 is not null and fct_ap.def54 <> '~' and fct_ap.def54 <> fct_ap.vbillcode) ")
		          .append("\r\n");// --�ܰ���ͬ����
		      // sql.append(
		      // "and (fct_ap.def55 is not null and  fct_ap.def55 <> '~') ")
		      // .append("\r\n");// --�ܰ���ͬ����
		    }
		    // if (ccode != null) {// ��ͬ����
		    // sql.append("and fct_ap.vbillcode = '" + ccode + "'").append("\r\n");
		    // }
		    //add by �ƹڻ� �ְܷ���ͬ����ʾ �ְܷ���ͬ����Ϊ�յ����� 20200917 begin
		    if(isGenCons){
		    	sb.append(" and (nvl(fct_ap.def54,'~')<> '~' ) ");
		    }
		  //add by �ƹڻ� �ְܷ���ͬ����ʾ �ְܷ���ͬ����Ϊ�յ����� 20200917 end
		    if (pk_org != null && !"".equals(pk_org)) {// ������֯
		      String[] orgstr = pk_org.split(",");
		      sb.append("and " + SQLUtil.buildSqlForIn("fct_ap.pk_org", orgstr))
		          .append("\r\n");
		    }
		    if (cfinancialo != null && cfinancialo.length() > 0) {// ��Ŀ����
		      if (cfinancialo.indexOf(",") >= 0) {
		        sb.append(" and "
		            + SQLUtil.buildSqlForIn("fct_ap.proname",
		                cfinancialo.split(",")));
		      } else {
		        sb.append(" and fct_ap.proname = '" + cfinancialo + "' ");
		      }
		    }
		    
		    if (pk_cust != null && !"".equals(pk_cust)) {// ����
			      String[] pk_custs = pk_cust.split(",");
			      sb.append("and " + SQLUtil.buildSqlForIn("fct_ap.second", pk_custs))
			          .append("\r\n");
			    }
		    // if (cname != null) {// ��ͬ����
		    // sql.append("and fct_ap.ctname like '%" + cname + "%' ").append(
		    // "\r\n");
		    // }
//		    sql.append(" order by b.project_name,fct_ap.vbillcode ");
		    sql=sql.append(" select * from ( ");
		    sql=sql.append(sb);
		    sql.append(" ) ");
		    sql.append(" where nvl(isneed,'N')='Y' ");
		  //add by �ƹڻ� �ְܷ���ͬ����ʾ ������ӷ� 20200917 begin
		    if(!isGenCons){
			    sql.append(" union ");
			    sql.append(getBalByCodeSql(FINACODE));
			    sql.append(" union ");
			    sql.append(getBalByCodeSql(DEVACODE));
		    }
		  //add by �ƹڻ� �ְܷ���ͬ����ʾ ������ӷ� 20200917 end
		    sql.append(" order by  projectname,contcode ");
		    
		    List<MinAndLongProjectCostLedgerVO> list = (List<MinAndLongProjectCostLedgerVO>) getBaseDAO()
		        .executeQuery(
		            sql.toString(),
		            new BeanListProcessor(
		                MinAndLongProjectCostLedgerVO.class));
		    return list;
	}
	
	


	/**
	 * ��ȡ��ͬ��¼
	 * 
	 * @param istotal
	 * @return
	 * @throws BusinessException
	 */
	private List<MinAndLongProjectCostLedgerVO> getCostDatas(boolean istotal)
			throws BusinessException {
		// ��Ŀ����
		String cfinancialo = queryValueMap.get("pk_project");
		// ������֯
		String pk_org = queryValueMap.get("pk_org");

		StringBuffer sql = new StringBuffer();
		sql.append(
				"select fct_ap.pk_fct_ap                                  pk_fct_ap ")
				// ��ͬ����
				.append("   ,fct_ap.proname                               pk_project")
				.append("   ,b.project_name                               projectname")

				// ��Ŀ����
				.append("   , ''                                                  vclass")
				// ����
				.append("   ,fct_ap.vbillcode                                     contcode")
				// ��ͬ����
				.append("   ,fct_ap.ctname                                       contname")
				// ��ͬ����
				.append("   ,fct_ap.def54                                        tatalcontcode")
				// �ܰ���ͬ����
				.append("   ,fct_ap.def55                                        tatalcontname")
				// �ܰ���ͬ����
				.append("   ,fct_ap.contype                                      conttype")
				// ��ͬ���
				.append("   ,nvl(decode(fct_ap.vdef3,'~','N',fct_ap.vdef3),'N')   contattribute")
				// ��ͬ����
				.append("   ,fct_ap.first                                         pk_first ")
				.append("   ,t.name                                               firstname ")

				// �׷���λ
				.append("   ,fct_ap.second                                        pk_second")
				.append("   ,w.name                                               secondname ")
				// �ҷ���λ
				.append("   ,fct_ap.d_sign                                         signdate")
				// ǩԼ����
				.append("   ,fct_ap.vdef15                                          settstate")
				// ����״̬
				.append("   ,fct_ap.msign                                          amount1")
				// ǩԼ���
				.append("   ,nvl(decode(fct_ap.vdef6,'~','0',fct_ap.vdef6),'0')     amount2")
				// �ۼƱ�����
				.append("   ,nvl(decode(fct_ap.vdef7,'~','0',fct_ap.vdef7),'0')     amount3")
				// �ۼƲ���Э����
				.append("   ,nvl(decode(fct_ap.vdef8,'~','0',fct_ap.vdef8),'0')     amount4   ")
				// ����������
				.append("   ,nvl(decode(fct_ap.vdef9,'~','0',fct_ap.vdef9),'0')   amount5 ")
				// ��̬����˰��
				.append("   ,fct_ap.rate                                           rate1")
				// ˰��
				.append("   ,nvl(decode(fct_ap.vdef10,'~','0',fct_ap.vdef10),'0')   amount6       ")
				// ��̬������˰��
				.append("   ,nvl(decode(fct_ap.vdef11,'~','0',fct_ap.vdef11),'0')   amount7 ")
				// ��̬��˰�
				.append("   ,nvl(decode(fct_ap.vdef16,'~','0',fct_ap.vdef16),'0')   amount8")
				// ������
				.append("   ,nvl(decode(fct_ap.def31,'~','0',fct_ap.def31),'0')     amount9")
				// �ۼ�Ӧ����
				.append("   ,0                                                      amount10")
				// �ۼ�Ӧ��δ����
				/*
				 * �ۼ���Ϣ������ȡ��ϸ���� .append(
				 * "   ,nvl(decode(fct_ap.vdef21,'~','0',fct_ap.vdef21),'0')     amount11"
				 * ) // �ۼ�ʵ�����˰�� .append(
				 * "   ,nvl(decode(fct_ap.def42,'~','0',fct_ap.def42),'0')     amount12"
				 * ) // �ۼ�ʵ�������˰�� // .append(
				 * "   ,nvl(decode(fct_ap.def43,'~','0',fct_ap.def43),'0')     amount13"
				 * ) // // �ۼ�ʵ�������˰�� .append(
				 * "   ,nvl(decode(fct_ap.def43,'~','0',fct_ap.def43),'0')     amount14"
				 * )
				 */
				// �ۼ�ʵ���˰�
				.append("   ,0                                                      amount15")
				// ?��ͬδ�����˰��
				.append("   ,0                                                      amount16")
				// ?��ͬδ�������˰��
				.append("   ,0                                                      amount17")
				// ��ͬδ���� ��˰�
				.append("   ,nvl(decode(fct_ap.vdef18,'~','0',fct_ap.vdef18),'0')   amount18")
				// EBS��Ʊ����ۼƣ���˰��
				.append("   ,nvl(decode(fct_ap.def98,'~','0',fct_ap.def98),'0')   amount19")
				// �ۼ�ʵ����-NC
				.append("   ,0                                                      amount20")
				// �ۼ�ʵ����-NC������˰��
				.append("   ,0                                                      amount21")
				// �ۼ�ʵ����-NC��˰�
				.append("   ,0                                                      amount22")
				// ��������
				.append("   ,nvl(decode(fct_ap.def61,'~','0',fct_ap.def61),'0')    amount23")
				// nc���˷�Ʊ����˰��
				.append("   ,nvl(decode(fct_ap.def62,'~','0',fct_ap.def62),'0')     amount24")
				// ����ɱ�������˰��
				.append("   ,0                                                      amount25")
				// �ɵֿ۽���˰��˰�
				.append("   ,0                                                      amount26")
				// δ��ɱ�����˰��
				.append("   ,0                                                      amount27")
				// δ��ɱ�������˰��
				.append("   ,0                                                      amount28")
				// δ��ɱ���˰�
				.append("   ,0                                                      amount29")
				// �ۼƲ�ֵ����˰��
				.append("   ,0                                                      amount30")
				// �ۼƲ�ֵ������˰��
				.append("   ,0                                                      amount31")
				// �ۼƲ�ֵ��˰�
				.append("   ,0                                                      amount32")
				// ���ղ�ֵ
				.append("   ,0                                                      amount33")
				// �ۼ� ��ֵδ���� ����˰��
				.append("   ,0                                                      amount34")
				// �ۼƲ�ֵδ���� ������˰��
				.append("   ,0                                                      amount35")
				// �ۼƲ�ֵδ���� ��˰�
				.append("   ,fct_ap.def50                                           pk_dept")
				// ����
				.append("   ,fct_ap.vdef12                                          pk_psndoc")
				// ������
				.append("   ,fct_ap.vdef4                                           modename")
				// �а���ʽ
				.append("   ,''                                                     pk_format")
				// ҵ̬
				.append("   ,0                                                      rate2")
				// ҵ̬����
				.append("  from fct_ap ")
				.append(" inner join bd_billtype billtype  on billtype.pk_billtypeid = fct_ap.ctrantypeid ")
				.append(" left join bd_project b on b.pk_project = fct_ap.proname and b.dr = 0  ")
				.append(" left join org_orgs t on t.pk_org = fct_ap.first and t.dr = 0  ")
				.append(" left join bd_cust_supplier w on w.pk_cust_sup = fct_ap.second and t.dr = 0  ")
				.append(" where billtype.pk_billtypecode = 'FCT1-Cxx-001' and fct_ap.dr = 0 ")
				.append(" and fct_ap.blatest = 'Y' ")// �Ƿ�����
				.append("and fct_ap.def52 = '����ͨ��' ").append("\r\n")// --��ͬ״̬
				.append("and fct_ap.vdef5 = 'Y' ").append("\r\n");// --����ɱ����
		if (istotal) {
			sql.append(
//					"and (fct_ap.def54 is null or fct_ap.def54 = '~' or fct_ap.def54 = fct_ap.vbillcode) ")
										"and ( fct_ap.def54 = fct_ap.vbillcode) ")

					.append("\r\n");// --�ܰ���ͬ����
			// sql.append("and (fct_ap.def55 is null or fct_ap.def55 = '~') ")
			// .append("\r\n");// --�ܰ���ͬ����
			// sql.append("and fct_ap.vbillcode = '100201003.01-���ϲɹ���-2019-0001'")
			// .append("\r\n");

		} else {
			sql.append(
					"and (fct_ap.def54 is not null and fct_ap.def54 <> '~' and fct_ap.def54 <> fct_ap.vbillcode) ")
					.append("\r\n");// --�ܰ���ͬ����
			// sql.append(
			// "and (fct_ap.def55 is not null and  fct_ap.def55 <> '~') ")
			// .append("\r\n");// --�ܰ���ͬ����
		}
		// if (ccode != null) {// ��ͬ����
		// sql.append("and fct_ap.vbillcode = '" + ccode + "'").append("\r\n");
		// }
		if (pk_org != null && !"".equals(pk_org)) {// ������֯
			String[] orgstr = pk_org.split(",");
			sql.append("and " + SQLUtil.buildSqlForIn("fct_ap.pk_org", orgstr))
					.append("\r\n");
		}
		if (cfinancialo != null && cfinancialo.length() > 0) {// ��Ŀ����
			if (cfinancialo.indexOf(",") >= 0) {
				sql.append(" and "
						+ SQLUtil.buildSqlForIn("fct_ap.proname",
								cfinancialo.split(",")));
			} else {
				sql.append(" and fct_ap.proname = '" + cfinancialo + "' ");
			}
		}
		// if (cname != null) {// ��ͬ����
		// sql.append("and fct_ap.ctname like '%" + cname + "%' ").append(
		// "\r\n");
		// }
		sql.append(" order by b.project_name,fct_ap.vbillcode ");

		List<MinAndLongProjectCostLedgerVO> list = (List<MinAndLongProjectCostLedgerVO>) getBaseDAO()
				.executeQuery(
						sql.toString(),
						new BeanListProcessor(
								MinAndLongProjectCostLedgerVO.class));
		return list;
	}

	/**
	 * ������㷽ʽ
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public void initBalatypeMap() throws BusinessException {
		balatypeMap = new HashMap<String, Map<String, String>>();
		String sql = "select bd_balatype.code,bd_balatype.name,bd_balatype.pk_balatype from bd_balatype where nvl(dr,0)=0 ";
		List<Map<String, String>> list = (List<Map<String, String>>) getBaseDAO()
				.executeQuery(sql, new MapListProcessor());
		if (list != null && list.size() > 0) {
			for (Map<String, String> info : list) {
				balatypeMap.put(info.get("code"), info);
			}

		}
	}

}