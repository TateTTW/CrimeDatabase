
/*
 * Decompiled with CFR 0_132.
 */
public class CrimeHistory {
    String crime;
    String severity;
    String date;
    String officer;
    String address;
    String city;
    String state;
    String description;

    public CrimeHistory(String crime, String severity, String date, String officer, String description, String address, String city, String state) {
        this.crime = crime;
        this.severity = severity;
        this.date = date;
        this.officer = officer;
        this.address = address;
        this.city = city;
        this.state = state;
        this.description = description;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCrime() {
        return this.crime;
    }

    public void setCrime(String crime) {
        this.crime = crime;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return this.severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOfficer() {
        return this.officer;
    }

    public void setOfficer(String officer) {
        this.officer = officer;
    }
}