package cbr;

import connector.AdditionalExamConnector;
import model.AdditionalExamDescription;

import ucm.gaia.jcolibri.casebase.LinealCaseBase;
import ucm.gaia.jcolibri.cbraplications.StandardCBRApplication;
import ucm.gaia.jcolibri.cbrcore.*;
import ucm.gaia.jcolibri.exception.ExecutionException;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import ucm.gaia.jcolibri.method.retrieve.RetrievalResult;
import ucm.gaia.jcolibri.method.retrieve.selection.SelectCases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AdditionalExamCbr implements StandardCBRApplication {
    Connector _connector;
    /**
     * Connector object
     */
    CBRCaseBase _caseBase;
    /**
     * CaseBase object
     */

    NNConfig simConfig;

    @Override
    public void configure() throws ExecutionException {
        _connector =  new AdditionalExamConnector();

        _caseBase = new LinealCaseBase();  // Create a Lineal case base for in-memory organization

        simConfig = new NNConfig(); // KNN configuration
        simConfig.setDescriptionSimFunction(new Average());  // global similarity function = average
        simConfig.addMapping(new Attribute("symptoms", AdditionalExamDescription.class), new SimilarityFunction("symptoms"));

    }

    @Override
    public CBRCaseBase preCycle() throws ExecutionException {
        _caseBase.init(_connector);
        java.util.Collection<CBRCase> cases = _caseBase.getCases();
        for (CBRCase c : cases)
            System.out.println(c.getDescription());
        return _caseBase;
    }

    @Override
    public void cycle(CBRQuery cbrQuery) throws ExecutionException {
        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), cbrQuery, simConfig);
        eval = SelectCases.selectTopKRR(eval, 5);
        System.out.println("Retrieved cases:");
        for (RetrievalResult nse : eval)
            System.out.println(nse.get_case().getDescription() + " -> " + nse.getEval());

    }

    @Override
    public void postCycle() throws ExecutionException {

    }

    public static void main(String[] args) {
        StandardCBRApplication recommender = new AdditionalExamCbr();
        try {
            recommender.configure();
            recommender.preCycle();

            CBRQuery query = new CBRQuery();
            AdditionalExamDescription additionalExamDescription = new AdditionalExamDescription();
            List<String> symptoms = new ArrayList<String>();
            symptoms.add("crvenilo");
            symptoms.add("svrab");
            additionalExamDescription.setSymptoms(symptoms);

            query.setDescription(additionalExamDescription);

            recommender.cycle(query);

            recommender.postCycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
