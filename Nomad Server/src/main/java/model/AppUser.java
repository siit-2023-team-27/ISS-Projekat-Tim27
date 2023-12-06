package model;

import DTO.LoginDTO;
import DTO.LoginResponseDTO;
import jakarta.persistence.*;
import model.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table (name = "users")
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class AppUser implements Serializable, UserDetails {
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

    //private List<UserType> roles;
    private Timestamp lastPasswordResetDate;
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

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }


//    public void setRoles(List<UserType> roles) {
//        this.roles = roles;
//    }

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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !suspended;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<UserType> getAuthorities() {
        return null;
    }

    public Timestamp getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }
    public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
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

//    public UserType getUserType() {
//        return userType;
//    }

//    public void setUserType(UserType userType) {
//        this.userType = userType;
//    }

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
    public LoginResponseDTO toLoginResponse(){
        return new LoginResponseDTO(this.id, this.username, "");
    }
}