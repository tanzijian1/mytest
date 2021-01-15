package nc.itf.tg.outside;

public interface IRepaymentRefContractService {

	public 	String getContractSqlmain(String pk_org);
	public 	String getContractSql(String pk_org);
	public 	boolean checkTotalOrg(String pk_org);
}
