package SisDis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;


public class BalanceadorUm {
	
	//quantidade de conexões ativas em cada servidor para tomada de decisao
	
	private static int serv1Quantity = 0;
	private static int serv2Quantity = 0;
	public static int cliente = 0;
	public static int Port = 4000;
	String serv1port = "4000";
	String serv2port = "4040";
	private String strNumUm, strNumDois;
	
	 public static void main(String[] arg) throws InterruptedException, IOException {

         try (ServerSocket serverSocket = new ServerSocket(Port)) {
        	 if(Port == 4002) {
        		 cliente = 2;
        	 }else {
        		 cliente = 1;
        	 }
               System.out.println("Balanceador 1 recebendo requisições do cliente: " + cliente);
               
            
               
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
   
   public void run() {
		
	   try {
	    	 
	    	 Socket c1 = new Socket(); //aqui seria depois q decidisse pra qual porta mandar
	    	 System.out.println("Mandando para o serv na porta: " + serv1port);
			 PrintStream output = new PrintStream(c1.getOutputStream());
			
			 output.println(strNumUm);
			 output.println(strNumDois);
			 c1.close();

	        } catch (Exception e) {
	        	
	          
	        } 
	}

}
