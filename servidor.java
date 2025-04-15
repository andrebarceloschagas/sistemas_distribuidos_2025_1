import java.awt.*;
import java.net.*;
import java.io.*;

class servidor2 {
    public static void main( String args[] ) {
        ServerSocket s = (ServerSocket)null;
        Socket s1;
        String cadena = "Mensagem do Servidor - Tutorial de Java!";
        int longCad;
        DataOutputStream s1out;

        try {
            s = new ServerSocket( 4321,300 );
        } catch( IOException e ) {
            System.out.println( e );
        }
		
		
        System.out.println( "Servidor Rodando!!!" );
        while( true ) {
            try {

                s1 = s.accept();
                s1out = new DataOutputStream(s1.getOutputStream());


                s1out.writeUTF(cadena);    


                s1.close();
            } catch( IOException e ) {
                System.out.println( e );
                }
            }
        }
    }
    