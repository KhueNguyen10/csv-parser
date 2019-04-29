import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Main {
	
	private static final String OUTPUT_DIR = "/output/";

	public static void main(String[] args) {
		
		String inputFilePath = args[0];
		CSVFile csv = new CSVFile(inputFilePath);
		EnrollmentData data = new EnrollmentData(csv);
		
		Set<String> insuranceCompanies = 
				data.getDistinctValues(EnrollmentData.Column.INSURANCE_COMPANY);
		
		EnrollmentData.Column[] sortBy = new EnrollmentData.Column[] {
				EnrollmentData.Column.LAST_NAME,
				EnrollmentData.Column.FIRST_NAME
		};
		
		for (String insuranceCompany : insuranceCompanies) {
			String fileName = insuranceCompany + ".csv"; // csv file to write to
			
			List<Enrollee> enrollees = data.getEnrolleesBy(EnrollmentData.Column.INSURANCE_COMPANY, 
					insuranceCompany, sortBy, EnrollmentData.SortDirection.ASCENDING);
			
			writeEnrolleesToFile(fileName, enrollees);
		}
		
	}
	
	public static void writeEnrolleesToFile(String fileName, List<Enrollee> enrollees) {
		
		String newFilePath = System.getProperty("user.dir") + OUTPUT_DIR + fileName;
		File csvFile = new File(newFilePath);
		csvFile.getParentFile().mkdirs(); // Create parent directories
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
			
			// Write each enrollee on a line in csv format
			for (Enrollee enrollee : enrollees) {
				writer.write(enrollee.toCSVString());
				writer.newLine();
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
}
