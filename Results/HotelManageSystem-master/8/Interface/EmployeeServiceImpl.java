import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
public class EmployeeServiceImpl implements EmployeeService{

 private RestTemplate restTemplate;

  String url = "http://2";


public Employee findByUserName(String userName){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/findByUserName"))
    .queryParam("userName",userName);
  Employee aux = restTemplate.getForObject(builder.toUriString(), Employee.class)

 return aux;
}


public Employee findByEmpNo(String empNo){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/findByEmpNo"))
    .queryParam("empNo",empNo);
  Employee aux = restTemplate.getForObject(builder.toUriString(), Employee.class)

 return aux;
}


}