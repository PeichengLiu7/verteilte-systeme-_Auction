package vsue.rmi;

import java.io.IOError;
import java.net.http.WebSocket.Listener;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class VSAuctionServiceImpl implements VSAuctionService{
    List<VSAuction> runningAuctions = new LinkedList<VSAuction>();
    HashMap<VSAuction, VSAuctionEventHandler> auctionHandlerPair = new HashMap<VSAuction, VSAuctionEventHandler>();

    @Override
    public void registerAuction(VSAuction auction, int duration, VSAuctionEventHandler handler)
            throws VSAuctionException, RemoteException {
        // check duplicate
        if(runningAuctions.contains(auction)){
            throw new VSAuctionException("Auction already exists");
        }
        runningAuctions.add(auction);
        auctionHandlerPair.put(auction, handler);
        
        // start async timer for auction
        endAuctionIn(auction.getName(), duration, handler);
    }

    private void endAuctionIn(String auctionName, int duration, VSAuctionEventHandler handler){
        new Thread(new Runnable(){
            public void run() {
                // WAIT
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // GET AUCTION
                VSAuction auctionToEnd = new VSAuction("POISON", 0);
                while(runningAuctions.iterator().hasNext()){
                    VSAuction current = runningAuctions.iterator().next();
                    if(current.getName().equals(auctionName)){
                        // this is the one
                        auctionToEnd = current;
                        break;
                    }
                }

                // END
                try {
                    endAuction(auctionToEnd, handler);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                
            }
        }).start();
    }

    private void endAuction(VSAuction auctionToEnd, VSAuctionEventHandler handler) throws RemoteException{
        handler.handleEvent(VSAuctionEventType.AUCTION_END, auctionToEnd);
        auctionHandlerPair.get(auctionToEnd).handleEvent(VSAuctionEventType.AUCTION_WON, auctionToEnd);
        runningAuctions.remove(auctionToEnd);
        auctionHandlerPair.remove(auctionToEnd);
    }

    @Override
    public VSAuction[] getAuctions() throws RemoteException {
        return runningAuctions.toArray(new VSAuction[runningAuctions.size()]);
    }

    @Override
    public boolean placeBid(String userName, String auctionName, int price, VSAuctionEventHandler handler)
            throws VSAuctionException, RemoteException {
        boolean auctionExists = false, higher = false;
        int index = 0;
        while(runningAuctions.iterator().hasNext()){
            VSAuction current = runningAuctions.iterator().next();
            if(current.getName().equals(auctionName)){
                // this is the one
                auctionExists = true;
                if(price > current.getPrice()){
                    // new bid
                    higher = true;
                    VSAuction updatedAuction = new VSAuction(current.getName(), price);
                    runningAuctions.add(index, updatedAuction);
                    handler.handleEvent(VSAuctionEventType.HIGHER_BID, updatedAuction);
                    auctionHandlerPair.remove(current);
                    auctionHandlerPair.put(updatedAuction, handler);
                }
                break;
            }
            index++;
        }
        if(!auctionExists){
            throw new VSAuctionException("No such auction");
        }
        return higher;
    }
  
}
