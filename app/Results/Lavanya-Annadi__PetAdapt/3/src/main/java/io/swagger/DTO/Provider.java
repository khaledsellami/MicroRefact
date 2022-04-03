package io.swagger.DTO;
 import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
public class Provider {

 private  Long id;

 private  String username;

 private  String firstName;

 private  String lastName;

 private  String email;

 private  String password;

 private  String phone;

 private  List<Address> address;

 private  LocalDateTime updatedAt;

 private  Timestamp createdAt;

 private  Integer status;


public String getPhone(){
    return phone;
}


public LocalDateTime getUpdatedAt(){
    return updatedAt;
}


public Long getId(){
    return id;
}


public Integer getStatus(){
    return status;
}


public String getLastName(){
    return lastName;
}


public String getUsername(){
    return username;
}


public Timestamp getCreatedAt(){
    return createdAt;
}


public String getPassword(){
    return password;
}


public String getEmail(){
    return email;
}


public List<Address> getAddress(){
    return address;
}


public String getFirstName(){
    return firstName;
}


}