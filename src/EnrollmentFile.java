import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class applies the Enrollment schema to a csv file.
 * 
 * @author nkhue
 *
 */
public class EnrollmentFile {
	
	public enum Column {
		USER_ID, LAST_NAME, FIRST_NAME, VERSION, INSURANCE_COMPANY
	}
	
	private CSVFile csv; // The csv file to read from
	
	public EnrollmentFile(CSVFile csv) {
		this.csv = csv;
	}
	
	/**
	 * Return all enrollees from csv file that matches given criterias
	 * 
	 * @param column - column to match to
	 * @param columnValue - value to match to the given column
	 * @param sortBy - sort criterias
	 * @param sortDirection - ascending or descending
	 * @return list of enrollees
	 */
	public List<Enrollee> getEnrolleesBy(Column column, String columnValue) {
		
		List<Enrollee> enrollees = new ArrayList<>();
		int columnIndex = column.ordinal(); // Use the ordinal of Column enum as column index
		
		// Retrieve data from csv file that matches given criteria
		List<String[]> records = csv.getFilteredRecords(columnIndex, columnValue);
		
		for (String[] record : records) {
			
			Enrollee newEnrollee = new Enrollee(record);
			int duplicateIndex; // Location of duplicate enrollee
			
			if ((duplicateIndex = findDuplicate(newEnrollee, enrollees)) >= 0) {
				
				// New enrollee already exist, replace the duplicate if it has lower version
				if (newEnrollee.getVersion() > enrollees.get(duplicateIndex).getVersion()) {
					enrollees.set(duplicateIndex, newEnrollee);
				}
				
			} else { // No duplicate found, add new enrollee
				enrollees.add(newEnrollee);
			}
			
		}
		
		// Sort by last name, then first name, in ascending order
		enrollees.sort((Enrollee e1, Enrollee e2) -> {
			int lastNameComparison = e1.getLastName().compareTo(e2.getLastName());
			if (lastNameComparison == 0) {
				return e1.getFirstName().compareTo(e2.getFirstName());
			} else {
				return lastNameComparison;
			}
		});
		
		return enrollees;
		
	}
	
	/**
	 * Find the duplicate in a list of enrollees.
	 * Two enrollees are considered duplicates if they have the same
	 * User ID and Insurance Company.
	 * 
	 * @param targetEnrollee - the enrollee to check for duplicate
	 * @param enrollees - the list to check for duplicate
	 * @return the index location of duplicate or -1 if no duplicate found
	 */
	private int findDuplicate(Enrollee targetEnrollee, List<Enrollee> enrollees) {
		
		int duplicateIndex = -1;
		
		for (int i = 0; i < enrollees.size(); i++) {
			if (targetEnrollee.getUserId().equals(enrollees.get(i).getUserId()) &&
					targetEnrollee.getInsuranceCompany().equals(enrollees.get(i).getInsuranceCompany())) {
				duplicateIndex = i;
				break; // There can only be one duplicate, break once found
			}
		}
		
		return duplicateIndex;
		
	}
	
	/**
	 * Return all distinct values for a given column is the csv file.
	 * 
	 * @param column - the column to get values from
	 * @return a set of values with no duplicates
	 */
	public Set<String> getDistinctValues(Column column) {
		return csv.getDistinctValues(column.ordinal());
	}
	
}
