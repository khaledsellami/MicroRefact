package com.cocay.sicecd;
 import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import com.cocay.sicecd.Interface.ProfesorRep;
import com.cocay.sicecd.Interface.ProfesorRepImpl;
import com.cocay.sicecd.Interface.InscripcionRep;
import com.cocay.sicecd.Interface.InscripcionRepImpl;
import com.cocay.sicecd.Interface.GrupoRep;
import com.cocay.sicecd.Interface.GrupoRepImpl;
import com.cocay.sicecd.Interface.GeneroRep;
import com.cocay.sicecd.Interface.GeneroRepImpl;
import com.cocay.sicecd.Interface.EstadoRep;
import com.cocay.sicecd.Interface.EstadoRepImpl;
import com.cocay.sicecd.Interface.Grado_profesorRep;
import com.cocay.sicecd.Interface.Grado_profesorRepImpl;
import com.cocay.sicecd.Interface.TurnoRep;
import com.cocay.sicecd.Interface.TurnoRepImpl;
import com.cocay.sicecd.Interface.Logging;
import com.cocay.sicecd.Interface.LoggingImpl;
import com.cocay.sicecd.Interface.ProfesorRep;
import com.cocay.sicecd.Interface.ProfesorRepImpl;
import com.cocay.sicecd.Interface.GrupoRep;
import com.cocay.sicecd.Interface.GrupoRepImpl;
import com.cocay.sicecd.Interface.Logging;
import com.cocay.sicecd.Interface.LoggingImpl;
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
public ProfesorRep profesorrep(){

return  new ProfesorRepImpl(); 
    }



@Bean
public InscripcionRep inscripcionrep(){

return  new InscripcionRepImpl(); 
    }



@Bean
public GrupoRep gruporep(){

return  new GrupoRepImpl(); 
    }



@Bean
public GeneroRep generorep(){

return  new GeneroRepImpl(); 
    }



@Bean
public EstadoRep estadorep(){

return  new EstadoRepImpl(); 
    }



@Bean
public Grado_profesorRep grado_profesorrep(){

return  new Grado_profesorRepImpl(); 
    }



@Bean
public TurnoRep turnorep(){

return  new TurnoRepImpl(); 
    }



@Bean
public Logging logging(){

return  new LoggingImpl(); 
    }



@Bean
public ProfesorRep profesorrep(){

return  new ProfesorRepImpl(); 
    }



@Bean
public GrupoRep gruporep(){

return  new GrupoRepImpl(); 
    }



@Bean
public Logging logging(){

return  new LoggingImpl(); 
    }



}