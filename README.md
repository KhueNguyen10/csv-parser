# csv-parser
This project will read a csv file containing insurance enrollment data and split it into separate insurance files.

## Requirements
The input csv file has to follow these rules:
1. The file should not contain a header row
2. There should be exactly 5 columns. Order from left to right: 
**User Id, Last Name, First Name, Version, Insurance Company**

## Instructions
* Compile the .java files in src/ and execute using the following command:
	java Main [pathToInputCSVFile.csv]
	
* The output files will be created in the output folder.