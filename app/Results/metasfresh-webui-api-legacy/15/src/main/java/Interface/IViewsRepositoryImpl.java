package Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
public class IViewsRepositoryImpl implements IViewsRepository{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://9";


public T getView(ViewId viewId,Class<T> type){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/getView"))
    .queryParam("viewId",viewId)
    .queryParam("type",type);
  T aux = restTemplate.getForObject(builder.toUriString(), T.class);

 return aux;
}


public void closeView(ViewId viewId,ViewCloseAction closeAction){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/closeView"))
    .queryParam("viewId",viewId)
    .queryParam("closeAction",closeAction);

  restTemplate.put(builder.toUriString(), null);
}


public IView getViewIfExists(ViewId viewId){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/getViewIfExists"))
    .queryParam("viewId",viewId);
  IView aux = restTemplate.getForObject(builder.toUriString(), IView.class);

 return aux;
}


public IView createView(CreateViewRequest request){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/createView"))
    .queryParam("request",request);
  IView aux = restTemplate.getForObject(builder.toUriString(), IView.class);

 return aux;
}


public void invalidateView(IView view){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/invalidateView"))
    .queryParam("view",view);

  restTemplate.put(builder.toUriString(), null);
}


}