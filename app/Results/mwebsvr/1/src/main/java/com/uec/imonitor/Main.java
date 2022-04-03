package com.uec.imonitor;
 import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import com.uec.imonitor.Interface.IRequestNewsJPARepository;
import com.uec.imonitor.Interface.IRequestNewsJPARepositoryImpl;
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
public IRequestNewsJPARepository irequestnewsjparepository(){

return  new IRequestNewsJPARepositoryImpl(); 
    }



}