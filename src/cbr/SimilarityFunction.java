package cbr;

import ucm.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimilarityFunction implements LocalSimilarityFunction {
    private HashMap<String, Double> hashMap;

    public SimilarityFunction(String key) {
        if (key.equals("medication")) {
            this.medicationTable();
        } else if (key.equals("symptoms")) {
            this.symptomsTable();
        } else {
            this.hashMap = new HashMap<>();
        }
    }

    @Override
    public double compute(Object list1, Object list2) throws NoApplicableSimilarityFunctionException {
        try {
            if ((list1 == null) || (list2 == null))
                return 0;
            if (!(list1 instanceof List))
                throw new NoApplicableSimilarityFunctionException(this.getClass(), list1.getClass());
            if (!(list2 instanceof List))
                throw new NoApplicableSimilarityFunctionException(this.getClass(), list2.getClass());

            List list = (List) list1;
            List input = (List) list2;
            List first = new ArrayList();
            List second = new ArrayList();
            if (list.size() >= input.size()) {
                first = list;
                second = input;
            } else {
                first = input;
                second = list;
            }
            double res = 0.0;

            for (Object s : first) {
                if (second.contains(s)) {
                    res += 1;
                } else {
                    List<Double> doubles = new ArrayList<>();
                    for (Object s2 : second) {
                        String key = s.toString() + '-' + s2.toString();
                        if (this.hashMap.containsKey(key)) {
                            doubles.add(this.hashMap.get(key));
                        }
                    }
                    double foundValue = 0;
                    for (Double v : doubles) {
                        if (v > foundValue)
                            foundValue = v;
                    }
                    res += foundValue;
                }
            }
            res = (res / first.size()) * 1.6;
            return res;
        } catch (Exception e) {
            return 0;
        }
    }

    public void medicationTable() {
        this.hashMap = new HashMap<>();
        this.hashMap.put("hydroxyzine-hydrocortisone", 0.6);
        this.hashMap.put("hydrocortisone-hydroxyzine", 0.6);
        this.hashMap.put("hydroxyzine-benadryl", 0.5);
        this.hashMap.put("benadryl-hydroxyzine", 0.5);
        this.hashMap.put("hydrocortisone-benadryl", 0.3);
        this.hashMap.put("benadryl-hydrocortisone", 0.3);
        this.hashMap.put("krotamiton_krema-krotamiton_losion", 0.7);
        this.hashMap.put("krotamiton_losion-krotamiton_krema", 0.7);
        this.hashMap.put("benzoil_peroksid-benzoil_eritromicin", 0.4);
        this.hashMap.put("benzoil_eritromicin-benzoil_peroksid", 0.4);
        this.hashMap.put("benzoil_peroksid-benzoil_klimadicin", 0.6);
        this.hashMap.put("benzoil_klimadicin-benzoil_peroksid", 0.6);
        this.hashMap.put("eritromicin-benzoil_eritromicin", 0.7);
        this.hashMap.put("benzoil_eritromicin-eritromicin", 0.7);
    }

    public void symptomsTable() {
        this.hashMap = new HashMap<>();
        this.hashMap.put("crvenilo-plikovi", 0.5);
        this.hashMap.put("plikovi-crvenilo", 0.5);
        this.hashMap.put("crni_mitiseri-beli_mitiseri", 0.6);
        this.hashMap.put("beli_mitiseri-crni_mitiseri", 0.6);
        this.hashMap.put("crvenilo-cisticne_akne", 0.3);
        this.hashMap.put("cisticne_akne-crvenilo", 0.3);
        this.hashMap.put("crvenilo-pristici", 0.5);
        this.hashMap.put("pristici-crvenilo", 0.5);
        this.hashMap.put("crvenilo-osip", 0.8);
        this.hashMap.put("osip-crvenilo", 0.8);
        this.hashMap.put("pristici-osip", 0.5);
        this.hashMap.put("osip-pristici", 0.5);
        this.hashMap.put("perutanje-isusena_koza", 0.6);
        this.hashMap.put("isusena_koza-perutanje", 0.6);

    }

    @Override
    public boolean isApplicable(Object o, Object o1) {
        return true;
    }
}
