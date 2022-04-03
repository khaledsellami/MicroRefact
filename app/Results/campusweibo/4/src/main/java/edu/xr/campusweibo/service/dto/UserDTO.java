package edu.xr.campusweibo.service.dto;
 import edu.xr.campusweibo.config.Constants;
import edu.xr.campusweibo.domain.Authority;
import edu.xr.campusweibo.domain.User;
import org.hibernate.validator.constraints.Email;
import javax.validation.constraints;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;
public class UserDTO {

 private  Long id;

@Pattern(regexp = Constants.LOGIN_REGEX)
@Size(min = 1, max = 50)
 private  String login;

@Size(max = 50)
 private  String firstName;

@Size(max = 50)
 private  String lastName;

@Email
@Size(min = 5, max = 100)
 private  String email;

@Size(max = 256)
 private  String imageUrl;

 private  boolean activated;

@Size(min = 2, max = 5)
 private  String langKey;

 private  String createdBy;

 private  ZonedDateTime createdDate;

 private  String lastModifiedBy;

 private  ZonedDateTime lastModifiedDate;

 private  Set<String> authorities;


public ZonedDateTime getLastModifiedDate(){
    return lastModifiedDate;
}


public String getLastModifiedBy(){
    return lastModifiedBy;
}


public String getLangKey(){
    return langKey;
}


public Long getId(){
    return id;
}


public boolean isActivated(){
    return activated;
}


public String getLastName(){
    return lastName;
}


public Set<String> getAuthorities(){
    return authorities;
}


public String getLogin(){
    return login;
}


public void setLastModifiedDate(ZonedDateTime lastModifiedDate){
    this.lastModifiedDate = lastModifiedDate;
}


public void setLogin(String login){
    this.login = login;
}


public String getImageUrl(){
    return imageUrl;
}


public void setId(Long id){
    this.id = id;
}


public String getEmail(){
    return email;
}


@Override
public String toString(){
    return "UserDTO{" + "login='" + login + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", imageUrl='" + imageUrl + '\'' + ", activated=" + activated + ", langKey='" + langKey + '\'' + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", lastModifiedBy='" + lastModifiedBy + '\'' + ", lastModifiedDate=" + lastModifiedDate + ", authorities=" + authorities + "}";
}


public ZonedDateTime getCreatedDate(){
    return createdDate;
}


public String getFirstName(){
    return firstName;
}


public String getCreatedBy(){
    return createdBy;
}


}