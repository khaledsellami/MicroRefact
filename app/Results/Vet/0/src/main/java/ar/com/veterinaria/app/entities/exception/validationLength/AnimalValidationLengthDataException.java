package ar.com.veterinaria.app.entities.exception.validationLength;
 import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ar.com.veterinaria.app.entities.Animal;
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AnimalValidationLengthDataException extends RuntimeException{

 private  long serialVersionUID;

public AnimalValidationLengthDataException(Animal animal) {
    super("Length name is over 70 character: " + animal.getName().length() + " for name: " + animal.getName());
}
}