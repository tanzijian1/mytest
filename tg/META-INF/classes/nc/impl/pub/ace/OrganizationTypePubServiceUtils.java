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
import nc.vo.tg.organizationtype.OrganizationTypeVO;

public class OrganizationTypePubServiceUtils extends
		BDTreeBaseService<OrganizationTypeVO> {
	String NBCR_CODE = IBillNbcrCodeConst.NBCR_ORGANIZATIONTYPE;
	private IMDPersistenceQueryService mdQueryService;

	public OrganizationTypePubServiceUtils() {
		super(IBDMetaDataIDConst.ORGANIZATIONTYPE, null,
				new MDTreePersistenceUtil<OrganizationTypeVO>(
						IBDMetaDataIDConst.ORGANIZATIONTYPE, null));
	}

	static OrganizationTypePubServiceUtils utils = null;

	public static OrganizationTypePubServiceUtils getUtils() {
		if (utils == null) {
			utils = new OrganizationTypePubServiceUtils();
		}
		return utils;
	}

	/**
	 * 删除
	 * 
	 * @param vo
	 * @throws BusinessException
	 */
	public void delete(OrganizationTypeVO vo) throws BusinessException {
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
	public OrganizationTypeVO insert(OrganizationTypeVO vo)
			throws BusinessException {
		try {
			vo = insertVO(vo);
		} catch (ValidationException e) {
			BillCodeUtils.getUtils().abandonBillCode(vo, NBCR_CODE, e);
		} catch (BusinessException e) {
			BillCodeUtils.getUtils().rollbackBillCode(vo, NBCR_CODE, e);
		}
		return vo;
	}

	protected String[] dbInsertVOs(OrganizationTypeVO... vos)
			throws BusinessException {
		String[] pks = getPersistenceUtil().insertVOWithInnerCode(vos);
		new BaseDAO().executeUpdate("update "
				+ OrganizationTypeVO.getDefaultTableName()
				+ " set "
				+ OrganizationTypeVO.PK_FATHER
				+ "=isnull("
				+ OrganizationTypeVO.PK_FATHER
				+ ",'~') where "
				+ SQLUtil.buildSqlForIn(OrganizationTypeVO.PK_ORGANIZATIONTYPE,
						pks));
		return pks;
	}

	/**
	 * 修改
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	public OrganizationTypeVO update(OrganizationTypeVO vo)
			throws BusinessException {
		return updateVO(vo);
	}

	/*
	 * 启用及停用设置
	 */
	public OrganizationTypeVO enableTreeVO(OrganizationTypeVO vo)
			throws BusinessException {
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
		Collection<OrganizationTypeVO> list = getMdQueryService()
				.queryBillOfVOByCond(OrganizationTypeVO.class, condition, false);
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
				.createMDNullValueValidator(OrganizationTypeVO.class.getName(),
						Arrays.asList(OrganizationTypeVO.PK_ORG,
								OrganizationTypeVO.CODE,
								OrganizationTypeVO.NAME));
		// 封存校验
		return new Validator[] { notNullValidator,
				new BDTreeParentEnableValidator(null),
				super.getInsertValidator()[0],
				new DistributedAddBaseValidator() };
	}

	@Override
	protected Validator[] getUpdateValidator(OrganizationTypeVO oldVO) {
		// 非空校验
		List<Validator> list = new ArrayList<Validator>();
		list.add(NullValueValidator.createMDNullValueValidator(
				OrganizationTypeVO.class.getName(), Arrays.asList(
						OrganizationTypeVO.PK_ORG, OrganizationTypeVO.CODE,
						OrganizationTypeVO.NAME)));
		// 上下级循环引用校验
		list.add(new BDTreeUpdateLoopValidator());
		list.add(new BDTreeParentEnableValidator(oldVO));
		list.addAll(Arrays.asList(super.getUpdateValidator(oldVO)));
		return list.toArray(new Validator[0]);
	}

}
