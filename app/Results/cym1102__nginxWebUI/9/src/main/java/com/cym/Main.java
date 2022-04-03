package com.cym;
 import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import com.cym.Interface.SettingService;
import com.cym.Interface.SettingServiceImpl;
import com.cym.Interface.AdminService;
import com.cym.Interface.AdminServiceImpl;
import com.cym.Interface.MessageUtils;
import com.cym.Interface.MessageUtilsImpl;
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
public SettingService settingservice(){

return  new SettingServiceImpl(); 
    }



@Bean
public AdminService adminservice(){

return  new AdminServiceImpl(); 
    }



@Bean
public MessageUtils messageutils(){

return  new MessageUtilsImpl(); 
    }



}