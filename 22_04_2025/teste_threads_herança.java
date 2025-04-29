public class TesteThreads extends Thread{
    public TesteThreads(String nome){
    super(nome); // chama o método contrutor da classe pai
    (no caso a classe Thread)
    }
    public void run(){
    for(int i=0;i<5;i++){
    System.out.println(getName()+" na etapa"+i);
    try{
    sleep((int)(Math.random()*2000));
    }catch(Exception e){}
    }
    System.out.println("Thread terminada: "+getName());
    }
    }
    public class CorridaTreads {
    public static void main (String args[]){
    TesteThreads a,b;
    a = new TesteThreads("A1");
    a.start();
    b = new TesteThreads("B1");
    b.start();
    try{a.join();}catch(Exception e){}
    try{b.join();}catch(Exception e){}
    }
    }