package org.example.securecorporatetalentportal.user;

import java.util.List;

//Represent a system user
public class User {

    private String userName;
    private String password;   //stored as BCrypt hash (not raw)
    private String email;
    private List<String> roles;  //ROLE_BASIC, ROLE_ADMIN

    public User() {}

    public User(String userName, String password, String email, List<String> roles) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public String getUserName() {return userName;}
    public void setUserName(String username) {this.userName = username;}

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

}
