import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * This class applies the Enrollment schema to a csv file.
 * 
 * @author nkhue
 *
 */
public class EnrollmentData {
	
	public enum Column {
		USER_ID, LAST_NAME, FIRST_NAME, VERSION, INSURANCE_COMPANY
	}
	
	public enum SortDirection {
		ASCENDING, DESCENDING
	}
	
	private CSVFile csv; // The csv file to read from
	
	public EnrollmentData(CSVFile csv) {
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
	public List<Enrollee> getEnrolleesBy(
			Column column, String columnValue, Column[] sortBy, SortDirection sortDirection) {
		
		List<Enrollee> enrollees = new ArrayList<>();
		int columnIndex = column.ordinal(); // Use the ordinal of Column enum as column index
		
		// Retrieve data from csv file that matches given criteria
		List<String[]> records = csv.getFilteredRecords(columnIndex, columnValue);
		
		for (String[] record : records) {
			
			Enrollee newEnrollee = new Enrollee(record);
			int existingIndex; // Location of existing enrollee
			
			if ((existingIndex = findExisting(newEnrollee, enrollees)) >= 0) {
				
				// New enrollee already exist, replace the existing enrollee if it has lower version
				if (newEnrollee.getVersion() > enrollees.get(existingIndex).getVersion()) {
					enrollees.set(existingIndex, newEnrollee);
				}
				
			} else { // No existing found, add new enrollee
				enrollees.add(newEnrollee);
			}
			
		}
		
		// Implement the comparator to be used for sorting the enrollees
		Comparator<Enrollee> comparator = (Enrollee e1, Enrollee e2) -> {
			
			int sortByIndex = 0;
			int comparison = 0;
			
			do {
				comparison = compare(sortBy[sortByIndex++], sortDirection, e1, e2);
			} while (comparison == 0 && sortByIndex < sortBy.length);
			
			return comparison;
			
		};
		
		enrollees.sort(comparator);
		
		return enrollees;
		
	}
	
	/**
	 * Return the comparison value between two enrollees based on a specific column and sort direction.
	 * The comparison value, for ascending order, is positive integer if first enrollee is 'greater' 
	 * than second enrollee, negative integer if second enrollee is 'less' than first enrollee, 
	 * and 0 if they are 'equal'. For descending order, the opposite rules are applied.
	 * 
	 * @param sortBy - the column to be used for comparison
	 * @param sortDirection - the direction used for sorting
	 * @param e1 - first enrollee
	 * @param e2 - second enrollee
	 * @return the comparison value
	 */
	private int compare(Column sortBy, SortDirection sortDirection, Enrollee e1, Enrollee e2) {
		
		int comparison;
		
		switch (sortBy) {
			case USER_ID:
				comparison = e1.getUserId().compareTo(e2.getUserId());
				break;
			case LAST_NAME:
				comparison =  e1.getLastName().compareTo(e2.getLastName());
				break;
			case FIRST_NAME:
				comparison =  e1.getFirstName().compareTo(e2.getFirstName());
				break;
			case VERSION:
				comparison = e1.getVersion() - e2.getVersion();
				break;
			case INSURANCE_COMPANY:
				comparison = e1.getInsuranceCompany().compareTo(e2.getInsuranceCompany());
				break;
			default:
				comparison = 0;
				break;
		}
		
		if (sortDirection == SortDirection.ASCENDING) {
			return comparison;
		} else {
			return -comparison;
		}
		
	}
	
	/**
	 * Find the existing enrollee in a list of enrollees.
	 * Two enrollees are considered duplicates if they have the same
	 * User ID and Insurance Company.
	 * 
	 * @param targetEnrollee - the enrollee to check for duplicate
	 * @param enrollees - the list to check for duplicate
	 * @return the index location of duplicate or -1 if no duplicate found
	 */
	private int findExisting(Enrollee targetEnrollee, List<Enrollee> enrollees) {
		
		int existingIndex = -1;
		
		for (int i = 0; i < enrollees.size(); i++) {
			if (targetEnrollee.getUserId().equals(enrollees.get(i).getUserId()) &&
					targetEnrollee.getInsuranceCompany().equals(enrollees.get(i).getInsuranceCompany())) {
				existingIndex = i;
				break; // There can only be one existing, break once found
			}
		}
		
		return existingIndex;
		
	}
	
	/**
	 * Return all distinct values for a given column in the csv file.
	 * 
	 * @param column - the column to get values from
	 * @return a set of values with no duplicates
	 */
	public Set<String> getDistinctValues(Column column) {
		return csv.getDistinctValues(column.ordinal());
	}
	
}
