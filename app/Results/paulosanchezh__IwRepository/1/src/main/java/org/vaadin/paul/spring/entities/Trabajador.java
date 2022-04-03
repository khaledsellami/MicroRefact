package org.vaadin.paul.spring.entities;
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
@Entity
public class Trabajador {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
 private  int id;

@Transient
 private  User user;

 private  LocalTime horaInicio;

 private  LocalTime horaFinal;

@Column(name = "salarioBruto")
 private  float salario;

@Column(name = "fechaContrato")
 private  LocalDate fechaContrato;

@Transient
 private  Centro centro;

@Column(name = "id")
 private int id;

@Transient
 private UserRequest userrequest = new UserRequestImpl();;

@Column(name = "id")
 private int id;

@Transient
 private CentroRequest centrorequest = new CentroRequestImpl();;

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


public void setSalario(float salario){
    this.salario = salario;
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


public void setHoraInicio(LocalTime horaInicio){
    this.horaInicio = horaInicio;
}


public int getId(){
    return id;
}


public LocalDate getFechaContrato(){
    return this.fechaContrato;
}


public void setFechaContrato(LocalDate fechaContrato){
    this.fechaContrato = fechaContrato;
}


public Centro getCentro(){
  this.centro = centrorequest.getCentro(this.id);
return this.centro;
}


public void setHoraFinal(LocalTime horaFinal){
    this.horaFinal = horaFinal;
}


public void setCentro(Centro centro){
 centrorequest.setCentro(centro,this.id);
}



public LocalTime getHoraFinal(){
    return horaFinal;
}


}