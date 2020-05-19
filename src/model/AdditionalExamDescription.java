package model;

import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;

import java.util.List;

public class AdditionalExamDescription implements CaseComponent {
    private List<String> additionalExam;
    private List<String> symptoms;

    public List<String> getAdditionalExam() {
        return additionalExam;
    }

    public void setAdditionalExam(List<String> additionalExam) {
        this.additionalExam = additionalExam;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<String> symptoms) {
        this.symptoms = symptoms;
    }

    @Override
    public String toString() {
        return "AdditionalExamDescription{" +
                "additionalExam=" + additionalExam +
                ", symptoms=" + symptoms +
                '}';
    }

    @Override
    public Attribute getIdAttribute() {
        return null;
    }
}
