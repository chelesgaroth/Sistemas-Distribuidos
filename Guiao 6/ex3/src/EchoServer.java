import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
    Desta forma, o servidor trata de vários clientes ao mesmo tempo, concorrentemente.
    Temos de fazer run da aplicação EchoClient e da EchoClient2.

 */
public class EchoServer {

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);

            while (true) {
                System.out.println("Waiting for the client request");
                Socket socket = ss.accept();
                //criação de uma thread de forma a tratar cada um dos clientes que querem fazer pedidos
                Thread t = new Thread(new ServerWorker(socket));
                t.start();
            }
        }catch(IOException e){
                e.printStackTrace();
            }
        }

    static class ServerWorker implements Runnable {
        private Socket s;

        public ServerWorker(Socket socket) {
            this.s = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter out = new PrintWriter(s.getOutputStream());
                String line; //quando o cliente mandar um pedido então entra neste ciclo
                int x = 0;
                int contador = 0;
                while ((line = in.readLine()) != null) {
                    try {
                        x += Integer.parseInt(line);
                        contador++;
                    } catch (NumberFormatException e) {
                        x += 0;
                    }
                    out.println(x);
                    out.flush();
                }
                //Fim de leitura
                out.println((x / contador));
                out.flush();

                s.shutdownOutput();
                s.shutdownInput();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}