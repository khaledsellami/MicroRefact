package org.opengeoportal.download.controllers;
 import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Future;
import org.opengeoportal.layer.BoundingBox;
import org.opengeoportal.solr.SolrRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.opengeoportal.Interface.BoundingBox;
public class SubmittedDownloadRequest {

@JsonIgnore
 final  Logger logger;

@JsonIgnore
 private  UUID requestId;

@JsonIgnore
 private  String sessionId;

@JsonProperty("srs")
 private String srs;

@JsonProperty("bbox")
 private String bbox;

@JsonIgnore
 private BoundingBox bounds;

@JsonProperty("format")
 private String format;

@JsonProperty("height")
 private int height;

@JsonProperty("width")
 private int width;

@JsonProperty("layers")
 private List<LayerImage> layerImage;

@JsonIgnore
 private File downloadFile;

@JsonIgnore
 private Boolean downloadFileSet;

@JsonIgnore
 private String name;

@JsonProperty("opacity")
 private int opacity;

@JsonProperty("zIndex")
 private int zIndex;

@JsonProperty("layerId")
 private String layerId;

 private String sld;

@JsonIgnore
 private String baseUrl;

@JsonIgnore
 private String queryString;

@JsonIgnore
 private SolrRecord solrRecord;

@JsonIgnore
 private File imageFile;

@JsonIgnore
 private Future<File> imageFileFuture;

@JsonIgnore
 private ImageStatus imageStatus;


public void setName(String name){
    this.name = name;
}


public ImageStatus getImageStatus(){
    return imageStatus;
}


public String getName(){
    return name;
}


public List<LayerImage> getLayers(){
    return this.layerImage;
}


public Set<String> getLayerIds(){
    Set<String> layerIds = new HashSet<String>();
    for (LayerImage layerImage : this.layerImage) {
        layerIds.add(layerImage.getLayerId());
    }
    return layerIds;
}


public int getWidth(){
    return width;
}


@Override
@JsonIgnore
public int compareTo(LayerImage n){
    return (zIndex < n.zIndex ? -1 : (zIndex == n.zIndex ? 0 : 1));
}


public void setImageFileFuture(Future<File> imageFileFuture){
    this.imageFileFuture = imageFileFuture;
}


public BoundingBox getBounds(){
    return bounds;
}


public File getImageFile(){
    return imageFile;
}


public int getOpacity(){
    return opacity;
}


public String getBaseUrl(){
    return baseUrl;
}


public int getHeight(){
    return height;
}


public SolrRecord getSolrRecord(){
    return solrRecord;
}


public void setzIndex(int zIndex){
    this.zIndex = zIndex;
}


public void setFormat(String format){
    this.format = format;
}


public void setRequestId(UUID requestId){
    this.requestId = requestId;
}


public void setImageFile(File imageFile){
    this.imageFile = imageFile;
}


public int getzIndex(){
    return zIndex;
}


public String getFormat(){
    return format;
}


public Future<File> getImageFileFuture(){
    return imageFileFuture;
}


public String getLayerId(){
    return layerId;
}


public File getDownloadFile(){
    return downloadFile;
}


public UUID getRequestId(){
    return requestId;
}


public String getBbox(){
    return bbox;
}


public void setBbox(String bbox){
    this.bbox = bbox;
}


public void setLayers(List<LayerImage> layers){
    this.layerImage = layers;
}


public void setQueryString(String queryString){
    this.queryString = queryString;
}


public String getQueryString(){
    return queryString;
}


public void setSolrRecord(SolrRecord solrRecord){
    this.solrRecord = solrRecord;
}


public void setSld(String sld){
    this.sld = sld;
}


public void setBounds(BoundingBox bounds){
    this.bounds = bounds;
}


public void setHeight(int height){
    this.height = height;
}


public void setWidth(int width){
    this.width = width;
}


public void setSrs(String srs){
    this.srs = srs;
}


public void setSessionId(String sessionId){
    this.sessionId = sessionId;
}


public void setLayerId(String layerId){
    this.layerId = layerId;
}


public void setDownloadFile(File downloadFile){
    this.downloadFile = downloadFile;
    this.downloadFileSet = true;
}


public String getSrs(){
    return srs;
}


public void setImageStatus(ImageStatus imageStatus){
    this.imageStatus = imageStatus;
}


public void setBaseUrl(String baseUrl){
    this.baseUrl = baseUrl;
}


@JsonIgnore
public boolean equals(Object o){
    if (!(o instanceof LayerImage))
        return false;
    LayerImage n = (LayerImage) o;
    return n.layerId.equals(layerId);
}


public String getSessionId(){
    return sessionId;
}


public String getSld(){
    return sld;
}


public void setOpacity(int opacity){
    this.opacity = opacity;
}


}