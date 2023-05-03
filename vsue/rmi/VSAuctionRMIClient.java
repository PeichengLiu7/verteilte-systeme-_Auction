package vsue.rmi;

import java.io.Serializable;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;


public class VSAuctionRMIClient extends VSShell implements VSAuctionEventHandler, Remote {


	private VSAuctionService service;

	// The user name provided via command line.
	private final String userName;




	public VSAuctionRMIClient(String userName) {
		this.userName = userName;
	}


	// #############################
	// # INITIALIZATION & SHUTDOWN #
	// #############################

	public void init(String registryHost, int registryPort) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(registryHost, registryPort);
		VSAuctionService service = (VSAuctionService) registry.lookup("VSAuctionService");
		this.service = service;
		UnicastRemoteObject.exportObject(this, 0);
	}

	public void shutdown() {
		try {
			UnicastRemoteObject.unexportObject(this, true);
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
	}


	// #################
	// # EVENT HANDLER #
	// #################

	@Override
	public void handleEvent(VSAuctionEventType event, VSAuction auction) {
		switch(event){
			case AUCTION_END:
				System.out.println("AUCTION " + auction.getName() + " HAS ENDED");
				break;
			case AUCTION_WON:
				System.out.println("YOU'VE WON AUCTION " + auction.getName());
				break;
			case HIGHER_BID:
				System.out.println("A HIGHER BID HAS BEEN PLACED ON AUCTION " + auction.getName() + " (" + auction.getPrice() +"€)");
		}
	}


	// ##################
	// # CLIENT METHODS #
	// ##################

	public void register(String auctionName, int duration, int startingPrice) {
		VSAuction auction = new VSAuction(auctionName, startingPrice);

		try {
			service.registerAuction(auction, duration, this);
			System.out.println("CREATED AUCTION " + auction.getName());
		} catch (RemoteException | VSAuctionException e) {
			e.printStackTrace();
		}
	}


	public void list(){
		System.out.println("RUNNING AUCTIONS:");
		VSAuction[] runningAuctions = new VSAuction[0];
		try {
			runningAuctions = service.getAuctions();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		for(VSAuction auction : runningAuctions){
			System.out.println(" - " + auction.getName());
		}
	}

	public void bid(String auctionName, int price){
		boolean bidden = false;
		try {
			bidden = service.placeBid(userName, auctionName, price, this);
			if(bidden){
				System.out.println("PLACED BID ON " + auctionName + " (" + price + "€)");
			}else{
				System.out.println("PRICE NOT HIGH ENOUGH OR AUCTION NOT FOUND");
			}
		} catch (RemoteException | VSAuctionException e) {
			e.printStackTrace();
		}
	}


	// #########
	// # SHELL #
	// #########

	protected boolean processCommand(String[] args) {
		switch (args[0]) {
			case "help":
			case "h":
				System.out.println("The following commands are available:\n"
						+ "  help\n"
						+ "  bid <auction-name> <price>\n"
						+ "  list\n"
						+ "  register <auction-name> <duration> [<starting-price>]\n"
						+ "  quit"
				);
				break;
			case "register":
			case "r":
				if (args.length < 3)
					throw new IllegalArgumentException("Usage: register <auction-name> <duration> [<starting-price>]");
				int duration = Integer.parseInt(args[2]);
				int startingPrice = (args.length > 3) ? Integer.parseInt(args[3]) : 0;
				register(args[1], duration, startingPrice);
				break;
			case "list":
			case "l":
				list();
				break;
			case "bid":
			case "b":
				if (args.length < 3) throw new IllegalArgumentException("Usage: bid <auction-name> <price>");
				int price = Integer.parseInt(args[2]);
				bid(args[1], price);
				break;
			case "exit":
			case "quit":
			case "x":
			case "q":
				return false;
			default:
				throw new IllegalArgumentException("Unknown command: " + args[0] + "\nUse \"help\" to list available commands");
		}
		return true;
	}


	// ########
	// # MAIN #
	// ########

	public static void main(String[] args) throws RemoteException, NotBoundException {
		checkArguments(args);
		createAndExecuteClient(args);
	}

	private static void checkArguments(String[] args) {
		if (args.length < 3) {
			System.err.println("usage: java " + VSAuctionRMIClient.class.getName() + " <user-name> <registry_host> <registry_port>");
			System.exit(1);
		}
	}

	private static void createAndExecuteClient(String[] args) throws RemoteException, NotBoundException {
		String userName = args[0];
		VSAuctionRMIClient client = new VSAuctionRMIClient(userName);

		String registryHost = args[1];
		int registryPort = Integer.parseInt(args[2]);
		client.init(registryHost, registryPort);
		client.shell();
		client.shutdown();
	}
}


