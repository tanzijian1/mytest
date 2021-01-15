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
 * 邻里-税费计提  影像附件弹窗页面
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
	private JPanel dialogContentPane = null;	//弹窗布局
	private UITablePane dataTablePnl = null;	//数据
	private UIPanel titilePanel = null;	//类似搜索框
	
	private UITable uitable;
	private NCTableModel tableModel;
	
	
	//上传附件
	private UIButton relateBtn = null;
	private final String[] colName = {"文件名","文件大小","文件类型","创建者","创建日期","上传状态","失败原因"};
	
	/** 调用者给定的单据OID */
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
	 * 初始化类。
	 * @throws Exception 
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() throws Exception {
		setName("ParticularQueryDlg");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(750, 500);
		setResizable(true);
		setTitle("影像附件上传");
		setContentPane(getUIDialogContentPane());
		/** 初始化表 */
		initTable();
		initControl();
		initRelateBillControl();
	}
	
	/**
	 * 注意点： m_X更新： 返回值： 创建日期：(2001-10-29 19:15:07)
	 *
	 * @author：屈淑轩
	 */
	public void initTable() {
		/** 初始化 */
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
		// 添加表头
		TableColumnModel tcolModel = uitable.getColumnModel();

		/* 调整个列 */
		uitable.sizeColumnsToFit(6);
		/* 取出自调整 */
		uitable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	
	
	/**
	 * 返回 UITablePane1 特性值。
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
	 * 返回 附件上传按钮 特性值。
	 *
	 * @return UIButton
	 */
	private UIButton getRelateBtn() {
		if (relateBtn == null) {
			relateBtn = new UIButton();
			relateBtn.setName("RelateBtn");
			relateBtn.setFont(new Font("dialog", 0, 12));
			relateBtn.setText("上传附件");
			
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
	 * 点击附件上传按钮事件，弹出文件选择框
	 * @throws BusinessException 
	 */
	public void onRelateBill() throws BusinessException{
		
		JFileChooser fileChooser = initFileChooser();
		File file = fileChooser.getSelectedFile();
		//File[] files=fileChooser.getSelectedFiles();
		//validateFiles(files);
		
		if(file !=null && file.length() !=0){
			//throw new BusinessException("上传失败，未选择任何文件");
			File[] files=  new File[1];
			files[0]= file;
			
			// 调用影像附件上传接口上传附件
			List<FileUploadResultVo>  fileInfoList = callImageUpLoadInter(aggVO, files);
			//回显数据到弹窗
			if(fileInfoList != null){
				Object[][] obj = convertDBData(fileInfoList);
				handleData(obj);
				//保存
				afterUploadAddMap(fileInfoList);
			}
		}
	}
	
	/**
	 * *方法说明：数据查询完成了以后，用以处理 *返回的数据，并且将数据正确的显示到表中 *参数 obj 中包含查询结果的全部数据 处理方式：
	 * 对于同一个OID相同的两项不能显示在表 不相同的 *返回值： *创建日期：(2001-11-14 8:50:37)
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
				invoice_type = "发票";
			}else if("70".equals(invoice_type)){
				invoice_type = "指定付款函";
			}else {
				invoice_type = "普通附件";
			}
			obj[i][2] = invoice_type;
			
			obj[i][3] = objects.getCreator();
			obj[i][4] = objects.getTs();
			obj[i][5] = "1".equals(objects.getUpload_status()) ? "失败":"成功";
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
	 * 返回 TitilePanel 特性值。
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
	 * 返回 UIDialogContentPane 搜索框、表头位置设置 特性值。
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
	 * 初始化文件选择器
	 * @return
	 */
	private JFileChooser initFileChooser(){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.showDialog(new JLabel(), "选择文件");
		return chooser;
	}
	
	/**
	 * 校验是否选择了文件
	 * @param files
	 * @throws BusinessException
	 */
	private void validateFiles(File[] files) throws BusinessException{
		if(files==null || files.length==0){
			throw new BusinessException("上传失败，未选择任何文件");
		}
	}
	
	private List<FileUploadResultVo> callImageUpLoadInter(AggregatedValueObject aggvo, File[] files) throws BusinessException{
		// 应收单
		String barcode = (String) aggvo.getParentVO().getAttributeValue(getImageNoField(aggvo)); 
		
		String pkOrg = (String) aggvo.getParentVO().getAttributeValue("pk_org");
		String orgCode = getCodeByOrgPk(pkOrg);
		Date date = new Date();
		
		Map<String, Object> postdata = new HashMap<String, Object>();
		// 请求参数数据
		Map<String, String> datas = new HashMap<String, String>();
		datas.put("useraccount", InvocationInfoProxy.getInstance().getUserCode());
		datas.put("flowflag", barcode+date.getTime()); 
		datas.put("branchcode", orgCode);
		datas.put("typecode", "1");
		datas.put("typename", "NC-bill");
		datas.put("barcode", barcode); 
		// 请求参数文件数据
		//文件名
		List<String> filesName = new ArrayList<String>();
		//文件
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
				throw new BusinessException("上传影像附件失败：NC请求上传附件影像返回数据空");
			}
			//String imageData = vo.getData();
			JSONObject imageDataJSON = JSON.parseObject(imageData);
			if(imageDataJSON==null || imageDataJSON.size()==0){
				throw new BusinessException("上传影像附件失败：影像返回数据空");
			}
			String imageResult = imageDataJSON.getString("Result");
			if("1".equals(imageResult)){
				String errormsg = imageDataJSON.getString("errormsg");
				fileVo.setCheck_status("上传失败："+ errormsg);
				fileVo.setInvoice_type("0");
				//fileVo.setStatus(1);
				fileVo.setUpload_status("1");
			}else {
				
				fileVo.setCheck_status("上传成功");
				fileVo.setInvoice_type("0");
				fileVo.setUpload_status("0");
			}
			//留痕附件上传结果
			insertUploadResult(fileVo);
			fileVoList.add(fileVo);
			
		} catch (Exception e) {
			throw new BusinessException("上传影像附件异常：" + e.getMessage());
		}
		
		return fileVoList;
	}
	
	
	/**
	 * 根据组织主键获取编码
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
			throw new BusinessException("获取组织编码异常：" + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取影像编码字段
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
			throw new BusinessException("获取交易类型【"+pk_tradetype+"】影像编码字段异常：" + e.getMessage(), e);
		}
		if(StringUtils.isEmpty(imageNoField)){
			throw new BusinessException("获取交易类型【"+pk_tradetype+"】影像编码字段为空，请检查交易类型是否配置了影像编码字段【NCRELINST】");
		}
		return imageNoField;
	}
	
	/**
	 * 根据交易类型ID获取交易类型编码
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
	 * 计算文件大小
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
	         // 因为如果以MB为单位的话，要保留最后1位小数，
	         // 因此，把此数乘以100之后再取余
	         size = size * 100;
	         if((size / 100) >= 50){
	        	 throw new BusinessException("该文件已大于50MB"); 
	         }
	         return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
	     } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            //size = size * 100 / 1024;
            //return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
	    	 throw new BusinessException("该文件已大于50MB");
	     }
	 }
	
	/**
	 * 根据【用户编码】获取主键
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
			throw new BusinessException("获取组织编码异常：" + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 保存附件上传结果信息
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
			throw new BusinessException("保存附件上传结果信息失败：" + e.getMessage(), e);
		}
	}
	
	public List<FileUploadResultVo> getUploadResult(String billno) throws BusinessException{
		try {
			String sql = "select * from tg_guoxinfileuploadresult where billno ='"+ billno +"' and nvl(dr,0)=0 and invoice_type = '0' order by ts desc";
			IUAPQueryBS service = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			List<FileUploadResultVo> list = (List<FileUploadResultVo>)service.executeQuery(sql, new BeanListProcessor(FileUploadResultVo.class));
			return list;
		} catch (BusinessException e) {
			throw new BusinessException("获取附件上传历史信息异常：" + e.getMessage(), e);
		}
	}
	
	/**
     * 文件转String
     *
     * @param file 文件
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
        	throw new BusinessException("附件信息处理异常：" + e.getMessage(), e);
        }
        return stringBuilder.toString();
    }
    
    
    private final Map<Integer, String[]> row_DyMap = new HashMap<Integer, String[]>();
    //删除附件按钮
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
	 * 返回 PrintBtn 特性值。
	 *
	 * @return UIButton
	 */
	private UIButton getPrintBtn() {
		if (printBtn == null) {
			printBtn = new UIButton();
			printBtn.setName("PrintBtn");
			printBtn.setFont(new Font("dialog", 0, 12));
			printBtn.setText("删除");
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
		 * 点击删除按钮，删除对应的影像附件
	     * @throws BusinessException 
		 */
		public void onDeleteFileResult() throws BusinessException{
			int row = uitable.getSelectedRow();
			if (row == -1) {
				MessageDialog.showErrorDlg(this, null, "请选择要删除的附件");
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
				//删除影像附件
				String imageData = deleteYxFile(barcode,pk_pdfid,pk_org);
			}
			
			//删除附件上传记录
			deleteYXFileUploadResult(pk_pdfid);
			
			//刷新列表
			tableModel.removeRow(row);
		}
		
		public void deleteYXFileUploadResult(String pdfuuid) throws BusinessException{
			CmpupdateUtils itf = NCLocator.getInstance().lookup(CmpupdateUtils.class);
			try {
				String sql = "update tg_guoxinfileuploadresult set dr = '1' where pdfuuid = '" + pdfuuid + "'";
				itf.update(sql);
			} catch (Exception e) {
				throw new BusinessException("删除NC影像附件上传结果失败："+e.getMessage());
			}
		}
	    
	    
	    public String deleteYxFile(String barcode,String attrflag,String orgcode){
			String imageData = "";
			try {
				Date date = new Date();
				Map<String, Object> postdata = new HashMap<String, Object>();
				// 请求参数数据
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
				throw new LfwRuntimeException("删除影像附件失败："+e.getMessage());
			}
			
			return imageData;
		}
	
    
}
