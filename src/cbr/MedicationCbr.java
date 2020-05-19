package cbr;

import connector.MedicationConnector;
import model.MedicationDescription;
import similarity.TableSimilarity;
import ucm.gaia.jcolibri.casebase.LinealCaseBase;
import ucm.gaia.jcolibri.cbraplications.StandardCBRApplication;
import ucm.gaia.jcolibri.cbrcore.*;
import ucm.gaia.jcolibri.exception.ExecutionException;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import ucm.gaia.jcolibri.method.retrieve.RetrievalResult;
import ucm.gaia.jcolibri.method.retrieve.selection.SelectCases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MedicationCbr implements StandardCBRApplication {

    Connector _connector;
    /**
     * Connector object
     */
    CBRCaseBase _caseBase;
    /**
     * CaseBase object
     */

    NNConfig simConfig;

    public static void main(String[] args) {
        StandardCBRApplication recommender = new MedicationCbr();
        try {
            recommender.configure();
            recommender.preCycle();

            CBRQuery query = new CBRQuery();
            MedicationDescription medicationDescription = new MedicationDescription();
            //Situacija kada na osnovu bolesti i lijekova koji se vec koriste  dobijamo informacije koji su jos lijekovi dobri
            //hydrocortisone -> benadryl (0.4), hydrocortisone->hydroxyzine (0.6)
            List<String> medications = new ArrayList<String>();
//            medications.add("hydrocortisone");
//            medications.add("benzocaine");
//            medicationDescription.setMedication(medications);
//            medicationDescription.setDisease("suga");
            query.setDescription(medicationDescription);

            recommender.cycle(query);

            recommender.postCycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cycle(CBRQuery query) throws ExecutionException {
        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
        eval = SelectCases.selectTopKRR(eval, 6);
        System.out.println("Retrieved cases:");
        for (RetrievalResult nse : eval)
            System.out.println(nse.get_case().getDescription() + " -> " + nse.getEval());
    }

    /**
     * KNN configuration
     */

    public void configure() throws ExecutionException {
        _connector = new MedicationConnector();

        _caseBase = new LinealCaseBase();  // Create a Lineal case base for in-memory organization

        simConfig = new NNConfig(); // KNN configuration
        simConfig.setDescriptionSimFunction(new Average());  // global similarity function = average
        simConfig.addMapping(new Attribute("medication", MedicationDescription.class), new SimilarityFunction("medication"));

        TableSimilarity diseaseSimilarity = new TableSimilarity((Arrays.asList("suga", "akne", "kontaktni_dermatitis")));
        diseaseSimilarity.setSimilarity("suga", "kontaktni_dermatitis", .5);
        diseaseSimilarity.setSimilarity("suga", "akne", .3);
        diseaseSimilarity.setSimilarity("akne", "kontaktni_dermatitis", .4);
        simConfig.addMapping(new Attribute("disease", MedicationDescription.class), diseaseSimilarity);

    }

    public CBRCaseBase preCycle() throws ExecutionException {
        _caseBase.init(_connector);
        java.util.Collection<CBRCase> cases = _caseBase.getCases();
        for (CBRCase c : cases)
            System.out.println(c.getDescription());
        return _caseBase;
    }

    public void postCycle() throws ExecutionException {

    }
}





