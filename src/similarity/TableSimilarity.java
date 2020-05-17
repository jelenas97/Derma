package similarity;

import java.util.ArrayList;
import java.util.List;

import ucm.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class TableSimilarity implements LocalSimilarityFunction {

	private double matrix[][];
	List<String> categories;
	
	public TableSimilarity(List<String> categories) {
		this.categories = categories;
		int n = categories.size();
		matrix = new double[n][n];
		for (int i=0; i<n; i++)
			matrix[i][i] = 1;  // symbolic similarity of term with itself
	}
	
	public void setSimilarity(String value1, String value2, double sim) {
		setSimilarity(value1, value2, sim, sim);
	}
	
	public void setSimilarity(String value1, String value2, double sim1, double sim2) {
		int index1 = categories.indexOf(value1);
		int index2 = categories.indexOf(value2);
		if (index1 != -1 && index2 != -1) {
			matrix[index1][index2] = sim1;
			matrix[index2][index1] = sim2;
		}
	}
	
	@Override
	public double compute(Object value1, Object value2) throws NoApplicableSimilarityFunctionException {
		int index1 = categories.indexOf(value1);
		int index2 = categories.indexOf(value2);
		if (index1 != -1 && index2 != -1)
			return matrix[index1][index2];
		if (value1 != null && value1.equals(value2))
			return 1;
		return 0;
	}

	@Override
	public boolean isApplicable(Object value1, Object value2) {
		if (value1 instanceof String && value2 instanceof String)
			return true;
		return false;
	}

	


	
	
}
