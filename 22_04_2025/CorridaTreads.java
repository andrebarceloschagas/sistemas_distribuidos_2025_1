import java.util.*;

public class CorridaTreads{
	public static void main(String args[]){
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nDigite o Número de Iteraçõe:");
        int n = scanner.nextInt();
		
		TesteThreads a,b;
		a=new TesteThreads("A1",n);
		a.start();
		b=new TesteThreads("B1",n);
		b.start();
		try{a.join();}catch(Exception e){}
		try{b.join();}catch(Exception e){}
		System.out.println("Thread Pai Terminada!!!");
	}
}