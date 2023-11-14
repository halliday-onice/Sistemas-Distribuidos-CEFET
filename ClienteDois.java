package SisDis;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class ClienteDois implements Runnable {

	private PrintStream output;

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket socket = new Socket("localhost", 4440);
		PrintStream output = new PrintStream(socket.getOutputStream());

		while (true) {
			ClienteDois thread = new ClienteDois(output);
			Thread t1 = new Thread(thread);
			t1.start();

			Random random = new Random();
			int minRange = 50;
			int maxRange = 200;
			int randomNumber = random.nextInt(maxRange - minRange + 1) + minRange;

			Thread.sleep(randomNumber);
		}
	}

	public ClienteDois(PrintStream output) {
		this.output = output;
	}

	public void run() {

		Random rndOne = new Random();
		Random rndTwo = new Random();
		int randomOne = rndOne.nextInt(999999) + 2;
		int randomTwo = rndTwo.nextInt(999999) + 2;

		// envia os numeros para o servidor
		output.println(randomOne);
		output.println(randomTwo);

	}
}