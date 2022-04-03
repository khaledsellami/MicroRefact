package com.uec.imonitor;
 import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import com.uec.imonitor.Interface.IRequestNewsService;
import com.uec.imonitor.Interface.IRequestNewsServiceImpl;
import com.uec.imonitor.Interface.INewsSpreadingAnalysisService;
import com.uec.imonitor.Interface.INewsSpreadingAnalysisServiceImpl;
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
public IRequestNewsService irequestnewsservice(){

return  new IRequestNewsServiceImpl(); 
    }



@Bean
public INewsSpreadingAnalysisService inewsspreadinganalysisservice(){

return  new INewsSpreadingAnalysisServiceImpl(); 
    }



}