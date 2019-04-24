import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class provide read access to an existing csv file.
 * 
 * @author nkhue
 *
 */
public class CSVFile {
	
	private List<String[]> content;
	
	public CSVFile(String filePath) {
		content = parseToList(filePath);
	}
	
	/**
	 * Read the input file and parse it as csv format.
	 * 
	 * @param filePath
	 * @return data as a List
	 */
	private List<String[]> parseToList(String filePath) {
		
		File csv = new File(filePath);
		List<String[]> content = new ArrayList<>();
		String[] values;
		
		try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
			String line;
			while ((line = br.readLine()) != null) {
				values = line.split(",");
				trimValues(values);
				content.add(values);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return content;
		
	}
	
	private void trimValues(String[] values) {
		for (int i = 0; i < values.length; i++) {
			values[i] = values[i].trim();
		}
	}
	
	public int getRowCount() {
		return content.size();
	}
	
	public String[] getRow(int rowIndex) {
		return content.get(rowIndex);
	}
	
	public int getColumnCount(int rowIndex) {
		return content.get(rowIndex).length;
	}
	
	/**
	 * Return all records matching given criteria
	 * 
	 * @param colIndex - the column index to match
	 * @param colValue - the column value to match
	 * @return records as List of String arrays
	 */
	public List<String[]> getFilteredRecords(int colIndex, String colValue) {
		
		List<String[]> filteredData = new ArrayList<>();
		
		for (String[] currentRow : content) {
			if (currentRow[colIndex].equals(colValue)) {
				filteredData.add(currentRow);
			}
		}
		
		return filteredData;
		
	}
	
	/**
	 * Return all distinct records for a certain column
	 * 
	 * @param colIndex
	 * @return all values as a Set
	 */
	public Set<String> getDistinctValues(int colIndex) {
		
		Set<String> values = new HashSet<>();
		
		for (String[] currentRow : content) {
			values.add(currentRow[colIndex]);
		}
		
		return values;
		
	}
	
}
