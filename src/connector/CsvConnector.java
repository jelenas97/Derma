package connector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;

import model.CaseDescription;
import model.PatientDescription;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CaseBaseFilter;
import ucm.gaia.jcolibri.cbrcore.Connector;
import ucm.gaia.jcolibri.exception.InitializingException;
import ucm.gaia.jcolibri.util.FileIO;

public class CsvConnector implements Connector {
	
	@Override
	public Collection<CBRCase> retrieveAllCases() {
		LinkedList<CBRCase> cases = new LinkedList<CBRCase>();
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(FileIO.openFile("data/example.csv")));
			if (br == null)
				throw new Exception("Error opening file");

			String line = "";
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#") || (line.length() == 0))
					continue;
				String[] values = line.split(";");

				CBRCase cbrCase = new CBRCase();

				PatientDescription patientDescription = new PatientDescription();

				patientDescription.setFirstName(values[0]);
				patientDescription.setLastName(values[1]);
				patientDescription.setGender(values[2]);
				patientDescription.setAge(Integer.parseInt(values[3]));
				patientDescription.setDisease(values[4]);
				patientDescription.setMedication(values[5]);
				patientDescription.setSymptom(values[6]);

				cbrCase.setDescription(patientDescription);
				cases.add(cbrCase);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cases;
	}

	@Override
	public Collection<CBRCase> retrieveSomeCases(CaseBaseFilter arg0) {
		return null;
	}

	@Override
	public void storeCases(Collection<CBRCase> arg0) {
	}
	
	@Override
	public void close() {
	}

	@Override
	public void deleteCases(Collection<CBRCase> arg0) {
	}

	@Override
	public void initFromXMLfile(URL arg0) throws InitializingException {
	}

}
