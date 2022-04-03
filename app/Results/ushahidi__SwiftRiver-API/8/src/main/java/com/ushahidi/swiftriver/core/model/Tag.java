package com.ushahidi.swiftriver.core.model;
 import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@Entity
@Table(name = "tags")
public class Tag implements Serializable{

 private  long serialVersionUID;

@Id
@GeneratedValue(strategy = GenerationType.TABLE, generator = "Seq")
@TableGenerator(name = "Seq", table = "seq", pkColumnName = "name", valueColumnName = "id", pkColumnValue = "tags")
 private  long id;

@Column(name = "hash", nullable = false)
 private  String hash;

@Column(name = "tag", nullable = false)
 private  String tag;

@Column(name = "tag_canonical", nullable = false)
 private  String tagCanonical;

@Column(name = "tag_type", nullable = false)
 private  String type;

public Tag() {
}
public String getHash(){
    return hash;
}


public String getType(){
    return type;
}


@Override
public int hashCode(){
    final int prime = 31;
    int result = 1;
    result = prime * result + ((hash == null) ? 0 : hash.hashCode());
    return result;
}


public String getTagCanonical(){
    return tagCanonical;
}


@Override
public boolean equals(Object obj){
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    Tag other = (Tag) obj;
    if (hash == null) {
        if (other.hash != null)
            return false;
    } else if (!hash.equals(other.hash))
        return false;
    return true;
}


public void setId(long id){
    this.id = id;
}


public void setHash(String hash){
    this.hash = hash;
}


public void setTag(String tag){
    this.tag = tag;
}


public long getId(){
    return id;
}


public String getTag(){
    return tag;
}


public void setTagCanonical(String tagCanonical){
    this.tagCanonical = tagCanonical;
}


public void setType(String type){
    this.type = type;
}


}