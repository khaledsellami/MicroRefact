package hei2017;
 import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import hei2017.Interface.SprintService;
import hei2017.Interface.SprintServiceImpl;
import hei2017.Interface.StoryService;
import hei2017.Interface.StoryServiceImpl;
import hei2017.Interface.TaskService;
import hei2017.Interface.TaskServiceImpl;
import hei2017.Interface.UserService;
import hei2017.Interface.UserServiceImpl;
import hei2017.Interface.SprintService;
import hei2017.Interface.SprintServiceImpl;
import hei2017.Interface.StoryService;
import hei2017.Interface.StoryServiceImpl;
import hei2017.Interface.SprintDAO;
import hei2017.Interface.SprintDAOImpl;
import hei2017.Interface.UserDAO;
import hei2017.Interface.UserDAOImpl;
import hei2017.Interface.SprintService;
import hei2017.Interface.SprintServiceImpl;
import hei2017.Interface.SprintService;
import hei2017.Interface.SprintServiceImpl;
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
public SprintService sprintservice(){

return  new SprintServiceImpl(); 
    }



@Bean
public StoryService storyservice(){

return  new StoryServiceImpl(); 
    }



@Bean
public TaskService taskservice(){

return  new TaskServiceImpl(); 
    }



@Bean
public UserService userservice(){

return  new UserServiceImpl(); 
    }



@Bean
public SprintService sprintservice(){

return  new SprintServiceImpl(); 
    }



@Bean
public StoryService storyservice(){

return  new StoryServiceImpl(); 
    }



@Bean
public SprintDAO sprintdao(){

return  new SprintDAOImpl(); 
    }



@Bean
public UserDAO userdao(){

return  new UserDAOImpl(); 
    }



@Bean
public SprintService sprintservice(){

return  new SprintServiceImpl(); 
    }



@Bean
public SprintService sprintservice(){

return  new SprintServiceImpl(); 
    }



}