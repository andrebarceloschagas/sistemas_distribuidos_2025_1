import java.net.*;
import java.io.*;

import java.util.*;

class scandeporta{
   public static void main( String args[] ) throws IOException {
     try {          
         
           Scanner scanner = new Scanner(System.in);
            
           System.out.println("\nDigite o IP:");
           String IP = scanner.nextLine();
           
           System.out.println("\nDigite o Porta de Inicio:");
           int portainic = scanner.nextInt();
		   String msg = scanner.nextLine();
		   
		   System.out.println("\nDigite o Porta Final:");
           int portafim = scanner.nextInt();
		   msg = scanner.nextLine();
		   
		   for(int porta=portainic;porta<=portafim;porta++){
			    Socket s=null;
		        try{
				   	
		           InetSocketAddress endereco = new InetSocketAddress(IP, porta);
				   s = new Socket();  
                   s.connect(endereco,50);
				   
				   System.out.println("Porta "+porta+" aberta");
				}
				catch( Exception e ) {
                     
                }
				finally{
				   s.close();
				}
				
			}
			
			
		   
        }
		catch( IOException e ) {
            System.out.println( e );
        }
   
   }
}
