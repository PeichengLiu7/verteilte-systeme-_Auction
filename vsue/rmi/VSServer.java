package vsue.rmi;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class VSServer {
    private ServerSocket serverSocket;
//含义：创建一个ServerSocket对象，绑定指定端口，并监听此端口。
//如果端口值为 0，则服务器将随机选择一个端口号。
//一般情况下，只有端口号小于 1024 的值才需要特别权限才可以使用。
//ServerSocket(int port) 创建绑定到特定端口的服务器套接字。
//ServerSocket(int port, int backlog) 创建绑定到特定端口的服务器套接字，并且具有指定的侦听器最大队列长度。
//ServerSocket(int port, int backlog, InetAddress bindAddr) 创建绑定到特定端口的服务器套接字，并且具有指定的侦听器最大队列长度和要绑定到的本地 IP 地址。


    public VSServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            WorkerThread workerThread = new WorkerThread(socket);
            workerThread.start();
        }
    }

    private class WorkerThread extends Thread {
        private Socket socket;

        public WorkerThread(Socket socket) {
            this.socket = socket;
        }
        public static void main(String[] args) {
            try {
                int serverPort = 12345;
                VSServer server = new VSServer(serverPort);
                System.out.println("Server is running on port " + serverPort);
                server.start();
            } catch (IOException e) {
                System.err.println("Error while starting the server: " + e.getMessage());
            }
        }


        public void run() {
            try {
                VSObjectConnection objectConnection = new VSObjectConnection(socket);
                while (true) {
                    Object receivedObject = objectConnection.receiveObject();
                    objectConnection.sendObject((Serializable) receivedObject);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

