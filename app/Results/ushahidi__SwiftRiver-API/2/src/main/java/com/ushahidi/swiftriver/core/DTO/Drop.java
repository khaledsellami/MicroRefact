package com.ushahidi.swiftriver.core.DTO;
 import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
public class Drop {

 private  long id;

 private  String hash;

 private  String channel;

 private  String title;

 private  String content;

 private  Identity identity;

 private  Date datePublished;

 private  Link originalUrl;

 private  String originalId;

 private  int commentCount;

 private  String type;

 private  String locale;

 private  Media image;

 private  Date dateAdded;

 private  List<Tag> tags;

 private  List<Place> places;

 private  List<Link> links;

 private  List<Media> media;

 private  List<Bucket> buckets;

 private  List<Long> riverIds;

 private  Boolean read;

 private  List<DropForm> forms;

 private  List<Long> markAsRead;

 private  List<Long> bucketIds;

 private  List<Long> channelIds;

 private  Long trackingId;

 private RestTemplate restTemplate = new RestTemplate();

  String url = "http://6";

public Drop() {
}
public List<Long> getChannelIds(){
    return channelIds;
}


public String getHash(){
    return hash;
}


public Boolean getRead(){
    return read;
}


public List<Tag> getTags(){
    return tags;
}


public String getTitle(){
    return title;
}


public String getChannel(){
    return channel;
}


@SuppressWarnings("rawtypes")
public List<DropForm> getForms(){
    return forms;
}


public List<Place> getPlaces(){
    return places;
}


public Date getDatePublished(){
    return datePublished;
}


public Link getOriginalUrl(){
    return originalUrl;
}


public List<Long> getRiverIds(){
    return riverIds;
}


public List<Bucket> getBuckets(){
    return buckets;
}


public int getCommentCount(){
    return commentCount;
}


public String getType(){
    return type;
}


public String getOriginalId(){
    return originalId;
}


public Media getImage(){
    return image;
}


public List<Link> getLinks(){
    return links;
}


public Long getTrackingId(){
    return trackingId;
}


public String getContent(){
    return content;
}


public long getId(){
    return id;
}


public List<Long> getMarkAsRead(){
    return markAsRead;
}


public Date getDateAdded(){
    return dateAdded;
}


public List<Long> getBucketIds(){
    return bucketIds;
}


public List<Media> getMedia(){
    return media;
}


public Identity getIdentity(){
    return identity;
}


public String getLocale(){
    return locale;
}


public void setId(long id){
    this.id = id;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setId"))

.queryParam("id",id)
;
restTemplate.put(builder.toUriString(),null);
}


public void setTrackingId(Long trackingId){
    this.trackingId = trackingId;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setTrackingId"))

.queryParam("trackingId",trackingId)
;
restTemplate.put(builder.toUriString(),null);
}


public void setChannel(String channel){
    this.channel = channel;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setChannel"))

.queryParam("channel",channel)
;
restTemplate.put(builder.toUriString(),null);
}


public void setTitle(String title){
    this.title = title;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setTitle"))

.queryParam("title",title)
;
restTemplate.put(builder.toUriString(),null);
}


public void setContent(String content){
    this.content = content;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setContent"))

.queryParam("content",content)
;
restTemplate.put(builder.toUriString(),null);
}


public void setDatePublished(Date dropletDatePub){
    this.datePublished = dropletDatePub;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setDatePublished"))

.queryParam("dropletDatePub",dropletDatePub)
;
restTemplate.put(builder.toUriString(),null);
}


public void setOriginalId(String originalId){
    this.originalId = originalId;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setOriginalId"))

.queryParam("originalId",originalId)
;
restTemplate.put(builder.toUriString(),null);
}


public void setCommentCount(int commentCount){
    this.commentCount = commentCount;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setCommentCount"))

.queryParam("commentCount",commentCount)
;
restTemplate.put(builder.toUriString(),null);
}


public void setRead(Boolean read){
    this.read = read;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setRead"))

.queryParam("read",read)
;
restTemplate.put(builder.toUriString(),null);
}


public void setOriginalUrl(Link originalUrl){
this.idLCK8 = originalUrl.getOriginalurl() ;
linkrequest.setOriginalUrl(originalUrl,this.idLCK8);
 this.originalUrl = originalUrl;
 


  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setOriginalUrl"))

.queryParam("originalUrl",originalUrl)
;
restTemplate.put(builder.toUriString(),null);
}


public void setIdentity(Identity identity){
this.idYKYP = identity.getIdentity() ;
identityrequest.setIdentity(identity,this.idYKYP);
 this.identity = identity;
 


  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setIdentity"))

.queryParam("identity",identity)
;
restTemplate.put(builder.toUriString(),null);
}


@SuppressWarnings("rawtypes")
public void setForms(List<DropForm> forms){
    this.forms = forms;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setForms"))

.queryParam("forms",forms)
;
restTemplate.put(builder.toUriString(),null);
}


public void setBuckets(List<Bucket> buckets){
    this.buckets = buckets;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setBuckets"))

.queryParam("buckets",buckets)
;
restTemplate.put(builder.toUriString(),null);
}


public void setPlaces(List<Place> places){
placerequest.setPlaces(places,this.id);
 this.places = places;
 


  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setPlaces"))

.queryParam("places",places)
;
restTemplate.put(builder.toUriString(),null);
}


public void setMedia(List<Media> media){
    this.media = media;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setMedia"))

.queryParam("media",media)
;
restTemplate.put(builder.toUriString(),null);
}


public void setImage(Media image){
this.idBTYE = image.getImage() ;
mediarequest.setImage(image,this.idBTYE);
 this.image = image;
 


  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setImage"))

.queryParam("image",image)
;
restTemplate.put(builder.toUriString(),null);
}


public void setLinks(List<Link> links){
    this.links = links;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setLinks"))

.queryParam("links",links)
;
restTemplate.put(builder.toUriString(),null);
}


public void setTags(List<Tag> tags){
tagrequest.setTags(tags,this.id);
 this.tags = tags;
 


  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/"+ id).concat("/setTags"))

.queryParam("tags",tags)
;
restTemplate.put(builder.toUriString(),null);
}


}