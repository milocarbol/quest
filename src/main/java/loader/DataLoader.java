package loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import data.Files;

/**
 * Loads data from a .DATA file.
 * @author Milo Carbol
 * @since 13 April 2013
 */
public class DataLoader {
	
	/** The deliminator between name and value in each line **/
	private static String deliminator = ":";
	
	/** The file type **/
	private static String fileType = "data";
	
	/** The data map **/
	protected Map<String, Map<String, String>> data;
	
	public DataLoader(String dataFile) {
		try {
			data = load(dataFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Clears the data map to free up memory.
	 */
	public void release() {
		data = null;
	}
	
	/**
	 * Creates a map of names to details from a data file.
	 * @param dataFile - The file
	 * @return A map of names to details
	 * @throws IOException if the file can't be opened or read from
	 */
	private Map<String, Map<String, String>> load(String dataFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(Files.DATA + dataFile + "." + fileType));
		
		Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
		
		String name = null;
		String line;
		while ((line = reader.readLine()) != null) {
			String[] lineData = line.split(deliminator);
			if (lineData[0].equals("name")) {
				name = lineData[1];
				data.put(name, new HashMap<String, String>());
			}
			else if (name != null) {
				data.get(name).put(lineData[0], lineData[1]);
			}
			else if (name == null) {
				reader.close();
				throw new IOException("Parse error: " + dataFile + " should begin with \"name:\"");
			}
		}
		
		reader.close();
		
		return data;
	}
}
