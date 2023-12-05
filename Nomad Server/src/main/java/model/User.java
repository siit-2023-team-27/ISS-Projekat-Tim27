package model;

import DTO.LoginDTO;
import DTO.LoginResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import model.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class User implements UserDetails {
    private long id;
    private String firstName;
    private String lastName;
    private String address;
    private String username;
    private String password;
    private String phoneNumber;
    //private UserType userType;
    private boolean suspended;

    private List<UserType> roles;
    private Timestamp lastPasswordResetDate;
    // Constructor
    public User(String firstName, String lastName, String address, String username, String password, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.suspended = false;
    }
    public User(){}

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }


    public void setRoles(List<UserType> roles) {
        this.roles = roles;
    }

    // Getters and setters for each attribute
    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        return roles;
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
        Timestamp now = new Timestamp(new Date().getTime());
        this.setLastPasswordResetDate(now);
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
    public void copyValues(User user){
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.address = user.address;
        this.password = user.password;
        this.phoneNumber = user.phoneNumber;
    }
    public LoginResponseDTO toLoginResponse(){
        return new LoginResponseDTO(this.id, this.username, "");
    }
}