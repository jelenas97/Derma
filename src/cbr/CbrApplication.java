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
//            symptoms.add("svrab");

            //Mladji pacijenti -> veca sansa za pojavom akni
//            patientDescription.setAge(27);
//            patientDescription.setGender("Male");
//            List<String> symptoms = new ArrayList<String>();
//            symptoms.add("papule");
//            symptoms.add("plikovi");


            patientDescription.setAge(26);
            patientDescription.setGender("Male");
            patientDescription.setDisease("acne_vulgaris");
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

		// simConfig.addMapping(new Attribute("attribute", CaseDescription.class), new Interval(5));

        simConfig.addMapping(new Attribute("age", PatientDescription.class), new Interval(12));
//		simConfig.addMapping(new Attribute("medication", PatientDescription.class), new MaxString());
        simConfig.addMapping(new Attribute("symptom", PatientDescription.class), new Equal());


		// Equal - returns 1 if both individuals are equal, otherwise returns 0
		// Interval - returns the similarity of two number inside an interval: sim(x,y) = 1-(|x-y|/interval)
		// Threshold - returns 1 if the difference between two numbers is less than a threshold, 0 in the other case
		// EqualsStringIgnoreCase - returns 1 if both String are the same despite case letters, 0 in the other case
		// MaxString - returns a similarity value depending of the biggest substring that belong to both strings
		// EnumDistance - returns the similarity of two enum values as the their distance: sim(x,y) = |ord(x) - ord(y)|
		// EnumCyclicDistance - computes the similarity between two enum values as their cyclic distance
		// Table - uses a table to obtain the similarity between two values. Allowed values are Strings or Enums. The table is read from a text file.
		// TableSimilarity(List<String> values).setSimilarity(value1,value2,similarity)

//        TableSimilarity medicationSimilarity = new TableSimilarity((Arrays.asList("hydroxyzine", "hydrocortisone", "eritromicin", "benadryl", "krotamiton_losion", "krotamiton_krema"
//                , "benzoil_eritromicin", "benzoil_peroksid", "benzoil_klimadicin")));
//        medicationSimilarity.setSimilarity("hydroxyzine", "hydrocortisone", .6);
//        medicationSimilarity.setSimilarity("hydroxyzine", "benadryl", .5);
//        medicationSimilarity.setSimilarity("hydrocortisone", "benadryl", .3);
//        medicationSimilarity.setSimilarity("krotamiton_krema", "krotamiton_losion", .7);
//        medicationSimilarity.setSimilarity("benzoil_eritromicin", "eritromicin", .7);
//        medicationSimilarity.setSimilarity("benzoil_peroksid", "benzoil_eritromicin", .4);
//        medicationSimilarity.setSimilarity("benzoil_peroksid", "benzoil_klimadicin", .6);
//        medicationSimilarity.setSimilarity("eritromicin", "benzoil_eritromicin", .7);
//        simConfig.addMapping(new Attribute("medication", PatientDescription.class), medicationSimilarity);

        simConfig.addMapping(new Attribute("medication", PatientDescription.class), new SimilarityFunction("medication"));
        simConfig.addMapping(new Attribute("symptom", PatientDescription.class), new SimilarityFunction("symptom"));
        TableSimilarity diseaseSimilarity = new TableSimilarity((Arrays.asList("svrab", "acne_vulgaris", "kontaktni_dermatitis")));
        diseaseSimilarity.setSimilarity("svrab", "kontaktni_dermatitis", .5);
        diseaseSimilarity.setSimilarity("svrab", "acne_vulgaris", .7);
        diseaseSimilarity.setSimilarity("acne_vulgaris", "kontaktni_dermatitis", .4);
        simConfig.addMapping(new Attribute("disease", PatientDescription.class), diseaseSimilarity);

//        TableSimilarity symptomSimilarity = new TableSimilarity((Arrays.asList("crvenilo", "plikovi", "crni_mitiseri", "beli_mitiseri"
//                , "cisticne_akne", "pristici", "osip", "perutanje", "isusena_koza")));
//        symptomSimilarity.setSimilarity("crvenilo", "plikovi", .5);
//        symptomSimilarity.setSimilarity("crni_mitiseri", "beli_mitiseri", .6);
//        symptomSimilarity.setSimilarity("crvenilo", "cisticne_akne", .3);
//        symptomSimilarity.setSimilarity("crvenilo", "pristici", .5);
//        symptomSimilarity.setSimilarity("crvenilo", "osip", .8);
//        symptomSimilarity.setSimilarity("pristici", "osip", .5);
//        symptomSimilarity.setSimilarity("perutanje", "isusena_koza", .6);
//        simConfig.addMapping(new Attribute("symptom", PatientDescription.class), symptomSimilarity);
        TableSimilarity genderSimilarity = new TableSimilarity((Arrays.asList("Male", "Female")));
        genderSimilarity.setSimilarity("Male", "Female", .8);
        simConfig.addMapping(new Attribute("gender", PatientDescription.class), genderSimilarity);

	}

    public void postCycle() throws ExecutionException {

    }
}





