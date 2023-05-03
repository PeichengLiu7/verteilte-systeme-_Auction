package vsue.rmi;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class VSClient {
    private VSObjectConnection objectConnection;

    public VSClient(String serverHost, int serverPort) throws IOException {
        Socket socket = new Socket(serverHost, serverPort);
        objectConnection = new VSObjectConnection(socket);
    }

    public Object sendAndReceiveObject(Object obj) throws IOException, ClassNotFoundException {
        objectConnection.sendObject((Serializable) obj);
        return objectConnection.receiveObject();
    }
    public static void main(String[] args) {
        try {
            VSClient client = new VSClient("localhost", 12345);

            // Send test data to the server
//            String testData = "Hello, server!";

            Object[] testObjects = {new VSAuction("Test", 0)};
            VSTestMessage testData = new VSTestMessage(0, null, null);//new VSTestMessage(100, "Verteilte Systeme", testObjects);
            System.out.println("Sending data: " + testData);
            Object response = client.sendAndReceiveObject(testData);

            // Receive the server's response and print it to the console
            System.out.println("Received response: " + response);


            testData = new VSTestMessage(10 , null, null);
            System.out.println("Sending data: " + testData.toString());
            response = client.sendAndReceiveObject(testData);

            testData = new VSTestMessage(50000 , null, null);
            System.out.println("Sending data: " + testData.toString());
            response = client.sendAndReceiveObject(testData);

            testData = new VSTestMessage(-3 , null, null);
            System.out.println("Sending data: " + testData.toString());
            response = client.sendAndReceiveObject(testData);



            // Receive the server's response and print it to the console
            System.out.println("Received response: " + response.toString());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error while communicating with the server: " + e.getMessage());
        }
    }

}


