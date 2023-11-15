package SisDis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class BalanceadorDois {

	private static List<String> listR = new ArrayList<String>();
	private static List<String> listA = new ArrayList<String>();
	private static List<String> listS = new ArrayList<String>();
	private static Semaphore semaphore;
	private static List<Integer> serv = new ArrayList<Integer>();
	private static int servQuantity = 2;

	private static Map<Integer, String> dicionario = new HashMap<>();

	public static void main(String[] args) throws IOException, InterruptedException {

		ServerSocket serverSocket = new ServerSocket(5556);

		serv.add(4000);
		serv.add(4040);

		Semaphore semaphore = new Semaphore(1);

		while (true) {

			Socket accept = serverSocket.accept();
			ReceiveConnections thread = new ReceiveConnections(accept, semaphore);
			Thread t1 = new Thread(thread);
			t1.start();

		}
	}

	private static class ReceiveConnections implements Runnable {

		private Socket accept;
		private Semaphore semaphore;

		public ReceiveConnections(Socket accept, Semaphore semaphore) {
			this.accept = accept;
			this.semaphore = semaphore;
		}

		public void run() {
			try {
				InputStreamReader inputReader = new InputStreamReader(this.accept.getInputStream());
				BufferedReader reader = new BufferedReader(inputReader);

				String message = "";
				String strNum = "";

				while ((message = reader.readLine()) != null) {

					if (message != null) {

						if (message.equals("-2") || message.equals("-1") || message.equals("0")) {

							semaphore.acquire();

							String flagServ = message; // Recebe qual servidor está ativo ou inativo

							int servPort = 0;

							if (flagServ.equals("-2")) { // Servidor 1 está on
								servPort = 4001;

							} else if (flagServ.equals("-1")) { // Servidor 2 está on
								servPort = 4002;

							} else { // Servidor 3 está on
								servPort = 4003;

							}

							String fileName = dicionario.get(servPort);
							File file = new File(fileName);
							FileReader fileReader = new FileReader(fileName);
							BufferedReader bufferedReader = new BufferedReader(fileReader);

							// Ler o conteúdo do arquivo
							StringBuilder conteudo = new StringBuilder();
							String linha;
							while ((linha = bufferedReader.readLine()) != null) {
								// Editar a linha conforme necessário
								Socket accept = new Socket("localhost", servPort);
								PrintStream output = new PrintStream(accept.getOutputStream());
								output.println(linha);

								accept.close();
							}

							bufferedReader.close();
							fileReader.close();

							file.delete(); // Apaga o arquivo
							dicionario.remove(servPort);

							BalanceadorDois.serv.add(servPort);

							BalanceadorDois.servQuantity += 1;

							semaphore.release();

						} else if (isNumber(message) || message.equals("Read")) {

							listR.add(message);

						} else {

							listA.add(message);

							while ((message = reader.readLine()) != null) {
								if (message != null) {
									listS.add(message);
									break;
								}
							}

						}
					}

					if (!listA.isEmpty()) {
						strNum = listA.get(0);
						String port = listS.get(0);
						listA.remove(0);
						listS.remove(0);

						Execution thread = new Execution(strNum, this.semaphore, BalanceadorDois.serv, port);
						Thread t1 = new Thread(thread);
						t1.start();

					} else if (!listR.isEmpty()) {
						strNum = listR.get(0);
						listR.remove(0);

						String flag = "0";

						Execution thread = new Execution(strNum, this.semaphore, BalanceadorDois.serv, flag);
						Thread t1 = new Thread(thread);
						t1.start();

					}
				}

			} catch (IOException e) {
				// TODO: handle exception
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	private static class Execution implements Runnable {

		private Semaphore semaphore;
		private String strNum;
		private List<Integer> serv;
		private String servPort;

		public Execution(String strNum, Semaphore semaphore, List<Integer> serv, String servPort) {
			this.strNum = strNum;
			this.semaphore = semaphore;
			this.serv = serv;
			this.servPort = servPort;

		}

		public void run() {

			try {

				if (servPort == "0") {

					semaphore.acquire();

					Random random = new Random();
					int randomNumber = random.nextInt(BalanceadorDois.servQuantity);

					int port = serv.get(randomNumber);
					boolean connected = true;

					try {

						Socket acc = new Socket("localhost", port);
						System.out.println("Mandando para o serv na porta: " + port);
						PrintStream output = new PrintStream(acc.getOutputStream());
						output.println(strNum);
						acc.close();

					} catch (Exception e) {

						BalanceadorDois.serv.remove(Integer.valueOf(port));
						String nameFile = "file" + port + ".txt";

						BalanceadorDois.dicionario.put(port, nameFile);

						BalanceadorDois.servQuantity -= 1;

						connected = false;

					}

					if (connected == false) {

						randomNumber = random.nextInt(BalanceadorDois.servQuantity);

						port = BalanceadorDois.serv.get(randomNumber);

						System.out.println("Mandando para o serv na porta: " + port);

						Socket acc = new Socket("localhost", port);
						PrintStream output = new PrintStream(acc.getOutputStream());
						output.println(strNum);
						acc.close();
					}

					semaphore.release();

				} else {

					if (servPort.equals("4001")) {

						semaphore.acquire();

						List<Integer> copia = new ArrayList<>(BalanceadorDois.serv);

						for (int servPort : copia) {
							if (servPort != 4001) {
								connection(servPort, strNum);
							}
						}

						semaphore.release();

					} else if (servPort.equals("4002")) {

						semaphore.acquire();

						List<Integer> copia = new ArrayList<>(BalanceadorDois.serv);

						for (int servPort : copia) {
							if (servPort != 4002) {
								connection(servPort, strNum);
							}
						}

						semaphore.release();

					} else if (servPort.equals("4003")) {

						semaphore.acquire();

						List<Integer> copia = new ArrayList<>(BalanceadorDois.serv);

						for (int servPort : copia) {
							if (servPort != 4003) {
								connection(servPort, strNum);
							}
						}

						semaphore.release();

					}

					// Se o meu dicionário estiver com algum elemento, quer dizer que precisamos
					// atualizar o seu arquivo correspondente

					if (!BalanceadorDois.dicionario.isEmpty()) {

						for (int servOff : dicionario.keySet()) {
							String fileName = dicionario.get(servOff);

							FileWriter file = new FileWriter(fileName, true);

							BufferedWriter writer = new BufferedWriter(file);
							writer.write(strNum);
							writer.newLine();
							writer.flush();

							writer.close();
							file.close();
						}

					}

				}

			} catch (IOException e) {

				e.printStackTrace();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}

		public static void connection(int port, String text) throws UnknownHostException, IOException {

			try {

				Socket accept = new Socket("localhost", port);
				PrintStream output = new PrintStream(accept.getOutputStream());
				output.println(text);

				accept.close();

			} catch (Exception e) {

				BalanceadorDois.serv.remove(Integer.valueOf(port));
				String nameFile = "file" + port + ".txt";

				BalanceadorDois.dicionario.put(port, nameFile);
				BalanceadorDois.servQuantity -= 1;
			}
		}
	}
}