package nc.vo.tg.recebill;

import java.io.Serializable;

public class ReceivableBodyVO implements Serializable{
		private String scomment;//ժҪ
		private String customer;//�ͻ�
		private float quantity_de;//����
		private float taxprice;//��˰����
		private float taxrate;//˰��
		private float local_tax_de;//˰��
		private float notax_de;//����˰���
		private float money_de;//��˰�ϼ�
		private float costamount;//�ɱ����
		private String contractcode;//��Ӧ�̺�ͬ����
		public String getScomment() {
			return scomment;
		}
		public void setScomment(String scomment) {
			this.scomment = scomment;
		}
		public String getCustomer() {
			return customer;
		}
		public void setCustomer(String customer) {
			this.customer = customer;
		}
		public float getQuantity_de() {
			return quantity_de;
		}
		public void setQuantity_de(float quantity_de) {
			this.quantity_de = quantity_de;
		}
		public float getTaxprice() {
			return taxprice;
		}
		public void setTaxprice(float taxprice) {
			this.taxprice = taxprice;
		}
		public float getTaxrate() {
			return taxrate;
		}
		public void setTaxrate(float taxrate) {
			this.taxrate = taxrate;
		}
		public float getLocal_tax_de() {
			return local_tax_de;
		}
		public void setLocal_tax_de(float local_tax_de) {
			this.local_tax_de = local_tax_de;
		}
		public float getNotax_de() {
			return notax_de;
		}
		public void setNotax_de(float notax_de) {
			this.notax_de = notax_de;
		}
		public float getMoney_de() {
			return money_de;
		}
		public void setMoney_de(float money_de) {
			this.money_de = money_de;
		}
		public float getCostamount() {
			return costamount;
		}
		public void setCostamount(float costamount) {
			this.costamount = costamount;
		}
		public String getContractcode() {
			return contractcode;
		}
		public void setContractcode(String contractcode) {
			this.contractcode = contractcode;
		}
		
		
		
		
		
		
}
