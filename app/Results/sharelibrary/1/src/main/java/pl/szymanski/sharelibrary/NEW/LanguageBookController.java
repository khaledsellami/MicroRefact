package pl.szymanski.sharelibrary.NEW;
 import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import pl.szymanski.sharelibrary.entity.Language;
@RestController
@CrossOrigin
public class LanguageBookController {

@Autowired
 private LanguageBookService languagebookservice;


}