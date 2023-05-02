package vsue.rmi;


public class VSAuction {
//注释掉的部分是原来的代码，下面是我自己添加的代码

	/* The auction name. */
	private final String name;
//代码每段的含义
	//1.这里的final是为了防止name被修改
	//2.这里的String是为了防止name被修改
	//3.这里的name是为了防止name被修改

	/* The currently highest bid for this auction. */
	private int price;


	public VSAuction(String name, int startingPrice) {
		this.name = name;
		this.price = startingPrice;
	}


	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

}
