package upce.semprace.eshop.DTO;
 import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence;
import java.util.Set;
public class Platba {

 private  Long id;

 private  String popis;

 private  Double prevod;

 private  Set<Nakup> nakup;


public Double getPrevod(){
    return prevod;
}


public Set<Nakup> getNakup(){
    return nakup;
}


public String getPopis(){
    return popis;
}


public Long getId(){
    return id;
}


}