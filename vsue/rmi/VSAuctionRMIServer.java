package vsue.rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class VSAuctionRMIServer {
  public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException {
    VSAuctionService auctionService = new VSAuctionServiceImpl();
    VSAuctionService exportableAuctionService = (VSAuctionService) UnicastRemoteObject.exportObject(auctionService, 12678);

    Registry registry = LocateRegistry.createRegistry(12345);
    registry.bind("VSAuctionService", exportableAuctionService);

    Thread.sleep(Long.MAX_VALUE);
  }
}
