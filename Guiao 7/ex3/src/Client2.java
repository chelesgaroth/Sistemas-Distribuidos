import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * Após conectar-se ao servidor, este cliente recebe a lista de contactos existentes !!
 */
public class Client2 {

    public static Contact parseLine (String userInput) {
        String[] tokens = userInput.split(" ");

        if (tokens[3].equals("null")) tokens[3] = null;

        return new Contact(
                tokens[0],
                Integer.parseInt(tokens[1]),
                Long.parseLong(tokens[2]),
                tokens[3],
                new ArrayList<>(Arrays.asList(tokens).subList(4, tokens.length)));
    }



    public static void main (String[] args) throws IOException {
        Socket socket = new Socket("localhost", 123456);

        DataInputStream in = new DataInputStream(socket.getInputStream());

        Contact c;
        for(int i = 0; i<in.readInt() ; i++){
            c = Contact.deserialize(in);
            System.out.println("Contacto nº" + (i+1) + ":" + c.toString());
        }
        socket.close();
    }
}