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
}


