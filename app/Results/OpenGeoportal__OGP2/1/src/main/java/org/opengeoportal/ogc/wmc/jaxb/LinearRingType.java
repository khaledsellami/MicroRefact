package org.opengeoportal.ogc.wmc.jaxb;
 import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LinearRingType", namespace = "http://www.opengis.net/gml", propOrder = { "coord", "coordinates" })
public class LinearRingType extends AbstractGeometryType{

 protected  List<CoordType> coord;

 protected  CoordinatesType coordinates;


public void setCoordinates(CoordinatesType value){
    this.coordinates = value;
}


public List<CoordType> getCoord(){
    if (coord == null) {
        coord = new ArrayList<CoordType>();
    }
    return this.coord;
}


public CoordinatesType getCoordinates(){
    return coordinates;
}


}