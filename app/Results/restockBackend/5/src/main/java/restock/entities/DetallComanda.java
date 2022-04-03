package restock.entities;
 import javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "detallcomanda")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class DetallComanda {

 private  long serialVersionUID;

 private  Integer id;

 private  Comanda comanda;

 private  Producte producte;

 private  Double quantitat;


public void setComanda(Comanda comanda){
    this.comanda = comanda;
}


public void setProducte(Producte producte){
    this.producte = producte;
}


@Fetch(FetchMode.JOIN)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "comanda_id", nullable = false)
public Comanda getComanda(){
    return comanda;
}


public void setId(Integer id){
    this.id = id;
}


@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "id", unique = true, nullable = false)
public Integer getId(){
    return id;
}


@Fetch(FetchMode.JOIN)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "producte_id", nullable = false)
public Producte getProducte(){
    return producte;
}


@Column(name = "quantitat", nullable = false)
public Double getQuantitat(){
    return quantitat;
}


public void setQuantitat(Double quantitat){
    this.quantitat = quantitat;
}


}