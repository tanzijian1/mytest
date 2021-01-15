package nc.ui.tg.ref.model;

import nc.ui.bd.ref.AbstractRefModel;

public class ContractExecDefaultRefModel extends AbstractRefModel {

  public ContractExecDefaultRefModel(String refNodeName) {
    setRefNodeName(refNodeName);
  }

  public void setRefNodeName(String refNodeName) {

    m_strRefNodeName = refNodeName;
    setFieldCode(new String[] {
      "def2", "billno","def25"
    }); // 5项
    setFieldName(new String[] {
      "EBS请款单号",
      "NC付款申请单号",
      "本次请款累计付款金额"
    });// 编码，名称
    setHiddenFieldCode(new String[] {
      "pk_payreq"
    });
    setDefaultFieldCount(3);
    setTableName("tgfn_payrequest");
    setPkFieldCode("pk_payreq");
//    setAddEnableStateWherePart(true);
    setResourceID("contractexec");
//    setRefMaintenanceHandler(new nc.ui.bd.ref.model.BankRefMaintenanceHandler());
    resetFieldName();
    setMutilLangNameRef(true);
//    setEnvWherePart(getWherePart());
  }

  
}
