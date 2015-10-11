import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Bash {
	public static void main(String args[]) {
		try{
            // for tilda expansion
            //if (filepath.startsWith("~" + File.separator)) {
                //filepath = System.getProperty("user.home") + filepath.substring(1);
            //}

            //ProcessBuilder builder = new ProcessBuilder("python", "-c", "import sys; import nltk; print \"whatever\"");
            ProcessBuilder builder = new ProcessBuilder("/usr/local/bin/python", "src/ServerUDP.py", "239.255.0.1", "9001", "src/video1.mp4");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            InputStream stdout = p.getInputStream();
            BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));

            String line;
            while ((line = reader.readLine ()) != null) {
                System.out.println ("Stdout: " + line);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
	}
}
