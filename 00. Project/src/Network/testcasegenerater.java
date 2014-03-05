package Network;

public class testcasegenerater {
	
	public static void main(String args[]){
		
		System.out.println("test case for hostMH");	
		for(int i=1; i<=20; i++){
			System.out.println(i+" sendtoclient"+i);
		}
		System.out.println("0 sendtoclientall");
		
		System.out.println();
		
		System.out.println("test case for clientMH");
		for(int i=1; i<=10; i++){
			System.out.println("Action"+i);
		}
	}
}

