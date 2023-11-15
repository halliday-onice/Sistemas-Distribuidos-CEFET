package SisDis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.math.*;
import java.util.concurrent.*;

public class ServidorUm implements Runnable { // Any class that implements the runnable interface is called a thread
      private String strNumUm, strNumDois;
      private BufferedWriter writer;

      public static int counterW = 0; // counter write files
      public static int counterR = 0; // counter read files

      Semaphore semaphore = new Semaphore(counterR);

      public static int serverPort = 4000;

      public static void main(String[] arg) throws InterruptedException, IOException {

            try (ServerSocket serverSocket = new ServerSocket(serverPort)) {

                  System.out.println("O Servidor esta ouvindo na porta " + serverPort);

                  // Semaphore semaphore = new Semaphore(serverPort);
                  FileWriter file = new FileWriter("fileServidorUm.txt", true);
                  BufferedWriter writer = new BufferedWriter(file);

                  while (true) {
                        Socket clientSocket = serverSocket.accept();

                        proccessRequisitionClient(clientSocket);

                  }

            } catch (IOException e) {
                  e.printStackTrace();
            }

      }

      private static void proccessRequisitionClient(Socket clientSocket) throws IOException {
            try (InputStream input = clientSocket.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

                  // Ler os numeros recebidos do cliente

                  while (reader != null) {
                        int firstNumberR = Integer.parseInt(reader.readLine());
                        int secondNumberR = Integer.parseInt(reader.readLine());
                        System.out.println("Received numbers from client: " + firstNumberR + ", " + secondNumberR);

                        ServidorUm servUm = new ServidorUm();
                        servUm.setNumbers(firstNumberR, secondNumberR);
                        Thread t1 = new Thread(servUm);
                        t1.start();
                        try {
                              t1.join();
                        } catch (InterruptedException e) {
                              e.printStackTrace();
                        }
                  }

            }

      }

      public void setNumbers(int numUm, int numDois) {
            this.strNumUm = String.valueOf(numUm);
            this.strNumDois = String.valueOf(numDois);

      }

      public void run() {
            // run eh chamada quando a thread for executada
            try {
                  if (isNumber(strNumUm) && isNumber(strNumDois)) {
                        int numUmInicio = Integer.parseInt(strNumUm);
                        int numDoisInicio = Integer.parseInt(strNumDois);
                        int numUm = Integer.parseInt(strNumUm);
                        int numDois = Integer.parseInt(strNumDois);
                        String resp = "";

                        int temp;
                        while (numDois != 0) {
                              temp = numDois;
                              numDois = numUm % numDois;
                              numUm = temp;
                        }
                        resp = "O MDC entre " + numUmInicio + " e " + numDoisInicio + " é: " + numUm;
                        System.out.println(resp);
                        counterW++;

                  } else if (strNumUm.equals("Read")) {

                        counterR++;
                        LocalTime currentTime = LocalTime.now();
                        System.out.println(currentTime.getHour() + ":" + currentTime.getMinute() + ":"
                                    + currentTime.getSecond()
                                    + " - Servidor 1: " + counterR + " requisições de leitura e " + counterW
                                    + " requisições de escrita.");

                  }

            } catch (NumberFormatException e) {
                  System.out.println("Número inválido!");
            }

      }

      public static boolean isNumber(String str) {
            try {
                  Integer.parseInt(str);
                  return true;
            } catch (NumberFormatException e) {
                  return false;
            }
      }

}
