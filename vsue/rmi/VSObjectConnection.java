package vsue.rmi;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class VSObjectConnection {
    private VSConnection vsConnection;

    public VSObjectConnection(Socket socket) throws IOException {
        vsConnection = new VSConnection(socket);
    }

//    public void sendObject(Serializable object) throws IOException {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//        objectOutputStream.writeObject(object);
//        objectOutputStream.close();
//
//        byte[] serializedObject = byteArrayOutputStream.toByteArray();
//        vsConnection.sendChunk(serializedObject);
//        //这里的sendChunk是VSConnection里的sendChunk
//        //这里的sendChunk是VSConnection里的sendChunk
//    }
    //1.2.4
    public void sendObject(Serializable object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();

        byte[] serializedObject = byteArrayOutputStream.toByteArray();
        vsConnection.sendChunk(serializedObject);

        // Print serialized data as hexadecimal values
        for (byte b : serializedObject) {
            System.out.print(Integer.toHexString(b & 0xFF) + " ");
        }
        System.out.println();

        // Print serialized data as a string
        String serializedString = new String(serializedObject, StandardCharsets.UTF_8);
        System.out.println("Serialized string: " + serializedString);
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

