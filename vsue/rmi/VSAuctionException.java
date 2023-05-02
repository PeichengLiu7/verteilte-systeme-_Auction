package vsue.rmi;


public class VSAuctionException extends Exception {

	public VSAuctionException(String message) {
		super(message);
	}
//这里的Exception是为了防止message被修改

}
