package logic;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	private List<ItemSet> itemSetList = new ArrayList<ItemSet>();
	//생성자는 없다. 
	public List<ItemSet> getItemSetList(){//get프로퍼티
		return itemSetList;
	}
	public void push(ItemSet itemSet) {
		//itemSet : 장바구니에 추가할 상품 정보가 된다.
		int count = itemSet.getQuantity();
		//itemSetList : 이미 장바구니에 추가되어 있는 상품 목록
		for(ItemSet old : itemSetList) {//itemSetList에서 객체를 꺼내서 ItemSet old에다가 넣겠다는 것 // itemSetList에 객체가 없을때까지
			if(itemSet.getItem().getId() == old.getItem().getId()) {
				count = old.getQuantity() + itemSet.getQuantity();
				old.setQuantity(count);
				return;
			}
		}
		itemSetList.add(itemSet);//장바구니에 상품을 그냥 추가하라고 해놓은게 이거다 . 이거만 있으면 안된다.
	}
	public long getTotal() {// getter가 get property  //checkout.jsp에서 호출한거 // 잘기억하기
		long sum = 0;
		for(ItemSet is : itemSetList) {//전체 주문의 합들이 sum에 들어있다고 정의
			sum += is.getItem().getPrice() * is.getQuantity();
		}
		return sum;
	}
	
	
}
