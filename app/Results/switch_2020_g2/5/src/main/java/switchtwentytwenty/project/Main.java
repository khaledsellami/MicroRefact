package switchtwentytwenty.project;
 import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import switchtwentytwenty.project.Interface.IAuthorizationService;
import switchtwentytwenty.project.Interface.IAuthorizationServiceImpl;
import switchtwentytwenty.project.Interface.IFamilyRepository;
import switchtwentytwenty.project.Interface.IFamilyRepositoryImpl;
@SpringBootApplication
public class Main {


@Bean
public RestTemplate restTemplate(){
 
 return new RestTemplate();

  }



public static void main(String[] args){

SpringApplication.run(Main.class,args);

   }



@Bean
public IAuthorizationService iauthorizationservice(){

return  new IAuthorizationServiceImpl(); 
    }



@Bean
public IFamilyRepository ifamilyrepository(){

return  new IFamilyRepositoryImpl(); 
    }



}