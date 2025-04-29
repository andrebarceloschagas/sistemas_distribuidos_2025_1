import java.net.*;
import java.io.*;

public class servidorthread extends Thread{
	public Socket s1=null;
	public servidorthread(Socket s1){
		super();
		this.s1=s1;
	}
	
	public void run(){
		String cadena = "Confirmação de Mensagem Recebida no Servidor";
		DataOutputStream s1out;
		DataInputStream sIn;
		try{
			s1out = new DataOutputStream(s1.getOutputStream());
			sIn = new DataInputStream(s1.getInputStream());
			
			while(true){
		        
				String msg=sIn.readUTF();
				System.out.println("\nMensagem recebida do cliente: "+msg);


                s1out.writeUTF(cadena); 
				
				//s1.close();

                if(msg.compareToIgnoreCase("terminar")==0){
				  break;
			    }
			}//fim do while
			s1.close();
		}
		catch(Exception e){
			System.out.println("Erro="+e.getMessage());
		}
		
	}
}