import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Bash {
	public static void main(String[] args) throws IOException {
		ProcessBuilder pb = new ProcessBuilder("./src/stream.sh", "./src/video1.mp4");
		Process p = pb.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		try{
			while ((line = reader.readLine()) != null)
			{
				System.out.println(line);
			}
		}
		catch(Exception e){
			
		}
		finally{
			p.destroy();//Comando para matar proceso
			System.out.println("Cerrado stream");
		}
	}
}
