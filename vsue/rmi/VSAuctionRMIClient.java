package vsue.rmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;


public class VSAuctionRMIClient extends VSShell implements VSAuctionEventHandler {

	// The user name provided via command line.
	private final String userName;
	private VSAuctionService auctionService;
//这里的RemoteException是为了防止handleEvent被修改
	// ################
	// # CONSTRUCTORS #


	public VSAuctionRMIClient(String userName) {
		this.userName = userName;
	}

//功能：初始化客户端
//参数：registryHost：注册表主机名
//参数：registryPort：注册表端口号
//返回值：无
//异常：无
//注释：无
	// #############################
	// # INITIALIZATION & SHUTDOWN #
	// #############################


	public void init(String registryHost, int registryPort) {
		/*
		 * TODO: Implement client startup code
		 */
		try {
			String rmiUrl = String.format("//%s:%d/VSAuctionService", registryHost, registryPort);
			auctionService = (VSAuctionService) Naming.lookup(rmiUrl);
		} catch (Exception e) {
			System.err.println("Failed to connect to server: " + e.getMessage());
			System.exit(1);
			//功能：连接到服务器
//参数：registryHost：注册表主机名
//参数：registryPort：注册表端口号
//返回值：无
		}
	}

	public void shutdown() {
		/*
		 * TODO: Implement client shutdown code
		 */

	}


	// #################
	// # EVENT HANDLER #
	// #################

	@Override
	public void handleEvent(VSAuctionEventType event, VSAuction auction) {
		/*
		 * TODO: Implement event handler
		 */
	}


	// ##################
	// # CLIENT METHODS #
	// ##################

	public void register(String auctionName, int duration, int startingPrice) {
		/*
		 * TODO: Register auction
		 */
	}

	public void list() {
		/*
		 * TODO: List all auctions that are currently in progress
		 */
	}

	public void bid(String auctionName, int price) {
		/*
		 * TODO: Place a new bid
		 */
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
		//含义：处理命令
//参数：args：命令参数
//返回值：无
//异常：无
	}


	// ########
	// # MAIN #
	// ########

	public static void main(String[] args) {
		checkArguments(args);
		createAndExecuteClient(args);
	}

	private static void checkArguments(String[] args) {
		if (args.length < 3) {
			System.err.println("usage: java " + VSAuctionRMIClient.class.getName() + " <user-name> <registry_host> <registry_port>");
			System.exit(1);
			//功能：检查参数
//参数：args：命令参数
//返回值：无
//异常：无

		}
	}

	private static void createAndExecuteClient(String[] args) {
		String userName = args[0];
		VSAuctionRMIClient client = new VSAuctionRMIClient(userName);

		String registryHost = args[1];
		int registryPort = Integer.parseInt(args[2]);
		client.init(registryHost, registryPort);
		client.shell();
		client.shutdown();
		//功能：创建并执行客户端
//参数：args：命令参数
	}
}
