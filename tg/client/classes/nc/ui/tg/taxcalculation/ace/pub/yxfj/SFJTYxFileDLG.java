package nc.ui.tg.taxcalculation.ace.pub.yxfj;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.TableColumnModel;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.os.outside.TGCallUtils;
import nc.bs.os.outside.TGOutsideUtils;
import nc.itf.cmp.CmpupdateUtils;
import nc.itf.tg.outside.IImageFileUploadService;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ms.tb.pubutil.DateUtil;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.table.NCTableModel;
import nc.vo.ml.Language;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.FileUploadResultVo;

/**
 * ����-˰�Ѽ���  Ӱ�񸽼�����ҳ��
 * @author zhaozhiying
 *
 */
public class SFJTYxFileDLG extends UIDialog {

BaseDAO baseDAO = null;
	
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
		baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	
	private static final long serialVersionUID = 2155255539479145231L;
	private JPanel dialogContentPane = null;	//��������
	private UITablePane dataTablePnl = null;	//����
	private UIPanel titilePanel = null;	//����������
	
	private UITable uitable;
	private NCTableModel tableModel;
	
	
	//�ϴ�����
	private UIButton relateBtn = null;
	private final String[] colName = {"�ļ���","�ļ���С","�ļ�����","������","��������","�ϴ�״̬","ʧ��ԭ��"};
	
	/** �����߸����ĵ���OID */
	String pk_bill = null;

	private AggregatedValueObject aggVO = null;
	public String getPk_bill() {
		return pk_bill;
	}

	public void setPk_bill(String pkBill) {
		pk_bill = pkBill;
	}
	
	
	public SFJTYxFileDLG(Container parent, AggregatedValueObject aggVO) throws Exception {
		super(parent);
		try {
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		String pk_bill = aggVO.getParentVO().getPrimaryKey();
		this.aggVO = aggVO;
		setPk_bill(pk_bill);
		//String billno = (String) aggVO.getParentVO().getAttributeValue("billno"); 
		String billno = (String) aggVO.getParentVO().getPrimaryKey(); 
		queryBillDealDataNew(billno);
	}
	
	
	public void queryBillDealDataNew(String billno)  throws BusinessException{
		
		List<FileUploadResultVo> list = getUploadResult(billno);
		if(list != null && list.size() >0){
			Object[][] obj = convertDBData(list);
			handleData(obj);
		}
	}
	
	/**
	 * ��ʼ���ࡣ
	 * @throws Exception 
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() throws Exception {
		setName("ParticularQueryDlg");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(750, 500);
		setResizable(true);
		setTitle("Ӱ�񸽼��ϴ�");
		setContentPane(getUIDialogContentPane());
		/** ��ʼ���� */
		initTable();
		initControl();
		initRelateBillControl();
	}
	
	/**
	 * ע��㣺 m_X���£� ����ֵ�� �������ڣ�(2001-10-29 19:15:07)
	 *
	 * @author��������
	 */
	public void initTable() {
		/** ��ʼ�� */
		uitable = getShowDataTablePnl().getTable();
		int width = 0;
		int len = colName.length;
		Object[][] obj = new Object[width][len];
		tableModel = new NCTableModel(obj, colName) {
			private static final long serialVersionUID = -5038880897605780628L;

			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		uitable.setModel(tableModel);
		// ��ӱ�ͷ
		TableColumnModel tcolModel = uitable.getColumnModel();

		/* �������� */
		uitable.sizeColumnsToFit(6);
		/* ȡ���Ե��� */
		uitable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	
	
	/**
	 * ���� UITablePane1 ����ֵ��
	 *
	 * @return UITablePane
	 */
	private UITablePane getShowDataTablePnl() {
		if (dataTablePnl == null) {
			dataTablePnl = new UITablePane();
			dataTablePnl.setName("UITablePane1");
			dataTablePnl.setAutoscrolls(true);
			dataTablePnl
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
		return dataTablePnl;
	}
	
	/**
	 * ���� �����ϴ���ť ����ֵ��
	 *
	 * @return UIButton
	 */
	private UIButton getRelateBtn() {
		if (relateBtn == null) {
			relateBtn = new UIButton();
			relateBtn.setName("RelateBtn");
			relateBtn.setFont(new Font("dialog", 0, 12));
			relateBtn.setText("�ϴ�����");
			
			if(NCLangRes4VoTransl.getNCLangRes().getCurrLanguage().getCode().equals(Language.SIMPLE_CHINESE_CODE)
			||NCLangRes4VoTransl.getNCLangRes().getCurrLanguage().getCode().equals(Language.TRAD_CHINESE_CODE)){
				relateBtn.setBounds(550, 8, 75, 22);
			}else{
				relateBtn.setBounds(520, 8, 105, 22);
			}
		}
		return relateBtn;
	}
	
	public void initRelateBillControl() throws BusinessException {
		getRelateBtn().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					onRelateBill();
				} catch (Exception e1) {
					throw new RuntimeException(e1.getMessage(), e1);
				}
			}
		});
	}
	
	/**
	 * ��������ϴ���ť�¼��������ļ�ѡ���
	 * @throws BusinessException 
	 */
	public void onRelateBill() throws BusinessException{
		
		JFileChooser fileChooser = initFileChooser();
		File file = fileChooser.getSelectedFile();
		//File[] files=fileChooser.getSelectedFiles();
		//validateFiles(files);
		
		if(file !=null && file.length() !=0){
			//throw new BusinessException("�ϴ�ʧ�ܣ�δѡ���κ��ļ�");
			File[] files=  new File[1];
			files[0]= file;
			
			// ����Ӱ�񸽼��ϴ��ӿ��ϴ�����
			List<FileUploadResultVo>  fileInfoList = callImageUpLoadInter(aggVO, files);
			//�������ݵ�����
			if(fileInfoList != null){
				Object[][] obj = convertDBData(fileInfoList);
				handleData(obj);
				//����
				afterUploadAddMap(fileInfoList);
			}
		}
	}
	
	/**
	 * *����˵�������ݲ�ѯ������Ժ����Դ��� *���ص����ݣ����ҽ�������ȷ����ʾ������ *���� obj �а�����ѯ�����ȫ������ ����ʽ��
	 * ����ͬһ��OID��ͬ���������ʾ�ڱ� ����ͬ�� *����ֵ�� *�������ڣ�(2001-11-14 8:50:37)
	 */
	public void handleData(Object[][] obj) {
		int len = obj.length;
		int row;
		for (row = 0; row < len; row++) {
			tableModel.addRow(obj[row]);
		}
	}
	
	public Object[][] convertDBData(List<FileUploadResultVo> list) throws BusinessException{
		int width = colName.length;
		Object[][] obj = new Object[list.size()][width];
		int i = 0;
		for (FileUploadResultVo objects : list) {
			obj[i][0] = objects.getFile_name();
			obj[i][1] = objects.getFile_size();
			
			String invoice_type = objects.getInvoice_type();
			if("1".equals(invoice_type)){
				invoice_type = "��Ʊ";
			}else if("70".equals(invoice_type)){
				invoice_type = "ָ�����";
			}else {
				invoice_type = "��ͨ����";
			}
			obj[i][2] = invoice_type;
			
			obj[i][3] = objects.getCreator();
			obj[i][4] = objects.getTs();
			obj[i][5] = "1".equals(objects.getUpload_status()) ? "ʧ��":"�ɹ�";
			obj[i][6] = "1".equals(objects.getUpload_status()) ? objects.getCheck_status():"";
			String[] billpk_type = new String[4];
		    billpk_type[0] =objects.getBillno();
		    billpk_type[1] = objects.getPdfuuid();
		    billpk_type[2] =objects.getPk_org();
		    billpk_type[3] = objects.getBarcode();
		    row_DyMap.put(Integer.valueOf(i), billpk_type);
			i++;
		}
		
		return obj;
	}
	
	
	/**
	 * ���� TitilePanel ����ֵ��
	 *
	 * @return UIPanel
	 */
	private UIPanel getTitilePanel() {
		if (titilePanel == null) {
			titilePanel = new UIPanel();
			titilePanel.setName("TitilePanel");
			titilePanel.setPreferredSize(new java.awt.Dimension(10, 40));
			titilePanel.setLayout(null);
			getTitilePanel().add(getRelateBtn(), getRelateBtn().getName());
			getTitilePanel().add(getPrintBtn(), getPrintBtn().getName());
		}
		return titilePanel;
	}
	
	/**
	 * ���� UIDialogContentPane �����򡢱�ͷλ������ ����ֵ��
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getUIDialogContentPane() {
		if (dialogContentPane == null) {
			dialogContentPane = new JPanel();
			dialogContentPane.setName("UIDialogContentPane");
			dialogContentPane.setLayout(new java.awt.BorderLayout());
			getUIDialogContentPane().add(getTitilePanel(), "North");
			getUIDialogContentPane().add(getShowDataTablePnl(), "Center");
		}
		return dialogContentPane;
	}
	
	/**
	 * ��ʼ���ļ�ѡ����
	 * @return
	 */
	private JFileChooser initFileChooser(){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.showDialog(new JLabel(), "ѡ���ļ�");
		return chooser;
	}
	
	/**
	 * У���Ƿ�ѡ�����ļ�
	 * @param files
	 * @throws BusinessException
	 */
	private void validateFiles(File[] files) throws BusinessException{
		if(files==null || files.length==0){
			throw new BusinessException("�ϴ�ʧ�ܣ�δѡ���κ��ļ�");
		}
	}
	
	private List<FileUploadResultVo> callImageUpLoadInter(AggregatedValueObject aggvo, File[] files) throws BusinessException{
		// Ӧ�յ�
		String barcode = (String) aggvo.getParentVO().getAttributeValue(getImageNoField(aggvo)); 
		
		String pkOrg = (String) aggvo.getParentVO().getAttributeValue("pk_org");
		String orgCode = getCodeByOrgPk(pkOrg);
		Date date = new Date();
		
		Map<String, Object> postdata = new HashMap<String, Object>();
		// �����������
		Map<String, String> datas = new HashMap<String, String>();
		datas.put("useraccount", InvocationInfoProxy.getInstance().getUserCode());
		datas.put("flowflag", barcode+date.getTime()); 
		datas.put("branchcode", orgCode);
		datas.put("typecode", "1");
		datas.put("typename", "NC-bill");
		datas.put("barcode", barcode); 
		// ��������ļ�����
		//�ļ���
		List<String> filesName = new ArrayList<String>();
		//�ļ�
		List<String> fileObjList = new ArrayList<String>();
		
		FileUploadResultVo fileVo = new FileUploadResultVo();
		List<FileUploadResultVo> fileVoList = new ArrayList<>();
		for(File file:files){
			filesName.add(file.getName());
			
			String fileString = jsonTransformFromFile(file);
			fileObjList.add(fileString);
			
			fileVo.setFile_name(file.getName());
			fileVo.setFile_size(getPrintSize(file.length()));
			String userCode = InvocationInfoProxy.getInstance().getUserCode();
			fileVo.setCreator(getUserPkByCode(userCode));
			fileVo.setTs(DateUtil.getFormatDate(date));
			fileVo.setBillno((String) aggvo.getParentVO().getPrimaryKey());
			fileVo.setPk_upload(barcode+date.getTime());
			
		}
		postdata.put("datas", datas);
		postdata.put("files", fileObjList);
		postdata.put("filesname", filesName);
		try {
			//ResultVO vo=TGCallUtils.getUtils().onDesCallService(null, "image", "checkFake", postdata);
			
			String URL = TGOutsideUtils.getUtils().getOutsidInfo("IMG02");
			//String imageData = NCLocator.getInstance().lookup(IImageFileUploadService.class).uploadFile(URL,datas,files);
			byte[] fileBuffer = FileUtils.readFileToByteArray(files[0]);
			String imageData = NCLocator.getInstance().lookup(IImageFileUploadService.class).uploadFileByByte(URL,datas,fileBuffer,files[0].getName());
			
			if(imageData == null){
				throw new BusinessException("�ϴ�Ӱ�񸽼�ʧ�ܣ�NC�����ϴ�����Ӱ�񷵻����ݿ�");
			}
			//String imageData = vo.getData();
			JSONObject imageDataJSON = JSON.parseObject(imageData);
			if(imageDataJSON==null || imageDataJSON.size()==0){
				throw new BusinessException("�ϴ�Ӱ�񸽼�ʧ�ܣ�Ӱ�񷵻����ݿ�");
			}
			String imageResult = imageDataJSON.getString("Result");
			if("1".equals(imageResult)){
				String errormsg = imageDataJSON.getString("errormsg");
				fileVo.setCheck_status("�ϴ�ʧ�ܣ�"+ errormsg);
				fileVo.setInvoice_type("0");
				//fileVo.setStatus(1);
				fileVo.setUpload_status("1");
			}else {
				
				fileVo.setCheck_status("�ϴ��ɹ�");
				fileVo.setInvoice_type("0");
				fileVo.setUpload_status("0");
			}
			//���۸����ϴ����
			insertUploadResult(fileVo);
			fileVoList.add(fileVo);
			
		} catch (Exception e) {
			throw new BusinessException("�ϴ�Ӱ�񸽼��쳣��" + e.getMessage());
		}
		
		return fileVoList;
	}
	
	
	/**
	 * ������֯������ȡ����
	 * @param pk_org
	 * @return
	 * @throws BusinessException 
	 */
	private String getCodeByOrgPk(String pk_org) throws BusinessException {
		String sql = "select code from org_orgs where (pk_org = '" + pk_org
				+ "') and dr=0 and enablestate=2 ";
		String code = null;
		IUAPQueryBS service = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		try {
			code = (String) service.executeQuery(sql,
					new ColumnProcessor());
			if (code != null) {
				return code;
			}
		} catch (Exception e) {
			throw new BusinessException("��ȡ��֯�����쳣��" + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * ��ȡӰ������ֶ�
	 * @param aggvo
	 * @return
	 * @throws BusinessException 
	 */
	private String getImageNoField(AggregatedValueObject aggvo) throws BusinessException{
		String pk_tradetype = (String) aggvo.getParentVO().getAttributeValue("transtype");
		IUAPQueryBS service = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String imageNoField = null;
		String sql = "select IMAGECODEFIELD from NCRELINST where PK_TRADETYPE = '"+pk_tradetype+"'";
		try {
			imageNoField = (String) service.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			throw new BusinessException("��ȡ�������͡�"+pk_tradetype+"��Ӱ������ֶ��쳣��" + e.getMessage(), e);
		}
		if(StringUtils.isEmpty(imageNoField)){
			throw new BusinessException("��ȡ�������͡�"+pk_tradetype+"��Ӱ������ֶ�Ϊ�գ����齻�������Ƿ�������Ӱ������ֶΡ�NCRELINST��");
		}
		return imageNoField;
	}
	
	/**
	 * ���ݽ�������ID��ȡ�������ͱ���
	 * @param pkTradeTypeid
	 * @return
	 */
	public String getPkTradeTypeid(String pkTradeTypeid){
		String sql = "select pk_billtypecode from bd_billtype where pk_billtypeid = '" + pkTradeTypeid+ "'";
		IUAPQueryBS service = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String pk_org = null;
		try {
			pk_org = (String) service.executeQuery(sql,new ColumnProcessor());
			if (pk_org != null) {
				return pk_org;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �����ļ���С
	 * @param size
	 * @return
	 * @throws BusinessException
	 */
	public String getPrintSize(long size) throws BusinessException {
		 if (size < 1024) {
	        return String.valueOf(size) + "B";
	     } else {
	        size = size / 1024;
	     }
		 
		 if (size < 1024) {
	         return String.valueOf(size) + "KB";
	     } else {
	         size = size / 1024;
	     }
		 
		 if (size < 1024) {
	         // ��Ϊ�����MBΪ��λ�Ļ���Ҫ�������1λС����
	         // ��ˣ��Ѵ�������100֮����ȡ��
	         size = size * 100;
	         if((size / 100) >= 50){
	        	 throw new BusinessException("���ļ��Ѵ���50MB"); 
	         }
	         return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
	     } else {
            // �������Ҫ��GBΪ��λ�ģ��ȳ���1024����ͬ���Ĵ���
            //size = size * 100 / 1024;
            //return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
	    	 throw new BusinessException("���ļ��Ѵ���50MB");
	     }
	 }
	
	/**
	 * ���ݡ��û����롿��ȡ����
	 * 
	 * @param code
	 * @return
	 */
	public String getUserPkByCode(String usercode) throws BusinessException {
		String sql = "select user_name   from sm_user where nvl(dr,0)=0 and user_code='"
				+ usercode + "'";
		String code = null;
		IUAPQueryBS service = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		try {
			code = (String) service.executeQuery(sql,
					new ColumnProcessor());
			if (code != null) {
				return code;
			}
		} catch (Exception e) {
			throw new BusinessException("��ȡ��֯�����쳣��" + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * ���渽���ϴ������Ϣ
	 * @param pk_jkbx
	 * @param djbh
	 * @param taskID
	 * @param stats
	 * @param result
	 * @param vo
	 * @throws BusinessException
	 */
	public void insertUploadResult(FileUploadResultVo vo) throws BusinessException{
		try {
			NCLocator.getInstance().lookup(IUifService.class).insert(vo);
		} catch (Exception e) {
			throw new BusinessException("���渽���ϴ������Ϣʧ�ܣ�" + e.getMessage(), e);
		}
	}
	
	public List<FileUploadResultVo> getUploadResult(String billno) throws BusinessException{
		try {
			String sql = "select * from tg_guoxinfileuploadresult where billno ='"+ billno +"' and nvl(dr,0)=0 and invoice_type = '0' order by ts desc";
			IUAPQueryBS service = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			List<FileUploadResultVo> list = (List<FileUploadResultVo>)service.executeQuery(sql, new BeanListProcessor(FileUploadResultVo.class));
			return list;
		} catch (BusinessException e) {
			throw new BusinessException("��ȡ�����ϴ���ʷ��Ϣ�쳣��" + e.getMessage(), e);
		}
	}
	
	/**
     * �ļ�תString
     *
     * @param file �ļ�
     * @return JSON
     */
    public String jsonTransformFromFile(File file) throws BusinessException{
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bf = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            fileInputStream.close();
            bf.close();
        } catch (Exception e) {
        	throw new BusinessException("������Ϣ�����쳣��" + e.getMessage(), e);
        }
        return stringBuilder.toString();
    }
    
    
    private final Map<Integer, String[]> row_DyMap = new HashMap<Integer, String[]>();
    //ɾ��������ť
  	private UIButton printBtn = null;
  	
  	public void afterUploadAddMap(List<FileUploadResultVo> list) {
		int count = tableModel.getRowCount() -1 ;
		for (FileUploadResultVo objects : list) {
			String[] billpk_type = new String[4];
		    billpk_type[0] =objects.getBillno();
		    billpk_type[1] = objects.getPdfuuid();
		    billpk_type[2] =objects.getPk_org();
		    billpk_type[3] = objects.getBarcode();
		    row_DyMap.put(Integer.valueOf(count), billpk_type);
		    count ++;
		}
	}
  	
    
    /**
	 * ���� PrintBtn ����ֵ��
	 *
	 * @return UIButton
	 */
	private UIButton getPrintBtn() {
		if (printBtn == null) {
			printBtn = new UIButton();
			printBtn.setName("PrintBtn");
			printBtn.setFont(new Font("dialog", 0, 12));
			printBtn.setText("ɾ��");
			printBtn.setBounds(635, 8, 75, 22);
		}
		return printBtn;
	}
	
	public void initControl() throws BusinessException {
		getPrintBtn().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					onDeleteFileResult();
				} catch (Exception e1) {
					throw new RuntimeException(e1.getMessage(), e1);
				}
			};
		});
	}
	
	 /**
		 * ���ɾ����ť��ɾ����Ӧ��Ӱ�񸽼�
	     * @throws BusinessException 
		 */
		public void onDeleteFileResult() throws BusinessException{
			int row = uitable.getSelectedRow();
			if (row == -1) {
				MessageDialog.showErrorDlg(this, null, "��ѡ��Ҫɾ���ĸ���");
				return;
			}
			/*if((uitable.getRowCount()-1) ==row){
				return;
			}*/
			String[] dy_bill = row_DyMap.get(Integer.valueOf(row));
			if(ArrayUtils.isEmpty(dy_bill)){
				return;
			}
			
			//String pk_bill =dy_bill[0];
			String pk_pdfid =dy_bill[1];
			String pk_org =dy_bill[2];
			String barcode = dy_bill[3];
			
			if(!StringUtils.isEmpty(pk_pdfid)){
				//ɾ��Ӱ�񸽼�
				String imageData = deleteYxFile(barcode,pk_pdfid,pk_org);
			}
			
			//ɾ�������ϴ���¼
			deleteYXFileUploadResult(pk_pdfid);
			
			//ˢ���б�
			tableModel.removeRow(row);
		}
		
		public void deleteYXFileUploadResult(String pdfuuid) throws BusinessException{
			CmpupdateUtils itf = NCLocator.getInstance().lookup(CmpupdateUtils.class);
			try {
				String sql = "update tg_guoxinfileuploadresult set dr = '1' where pdfuuid = '" + pdfuuid + "'";
				itf.update(sql);
			} catch (Exception e) {
				throw new BusinessException("ɾ��NCӰ�񸽼��ϴ����ʧ�ܣ�"+e.getMessage());
			}
		}
	    
	    
	    public String deleteYxFile(String barcode,String attrflag,String orgcode){
			String imageData = "";
			try {
				Date date = new Date();
				Map<String, Object> postdata = new HashMap<String, Object>();
				// �����������
				Map<String, String> datas = new HashMap<String, String>();
				datas.put("useraccount",  InvocationInfoProxy.getInstance().getUserCode());
				datas.put("flowflag", barcode+date.getTime()); 
				datas.put("branchcode", orgcode);
				datas.put("typecode", "1");
				datas.put("typename", "NC-bill");
				datas.put("barcode", barcode); 
				datas.put("attrflag", attrflag);
				datas.put("optype", "2");
				String URL = TGOutsideUtils.getUtils().getOutsidInfo("IMG02");
				//imageData = YxFileUploadUtil.getInstance().deleteFile(URL, datas);
				imageData = NCLocator.getInstance().lookup(IImageFileUploadService.class).deleteFile(URL, datas);
			} catch (BusinessException e) {
				throw new LfwRuntimeException("ɾ��Ӱ�񸽼�ʧ�ܣ�"+e.getMessage());
			}
			
			return imageData;
		}
	
    
}
