package Host;


import java.io.*;
import Host.Host;

public class Invoker {
	
	public static void main(String[] args) throws Exception {
		exec(Host.class);
		System.out.println("Invoked process terminated.");
	}
	
	
	 public static int exec(Class klass) throws IOException, InterruptedException {
		 
		String javaHome = System.getProperty("java.home");
		String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
		String classpath = System.getProperty("java.class.path");
		String className = klass.getCanonicalName();
		
		ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className);
		
		Process p = builder.start();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		String line;
		while ( (line = br.readLine()) != null) {
			   System.out.println("read line: "+line);
			}
		
		p.waitFor();
		return p.exitValue();
	}
	
	
}