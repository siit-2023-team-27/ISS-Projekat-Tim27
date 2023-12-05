package model;

import DTO.LoginResponseDTO;
import jakarta.persistence.*;
import model.enums.UserType;

import java.io.Serializable;

@Entity
@Table (name = "users")
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class AppUser implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String address;
    private String username;
    private String password;
    private String phoneNumber;
    private boolean suspended;


    // Constructor
    public AppUser(String firstName, String lastName, String address, String username, String password, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
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
                '}';
    }
    public void copyValues(AppUser appUser){
        this.firstName = appUser.firstName;
        this.lastName = appUser.lastName;
        this.address = appUser.address;
        this.password = appUser.password;
        this.phoneNumber = appUser.phoneNumber;
    }
//    public LoginResponseDTO toLoginResponse(){
//        return new LoginResponseDTO(this.id, this.username, "type");
//    }
}