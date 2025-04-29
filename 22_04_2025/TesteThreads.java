public class TesteThreads extends Thread{
	int n;
	public TesteThreads(String nome, int n){
		super(nome);
		this.n=n;
	} //fim do contrutor
	public void run(){
		for(int i=0;i<n;i++){
			System.out.println(getName()+" na etapa "+i);
			try{
				sleep((int)Math.random()*100000);
			}catch(Exception e){}
		}
		System.out.println("Thread terminada: "+getName());
	}//fim do método run	
}//fim da classe