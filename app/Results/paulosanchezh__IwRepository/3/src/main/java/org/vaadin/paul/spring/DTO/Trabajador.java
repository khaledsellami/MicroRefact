package org.vaadin.paul.spring.DTO;
 import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.vaadin.paul.spring.Request.UserRequest;
import org.vaadin.paul.spring.Request.Impl.UserRequestImpl;
import org.vaadin.paul.spring.DTO.User;
import org.vaadin.paul.spring.Request.CentroRequest;
import org.vaadin.paul.spring.Request.Impl.CentroRequestImpl;
import org.vaadin.paul.spring.DTO.Centro;
public class Trabajador {

 private  int id;

 private  User user;

 private  LocalTime horaInicio;

 private  LocalTime horaFinal;

 private  float salario;

 private  LocalDate fechaContrato;

 private  Centro centro;

 private int id;

 private int id;

public Trabajador() {
}public Trabajador(User usuario, LocalTime horaInicio, LocalTime horaFinal, float salario, LocalDate fechaContrato) {
    this.user = usuario;
    this.horaInicio = horaInicio;
    this.horaFinal = horaFinal;
    this.salario = salario;
    this.fechaContrato = fechaContrato;
}
public float getSalario(){
    return this.salario;
}


public String getNombre(){
    return getUser().getNombreyApellidos();
}


public User getUser(){
  this.user = userrequest.getUser(this.id);
return this.user;
}


public LocalTime getHoraInicio(){
    return horaInicio;
}


public int getId(){
    return id;
}


public LocalDate getFechaContrato(){
    return this.fechaContrato;
}


public Centro getCentro(){
  this.centro = centrorequest.getCentro(this.id);
return this.centro;
}


public LocalTime getHoraFinal(){
    return horaFinal;
}


}