import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);

            while (true) {
                System.out.println("Waiting for the client request");
                Socket socket = ss.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                String line; //quando o cliente mandar um pedido então entra neste ciclo
                int x =0;
                int contador = 0;
                while ((line = in.readLine()) != null) {
                    try {
                        x += Integer.parseInt(line);
                        contador++;
                    }catch (NumberFormatException e){
                        x += 0;
                    }
                    out.println(x);
                    out.flush();
                }
                //Fim de leitura
                out.println((x/contador));
                out.flush();

                socket.shutdownOutput();
                socket.shutdownInput();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}