public class OfficerRecord {
    int id;
    String rank = "";
    String firstName = "";
    String lastName = "";
    String eyes = "";
    String hair = "";
    String birthDate = "";
    String address = "";
    String state = "";
    String sex = "";
    String phone = "";
    String city = "";
    String height;
    String weight;
    String hireDate = "";
    String zip;

    public OfficerRecord(int id, String rank, String firstName, String lastName, String address, String city, String state, String zip, String phone, String height, String weight, String eyes, String hair, String sex, String birthDate, String hireDate) {
        this.rank = rank;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.eyes = eyes;
        this.hair = hair;
        this.birthDate = birthDate;
        this.address = address;
        this.state = state;
        this.sex = sex;
        this.phone = this.formatPhoneNum(phone);
        this.height = height;
        this.weight = weight;
        this.zip = zip;
        this.city = city;
        this.hireDate = hireDate;
    }

    public String formatPhoneNum(String num) {
        String formattedNum = "(";
        int i = 0;
        while (i < num.length()) {
            if (i == 3) {
                formattedNum = String.valueOf(formattedNum) + ") ";
            }
            if (i == 6) {
                formattedNum = String.valueOf(formattedNum) + "-";
            }
            formattedNum = String.valueOf(formattedNum) + num.charAt(i);
            ++i;
        }
        return formattedNum;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRank() {
        return this.rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getHireDate() {
        return this.hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEyes() {
        return this.eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public String getHair() {
        return this.hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}