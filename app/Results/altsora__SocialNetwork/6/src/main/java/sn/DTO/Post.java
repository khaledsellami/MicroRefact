package sn.DTO;
 import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence;
import java.time.LocalDateTime;
import java.util.Set;
public class Post {

 private  long id;

 private  LocalDateTime time;

 private  Person author;

 private  String title;

 private  String text;

 private  boolean isBlocked;

 private  boolean isDeleted;

 private  int likesCount;

 private  Set<Comment> comments;

 private  Set<Tag> tags;

 private RestTemplate restTemplate = new RestTemplate();

  String url = "http://9";


@Column(name = "title", nullable = false)
public String getTitle(){
    return title;
}


@JsonBackReference
@ManyToOne(fetch = FetchType.EAGER, optional = false)
@JoinColumn(name = "author_id")
public Person getAuthor(){
    return author;
}


@CreationTimestamp
@Column(name = "time", nullable = false, columnDefinition = "timestamp with time zone")
public LocalDateTime getTime(){
    return time;
}


@Column(name = "post_text", nullable = false)
public String getText(){
    return text;
}


@JsonManagedReference
@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
public Set<Comment> getComments(){
    return comments;
}


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
public long getId(){
    return id;
}


@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
@JoinTable(name = "post2tag", joinColumns = { @JoinColumn(name = "post_id") }, inverseJoinColumns = { @JoinColumn(name = "tag_id") })
public Set<Tag> getTags(){
    return tags;
}


@Column(name = "likes")
public int getLikesCount(){
    return likesCount;
}


@Column(name = "is_blocked")
public boolean isBlocked(){
    return isBlocked;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/isBlocked"))

;
boolean aux = restTemplate.getForObject(builder.toUriString(),boolean.class);
return aux;
}


}