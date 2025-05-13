import java.net.*;
import java.io.*;

class servidor5 {
    public static void main( String args[] ) {
        ServerSocket s = (ServerSocket)null;
        Socket s1;
        
        ObjectInputStream entrada;

        try {
            s = new ServerSocket( 4321,300 );
        } catch( IOException e ) {
            System.out.println( e );
        }

        while( true ) {
            try {

                s1 = s.accept();
				
				
                entrada = new ObjectInputStream (s1.getInputStream());

                teste alo = (teste) entrada.readObject();
				
				alo.escreve();    


                s1.close();
            } catch( Exception e ) {
                System.out.println( e );
                }
            }
        }
    }
