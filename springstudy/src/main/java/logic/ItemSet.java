package logic;

public class ItemSet {//이건 상품정보과 수량이기 때문에 Item에 넣을수가 없다.
	private Item item; //상품정보
	private Integer quantity; //수량
	
	public ItemSet(Item item, Integer quantity) {//생성자.
		this.item = item;
		this.quantity = quantity;
	}
	//getter, setter, toString
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "ItemSet [item=" + item + ", quantity=" + quantity + "]";
	}
	
}
