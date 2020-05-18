package cbr;

import connector.CsvConnector;
import model.PatientDescription;
import similarity.TableSimilarity;
import ucm.gaia.jcolibri.casebase.LinealCaseBase;
import ucm.gaia.jcolibri.cbraplications.StandardCBRApplication;
import ucm.gaia.jcolibri.cbrcore.*;
import ucm.gaia.jcolibri.exception.ExecutionException;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import ucm.gaia.jcolibri.method.retrieve.RetrievalResult;
import ucm.gaia.jcolibri.method.retrieve.selection.SelectCases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CbrApplication implements StandardCBRApplication {
	
	Connector _connector;  /** Connector object */
	CBRCaseBase _caseBase;  /** CaseBase object */

    NNConfig simConfig;

    public void cycle(CBRQuery query) throws ExecutionException {
        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
        eval = SelectCases.selectTopKRR(eval, 5);
        System.out.println("Retrieved cases:");
        for (RetrievalResult nse : eval)
            System.out.println(nse.get_case().getDescription() + " -> " + nse.getEval());
    }

    public static void main(String[] args) {
        StandardCBRApplication recommender = new CbrApplication();
        try {
            recommender.configure();

            recommender.preCycle();

            CBRQuery query = new CBRQuery();
            PatientDescription patientDescription = new PatientDescription();
//            patientDescription.setAge(33);
//            patientDescription.setGender("Male");
//
//            symptoms.add("papule");
//            symptoms.add("suga");

            //Mladji pacijenti -> veca sansa za pojavom akni
//            patientDescription.setAge(27);
//            patientDescription.setGender("Male");
//            List<String> symptoms = new ArrayList<String>();
//            symptoms.add("papule");
//            symptoms.add("plikovi");


            patientDescription.setAge(26);
            patientDescription.setGender("Male");
            patientDescription.setDisease("akne");
            List<String> medication = new ArrayList<String>();
            medication.add("prednizon");
            List<String> symptoms = new ArrayList<String>();
            symptoms.add("papule");

            patientDescription.setSymptom(symptoms);

            //Stariji pacijenti -> veca sansa za kontaktni dermatitis
//			patientDescription.setAge(50);
//			patientDescription.setGender("Male");
//			patientDescription.setSymptom("crvenilo");

            // TODO

            query.setDescription(patientDescription);

            recommender.cycle(query);

            recommender.postCycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CBRCaseBase preCycle() throws ExecutionException {
        _caseBase.init(_connector);
        java.util.Collection<CBRCase> cases = _caseBase.getCases();
        for (CBRCase c : cases)
            System.out.println(c.getDescription());
        return _caseBase;
    }

    /**
     * KNN configuration
     */

	public void configure() throws ExecutionException {
		_connector =  new CsvConnector();

		_caseBase = new LinealCaseBase();  // Create a Lineal case base for in-memory organization

		simConfig = new NNConfig(); // KNN configuration
		simConfig.setDescriptionSimFunction(new Average());  // global similarity function = average

        simConfig.addMapping(new Attribute("age", PatientDescription.class), new Interval(12));
        simConfig.addMapping(new Attribute("symptom", PatientDescription.class), new Equal());

        simConfig.addMapping(new Attribute("medication", PatientDescription.class), new SimilarityFunction("medication"));
        simConfig.addMapping(new Attribute("symptom", PatientDescription.class), new SimilarityFunction("symptom"));
        TableSimilarity diseaseSimilarity = new TableSimilarity((Arrays.asList("suga", "akne", "kontaktni_dermatitis")));
        diseaseSimilarity.setSimilarity("suga", "kontaktni_dermatitis", .5);
        diseaseSimilarity.setSimilarity("suga", "akne", .7);
        diseaseSimilarity.setSimilarity("akne", "kontaktni_dermatitis", .4);
        simConfig.addMapping(new Attribute("disease", PatientDescription.class), diseaseSimilarity);

        TableSimilarity genderSimilarity = new TableSimilarity((Arrays.asList("Male", "Female")));
        genderSimilarity.setSimilarity("Male", "Female", .8);
        simConfig.addMapping(new Attribute("gender", PatientDescription.class), genderSimilarity);

	}

    public void postCycle() throws ExecutionException {

    }
}





