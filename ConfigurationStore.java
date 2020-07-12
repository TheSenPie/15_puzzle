import java.io.*;
import java.net.*;
import java.util.*;

public class ConfigurationStore {
	private ArrayList < Configuration > configs = new ArrayList < > ();

	public ConfigurationStore(String source) throws IOException {
		if (source.startsWith("http://") || source.startsWith("https://"))
			loadFromURL(source);
		else
			loadFromDisk(source);
	}
	public ConfigurationStore(Reader source) throws IOException {
		load(source);
	}
	public ArrayList < Configuration > getConfigurationsSorted() {
		ArrayList < Configuration > copy = new ArrayList < Configuration > (configs);
		Collections.sort(copy);
		return copy;
	}
	private void load(Reader r) throws IOException {
		BufferedReader br = new BufferedReader(r);
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			try {
				configs.add(new Configuration(line));
			} catch (ConfigurationFormatException e) {
				System.out.println("Skipping malformed line:\n" + line);
			}
		}
	}
	private void loadFromURL(String url) throws IOException {
		URL destination = new URL(url);
		URLConnection conn = destination.openConnection();
		Reader r = new InputStreamReader(conn.getInputStream());
		load(r);
	}
	private void loadFromDisk(String filename) throws IOException {
		Reader r = new FileReader(filename);
		load(r);
	}
	public static void main(String[] args) throws IOException {
		ConfigurationStore c = new ConfigurationStore(args[0]);
	}
}