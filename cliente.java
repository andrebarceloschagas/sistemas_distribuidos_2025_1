import java.awt.*;
import java.net.*;
import java.io.*;

class cliente2{
    public static void main( String args[] ) throws IOException {
        int c;
	String msg;
        Socket s;
        DataInputStream sIn;

        try {
            /*
            s = new Socket(InetAddress.getByName("127.0.0.1"),4321 ); //pelo numero IP
           */
           s = new Socket();
         
           
           InetSocketAddress endereco = new InetSocketAddress("localhost", 4321);
           s.connect(endereco,1000);  
            //s = new Socket("localhost",4321 );  pelo nome
         


        sIn = new DataInputStream(s.getInputStream());
	
	/*
        while( ( c = sIn.read() ) != -1 )
            System.out.print( (char)c );
	*/
	
	msg=sIn.readUTF();
	System.out.println("\nMensagem recebida: "+msg);    
    
        s.close();

        } 
        catch( IOException e ) {
            System.out.println( e );
        }
  }
}
