package serializers;

public class ShippingDetails {

    private String state;
    private String address;
    private String city;
    private String zip;
    private String country;
    private String phone;

    public ShippingDetails() {
        // construtor padrão para o Jackson
    }

    public String getCity(){
        return this.city;
    }

    public void setCity(String city){
        this.city =  city;
    }

    public String getZip(){
        return this.zip;
    }

    public void setZip(String zip){
        this.zip = zip;
    }

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
