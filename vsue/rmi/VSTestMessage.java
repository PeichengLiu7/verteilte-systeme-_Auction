package vsue.rmi;

import java.io.*;
import java.nio.ByteBuffer;

import javax.swing.UIClientPropertyKey;

public class VSTestMessage implements Externalizable {
    private int integer;
    private String string;
    private Object[] objects;


    public String toString() {
        String retVal = "Integer: " + integer + " | String: " + string + " | Objects: [";
        if (objects != null && objects.length > 0) {
            for (Object o : objects) {
                retVal += o.toString() + ",";
            }
            retVal = retVal.substring(0, retVal.length() - 1);
        }
        retVal += "]";
        return retVal;
    }
    
    public VSTestMessage(int integer, String string, Object[] objects){
        this.integer = integer;
        this.string = string;
        this.objects = objects;
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        /*
        Aufbau:
        INT, Länge String, String, Länge Objects, (Länge Object, Object)*
        bzw.
        INT, INT, STRING, SHORT, (INT, OBJ)*
         */
        int lengthInt = Integer.BYTES;
        int lengthShort = Short.BYTES;

        long totalLength = lengthInt + lengthInt + string.getBytes().length + lengthShort;
        if(objects != null){
            for(Object o : objects){
                totalLength += lengthInt + Integer.MAX_VALUE;
            }
        }

        ByteBuffer serializedTestMessage = ByteBuffer.allocate((int)totalLength);

        serializedTestMessage.putInt(integer);
        serializedTestMessage.putInt(string.length());
        serializedTestMessage.put(string.getBytes());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(serializedTestMessage.array());
        if(objects != null){
            serializedTestMessage.putInt(objects.length);
            for(Object o : objects){
                Serializable obj = (Serializable) o;
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(obj);
                objectOutputStream.close();
            }
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int currentRead = 0;
        int startStringRead = 0;
        int lengthString = 0;
        int lengthArray = 0;
        ByteBuffer buf = null;
        int read = in.read();
        while(read != 0){
            if(currentRead == 0) {
                integer = read;
            }
            if(currentRead == 1){
                lengthString = read;
                startStringRead = currentRead;
                buf = ByteBuffer.allocate(lengthString);
            }
            if(currentRead < lengthString + startStringRead){
                buf.putInt(read);
            }
            if(currentRead == lengthString+startStringRead){
                string = buf.toString();
                lengthArray = read;
                buf = ByteBuffer.allocate(lengthArray);
            }
            if(currentRead > lengthArray+startStringRead){
                buf.putInt(read);
            }


            read = in.read();
        }

        if(lengthArray > 0){
            // read objects
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf.array());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Serializable[] object = new Serializable[lengthArray];
            for(int i = 0; i < lengthArray; i++){
                object[i] = (Serializable) objectInputStream.readObject();
            }
            objectInputStream.close();
        }
    }
}


