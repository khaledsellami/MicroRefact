package com.optimize.chapter2.trip.flyweight;
 public class EmployeeReportManager implements IReportManager{

 protected  String tenantId;

public EmployeeReportManager(String tenantId) {
    this.tenantId = tenantId;
}
@Override
public String createReport(){
    return "This is a employee report";
}


}