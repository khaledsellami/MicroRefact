package guru.springframework.domain;
 import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "role")
public class Role {

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
@Column(name = "role_id")
 private  int id;

@Column(name = "role")
 private  String role;


public void setRole(String role){
    this.role = role;
}


public String getRole(){
    return role;
}


public void setId(int id){
    this.id = id;
}


public int getId(){
    return id;
}


}