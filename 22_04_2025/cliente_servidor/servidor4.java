import java.awt.*;
import java.net.*;
import java.io.*;

class servidor4 {
    public static void main( String args[] ) {
        ServerSocket s = (ServerSocket)null;
        Socket s1;                     

        try {
            s = new ServerSocket( 4321,300 );
        } catch( IOException e ) {
            System.out.println( e );
        }
		
		System.out.println("\nServidor Iniciado!!!");

        while( true ) {
            try {

                s1 = s.accept();
				servidorthread st;
				st=new servidorthread(s1);
				st.start();
				
				/*****
                s1out = new DataOutputStream(s1.getOutputStream());
				
				DataInputStream sIn;
				sIn = new DataInputStream(s1.getInputStream());
				String msg=sIn.readUTF();
				System.out.println("\nMensagem recebida do cliente: "+msg);


                s1out.writeUTF(cadena); 
				
				s1.close();

                if(msg.compareToIgnoreCase("terminar")==0){
				  break;
			    }				
                ****/   

                
            } catch( IOException e ) {
                System.out.println( e );
                }
        }//fim do while
    }
}
