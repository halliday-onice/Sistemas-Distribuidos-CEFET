package SisDis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.concurrent.Semaphore;

public class ServidorDois implements Runnable {

	private String strNumUm;
	private String strNumDois;
	private BufferedWriter writer;

//    static int permits; 
	Semaphore semaphore;

	static int countWrite = 0;
	static int countRead = 0;

	public static void main(String[] args) throws IOException, InterruptedException {

		ServerSocket serverSocket = new ServerSocket(4001);

		Semaphore semaphore = new Semaphore(1);

		int countRead = 0;

		FileWriter file = new FileWriter("fileServidorUm.txt", true);
		BufferedWriter writer = new BufferedWriter(file);

		String mensagem = "";
		String strNumUm = "";
		String strNumDois = "";

		while (true) {

			Socket accept = serverSocket.accept();

			InputStreamReader inputReader = new InputStreamReader(accept.getInputStream());
			BufferedReader reader = new BufferedReader(inputReader);

			while ((mensagem = reader.readLine()) != null) {

				if (mensagem != null) {

					ServidorDois thread = new ServidorDois(strNumUm, strNumDois, writer, semaphore);
					Thread t1 = new Thread(thread);
					t1.start();
					t1.join();

				}
			}
		}
	}

	public void connection(int port, String text) throws UnknownHostException, IOException {

		Socket accept = new Socket("localhost", port);

		if (text != null) {
			PrintStream output = new PrintStream(accept.getOutputStream());
			output.println(text);
			output.println("4001");
		}

		accept.close();

	}

	public ServidorDois(String strNumUm, String strNumDois, BufferedWriter writer, Semaphore semaphore) {
		this.strNumUm = strNumUm;
		this.strNumDois = strNumDois;
		this.writer = writer;
		this.semaphore = semaphore;

	}

	public void run() {

		try {
			if (isNumber(strNumUm) && isNumber(strNumDois)) {
				int numUmInicio = Integer.parseInt(strNumUm);
				int numUm = Integer.parseInt(strNumUm);
				int numDois = Integer.parseInt(strNumDois);
				String resp = "";

				int temp;
				while (numDois != 0) {
		            temp = numDois;
		            numDois = numUm % numDois;
		            numUm = temp;
				}
				resp = "O MDC entre " + numUmInicio + "e " + numDois + "é: " + numUm;
				countWrite++;

				try {

					semaphore.acquire();
					writer.append(resp);
					writer.newLine();
					writer.flush();

					connection(5556, resp); // Conexão com o Load Balance para envio da resposta.
					semaphore.release();

				} catch (IOException e) {
					System.out.println("Ocorreu um erro ao gravar o arquivo: " + e.getMessage());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (strNumUm.equals("Read")) {

				countRead++;
				LocalTime currentTime = LocalTime.now();
				System.out.println(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond()
						+ " - Servidor 1: " + countRead + " requisições de leitura e " + countWrite
						+ " requisições de escrita.");

			} else {

				try {

					semaphore.acquire();

					writer.append(strNumUm);
					writer.append(strNumDois);
					writer.newLine();
					writer.flush();

					semaphore.release();

				} catch (IOException e) {
					System.out.println("Ocorreu um erro ao atualizar o arquivo: " + e.getMessage());
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}

		} catch (NumberFormatException e) {
			System.out.println("Número inválido!");
		}
	}

	public static boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}