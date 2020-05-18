package model;

import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;

import java.util.List;

public class PatientDescription implements CaseComponent {
    //private String firstName;
    //private String lastName;
    private String gender;
    private int age;
    private String disease;
    private List<String> medication;
    private List<String> symptom;

    public PatientDescription() {
    }
/*
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
*/
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }


    public List<String> getSymptom() {
        return symptom;
    }

    public void setSymptom(List<String> symptom) {
        this.symptom = symptom;
    }

    @Override
    public String toString() {
        return "PatientDescription{" +
                "gender='" + gender + '\'' +
                ", age=" + age +
                ", disease='" + disease + '\'' +
                ", medication=" + medication +
                ", symptom=" + symptom +
                '}';
    }

    public List<String> getMedication() {
        return medication;
    }

    public void setMedication(List<String> medication) {
        this.medication = medication;
    }

    @Override
    public Attribute getIdAttribute() {
        return null;
    }
}
