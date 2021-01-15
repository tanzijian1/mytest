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
 * 中长期项目成本台账（成本报表） ljf
 * 
 * @author ASUS
 * 
 *         报表相关项:R 税率 L 动态金额（含税） O 累计应付款 M 合同未付款 （含税） N 合同未付款 （不含税） A 累计实付款-NC B
 *         累计实付款-NC（不含税） C 累计实付款-NC（税额） D nc入账发票金额（含税） E 已入成本（不含税） F 未入成本（含税） G
 *         未入成本（不含税） H 累计产值（含税） I 最终产值 J 累计 产值未付款 （含税） K 累计产值未付款 （不含税）
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
	 * 成本合同信息
	 * 
	 * @param istotal
	 *            是否总包
	 * @return
	 * @throws BusinessException
	 */
	protected List<MinAndLongProjectCostLedgerVO> getCosttLedgerDatas(
			boolean istotal) throws BusinessException {
		initBalatypeMap();
		initContType();
		// 1.初始读取总包/分包成本合同信息
		//add by 黄冠华 SDYC-53 成本台账（成本报表） 20200813 begin
		isGenCons=istotal;
//		List<MinAndLongProjectCostLedgerVO> totallist = getCostDatas(true);// 总包合同信息
		List<MinAndLongProjectCostLedgerVO> totallist = getCostDatas2(true);// 总包合同信息
//		List<MinAndLongProjectCostLedgerVO> list = getCostDatas(false);// 分包合同信息
		List<MinAndLongProjectCostLedgerVO> list = getCostDatas2(false);// 分包合同信息
		//add by 黄冠华 SDYC-53 成本台账（成本报表） 20200813 end
		List<MinAndLongProjectCostLedgerVO> integrationList = new ArrayList<MinAndLongProjectCostLedgerVO>();
		Map<String, List<MinAndLongProjectCostLedgerVO>> detailedMap = new HashMap<String, List<MinAndLongProjectCostLedgerVO>>();// 分包对照信息缓存
		Map<String, List<MinAndLongProjectCostLedgerVO>> detailedMap2 = new HashMap<String, List<MinAndLongProjectCostLedgerVO>>();// 分包对照信息缓存(包含业态)

		// 2.读取累计实付款-NC
		// 会计台帐期初实付款（含税）（NC期初实付款（含税）def98）【字段是新增】+112301借方发生（根据付款单凭证找回单据，匹配合同）A
		Map<String, UFDouble> actualMap = null;
		// 3.读取保理付款金额
		// 根据合同编码获取对应的付款单ap_paybill.pk_balatype（付款合同的合同编码、关联业务单据，单据关联凭证、凭证明细获取：组织本币（借方））
		Map<String, UFDouble> factorPayMap = null;// 取付款方式为ABS付款的
		// 4.读取累计产值
		// 取合同产值表（受查询时间条件限制，取最近值）
		Map<String, Map<String, UFDouble>> outputMap = null;
		// 5.读取业态信息
		Map<String, List<Map<String, Object>>> formatMap = null;
		// 6.读取nc入账发票金额（含税）
		// 初始化进去的发票数（NC期初数据）+112301贷方发生额（根据应付单凭证找回单据，匹配合同）
		Map<String, Map<String, UFDouble>> accInvMap = null;
		// 7.读取已入成本（不含税）
		// 期初发票金额（不含税）+5001借方发生额（根据应付单凭证找回单据，匹配合同）
		Map<String, Map<String, UFDouble>> costMap = null;
		// 8.读取应付单相关的价税金额信息【累计实付款（含税）相关信息存在错乱引起】
		Map<String, Map<String, UFDouble>> invMnyMap = null;
		
		//add by 黄冠华 添加融资费用和开发间接费的余额数据 20200813 begin
		Map<String, UFDouble> finaMnyMap = null;
		Map<String, UFDouble> devMnyMap = null;
		//add by 黄冠华 添加融资费用和开发间接费的余额数据 20200813 end

		// 缓存需要过滤的内容【合同主键、合同编码】
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
				//add by 黄冠华 添加开发间接费等后合同主键为空，添加是否为空判断 20200915 begin
//				keySet.add(vo.getPk_fct_ap());
				String pk_fct_ap=vo.getPk_fct_ap();
				if(pk_fct_ap!=null){
					contSet.add(vo.getContcode());
					keySet.add(pk_fct_ap);
				}
				//add by 黄冠华 添加开发间接费等后合同主键为空，添加是否为空判断 20200915 end
			}

		}

		if (totallist != null && totallist.size() > 0) {
			for (MinAndLongProjectCostLedgerVO vo : totallist) {
				//add by 黄冠华 添加开发间接费等后合同主键为空，添加是否为空判断 20200915 begin
//				keySet.add(vo.getPk_fct_ap());
//				contSet.add(vo.getContcode());
				String pk_fct_ap=vo.getPk_fct_ap();
				if(pk_fct_ap!=null){
					keySet.add(pk_fct_ap);
					contSet.add(vo.getContcode());
				}
				//add by 黄冠华 添加开发间接费等后合同主键为空，添加是否为空判断 20200915 end
				
			}
		}
		if (keySet != null && keySet.size() > 0){
			// 2.读取累计实付款-NC
			// 会计台帐期初实付款（含税）（NC期初实付款（含税）def98）【字段是新增】+112301借方发生（根据付款单凭证找回单据，匹配合同）A
			actualMap = getPayVoucherMap(contSet, null, "112301");
			// 3.读取保理付款金额
			// 根据合同编码获取对应的付款单ap_paybill.pk_balatype（付款合同的合同编码、关联业务单据，单据关联凭证、凭证明细获取：组织本币（借方））
			factorPayMap = getPayVoucherMap(contSet,
					balatypeMap.get("9").get("pk_balatype"), "112301");// 取付款方式为ABS付款的
			// 4.读取累计产值
			// 取合同产值表（受查询时间条件限制，取最近值）
			outputMap = getOutputMap(keySet);
			// 5.读取业态信息
			//add by 黄冠华 SDYC-388 需求调整：成本台账 添加是否按业态汇总条件 20200817 begin
			if("36HA0201".equals(FUNCODE)){
				formatMap = getFormatMap(keySet);
			}else {
				formatMap=new HashMap<String, List<Map<String,Object>>>();
			}
			//add by 黄冠华 SDYC-388 需求调整：成本台账 添加是否按业态汇总条件 20200817 end
			// 6.读取nc入账发票金额（含税）
			// 初始化进去的发票数（NC期初数据）+112301贷方发生额（根据应付单凭证找回单据，匹配合同）
			accInvMap = getPayableVoucherMap(contSet, "112301");
			// 7.读取已入成本（不含税）
			// 期初发票金额（不含税）+5001借方发生额（根据应付单凭证找回单据，匹配合同）
			costMap = getPayableVoucherMap(contSet, "5001");
			// 8.读取应付单相关的价税金额信息【累计实付款（含税）相关信息存在错乱引起】
			invMnyMap = getPayableMnyMap(keySet);
			
			//add by 黄冠华 添加融资费用和开发间接费的余额数据 20200813 begin
//			finaMnyMap=getBalByCode(FINACODE);
//			devMnyMap = getBalByCode(DEVACODE);
			//add by 黄冠华 添加融资费用和开发间接费的余额数据 20200813 end
		}
		if (totallist != null && totallist.size() > 0) {
			int firstrow = 1;
			for (MinAndLongProjectCostLedgerVO vo : totallist) {
				vo.setTatalcontcode("~");
				vo.setTatalcontname("~");
				vo.setRate1(vo.getRate1());// 税率
				UFDouble rate = UFDoubleUtils.add(UFDouble.ONE_DBL,
						UFDoubleUtils.div(vo.getRate1(), new UFDouble(100)));
				vo.setVclass(contTypeMap.get(vo.getConttype()));
				vo.setInnercode(String.format("%04d", firstrow));
				vo.setFormatname("汇总");
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
				UFDouble signTotalMny = UFDouble.ZERO_DBL;// 签约金额总计
				Map<String, UFDouble> formatSignMnyMap = new HashMap<String, UFDouble>();
				if(isGenCons)vo.setRate2(UFDouble.ZERO_DBL);//add by 黄冠华 总分包台账业态比例先清零 后面做汇总分包合同 20200917

				if (detailedMap.containsKey(vo.getContcode())) {// 总包合同存在分包信息
					for (String attName : attNames) {
						if (attName.contains("amount")) {
							if (!"amount1".equals(attName)) {
								vo.setAttributeValue(attName, UFDouble.ZERO_DBL);
							}
						}

					}

					List<MinAndLongProjectCostLedgerVO> detailedlists = detailedMap
							.get(vo.getContcode());
					int secondrow = 200;// 分包信息由200开始,以便于总包合同业态信息排序
					for (MinAndLongProjectCostLedgerVO detvo : detailedlists) {
						detvo.setVclass(contTypeMap.get(vo.getConttype()));
//						detvo = getHandleContMainData(detvo, actualMap,
//								factorPayMap, outputMap, accInvMap, costMap,
//								invMnyMap, secondrow, vo.getInnercode());
						detvo = getHandleContMainData2(detvo, actualMap,
						factorPayMap, outputMap, accInvMap, costMap,
						invMnyMap, secondrow, vo.getInnercode());
						// 除开签约金额字段,其他涉及到金额信息由明细汇总到表头
						for (String attName : attNames) {
							//if (attName.contains("amount")||attName.contains("rate2")) {
							//add by 黄冠华 总包合同的业态比例也需要汇总分包合同 20200917 begin
							if (attName.contains("amount")||attName.contains("rate2")) {

								if ("amount1".equals(attName)) {
									signTotalMny = UFDoubleUtils.add(
											signTotalMny, detvo.getAmount1());// 获取签约金额
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
							//add by 黄冠华 总包合同的业态比例也需要汇总分包合同 20200917 end

						}

						secondrow++;
						if (istotal) {
							integrationList.add(detvo);
						}
//						if (formatMap.containsKey(detvo.getPk_fct_ap())) {
						if (formatMap!=null&&formatMap.containsKey(detvo.getPk_fct_ap())) {//add by 黄冠华 添加开发间接费等后合同主键为空，添加是否为空判断 20200915
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
												.getAmount1());// 获得业态比率签约金额

								formatSignMnyMap.put(foramtvo.getFormatname(),
										mny);
							}

							if (istotal) {
								integrationList.addAll(foramtList);
							}
						}

					}

					// 存在分包信息汇总,与不含税金额与税额重计
					// 动态金额
					vo.setAmount6(UFDoubleUtils.div(vo.getAmount6(), rate));
					//add by 黄冠华 动态金额（税额）=动态金额（含税）-动态金额（不含税）20200917 begin
//					vo.setAmount7(UFDoubleUtils.sub(vo.getAmount6(),
//							vo.getAmount7()));
					vo.setAmount7(UFDoubleUtils.sub(vo.getAmount5(),
					vo.getAmount6()));
					//add by 黄冠华 动态金额（税额）=动态金额（含税）-动态金额（不含税）20200917 end

					// 累计实付款
					vo.setAmount12(UFDoubleUtils.div(vo.getAmount11(), rate));
					vo.setAmount14(UFDoubleUtils.sub(vo.getAmount11(),
							vo.getAmount12()));

					// 合同未付款
					vo.setAmount16(UFDoubleUtils.div(vo.getAmount15(), rate));
					vo.setAmount17(UFDoubleUtils.sub(vo.getAmount15(),
							vo.getAmount16()));
					// 累计实付款-NC
					vo.setAmount20(UFDoubleUtils.div(vo.getAmount19(), rate));
					vo.setAmount21(UFDoubleUtils.sub(vo.getAmount19(),
							vo.getAmount20()));

					// 累计实付款-NC
					vo.setAmount20(UFDoubleUtils.div(vo.getAmount19(), rate));
					vo.setAmount21(UFDoubleUtils.sub(vo.getAmount19(),
							vo.getAmount20()));

					// 未入成本

					vo.setAmount27(UFDoubleUtils.div(vo.getAmount26(), rate));
					vo.setAmount28(UFDoubleUtils.sub(vo.getAmount26(),
							vo.getAmount27()));

					// 累计产值

					vo.setAmount29(UFDoubleUtils.div(vo.getAmount29(), rate));
//					vo.setAmount30(UFDoubleUtils.sub(vo.getAmount29(),
//							vo.getAmount30()));
					//add by 黄冠华 累计产值（不含税）=累计产值（含税）-累计产值（税额） 20200917 begin
					vo.setAmount30(UFDoubleUtils.sub(vo.getAmount29(),
					vo.getAmount31()));
					//add by 黄冠华 累计产值（不含税）=累计产值（含税）-累计产值（税额） 20200917 end

					// 累计 产值未付款
					vo.setAmount34(UFDoubleUtils.div(vo.getAmount33(), rate));
					vo.setAmount35(UFDoubleUtils.sub(vo.getAmount33(),
							vo.getAmount34()));

				}

				// 处理总分包业态比率信息
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
					if (formatMap!=null&&formatMap.containsKey(vo.getPk_fct_ap())) {//add by 黄冠华 添加开发间接费等后合同主键为空，添加是否为空判断
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
	 * 获得合同对应的业态比例信息
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
			String format = (String) formatInfoMap.get("vbdef22");// 业态
			String formatname = (String) formatInfoMap.get("name");// 业态

			UFDouble formatrate = (String) formatInfoMap.get("vbdef13") != null
					&& !"~".equals(formatInfoMap.get("vbdef13")) ? new UFDouble(
					formatInfoMap.get("vbdef13").toString())
					: UFDouble.ZERO_DBL;// 业态比例

			MinAndLongProjectCostLedgerVO coloneVO = (MinAndLongProjectCostLedgerVO) vo
					.clone();
			coloneVO.setInnercode(vo.getInnercode()
					+ String.format("%04d", secondrow));
			coloneVO.setFormatname(formatname + "——" + format);
			UFDouble rate = UFDoubleUtils.div(formatrate, new UFDouble(100));// 业态比例
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
	 * 处理合同主信息
	 * @author 黄冠华
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
		String contcode = vo.getContcode();// 合同编码
		String key = vo.getPk_fct_ap();// 合同主键
		// vo.setInnercode(RandomSeqUtil.getRandomSeq(4));
		UFDouble rate = UFDoubleUtils.div(vo.getRate1(), new UFDouble(100));
		vo.setRate1(rate);
//		if (invMnyMap.containsKey(key)) {
		if (invMnyMap!=null&&invMnyMap.containsKey(key)) {//add by 黄冠华 添加开发间接费等后合同主键为空，添加是否为空判断 20200915
			vo.setAmount11(invMnyMap.get(key).get("local_money")); // 累计实付款
			vo.setAmount12(UFDoubleUtils.div(vo.getAmount11(),
					UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// 累计实付(不含税)
																// 累计实付款/(1+税率)
			vo.setAmount14(UFDoubleUtils.sub(vo.getAmount11(), vo.getAmount12()));// 累计实付款（税额
																					// 累计实付款-累计实付款（不含税）
		}
		//add by 黄冠华 添加开发间接费等后合同主键为空，添加是否为空判断 20200915 begin
		UFDouble tempAmount=UFDouble.ZERO_DBL;
		if(actualMap!=null&&actualMap.get(contcode) != null){
			tempAmount=actualMap.get(contcode);
		}
//		vo.setAmount19(UFDoubleUtils.add(
//				vo.getAmount19(),
//				actualMap.get(contcode) == null ? UFDouble.ZERO_DBL : actualMap
//						.get(contcode))); // 累计实付款-NC
		vo.setAmount19(UFDoubleUtils.add(
		vo.getAmount19(),tempAmount)); // 累计实付款-NC
		//add by 黄冠华 添加开发间接费等后合同主键为空，添加是否为空判断 20200915 end
		vo.setAmount20(UFDoubleUtils.div(vo.getAmount19(),
				UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// 累计实付款-NC（不含税）
															// B=A/(1+R)
															// 累计实付款-NC/(1+税率)
		vo.setAmount21(UFDoubleUtils.sub(vo.getAmount19(), vo.getAmount20()));// 累计实付款-NC（税额）
																				// C=A-B
																				// 累计实付款-NC-累计实付款-NC（不含税）

		vo.setAmount10(UFDoubleUtils.sub(vo.getAmount9(), vo.getAmount19()));// 累计应付未付款
																				// O-A
																				// 累计应付款-累计实付款-NC

		vo.setAmount15(UFDoubleUtils.sub(vo.getAmount5(), vo.getAmount19()));// 合同未付款（含税）M=L-A
																				// 动态金额(税额)-累计实付款-NC
		vo.setAmount16(UFDoubleUtils.div(vo.getAmount15(),
				UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// 合同未付款（不含税）
															// M/(1+R)
															// 合同未付款（含税）/(1+税率)
		vo.setAmount17(UFDoubleUtils.sub(vo.getAmount15(), vo.getAmount16()));// 合同未付款（含税）
																				// M-N
																				// 合同未付款（含税）-合同未付款（不含税）

//		vo.setAmount22(factorPayMap.get(contcode));// 保理付款金额
		vo.setAmount22(factorPayMap==null?UFDouble.ZERO_DBL:factorPayMap.get(contcode));// 保理付款金额 //add by 黄冠华 添加开发间接费等后合同主键为空，添加是否为空判断 20200915 

		// nc入账发票金额（含税）
//		if (accInvMap.containsKey(contcode)) {
		if (accInvMap!=null&&accInvMap.containsKey(contcode)) { //add by 黄冠华 添加开发间接费等后合同主键为空，添加是否为空判断 20200915 
			vo.setAmount23(UFDoubleUtils.add(vo.getAmount23(),
					accInvMap.get(contcode).get("local_cr")));
		}
		// 已入成本（不含税）
		if (costMap!=null&&costMap.containsKey(contcode)) {//add by 黄冠华 添加开发间接费等后合同主键为空，添加是否为空判断 20200915 
			vo.setAmount24(UFDoubleUtils.add(vo.getAmount24(),
					costMap.get(contcode).get("local_de")));
		}

		vo.setAmount25(UFDoubleUtils.sub(vo.getAmount23(), vo.getAmount24()));// 可抵扣进项税（税额）D-E
																				// nc入账发票金额（含税）-已入成本（不含税）

		vo.setAmount26(UFDoubleUtils.sub(vo.getAmount19(), vo.getAmount23()));// 未入成本（含税）A-D
																				// 累计实付款-NC-nc入账发票金额（含税）
		vo.setAmount27(UFDoubleUtils.sub(vo.getAmount20(), vo.getAmount24()));// B-E
																				// 累计实付款-NC（不含税）
																				// -已入成本（不含税）
		vo.setAmount28(UFDoubleUtils.sub(vo.getAmount21(), vo.getAmount25()));// 未入成本（税额）F-G
																				// 未入成本（含税）
																				// -
																				// 未入成本（不含税）

		if (outputMap!=null&&outputMap.containsKey(key)) {//add by 黄冠华 添加开发间接费等后合同主键为空，添加是否为空判断 20200915
			vo.setAmount29(outputMap.get(key).get("local_money"));// 累计产值（含税）
			vo.setAmount30(outputMap.get(key).get("local_notax"));// 累计产值（不含税）
			vo.setAmount31(outputMap.get(key).get("local_tax"));// 累计产值（税额）
		}

		// 最终产值 A与H较大值 累计实付款-NC or 累计产值（含税）
		if (UFDoubleUtils.isGreaterThan(vo.getAmount19(), vo.getAmount29())) {
			vo.setAmount32(vo.getAmount19());
		} else {
			vo.setAmount32(vo.getAmount29());
		}

		vo.setAmount33(UFDoubleUtils.sub(vo.getAmount32(), vo.getAmount19()));// 累计
																				// 产值未付款
																				// （含税）I-A
																				// 最终产值
																				// -
																				// 累计实付款-NC
		vo.setAmount34(UFDoubleUtils.div(vo.getAmount33(),
				UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// 累计产值未付款（不含税）J/(1+R)
															// 累计产值未付款（含税/(1+税率)
		vo.setAmount35(UFDoubleUtils.sub(vo.getAmount33(), vo.getAmount34()));// 累计产值未付款
																				// （税额）
																				// J-K
																				// 累计
																				// 产值未付款
																				// （含税）-累计产值未付款
																				// （不含税）

		vo.setInnercode((innercode != null && !"".equals(innercode) ? innercode
				: "") + String.format("%04d", row));
		vo.setFormatname("汇总");
		vo.setRate2(new UFDouble(1));
		
		//add by 黄冠华 成本台账 添加融资费用和开发间接费的余额数据 20200813 begin
		if(!isGenCons){
			String contCode=vo.getContcode();
			if("开发间接费".equals(contCode)||"融资费用".equals(contCode)){
				vo.setAmount10(new UFDouble(0));
				vo.setAmount11(vo.getAmount5());
				vo.setAmount12(vo.getAmount5());
				vo.setPk_format("");
				vo.setRate2(new UFDouble(0));
				vo.setRate1(new UFDouble(0));
			}
		}
		
		//add by 黄冠华 成本台账 添加融资费用和开发间接费的余额数据 20200813 end
		
		return vo;
	}

	/**
	 * 处理合同主信息
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
		String contcode = vo.getContcode();// 合同编码
		String key = vo.getPk_fct_ap();// 合同主键
		// vo.setInnercode(RandomSeqUtil.getRandomSeq(4));
		UFDouble rate = UFDoubleUtils.div(vo.getRate1(), new UFDouble(100));
		vo.setRate1(rate);
		if (invMnyMap.containsKey(key)) {
			vo.setAmount11(invMnyMap.get(key).get("local_money")); // 累计实付款
			vo.setAmount12(UFDoubleUtils.div(vo.getAmount11(),
					UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// 累计实付(不含税)
																// 累计实付款/(1+税率)
			vo.setAmount14(UFDoubleUtils.sub(vo.getAmount11(), vo.getAmount12()));// 累计实付款（税额
																					// 累计实付款-累计实付款（不含税）
		}

		vo.setAmount19(UFDoubleUtils.add(
				vo.getAmount19(),
				actualMap.get(contcode) == null ? UFDouble.ZERO_DBL : actualMap
						.get(contcode))); // 累计实付款-NC
		vo.setAmount20(UFDoubleUtils.div(vo.getAmount19(),
				UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// 累计实付款-NC（不含税）
															// B=A/(1+R)
															// 累计实付款-NC/(1+税率)
		vo.setAmount21(UFDoubleUtils.sub(vo.getAmount19(), vo.getAmount20()));// 累计实付款-NC（税额）
																				// C=A-B
																				// 累计实付款-NC-累计实付款-NC（不含税）

		vo.setAmount10(UFDoubleUtils.sub(vo.getAmount9(), vo.getAmount19()));// 累计应付未付款
																				// O-A
																				// 累计应付款-累计实付款-NC

		vo.setAmount15(UFDoubleUtils.sub(vo.getAmount5(), vo.getAmount19()));// 合同未付款（含税）M=L-A
																				// 动态金额(税额)-累计实付款-NC
		vo.setAmount16(UFDoubleUtils.div(vo.getAmount15(),
				UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// 合同未付款（含税）
															// M/(1+R)
															// 合同未付款（含税）/(1+税率)
		vo.setAmount17(UFDoubleUtils.sub(vo.getAmount15(), vo.getAmount16()));// 合同未付款（含税）
																				// M-N
																				// 合同未付款（含税）-合同未付款（不含税）

		vo.setAmount22(factorPayMap.get(contcode));// 保理付款金额

		// nc入账发票金额（含税）
		if (accInvMap.containsKey(contcode)) {
			vo.setAmount23(UFDoubleUtils.add(vo.getAmount23(),
					accInvMap.get(contcode).get("local_cr")));
		}
		// 已入成本（不含税）
		if (costMap.containsKey(contcode)) {
			vo.setAmount24(UFDoubleUtils.add(vo.getAmount24(),
					costMap.get(contcode).get("local_de")));
		}

		vo.setAmount25(UFDoubleUtils.sub(vo.getAmount23(), vo.getAmount24()));// 可抵扣进项税（税额）D-E
																				// nc入账发票金额（含税）-已入成本（不含税）

		vo.setAmount26(UFDoubleUtils.sub(vo.getAmount19(), vo.getAmount23()));// 未入成本（含税）A-D
																				// 累计实付款-NC-nc入账发票金额（含税）
		vo.setAmount27(UFDoubleUtils.sub(vo.getAmount20(), vo.getAmount24()));// B-E
																				// 累计实付款-NC（不含税）
																				// -已入成本（不含税）
		vo.setAmount28(UFDoubleUtils.sub(vo.getAmount21(), vo.getAmount25()));// 未入成本（税额）F-G
																				// 未入成本（含税）
																				// -
																				// 未入成本（不含税）

		if (outputMap.containsKey(key)) {
			vo.setAmount29(outputMap.get(key).get("local_money"));// 累计产值（含税）
			vo.setAmount30(outputMap.get(key).get("local_notax"));// 累计产值（不含税）
			vo.setAmount31(outputMap.get(key).get("local_tax"));// 累计产值（税额）
		}

		// 最终产值 A与H较大值 累计实付款-NC or 累计产值（含税）
		if (UFDoubleUtils.isGreaterThan(vo.getAmount19(), vo.getAmount29())) {
			vo.setAmount32(vo.getAmount19());
		} else {
			vo.setAmount32(vo.getAmount29());
		}

		vo.setAmount33(UFDoubleUtils.sub(vo.getAmount32(), vo.getAmount19()));// 累计
																				// 产值未付款
																				// （含税）I-A
																				// 最终产值
																				// -
																				// 累计实付款-NC
		vo.setAmount34(UFDoubleUtils.div(vo.getAmount33(),
				UFDoubleUtils.add(UFDouble.ONE_DBL, rate)));// 累计产值未付款（不含税）J/(1+R)
															// 累计产值未付款（含税/(1+税率)
		vo.setAmount35(UFDoubleUtils.sub(vo.getAmount33(), vo.getAmount34()));// 累计产值未付款
																				// （税额）
																				// J-K
																				// 累计
																				// 产值未付款
																				// （含税）-累计产值未付款
																				// （不含税）

		vo.setInnercode((innercode != null && !"".equals(innercode) ? innercode
				: "") + String.format("%04d", row));
		vo.setFormatname("汇总");
		vo.setRate2(new UFDouble(1));
		return vo;
	}

	/**
	 * 读取合同对应应付发票信息
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
	 * 读取合同关联应付单相关的凭证金额信息
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
	 * 读取业态信息
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
	 * 累计产值
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
	 * 读取合同关联付款单相关的凭证金额信息
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
			sql.append(" and item.pk_balatype ='" + pk_balatype + "'");//add by黄冠华 结算方式在表体填写需要用表体做查询条件 20200904
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
	 * @author 黄冠华
	 * @param acccode
	 * 获取科目余额
	 */
	private  Map<String, UFDouble> getBalByCode(String acccode) throws BusinessException {
		
		// 财务组织
		String pk_org = queryValueMap.get("pk_org");
		//截至日期
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
	    if (pk_org != null && !"".equals(pk_org)) {// 财务组织
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
	    if (pk_org != null && !"".equals(pk_org)) {// 财务组织
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
	 * @author 黄冠华
	 * @param acccode
	 * 获取科目余额sql
	 */
	private  String getBalByCodeSql(String acccode) throws BusinessException {
		
		// 财务组织
		String pk_org = queryValueMap.get("pk_org");
		//截至日期
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
	    	contCode="开发间接费";
	    }else if(DEVACODE.equals(acccode)){
	    	contCode="融资费用";
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
	    if (pk_org != null && !"".equals(pk_org)) {// 财务组织
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
	    if (pk_org != null && !"".equals(pk_org)) {// 财务组织
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
	 * 读取合同记录
	 * @author 黄冠华
	 * @param istotal
	 * @return
	 * @throws BusinessException
	 */
	private List<MinAndLongProjectCostLedgerVO> getCostDatas2(boolean istotal)
			throws BusinessException {
		// 项目名称
		  String cfinancialo = queryValueMap.get("pk_project");
		    // 财务组织
		    String pk_org = queryValueMap.get("pk_org");
		    
		 // 客商
		    String pk_cust = queryValueMap.get("pk_cust");

		    StringBuffer sql = new StringBuffer();
		    //add by 黄冠华 SDYC-388 需求调整：成本台账 20200812 begin
		    StringBuffer sb = new StringBuffer();
//		    sql.append(
		    sb.append(
		        "select fct_ap.pk_fct_ap                                  pk_fct_ap ")
		        // 合同主键
		        .append("   ,fct_ap.proname                               pk_project")
		        .append("   ,b.project_name                               projectname")

		        // 项目名称
		        .append("   , ''                                                  vclass")
		        // 大类
		        .append("   ,fct_ap.vbillcode                                     contcode")
		        // 合同编码
		        .append("   ,fct_ap.ctname                                       contname")
		        // 合同名称
		        .append("   ,nvl(decode(fct_ap.def54,'~','',fct_ap.def54),'')    tatalcontcode")
		        // 总包合同编码
		        .append("   ,fct_ap.def55                                        tatalcontname")
		        // 总包合同名称
		        .append("   ,fct_ap.contype                                      conttype")
		        // 合同类别
		        .append("   ,nvl(decode(fct_ap.vdef3,'~','N',fct_ap.vdef3),'N')   contattribute")
		        // 合同属性
		        .append("   ,fct_ap.first                                         pk_first ")
		        .append("   ,t.name                                               firstname ")

		        // 甲方单位
		        .append("   ,fct_ap.second                                        pk_second")
		        .append("   ,w.name                                               secondname ")
		        // 乙方单位
		        .append("   ,fct_ap.d_sign                                         signdate")
		        // 签约日期
		        .append("   ,fct_ap.vdef15                                          settstate")
		        // 结算状态
		        .append("   ,fct_ap.msign                                          amount1")
		        // 签约金额
		        .append("   ,nvl(decode(fct_ap.vdef6,'~',0,fct_ap.vdef6),0)     amount2")
		        // 累计变更金额
		        .append("   ,nvl(decode(fct_ap.vdef7,'~',0,fct_ap.vdef7),0)     amount3")
		        // 累计补充协议金额
		        .append("   ,nvl(decode(fct_ap.vdef8,'~',0,fct_ap.vdef8),0)     amount4   ")
		        // 结算调整金额
		        .append("   ,nvl(decode(fct_ap.vdef9,'~',0,fct_ap.vdef9),0)   amount5 ")
		        // 动态金额（含税）
		        .append("   ,fct_ap.rate                                           rate1")
		        // 税率
		        .append("   ,nvl(decode(fct_ap.vdef10,'~',0,fct_ap.vdef10),0)   amount6       ")
		        // 动态金额（不含税）
		        .append("   ,nvl(decode(fct_ap.vdef11,'~',0,fct_ap.vdef11),0)   amount7 ")
		        // 动态金额（税额）
		        .append("   ,nvl(decode(fct_ap.vdef16,'~',0,fct_ap.vdef16),0)   amount8")
		        // 结算金额
		        .append("   ,nvl(decode(fct_ap.def31,'~',0,fct_ap.def31),0)     amount9")
		        // 累计应付款
		        .append("   ,0                                                      amount10")
		        // 累计应付未付款
		        /*
		         * 累计信息有误重取明细计算 .append(
		         * "   ,nvl(decode(fct_ap.vdef21,'~',0,fct_ap.vdef21),0)     amount11"
		         * ) // 累计实付款（含税） .append(
		         * "   ,nvl(decode(fct_ap.def42,'~',0,fct_ap.def42),0)     amount12"
		         * ) // 累计实付款（不含税） // .append(
		         * "   ,nvl(decode(fct_ap.def43,'~',0,fct_ap.def43),0)     amount13"
		         * ) // // 累计实付款（不含税） .append(
		         * "   ,nvl(decode(fct_ap.def43,'~',0,fct_ap.def43),0)     amount14"
		         * )
		         */
		        // 累计实付款（税额）
		        .append("   ,0                                                      amount15")
		        // ?合同未付款（含税）
		        .append("   ,0                                                      amount16")
		        // ?合同未付款（不含税）
		        .append("   ,0                                                      amount17")
		        // 合同未付款 （税额）
		        .append("   ,nvl(decode(fct_ap.vdef18,'~',0,fct_ap.vdef18),0)   amount18")
		        // EBS发票金额累计（含税）
		        //.append("   ,nvl(decode(fct_ap.def98,'~',0,fct_ap.def98),0)   amount19")
		        .append("   ,nvl(decode(fct_ap.vdef21,'~',0,fct_ap.vdef21),0)   amount19")//add by 黄冠华 累计实付款-NC应该是fct_ap.vdef21 20200904
		        // 累计实付款-NC
		        .append("   ,0                                                      amount20")
		        // 累计实付款-NC（不含税）
		        .append("   ,0                                                      amount21")
		        // 累计实付款-NC（税额）
		        .append("   ,0                                                      amount22")
		        // 保理付款金额
		        .append("   , nvl(decode(fct_ap.def61,'~',0,decode(billtype.pk_billtypecode,'FCT1-Cxx-003',0,fct_ap.def61)),0)    amount23")
		        // nc入账发票金额（含税）
		        .append("   ,nvl(decode(fct_ap.def62,'~',0,fct_ap.def62),0)     amount24")
		        // 已入成本（不含税）
		        .append("   ,0                                                      amount25")
		        // 可抵扣进项税（税额）
		        .append("   ,0                                                      amount26")
		        // 未入成本（含税）
		        .append("   ,0                                                      amount27")
		        // 未入成本（不含税）
		        .append("   ,0                                                      amount28")
		        // 未入成本（税额）
		        .append("   ,0                                                      amount29")
		        // 累计产值（含税）
		        .append("   ,0                                                      amount30")
		        // 累计产值（不含税）
		        .append("   ,0                                                      amount31")
		        // 累计产值（税额）
		        .append("   ,0                                                      amount32")
		        // 最终产值
		        .append("   ,0                                                      amount33")
		        // 累计 产值未付款 （含税）
		        .append("   ,0                                                      amount34")
		        // 累计产值未付款 （不含税）
		        .append("   ,0                                                      amount35")
		        // 累计产值未付款 （税额）
		        .append("   ,fct_ap.def50                                           pk_dept")
		        // 部门
		        .append("   ,fct_ap.vdef12                                          pk_psndoc")
		        // 经办人
		        .append("   ,fct_ap.vdef4                                           modename")
		        // 承包方式
		        .append("   ,''                                                     pk_format")
		        // 业态
		        .append("   ,0                                                      rate2")
		        // 业态比例
		        .append("   ,case when billtype.pk_billtypecode ='FCT1-Cxx-001' and nvl(fct_ap.contype,'~')<>'营销类' and nvl(fct_ap.def103,'~')<>'Y' then 'Y' else (")
		        .append(" case when billtype.pk_billtypecode ='FCT1-Cxx-001' and nvl(fct_ap.contype,'~')='营销类' and nvl(fct_ap.def104,'~')='Y' then 'Y' else (")
		        .append(" case when billtype.pk_billtypecode ='FCT1-Cxx-003' and nvl(fct_ap.def105,'~')='Y' then 'Y' else 'N' end)end)end isneed")//判断是否需要过滤(1非营销类取def103为否的数据，2营销类取def104为是的数据3通用合同def105勾选才取数)
		        .append("  from fct_ap ")
		        .append(" inner join bd_billtype billtype  on billtype.pk_billtypeid = fct_ap.ctrantypeid ")
		        .append(" left join bd_project b on b.pk_project = fct_ap.proname and b.dr = 0  ")
		        .append(" left join org_orgs t on t.pk_org = fct_ap.first and t.dr = 0  ")
		        .append(" left join bd_cust_supplier w on w.pk_cust_sup = fct_ap.second and t.dr = 0  ")
		        .append(" where   nvl(fct_ap.dr,0) = 0 ")
		        .append(" and fct_ap.blatest = 'Y' ")// 是否最新
		        .append("and fct_ap.def52 = '审批通过' ").append("\r\n")// --合同状态
//		        .append("and fct_ap.vdef5 = 'Y' ").append("\r\n");// --财务成本标记
		        .append("and billtype.pk_billtypecode in ('FCT1-Cxx-003','FCT1-Cxx-001') ").append("\r\n");//EBS成本付款合同、通用合同
		    if (istotal) {
		      sb.append(
		          "and (fct_ap.def54 is null or fct_ap.def54 = '~' or fct_ap.def54 = fct_ap.vbillcode) ")
//		                    "and ( fct_ap.def54 = fct_ap.vbillcode) ")

		          .append("\r\n");// --总包合同编码
		      // sql.append("and (fct_ap.def55 is null or fct_ap.def55 = '~') ")
		      // .append("\r\n");// --总包合同名称
		      // sql.append("and fct_ap.vbillcode = '100201003.01-材料采购类-2019-0001'")
		      // .append("\r\n");

		    } else {
		      sb.append(
		          "and (   fct_ap.def54 <> fct_ap.vbillcode) ")
//		          "and (fct_ap.def54 is not null and fct_ap.def54 <> '~' and fct_ap.def54 <> fct_ap.vbillcode) ")
		          .append("\r\n");// --总包合同编码
		      // sql.append(
		      // "and (fct_ap.def55 is not null and  fct_ap.def55 <> '~') ")
		      // .append("\r\n");// --总包合同名称
		    }
		    // if (ccode != null) {// 合同编码
		    // sql.append("and fct_ap.vbillcode = '" + ccode + "'").append("\r\n");
		    // }
		    //add by 黄冠华 总分包合同不显示 总分包合同编码为空的数据 20200917 begin
		    if(isGenCons){
		    	sb.append(" and (nvl(fct_ap.def54,'~')<> '~' ) ");
		    }
		  //add by 黄冠华 总分包合同不显示 总分包合同编码为空的数据 20200917 end
		    if (pk_org != null && !"".equals(pk_org)) {// 财务组织
		      String[] orgstr = pk_org.split(",");
		      sb.append("and " + SQLUtil.buildSqlForIn("fct_ap.pk_org", orgstr))
		          .append("\r\n");
		    }
		    if (cfinancialo != null && cfinancialo.length() > 0) {// 项目名称
		      if (cfinancialo.indexOf(",") >= 0) {
		        sb.append(" and "
		            + SQLUtil.buildSqlForIn("fct_ap.proname",
		                cfinancialo.split(",")));
		      } else {
		        sb.append(" and fct_ap.proname = '" + cfinancialo + "' ");
		      }
		    }
		    
		    if (pk_cust != null && !"".equals(pk_cust)) {// 客商
			      String[] pk_custs = pk_cust.split(",");
			      sb.append("and " + SQLUtil.buildSqlForIn("fct_ap.second", pk_custs))
			          .append("\r\n");
			    }
		    // if (cname != null) {// 合同名称
		    // sql.append("and fct_ap.ctname like '%" + cname + "%' ").append(
		    // "\r\n");
		    // }
//		    sql.append(" order by b.project_name,fct_ap.vbillcode ");
		    sql=sql.append(" select * from ( ");
		    sql=sql.append(sb);
		    sql.append(" ) ");
		    sql.append(" where nvl(isneed,'N')='Y' ");
		  //add by 黄冠华 总分包合同不显示 开发间接费 20200917 begin
		    if(!isGenCons){
			    sql.append(" union ");
			    sql.append(getBalByCodeSql(FINACODE));
			    sql.append(" union ");
			    sql.append(getBalByCodeSql(DEVACODE));
		    }
		  //add by 黄冠华 总分包合同不显示 开发间接费 20200917 end
		    sql.append(" order by  projectname,contcode ");
		    
		    List<MinAndLongProjectCostLedgerVO> list = (List<MinAndLongProjectCostLedgerVO>) getBaseDAO()
		        .executeQuery(
		            sql.toString(),
		            new BeanListProcessor(
		                MinAndLongProjectCostLedgerVO.class));
		    return list;
	}
	
	


	/**
	 * 读取合同记录
	 * 
	 * @param istotal
	 * @return
	 * @throws BusinessException
	 */
	private List<MinAndLongProjectCostLedgerVO> getCostDatas(boolean istotal)
			throws BusinessException {
		// 项目名称
		String cfinancialo = queryValueMap.get("pk_project");
		// 财务组织
		String pk_org = queryValueMap.get("pk_org");

		StringBuffer sql = new StringBuffer();
		sql.append(
				"select fct_ap.pk_fct_ap                                  pk_fct_ap ")
				// 合同主键
				.append("   ,fct_ap.proname                               pk_project")
				.append("   ,b.project_name                               projectname")

				// 项目名称
				.append("   , ''                                                  vclass")
				// 大类
				.append("   ,fct_ap.vbillcode                                     contcode")
				// 合同编码
				.append("   ,fct_ap.ctname                                       contname")
				// 合同名称
				.append("   ,fct_ap.def54                                        tatalcontcode")
				// 总包合同编码
				.append("   ,fct_ap.def55                                        tatalcontname")
				// 总包合同名称
				.append("   ,fct_ap.contype                                      conttype")
				// 合同类别
				.append("   ,nvl(decode(fct_ap.vdef3,'~','N',fct_ap.vdef3),'N')   contattribute")
				// 合同属性
				.append("   ,fct_ap.first                                         pk_first ")
				.append("   ,t.name                                               firstname ")

				// 甲方单位
				.append("   ,fct_ap.second                                        pk_second")
				.append("   ,w.name                                               secondname ")
				// 乙方单位
				.append("   ,fct_ap.d_sign                                         signdate")
				// 签约日期
				.append("   ,fct_ap.vdef15                                          settstate")
				// 结算状态
				.append("   ,fct_ap.msign                                          amount1")
				// 签约金额
				.append("   ,nvl(decode(fct_ap.vdef6,'~','0',fct_ap.vdef6),'0')     amount2")
				// 累计变更金额
				.append("   ,nvl(decode(fct_ap.vdef7,'~','0',fct_ap.vdef7),'0')     amount3")
				// 累计补充协议金额
				.append("   ,nvl(decode(fct_ap.vdef8,'~','0',fct_ap.vdef8),'0')     amount4   ")
				// 结算调整金额
				.append("   ,nvl(decode(fct_ap.vdef9,'~','0',fct_ap.vdef9),'0')   amount5 ")
				// 动态金额（含税）
				.append("   ,fct_ap.rate                                           rate1")
				// 税率
				.append("   ,nvl(decode(fct_ap.vdef10,'~','0',fct_ap.vdef10),'0')   amount6       ")
				// 动态金额（不含税）
				.append("   ,nvl(decode(fct_ap.vdef11,'~','0',fct_ap.vdef11),'0')   amount7 ")
				// 动态金额（税额）
				.append("   ,nvl(decode(fct_ap.vdef16,'~','0',fct_ap.vdef16),'0')   amount8")
				// 结算金额
				.append("   ,nvl(decode(fct_ap.def31,'~','0',fct_ap.def31),'0')     amount9")
				// 累计应付款
				.append("   ,0                                                      amount10")
				// 累计应付未付款
				/*
				 * 累计信息有误重取明细计算 .append(
				 * "   ,nvl(decode(fct_ap.vdef21,'~','0',fct_ap.vdef21),'0')     amount11"
				 * ) // 累计实付款（含税） .append(
				 * "   ,nvl(decode(fct_ap.def42,'~','0',fct_ap.def42),'0')     amount12"
				 * ) // 累计实付款（不含税） // .append(
				 * "   ,nvl(decode(fct_ap.def43,'~','0',fct_ap.def43),'0')     amount13"
				 * ) // // 累计实付款（不含税） .append(
				 * "   ,nvl(decode(fct_ap.def43,'~','0',fct_ap.def43),'0')     amount14"
				 * )
				 */
				// 累计实付款（税额）
				.append("   ,0                                                      amount15")
				// ?合同未付款（含税）
				.append("   ,0                                                      amount16")
				// ?合同未付款（不含税）
				.append("   ,0                                                      amount17")
				// 合同未付款 （税额）
				.append("   ,nvl(decode(fct_ap.vdef18,'~','0',fct_ap.vdef18),'0')   amount18")
				// EBS发票金额累计（含税）
				.append("   ,nvl(decode(fct_ap.def98,'~','0',fct_ap.def98),'0')   amount19")
				// 累计实付款-NC
				.append("   ,0                                                      amount20")
				// 累计实付款-NC（不含税）
				.append("   ,0                                                      amount21")
				// 累计实付款-NC（税额）
				.append("   ,0                                                      amount22")
				// 保理付款金额
				.append("   ,nvl(decode(fct_ap.def61,'~','0',fct_ap.def61),'0')    amount23")
				// nc入账发票金额（含税）
				.append("   ,nvl(decode(fct_ap.def62,'~','0',fct_ap.def62),'0')     amount24")
				// 已入成本（不含税）
				.append("   ,0                                                      amount25")
				// 可抵扣进项税（税额）
				.append("   ,0                                                      amount26")
				// 未入成本（含税）
				.append("   ,0                                                      amount27")
				// 未入成本（不含税）
				.append("   ,0                                                      amount28")
				// 未入成本（税额）
				.append("   ,0                                                      amount29")
				// 累计产值（含税）
				.append("   ,0                                                      amount30")
				// 累计产值（不含税）
				.append("   ,0                                                      amount31")
				// 累计产值（税额）
				.append("   ,0                                                      amount32")
				// 最终产值
				.append("   ,0                                                      amount33")
				// 累计 产值未付款 （含税）
				.append("   ,0                                                      amount34")
				// 累计产值未付款 （不含税）
				.append("   ,0                                                      amount35")
				// 累计产值未付款 （税额）
				.append("   ,fct_ap.def50                                           pk_dept")
				// 部门
				.append("   ,fct_ap.vdef12                                          pk_psndoc")
				// 经办人
				.append("   ,fct_ap.vdef4                                           modename")
				// 承包方式
				.append("   ,''                                                     pk_format")
				// 业态
				.append("   ,0                                                      rate2")
				// 业态比例
				.append("  from fct_ap ")
				.append(" inner join bd_billtype billtype  on billtype.pk_billtypeid = fct_ap.ctrantypeid ")
				.append(" left join bd_project b on b.pk_project = fct_ap.proname and b.dr = 0  ")
				.append(" left join org_orgs t on t.pk_org = fct_ap.first and t.dr = 0  ")
				.append(" left join bd_cust_supplier w on w.pk_cust_sup = fct_ap.second and t.dr = 0  ")
				.append(" where billtype.pk_billtypecode = 'FCT1-Cxx-001' and fct_ap.dr = 0 ")
				.append(" and fct_ap.blatest = 'Y' ")// 是否最新
				.append("and fct_ap.def52 = '审批通过' ").append("\r\n")// --合同状态
				.append("and fct_ap.vdef5 = 'Y' ").append("\r\n");// --财务成本标记
		if (istotal) {
			sql.append(
//					"and (fct_ap.def54 is null or fct_ap.def54 = '~' or fct_ap.def54 = fct_ap.vbillcode) ")
										"and ( fct_ap.def54 = fct_ap.vbillcode) ")

					.append("\r\n");// --总包合同编码
			// sql.append("and (fct_ap.def55 is null or fct_ap.def55 = '~') ")
			// .append("\r\n");// --总包合同名称
			// sql.append("and fct_ap.vbillcode = '100201003.01-材料采购类-2019-0001'")
			// .append("\r\n");

		} else {
			sql.append(
					"and (fct_ap.def54 is not null and fct_ap.def54 <> '~' and fct_ap.def54 <> fct_ap.vbillcode) ")
					.append("\r\n");// --总包合同编码
			// sql.append(
			// "and (fct_ap.def55 is not null and  fct_ap.def55 <> '~') ")
			// .append("\r\n");// --总包合同名称
		}
		// if (ccode != null) {// 合同编码
		// sql.append("and fct_ap.vbillcode = '" + ccode + "'").append("\r\n");
		// }
		if (pk_org != null && !"".equals(pk_org)) {// 财务组织
			String[] orgstr = pk_org.split(",");
			sql.append("and " + SQLUtil.buildSqlForIn("fct_ap.pk_org", orgstr))
					.append("\r\n");
		}
		if (cfinancialo != null && cfinancialo.length() > 0) {// 项目名称
			if (cfinancialo.indexOf(",") >= 0) {
				sql.append(" and "
						+ SQLUtil.buildSqlForIn("fct_ap.proname",
								cfinancialo.split(",")));
			} else {
				sql.append(" and fct_ap.proname = '" + cfinancialo + "' ");
			}
		}
		// if (cname != null) {// 合同名称
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
	 * 缓存结算方式
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