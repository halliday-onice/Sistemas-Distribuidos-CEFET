package SisDis;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class ClienteUm implements Runnable {

	private PrintStream out;

	public static void main(String[] arg) throws UnknownHostException, IOException, InterruptedException {
		// final int portAccess = ServidorUm.serverPort;
		// System.out.println("A porta do servidor eh " + portAccess);
		Socket socket = new Socket("localhost", 4001);
		PrintStream output = new PrintStream(socket.getOutputStream());
		// System.out.println("Output Stream: " + output);

		while (true) {
			// cria uma thread
			ClienteUm threadClient = new ClienteUm(output);
			Thread t = new Thread(threadClient);

			t.start();

			Random rnd = new Random();
			int rangeMin = 500;
			int rangeMax = 2000;
			int randomNumber = rnd.nextInt(rangeMax - rangeMin + 1) + rangeMin;

			Thread.sleep(randomNumber);
		}
	}

	// preciso inicializar o constructor para poder enviar os dados ao servidor
	public ClienteUm(PrintStream out) {
		this.out = out;
	}

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
		Random rndReq = new Random();
		int request = rndReq.nextInt(2);

		if (request == 1) { // Requisição escrita
			Random rndOne = new Random();
			Random rndTwo = new Random();
			int randomOne = rndOne.nextInt(999999) + 2;
			int randomTwo = rndTwo.nextInt(999999) + 2;

			// envia os numeros para o servidor
			out.println(randomOne);
			out.println(randomTwo);
		} else {
			out.println("-1");
			out.println("-1");
		}
	}
}
