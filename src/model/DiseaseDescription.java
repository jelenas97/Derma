package model;

import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;

import java.util.List;

public class DiseaseDescription implements CaseComponent {

    private String disease;
    private List<String> symptoms;

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<String> symptoms) {
        this.symptoms = symptoms;
    }

    @Override
    public String toString() {
        return "DiseaseDescription{" +
                "disease='" + disease + '\'' +
                ", symptom=" + symptoms +
                '}';
    }

    @Override
    public Attribute getIdAttribute() {
        return null;
    }
}
