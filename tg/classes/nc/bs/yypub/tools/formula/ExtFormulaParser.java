package nc.bs.yypub.tools.formula;

import java.util.Hashtable;

import nc.ui.pub.formulaparse.FormulaParse;
import nc.vo.ep.bx.BXVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IExAggVO;

import org.hsqldb.lib.StringUtil;

public class ExtFormulaParser {

	FormulaParseFather formulaParse;

	public FormulaParseFather getFormulaParse() {
		if (formulaParse == null) {
			formulaParse = new FormulaParse();
		}
		return formulaParse;
	}

	public ExtFormulaParser() {
	}

	public ExtFormulaParser(FormulaParseFather formulaParse) {
		this.formulaParse = formulaParse;
	}

	public void execFormulas(String[] formulas, AggregatedValueObject aggVO, boolean isHead) {
		if (formulas != null && aggVO != null) {
			if (isHead) {
				if (aggVO.getParentVO() != null) {
					execFormulas(formulas, new CircularlyAccessibleValueObject[] { aggVO.getParentVO() }, getFormulaParse(), aggVO);
				}
			} else {
				if (aggVO.getChildrenVO() != null && aggVO.getChildrenVO().length > 0) {
					execFormulas(formulas, aggVO.getChildrenVO(), getFormulaParse(), aggVO);
				}
			}
		}
	}

	public void execFormulas(String[] formulas, IExAggVO exAggVO, boolean isHead, String bodyTableCode) {
		if (formulas != null && exAggVO != null) {
			if (isHead) {
				if (exAggVO.getParentVO() != null) {
					execFormulas(formulas, new CircularlyAccessibleValueObject[] { exAggVO.getParentVO() }, getFormulaParse(), exAggVO);
				}
			} else if (exAggVO.getChildrenVO() != null && exAggVO.getChildrenVO().length > 0) {
				execFormulas(formulas, exAggVO.getTableVO(bodyTableCode), getFormulaParse(), exAggVO);
			}
		}
	}

	public void execFormulas(String[] formulas, CircularlyAccessibleValueObject[] vos) {
		execFormulas(formulas, vos, getFormulaParse(), null);
	}

	public void execFormulas(String[] formulas, CircularlyAccessibleValueObject[] vos, FormulaParseFather formulaParse) {
		execFormulas(formulas, vos, formulaParse, null);
	}

	protected void execFormulas(String[] formulas, CircularlyAccessibleValueObject[] vos, FormulaParseFather formulaParse, Object bill) {
		if (formulas == null || formulas.length <= 0 || vos == null || vos.length <= 0 || formulaParse == null)
			return;

		formulaParse.setExpressArray(formulas);

		VarryVO[] varrys = formulaParse.getVarryArray();

		Hashtable[] hs = new Hashtable[varrys.length];

		Class cl = null;

		if (varrys != null) {
			for (int i = 0, loop = varrys.length; i < loop; i++) {
				VarryVO varry = varrys[i];
				Hashtable h = new Hashtable();
				String[] strVarry = varry.getVarry();
				if (strVarry != null) {
					for (int j = 0, loop1 = strVarry.length; j < loop1; j++) {
						String key = strVarry[j];
						if (IFormulaExt.BILL_VO.equalsIgnoreCase(key)) {
							if (bill != null) {
								if (bill instanceof IExAggVO) {
									formulaParse.addVariable(IFormulaExt.BILL_VO, (IExAggVO) bill);
								} else if (bill instanceof AggregatedValueObject) {
									formulaParse.addVariable(IFormulaExt.BILL_VO, (AggregatedValueObject) bill);
								}
							}
						} else if (!StringUtil.isEmpty(key) && key.indexOf(IFormulaExt.HEAD_FIELD) != -1) {
							String newKey = key.replace(IFormulaExt.HEAD_FIELD, "");
							String[] os = new String[vos.length];
							CircularlyAccessibleValueObject headVO = null;
							if (bill != null) {
								if (bill instanceof IExAggVO) {
									headVO = ((IExAggVO) bill).getParentVO();
								} else if (bill instanceof AggregatedValueObject) {
									headVO = ((AggregatedValueObject) bill).getParentVO();
								}
							}
							for (int row = 0, loop2 = vos.length; row < loop2; row++) {
								Object o = null;
								if (headVO != null) {
									o = headVO.getAttributeValue(newKey);
								}
								String value = null;
								if (o != null) {
									cl = o.getClass();
									if (cl == Integer.class || cl == UFDouble.class || cl == Double.class)
										value = o.toString();
									else
										value = "\"" + o.toString() + "\"";
								} else {
								}
								os[row] = value;
							}
							h.put(key, os);
						} else if (!StringUtil.isEmpty(key) && key.indexOf(IFormulaExt.BODY_FIELD) != -1) {
							String[] os = new String[vos.length];
							for (int row = 0, loop2 = vos.length; row < loop2; row++) {
								Object o = vos[row].getAttributeValue(key);
								String value = null;
								if (o != null) {
									cl = o.getClass();
									if (cl == Integer.class || cl == UFDouble.class || cl == Double.class)
										value = o.toString();
									else
										value = "\"" + o.toString() + "\"";
								} else {

								}
								os[row] = value;
							}
							h.put(key, os);
						} else {
							String[] os = new String[vos.length];
							for (int row = 0, loop2 = vos.length; row < loop2; row++) {
								Object o = vos[row].getAttributeValue(key);
								String value = null;
								if (o != null) {
									cl = o.getClass();
									if (cl == Integer.class || cl == UFDouble.class || cl == Double.class)
										value = o.toString();
									else
										value = "\"" + o.toString() + "\"";
								} else {

								}
								os[row] = value;
							}
							h.put(key, os);
						}
					}
				}
				hs[i] = h;
			}
		}

		formulaParse.setDataSArray(hs);

		String[][] results = formulaParse.getValueSArray();
		if (results != null) {

			for (int i = 0, loop = results.length; i < loop; i++) {
				String result[] = results[i];
				VarryVO varry = varrys[i];
				for (int row = 0, loop1 = vos.length; row < loop1; row++) {
					String formulaname = varry.getFormulaName();
					if (formulaname != null && formulaname.trim().length() > 0) {
						if (result[row] != null && "".equals(result[row].toString().trim()))
							result[row] = null;
						setValueToVo(vos[row], result[row], formulaname);

					}
				}
			}

		}
	}

	public static void setValueToVo(CircularlyAccessibleValueObject vo, Object value, String key) {
		if ((vo == null) || (key == null)) {
			return;
		}
		if (value == null) {
			vo.setAttributeValue(key, value);
			return;
		}
		key = key.trim();

		Class cl = null;
		try {
			cl = vo.getClass().getDeclaredField("m_" + key).getType();
		} catch (NoSuchFieldException e) {
			try {
				cl = vo.getClass().getDeclaredField(key).getType();
			} catch (NoSuchFieldException ex) {
				cl = String.class;
			} catch (Exception ex) {
			}
		} catch (Exception e) {
			// SCMEnv.out(e.getMessage());
		}
		Object oTarget = value;
		if (cl != null) {
			Class vcl = value.getClass();
			if (vcl != cl) {
				if (cl == String.class) {
					oTarget = value.toString();
				} else if (cl == UFDouble.class) {
					oTarget = new UFDouble(value.toString());
				} else if (cl == Integer.class) {
					String stemp = value.toString();
					int index = stemp.indexOf(".");
					if (index >= 0) {
						stemp = stemp.substring(0, index);
					}
					oTarget = new Integer(stemp);
				} else if (cl == UFBoolean.class) {
					oTarget = new UFBoolean(value.toString());
				} else if (cl == UFDate.class) {
					oTarget = new UFDate(value.toString());
				}
			}
		}
		try {
			vo.setAttributeValue(key, oTarget);
		} catch (Exception e) {
			// SCMEnv.out(e.getMessage());
		}
	}

	public void execFormulas(String[] headFormulas, String[] bodyFormulas, AggregatedValueObject aggVO) {
		if (aggVO != null) {
			if (headFormulas != null && aggVO.getParentVO() != null) {
				execFormulas(headFormulas, new CircularlyAccessibleValueObject[] { aggVO.getParentVO() }, getFormulaParse(), aggVO);
			}
			if (bodyFormulas != null && aggVO.getChildrenVO() != null && aggVO.getChildrenVO().length > 0) {
				execFormulas(bodyFormulas, aggVO.getChildrenVO(), getFormulaParse(), aggVO);
			}
		}
	}

	public void execFormulas(String[] headFormulas, String[] bodyFormulas, AggregatedValueObject aggVO, String billtype) {
		if (headFormulas != null && bodyFormulas != null && aggVO != null) {
			if (aggVO.getParentVO() != null) {
				execCostFormulas(headFormulas, new CircularlyAccessibleValueObject[] { aggVO.getParentVO() }, getFormulaParse(), aggVO);
			}
			if (((BXVO) aggVO).getcShareDetailVo() != null && ((BXVO) aggVO).getcShareDetailVo().length > 0) {
				execCostFormulas(bodyFormulas, ((BXVO) aggVO).getcShareDetailVo(), getFormulaParse(), aggVO);
			}
		}
	}

	private void execCostFormulas(String[] formulas, CircularlyAccessibleValueObject[] vos, FormulaParseFather formulaParse, Object bill) {
		if (formulas == null || formulas.length <= 0 || vos == null || vos.length <= 0 || formulaParse == null)
			return;
		formulaParse.setExpressArray(formulas);
		VarryVO[] varrys = formulaParse.getVarryArray();
		Hashtable[] hs = new Hashtable[varrys.length];
		Class cl = null;
		if (varrys != null) {
			for (int i = 0, loop = varrys.length; i < loop; i++) {
				VarryVO varry = varrys[i];
				Hashtable h = new Hashtable();
				String[] strVarry = varry.getVarry();
				if (strVarry != null) {
					for (int j = 0, loop1 = strVarry.length; j < loop1; j++) {
						String key = strVarry[j];
						String[] os = new String[vos.length];
						for (int row = 0, loop2 = vos.length; row < loop2; row++) {
							Object o = vos[row].getAttributeValue(key);
							String value = null;
							if (o != null) {
								cl = o.getClass();
								if (cl == Integer.class || cl == UFDouble.class || cl == Double.class)
									value = o.toString();
								else
									value = "\"" + o.toString() + "\"";
							} else {

							}
							os[row] = value;
						}
						h.put(key, os);
					}
				}
				hs[i] = h;
			}
		}
		formulaParse.setDataSArray(hs);
		String[][] results = formulaParse.getValueSArray();
		if (results != null) {
			for (int i = 0, loop = results.length; i < loop; i++) {
				String result[] = results[i];
				VarryVO varry = varrys[i];
				for (int row = 0, loop1 = vos.length; row < loop1; row++) {
					String formulaname = varry.getFormulaName();
					if (formulaname != null && formulaname.trim().length() > 0) {
						if (result[row] != null && "".equals(result[row].toString().trim()))
							result[row] = null;
						setValueToCostVo(vos[row], result[row], formulaname);
					}
				}
			}

		}
	}

	private static void setValueToCostVo(CircularlyAccessibleValueObject vo, Object value, String key) {
		if ((vo == null) || (key == null)) {
			return;
		}
		if (value == null) {
			vo.setAttributeValue(key, value);
			return;
		}
		key = key.trim();
		Class cl = null;
		try {
			cl = vo.getClass().getField(key).getType();
		} catch (Exception e) {
			try {
				cl = vo.getClass().getDeclaredField(key).getType();
			} catch (Exception e1) {
				if ("ismashare".equals(key)) {
					cl = UFBoolean.class;
				}
			}
		}
		Object oTarget = value;
		if (cl != null) {
			Class vcl = value.getClass();
			if (vcl != cl) {
				if (cl == String.class) {
					oTarget = value.toString();
				} else if (cl == UFDouble.class) {
					oTarget = new UFDouble(value.toString());
				} else if (cl == Integer.class) {
					String stemp = value.toString();
					int index = stemp.indexOf(".");
					if (index >= 0) {
						stemp = stemp.substring(0, index);
					}
					oTarget = new Integer(stemp);
				} else if (cl == UFBoolean.class) {
					oTarget = new UFBoolean(value.toString());
				} else if (cl == UFDate.class) {
					oTarget = new UFDate(value.toString());
				} else if (cl == UFDateTime.class) {
					oTarget = new UFDateTime(value.toString());
				}
			}
		}
		try {
			vo.setAttributeValue(key, oTarget);
		} catch (Exception e) {
		}
	}
}
