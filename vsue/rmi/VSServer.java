package vsue.rmi;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class VSServer {
    //这里的ServerSocket是java.net.ServerSocket
    //我在服务器的函数
    //这里的ServerSocket是java.net.ServerSocket,通过这个类可以创建一个服务器
    //执行客户端发来的请求，实现调用客户端的功能
    //把客户端的请求转化为服务器的功能
    //注册表接受 你的个人信息过来，然后在服务端做处理
    //然后在给你返回一个结果
    private ServerSocket serverSocket;

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
}
