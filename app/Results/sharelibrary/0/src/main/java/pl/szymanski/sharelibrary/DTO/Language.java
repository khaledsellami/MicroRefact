package pl.szymanski.sharelibrary.DTO;
 import lombok.Data;
import javax.persistence;
import java.util.List;
import java.util.Objects;
import pl.szymanski.sharelibrary.Request.BookRequest;
import pl.szymanski.sharelibrary.Request.Impl.BookRequestImpl;
import pl.szymanski.sharelibrary.DTO.Book;
public class Language {

 private  Integer id;

 private  String name;

 private  List<Book> books;


@Override
public boolean equals(Object o){
    if (this == o)
        return true;
    if (!(o instanceof Language))
        return false;
    Language language = (Language) o;
    return id.equals(language.id);
}


}