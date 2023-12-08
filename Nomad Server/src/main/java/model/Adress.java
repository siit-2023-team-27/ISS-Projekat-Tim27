package model;

public class Adress {
    private String country;
    private String city;

    private String street;
    private int streetNum;

    public Adress(String country, String city, String street, int streetNum) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.streetNum = streetNum;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(int streetNum) {
        this.streetNum = streetNum;
    }

}
