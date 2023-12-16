package DTO;

import model.enums.UserType;

import java.util.List;

public class UserRegistrationDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String username;
    private String password;
    private String phoneNumber;
    private List<UserType> roles;

    // Constructor
    public UserRegistrationDTO(String firstName, String lastName, String address, String username, String password, String phoneNumber,  List<UserType> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }
    public UserRegistrationDTO(){}
    // Getters and setters for each attribute
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<UserType> getRoles() {
        return roles;
    }

    public void setRoles(List<UserType> roles) {
        this.roles = roles;
    }
}