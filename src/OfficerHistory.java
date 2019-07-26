public class OfficerHistory {
    String crime;
    String severity;
    String date;
    String description;
    String arrestedPerson;
    String address;
    String city;
    String state;

    public OfficerHistory(String crime, String severity, String date, String description, String arrestedPerson, String address, String city, String state) {
        this.crime = crime;
        this.date = date;
        this.description = description;
        this.arrestedPerson = arrestedPerson;
        this.severity = severity;
        this.address = address;
        this.city = city;
        this.state = state;
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

    public String getSeverity() {
        return this.severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getArrestedPerson() {
        return this.arrestedPerson;
    }

    public void setArrestedPerson(String arrestedPerson) {
        this.arrestedPerson = arrestedPerson;
    }

    public String getCrime() {
        return this.crime;
    }

    public void setCrime(String crime) {
        this.crime = crime;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}