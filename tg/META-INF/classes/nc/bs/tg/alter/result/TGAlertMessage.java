package nc.bs.tg.alter.result;

import java.util.List;

import nc.bs.pub.pa.html.IAlertMessage2;

public abstract class TGAlertMessage implements IAlertMessage2 {
	private String messageTitle;

	List<Object[]> list = null;

	/**
	 * ����(����)
	 * 
	 * @return
	 */
	public abstract String[] getNames();

	/**
	 * ������
	 * 
	 * @return
	 */
	public abstract int[] getColumnType();

	/**
	 * �п��
	 * 
	 * @return
	 */
	public abstract float[] getWidths();

	@Override
	public String[] getBodyFields() {
		return getNames();

	}

	@Override
	public Object[][] getBodyValue() {
		if (getData() == null || getData().size() == 0) {
			return null;
		} else {
			Object[][] returnObjs = new Object[getData().size()][getNames().length];
			for (int i = 0; i < getData().size(); i++) {
				Object[] objs = (Object[]) getData().get(i);
				returnObjs[i] = objs;
			}
			return returnObjs;
		}
	}

	@Override
	public float[] getBodyWidths() {
		return getWidths();
	}

	@Override
	public String[] getBottom() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public String getTitle() {
		// TODO �Զ����ɵķ������
		return getMessageTitle();
	}

	@Override
	public String[] getTop() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public int[] getBodyColumnType() {
		// TODO �Զ����ɵķ������
		return getColumnType();
	}

	@Override
	public String getNullPresent() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public String getOmitPresent() {
		// TODO �Զ����ɵķ������
		return null;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public List<Object[]> getData() {
		return list;
	}

	public void setData(List<Object[]> list) {
		this.list = list;
	}

}
