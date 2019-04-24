
public class Enrollee {
	
	private String userId;
	private String firstName;
	private String lastName;
	private int version;
	private String insuranceCompany;
	
	public Enrollee(String userId, String lastName, String firstName,
			int version, String insuranceCompany) {
		
		this.userId = userId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.version = version;
		this.insuranceCompany = insuranceCompany;
		
	}
	
	public Enrollee(String[] enrolleeData) {
		
		this(enrolleeData[0], 
				enrolleeData[1], 
				enrolleeData[2], 
				Integer.parseInt(enrolleeData[3]), 
				enrolleeData[4]);
		
	}
	
	public String getUserId() {
		return userId;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public int getVersion() {
		return version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	public String getInsuranceCompany() {
		return insuranceCompany;
	}
	
	public String toCSVString() {
		return userId + ", " + lastName + ", " + firstName + ", " 
				+ version + ", " + insuranceCompany;
	}
	
}
