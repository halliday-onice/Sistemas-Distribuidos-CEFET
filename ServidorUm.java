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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.math.*;
import java.util.concurrent.*;

public class ServidorUm implements Runnable{ // Any class that implements the runnable interface is called a thread
      private String Num;
      private BufferedWriter writer;

      public static int counterW = 0; // counter write files
      public static int counterR = 0; // counter read files

      Semaphore semaphore;
      public static void main(String[] arg) throws InterruptedException,IOException
      {
            static final int serverPort = 4000;
            
            // tem algumas coisas a mais aqui nesse meio do caminho...


            try{
                  ServerSocket serverSocket = new  ServerSocket(serverPort);
                  System.out.println("O Servidor esta ouvindo na porta " + serverPort);

                  
                  //Semaphore semaphore = new Semaphore(serverPort);
                  while(true)
                  {


                  }

            } catch(IOException e){
                  e.printStackTrace();
            }
            



            @Override
            public void run(){
                  //run eh chamada quando a thread for executada
                  System.out.println();
                  static int sleepDuration = (int) (Math.random() * (200 - 50 + 1) + 50);//calcula os numeros entre 50 e 200, inclusive add 50 pra deslocar todo o intervalo pra que comece em 50
                  // thread tem que dormir por um tempo especificado entre 50 e 200 ms
                  Random rndOne = new Random();
                  Random rndTwo = new Random();
                  int randomOne = rndOne.nextInt(99999) + 2;
                  int randomTwo = rndTwo.nextInt(99999) + 2;



            }
      
     
      }
     
      

}
