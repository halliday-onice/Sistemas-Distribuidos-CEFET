package SisDis;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class ClienteDois implements Runnable {

      private PrintStream out;

<<<<<<< HEAD
      public static void main(String[] arg) throws UnknownHostException, IOException, InterruptedException {
            Socket sock = new Socket("localhost", 4000);
            PrintStream output = new PrintStream(sock.getOutputStream());
            System.out.println("Output Stream: " + output);
=======
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket socket = new Socket("localhost", 4001);
		PrintStream output = new PrintStream(socket.getOutputStream());
>>>>>>> abf2153 (MDC calculado corretamente, ajutar escrita em arquivo)

            while (true) {
                  // cria uma thread
                  ClienteDois threadClient = new ClienteDois(output);
                  Thread t = new Thread(threadClient);
                  t.start();

                  Random random = new Random();
                  int minRange = 50;
                  int maxRange = 200;
                  int randomNumber = random.nextInt(maxRange - minRange + 1) + minRange;

<<<<<<< HEAD
                  t.sleep(randomNumber);
            }
      }

      // preciso inicializar o constructor para poder enviar os dados ao servidor
      public ClienteDois(PrintStream out) {
            this.out = out;
      }
=======
			Thread.sleep(randomNumber);
		}
	}
	// Constructor created 
	public ClienteDois(PrintStream output) {
		this.output = output;
	}
>>>>>>> abf2153 (MDC calculado corretamente, ajutar escrita em arquivo)

      public void run() {
            // eh nessa funcao run onde ocorrera o processamento da thread
            // Dado que a restricao eh:
            // devem ser sorteados dois números entre 2 e 1.000.000, que
            // serão enviados a um Servidor de Aplicação (por meio de um Balanceador de
            // carga) para
            // verificação do MDC (Máximo Divisor Comum) entre esses 2 números.
            // static int sleepDuration = (int) (Math.random() * (200 - 50 + 1) +
            // 50);//calcula os numeros entre 50 e 200, inclusive add 50 pra deslocar todo o
            // intervalo pra que comece em 50
            // thread tem que dormir por um tempo especificado entre 50 e 200 ms
            Random rndOne = new Random();
            Random rndTwo = new Random();
            int randomOne = rndOne.nextInt(999999) + 2;
            int randomTwo = rndTwo.nextInt(999999) + 2;

            // envia os numeros para o servidor
            out.println(randomOne);
            out.println(randomTwo);
      }
}
