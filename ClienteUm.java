package SisDis;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
//import java.util.Math;

public class ClienteUm implements Runnable{
      
      private PrintStream out;

      public static void main(String[] arg) throws UnknownHostException, IOException, InterruptedException{
            int portAccess = ServidorUm.serverPort;
            System.out.println("A porta do servidor eh " + portAccess);
            Socket sock = new Socket();
            PrintStream output = new PrintStream(sock.getOutputStream());
            System.out.println("Output Stream: " + output);

            while(true) {
                  // cria uma thread
                  ClienteUm threadClient = new ClienteUm(output);
                  Thread t = new Thread(threadClient);

                  t.start();

            }
      }

      // preciso inicializar o constructor para poder enviar os dados ao servidor
      public ClienteUm(PrintStream out){
            this.out = out;
      }

      public void run(){
            //eh nessa funcao run onde ocorrera o processamento da thread
            // Dado que a restricao eh: 
            // devem ser sorteados dois números entre 2 e 1.000.000, que
            //serão enviados a um Servidor de Aplicação (por meio de um Balanceador de carga) para
            //verificação do MDC (Máximo Divisor Comum) entre esses 2 números.
            //static int sleepDuration = (int) (Math.random() * (200 - 50 + 1) + 50);//calcula os numeros entre 50 e 200, inclusive add 50 pra deslocar todo o intervalo pra que comece em 50
            // thread tem que dormir por um tempo especificado entre 50 e 200 ms
            try {
                  Random rndOne = new Random();
                  Random rndTwo = new Random();
                  int randomOne = rndOne.nextInt(99999) + 2;
                  int randomTwo = rndTwo.nextInt(99999) + 2;

                  // envia os numeros para o servidor
                  out.println(randomOne);
                  out.println(randomTwo);

                  Thread.sleep(randomOne);
                  Thread.sleep(randomTwo);

            } catch(InterruptedException e){
                  e.printStackTrace();
            }
            

      }

}

