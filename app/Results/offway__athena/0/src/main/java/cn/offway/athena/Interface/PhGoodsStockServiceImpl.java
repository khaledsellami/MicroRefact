package cn.offway.athena.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import cn.offway.athena.Interface.PhGoodsStockService;
public class PhGoodsStockServiceImpl implements PhGoodsStockService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://2";


public boolean updateStock(String orderNo){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/updateStock"))
    .queryParam("orderNo",orderNo)
;  boolean aux = restTemplate.getForObject(builder.toUriString(), boolean.class);

 return aux;
}


}