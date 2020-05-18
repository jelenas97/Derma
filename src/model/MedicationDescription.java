package model;

import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;

import java.util.List;

public class MedicationDescription implements CaseComponent {
    private String disease;
    private List<String> medication;

    @Override
    public Attribute getIdAttribute() {
        return null;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public List<String> getMedication() {
        return medication;
    }

    public void setMedication(List<String> medication) {
        this.medication = medication;
    }

    @Override
    public String toString() {
        return "MedicationDescription{" +
                "disease='" + disease + '\'' +
                ", medication='" + medication + '\'' +
                '}';
    }
}
