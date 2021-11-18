package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sale {

	private int saleid;
	private String userid;
	private Date saledate;
	private User user;
	private List<SaleItem> itemList = new ArrayList<SaleItem>();
	public long getTotal() {//주문총액
		//jdk8.0 이후에 추가된 개념 : 람다, Stream. // 이게 람다방식이다. 람다방식이 Stream으로 만든다고 하는것.
		//return itemList.stream().mapToInt(is->is.getItem().getPrice() * is.getQuantity()).sum(); // 아래를 이거 한줄로 바꾼것
		long sum = 0;
		for(SaleItem si : itemList) {
			sum += si.getItem().getPrice() * si.getQuantity();
		}
		return sum;
	}
	//setter,getter,toString
	public int getSaleid() {
		return saleid;
	}
	public void setSaleid(int saleid) {
		this.saleid = saleid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Date getSaledate() {
		return saledate;
	}
	public void setSaledate(Date saledate) {
		this.saledate = saledate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<SaleItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SaleItem> itemList) {
		this.itemList = itemList;
	}
	@Override
	public String toString() {
		return "Sale [saleid=" + saleid + ", userid=" + userid + ", saledate=" + saledate + ", user=" + user + "]";
	}
	
}
