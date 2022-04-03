package upce.semprace.eshop.DTO;
 import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence;
import java.util.Date;
import java.util.Set;
import upce.semprace.eshop.Request.UzivatelRequest;
import upce.semprace.eshop.Request.Impl.UzivatelRequestImpl;
import upce.semprace.eshop.DTO.Uzivatel;
import upce.semprace.eshop.Request.DopravaRequest;
import upce.semprace.eshop.Request.Impl.DopravaRequestImpl;
import upce.semprace.eshop.DTO.Doprava;
public class Nakup {

 private  Long id;

 private  Date datumVytvoreni;

 private  Integer objednavka;

 private  Boolean stav;

 private  Uzivatel uzivatel;

 private  Doprava doprava;

 private  Platba platba;

 private  Set<NakoupenaPolozka> nakoupenaPolozka;

 private Long id;

 private UzivatelRequest uzivatelrequest = new UzivatelRequestImpl();;

 private Long id;

 private DopravaRequest dopravarequest = new DopravaRequestImpl();;

 private Long id;


public void setDatumVytvoreni(Date datumVytvoreni){
    this.datumVytvoreni = datumVytvoreni;
}


public void setUzivatel(Uzivatel uzivatel){
 uzivatelrequest.setUzivatel(uzivatel,this.id);
}



public Integer getObjednavka(){
    return objednavka;
}


public void setObjednavka(Integer objednavka){
    this.objednavka = objednavka;
}


public void setStav(Boolean stav){
    this.stav = stav;
}


public Boolean getStav(){
    return stav;
}


public Long getId(){
    return id;
}


public Date getDatumVytvoreni(){
    return datumVytvoreni;
}


public void setDoprava(Doprava doprava){
 dopravarequest.setDoprava(doprava,this.id);
}



public Doprava getDoprava(){
  this.doprava = dopravarequest.getDoprava(this.id);
return this.doprava;
}


public Platba getPlatba(){
    return platba;
}


public Uzivatel getUzivatel(){
  this.uzivatel = uzivatelrequest.getUzivatel(this.id);
return this.uzivatel;
}


public void setNakoupenaPolozka(Set<NakoupenaPolozka> nakoupenaPolozka){
    this.nakoupenaPolozka = nakoupenaPolozka;
}


public void setId(Long id){
    this.id = id;
}


public void setPlatba(Platba platba){
    this.platba = platba;
}


public Set<NakoupenaPolozka> getNakoupenaPolozka(){
    return nakoupenaPolozka;
}


}