package com.jepescados.vendasapi.model.entity;
 import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
@Entity
@Data
public class Imagem {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
 private  Integer id;

@Column
 private  byte[] imagem;

@Column
 private  Integer ordem;

@ManyToOne
@JoinColumn(name = "id_produto")
 private  Produto produto;

@JsonDeserialize(using = LocalDateTimeDeserializer.class)
@JsonSerialize(using = LocalDateTimeSerializer.class)
@Column(name = "dh_criacao", nullable = false, updatable = false)
 private  LocalDateTime dataCriacao;

@JsonDeserialize(using = LocalDateTimeDeserializer.class)
@JsonSerialize(using = LocalDateTimeSerializer.class)
@Column(name = "dh_alteracao")
 private  LocalDateTime dataAlteracao;

public Imagem() {
}public Imagem(byte[] imagem, Integer ordem, Produto produto) {
    this.imagem = imagem;
    this.ordem = ordem;
    this.produto = produto;
}
@PrePersist
public void prePersist(){
    setDataCriacao(LocalDateTime.now());
}


}