package model;

import DTO.LoginResponseDTO;
import jakarta.persistence.*;
import model.enums.UserType;
@Entity
@Table (name = "users")
public class AppUser {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String address;
    private String username;
    private String password;
    private String phoneNumber;
    private UserType userType;
    private boolean suspended;


    // Constructor
    public AppUser(String firstName, String lastName, String address, String username, String password, String phoneNumber, UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.suspended = false;
    }
    public AppUser(){}

    // Getters and setters for each attribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void suspend(){
        this.suspended = true;
    }
    public void unsuspend(){
        this.suspended = false;
    }
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

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    // toString method to represent the user as a string
    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userType=" + userType +
                '}';
    }
    public void copyValues(AppUser appUser){
        this.firstName = appUser.firstName;
        this.lastName = appUser.lastName;
        this.address = appUser.address;
        this.password = appUser.password;
        this.phoneNumber = appUser.phoneNumber;
        this.userType = appUser.userType;
    }
    public LoginResponseDTO toLoginResponse(){
        return new LoginResponseDTO(this.id, this.username, this.userType.toString());
    }
}