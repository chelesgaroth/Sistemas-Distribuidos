import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

    public class EchoClient {

        public static void main(String[] args) {
            try {
                Socket socket = new Socket("localhost", 12345);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));

                String userInput;
                while ((userInput = systemIn.readLine()) != null) {
                    out.println(userInput);
                    out.flush();

                    String response = in.readLine();
                    System.out.println("Valor Atualizado " + response);
                }
                socket.shutdownOutput();
                /*temos que fechar o output que manda para o servidor porque caso contrário o servidor
                vai ficar sempre à espera de receber um pedido de umc cliente*/
                String media = in.readLine();
                System.out.println("Média Final: " + media);


                socket.shutdownInput();
                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//Usar CTRL + D para mandar interromper o ciclo e obter a média