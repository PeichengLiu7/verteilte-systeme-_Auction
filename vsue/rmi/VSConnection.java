package vsue.rmi;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class VSConnection {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public VSConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    public void sendChunk(byte[] chunk) throws IOException {
        // Write the length of the chunk (as an integer) before the actual data
        outputStream.write(chunk.length >> 24);
        outputStream.write(chunk.length >> 16);
        outputStream.write(chunk.length >> 8);
        outputStream.write(chunk.length);

        // Write the actual data
        outputStream.write(chunk);
        outputStream.flush();
    }

    public byte[] receiveChunk() throws IOException {
        // Read the length of the chunk (as an integer)
        int length = (inputStream.read() << 24) | (inputStream.read() << 16) | (inputStream.read() << 8) | inputStream.read();

        // Read the actual data
        byte[] chunk = new byte[length];
        int bytesRead = 0;
        while (bytesRead < length) {
            bytesRead += inputStream.read(chunk, bytesRead, length - bytesRead);
        }

        return chunk;
    }
}


