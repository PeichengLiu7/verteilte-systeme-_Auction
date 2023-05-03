package vsue.rmi;


public enum VSAuctionEventType {

	/* Used by the service to notify a client that the client's bid
	 * is no longer the highest bid for a particular auction. */
	HIGHER_BID,
//含义：这个是用来通知客户端，客户端的出价不再是最高价了

	/* Used by the service to notify the creator of an auction
	 * that the auction has ended. */
	AUCTION_END,
	//含义：这个是用来通知拍卖的创建者，拍卖已经结束了
	/* Used by the service to notify a client that the client has
	 * won a particular auction, that is, that the auction has
	 * ended and that the client's bid was the highest. */
	AUCTION_WON
	//含义：这个是用来通知客户端，客户端赢得了这个拍卖，也就是说，拍卖已经结束了，而且客户端的出价是最高价

}
