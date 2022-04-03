package co.edu.uniquindio.gri.DTO;
 import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import co.edu.uniquindio.gri.Request.TipoRequest;
import co.edu.uniquindio.gri.Request.Impl.TipoRequestImpl;
import co.edu.uniquindio.gri.DTO.Tipo;
import co.edu.uniquindio.gri.Request.InvestigadorRequest;
import co.edu.uniquindio.gri.Request.Impl.InvestigadorRequestImpl;
import co.edu.uniquindio.gri.DTO.Investigador;
public class Produccion implements Serializable{

 private  long serialVersionUID;

 private  long id;

 private  String autores;

 private  String anio;

 private  String referencia;

 private  String repetido;

 private  Tipo tipo;

 private  Investigador investigador;

 private long id;

 private long id;

/**
 * Instantiates a new produccion.
 *
 * @param id the id
 * @param autores the autores
 * @param anio the anio
 * @param referencia the referencia
 * @param tipo the tipo
 * @param repetido the repetido
 * @param tipoProduccion the tipo produccion
 * @param investigador the investigador
 * @param estado the estado
 */
public Produccion(long id, String autores, String anio, String referencia, Tipo tipo, String repetido, TipoProduccion tipoProduccion, Investigador investigador) {
    this.id = id;
    this.autores = autores;
    this.anio = anio;
    this.referencia = referencia;
    this.tipo = tipo;
    this.investigador = investigador;
}/**
 * Instantiates a new produccion.
 */
public Produccion() {
}
public Investigador getInvestigador(){
  this.investigador = investigadorrequest.getInvestigador(this.id);
return this.investigador;
}


public String getReferencia(){
    return referencia;
}


public String getRepetido(){
    return repetido;
}


public long getId(){
    return id;
}


public Tipo getTipo(){
  this.tipo = tiporequest.getTipo(this.id);
return this.tipo;
}


public String getAnio(){
    return anio;
}


public String getAutores(){
    return autores;
}


}