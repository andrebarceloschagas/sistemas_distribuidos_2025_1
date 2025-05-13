import java.net.*;
import java.io.*;

class cliente5{
    public static void main( String args[] ) throws IOException {

        Socket s;
        ObjectOutputStream saida;

        try {
            
           s = new Socket();
         
           
           InetSocketAddress endereco = new InetSocketAddress("10.90.8.175", 4321);
           s.connect(endereco,1000);  
            
           saida = new ObjectOutputStream(s.getOutputStream());
		   
		   teste alo=new teste("mensagem de teste a ser enviada");
		   
		   saida.writeObject(alo);
		   
		   saida.flush();
    
           s.close();

        } 
        catch( Exception e ) {
            System.out.println( e );
        }
  }
}