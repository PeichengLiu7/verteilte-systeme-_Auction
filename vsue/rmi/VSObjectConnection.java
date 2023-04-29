package vsue.rmi;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class VSObjectConnection {
    private VSConnection vsConnection;

    public VSObjectConnection(Socket socket) throws IOException {
        vsConnection = new VSConnection(socket);
    }

    public void sendObject(Serializable object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();

        byte[] serializedObject = byteArrayOutputStream.toByteArray();
        vsConnection.sendChunk(serializedObject);
    }

    public Serializable receiveObject() throws IOException, ClassNotFoundException {
        byte[] serializedObject = vsConnection.receiveChunk();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedObject);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Serializable object = (Serializable) objectInputStream.readObject();
        objectInputStream.close();

        return object;
    }
}

