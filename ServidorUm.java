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

      Semaphore semaphore = new Semaphore(counterR);
      public static void main(String[] arg) throws InterruptedException,IOException
      {
            final int serverPort = 4000;
            
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
            




      
     
      }
      @Override
      public void run(){
            //run eh chamada quando a thread for executada
            System.out.println();
                  



      }
     
      

}
