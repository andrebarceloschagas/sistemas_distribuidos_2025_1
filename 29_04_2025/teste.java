import java.io.*;

class teste implements Serializable{
  String texto;
  public teste(String t){
    texto=t;
  }
  public void escreve(){
    System.out.println("\n "+texto);
  }
}


