package nc.impl.pub.ace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import nc.bs.bd.baseservice.md.BDTreeBaseService;
import nc.bs.bd.baseservice.persistence.MDTreePersistenceUtil;
import nc.bs.bd.baseservice.validator.BDTreeParentEnableValidator;
import nc.bs.bd.baseservice.validator.BDTreeUpdateLoopValidator;
import nc.bs.dao.BaseDAO;
import nc.bs.tg.util.BillCodeUtils;
import nc.bs.uif2.validation.NullValueValidator;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.Validator;
import nc.itf.tg.bd.pub.IBDMetaDataIDConst;
import nc.itf.tg.bd.pub.IBillNbcrCodeConst;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.MDPersistenceService;
import nc.vo.bd.pub.DistributedAddBaseValidator;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BusinessException;
import nc.vo.tg.fintype.FinTypeVO;
import nc.vo.util.BDUniqueRuleValidate;
import nc.vo.util.SuperVOTreeStructrueValidator;

public class FinTypePubServiceUtils extends BDTreeBaseService<FinTypeVO> {
	String NBCR_CODE = IBillNbcrCodeConst.NBCR_FINTYPE;
	private IMDPersistenceQueryService mdQueryService;

	public FinTypePubServiceUtils() {
		super(IBDMetaDataIDConst.FINTYPE, null,
				new MDTreePersistenceUtil<FinTypeVO>(
						IBDMetaDataIDConst.FINTYPE, null));
	}

	static FinTypePubServiceUtils utils = null;

	public static FinTypePubServiceUtils getUtils() {
		if (utils == null) {
			utils = new FinTypePubServiceUtils();
		}
		return utils;
	}

	/**
	 * 删除
	 * 
	 * @param vo
	 * @throws BusinessException
	 */
	public void delete(FinTypeVO vo) throws BusinessException {
		deleteVO(vo);
		BillCodeUtils.getUtils().returnBillCodeForDel(vo, NBCR_CODE);

	}

	/**
	 * 插入
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	public FinTypeVO insert(FinTypeVO vo) throws BusinessException {
		try {
			vo = insertVO(vo);
		} catch (ValidationException e) {
			BillCodeUtils.getUtils().abandonBillCode(vo, NBCR_CODE, e);
		} catch (BusinessException e) {
			BillCodeUtils.getUtils().rollbackBillCode(vo, NBCR_CODE, e);
		}
		return vo;
	}

	protected String[] dbInsertVOs(FinTypeVO... vos) throws BusinessException {
		String[] pks = getPersistenceUtil().insertVOWithInnerCode(vos);
		new BaseDAO().executeUpdate("update " + FinTypeVO.getDefaultTableName()
				+ " set " + FinTypeVO.PK_FATHER + "=isnull("
				+ FinTypeVO.PK_FATHER + ",'~') where "
				+ SQLUtil.buildSqlForIn(FinTypeVO.PK_FINTYPE, pks));
		return pks;
	}

	/**
	 * 修改
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	public FinTypeVO update(FinTypeVO vo) throws BusinessException {
		return updateVO(vo);
	}

	/*
	 * 启用及停用设置
	 */
	public FinTypeVO enableTreeVO(FinTypeVO vo) throws BusinessException {
		if (IPubEnumConst.ENABLESTATE_ENABLE != vo.getEnablestate())
			return enableVO(vo);
		else
			return disableVO(vo);
	}

	/**
	 * 查询
	 * 
	 * @param condition
	 * @param c
	 * @return
	 * @throws BusinessException
	 */
	public Object[] query(String condition) throws BusinessException {
		Collection<FinTypeVO> list = getMdQueryService().queryBillOfVOByCond(
				FinTypeVO.class, condition, false);
		return list != null && list.size() > 0 ? list.toArray() : null;
	}

	private IMDPersistenceQueryService getMdQueryService() {
		if (mdQueryService == null)
			mdQueryService = MDPersistenceService
					.lookupPersistenceQueryService();
		return mdQueryService;
	}

	@Override
	protected Validator[] getInsertValidator() {
		// 非空校验
		NullValueValidator notNullValidator = NullValueValidator
				.createMDNullValueValidator(FinTypeVO.class.getName(), Arrays
						.asList(FinTypeVO.PK_ORG, FinTypeVO.CODE,
								FinTypeVO.NAME));

		// 封存校验
		return new Validator[] { notNullValidator,
				new BDTreeParentEnableValidator(null),
				new BDUniqueRuleValidate(),
				new SuperVOTreeStructrueValidator(FinTypeVO.PK_FATHER),
				super.getInsertValidator()[0],
				new DistributedAddBaseValidator() };
	}

	@Override
	protected Validator[] getUpdateValidator(FinTypeVO oldVO) {
		// 非空校验
		List<Validator> list = new ArrayList<Validator>();
		list.add(NullValueValidator.createMDNullValueValidator(
				FinTypeVO.class.getName(),
				Arrays.asList(FinTypeVO.PK_ORG, FinTypeVO.CODE, FinTypeVO.NAME)));
		// 上下级循环引用校验
		list.add(new BDTreeUpdateLoopValidator());
		list.add(new BDTreeParentEnableValidator(oldVO));
		list.addAll(Arrays.asList(super.getUpdateValidator(oldVO)));
		return list.toArray(new Validator[0]);
	}

}
