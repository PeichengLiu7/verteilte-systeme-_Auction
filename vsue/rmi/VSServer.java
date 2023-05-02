package vsue.rmi;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class VSServer {
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
