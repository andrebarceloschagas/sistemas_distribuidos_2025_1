
import java.net.*;
import java.io.*;

import java.util.*;

class cliente4{
    public static void main( String args[] ) throws IOException {        
	    String msg;
        Socket s;
        DataInputStream sIn;

        try {          
         
           Scanner scanner = new Scanner(System.in);
            
           System.out.println("\nDigite o IP:");
           String IP = scanner.nextLine();
           
           System.out.println("\nDigite o Porta:");
           int porta = scanner.nextInt();
		   msg = scanner.nextLine();
		   
		   
           InetSocketAddress endereco = new InetSocketAddress(IP, porta);
		   s = new Socket();  
           s.connect(endereco,1000);
		   
		   while (true){
			 

		     System.out.println("\nDigite a Mensagem a ser Enviada:");
             msg = scanner.nextLine();
               			 
         
		     DataOutputStream s1out;
		     s1out = new DataOutputStream(s.getOutputStream());
		   
		     s1out.writeUTF(msg);

             sIn = new DataInputStream(s.getInputStream());
	
			 String msg2=sIn.readUTF();
			 System.out.println("\nMensagem recebida: "+msg2);    
    
             
			 
			 if(msg.compareToIgnoreCase("terminar")==0){
				 break;
			 }	 
			 
		   } //fim do while	 
           s.close();
        } 
        catch( IOException e ) {
            System.out.println( e );
        }
  }
}