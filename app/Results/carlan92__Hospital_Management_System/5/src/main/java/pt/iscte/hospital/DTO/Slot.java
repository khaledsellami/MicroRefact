package pt.iscte.hospital.DTO;
 import org.springframework.format.annotation.DateTimeFormat;
import pt.iscte.hospital.entities.waiting.PatientWaitingAppointment;
import javax.persistence;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import pt.iscte.hospital.objects.utils.Calendar;
import pt.iscte.hospital.Request.AppointmentRequest;
import pt.iscte.hospital.Request.Impl.AppointmentRequestImpl;
import pt.iscte.hospital.DTO.Appointment;
import pt.iscte.hospital.Request.DoctorRequest;
import pt.iscte.hospital.Request.Impl.DoctorRequestImpl;
import pt.iscte.hospital.DTO.Doctor;
import pt.iscte.hospital.Request.PatientWaitingAppointmentRequest;
import pt.iscte.hospital.Request.Impl.PatientWaitingAppointmentRequestImpl;
import pt.iscte.hospital.DTO.PatientWaitingAppointment;
public class Slot implements Comparable<Slot>{

 private  Long slotId;

 private  LocalDate date;

 private  LocalTime timeBegin;

 private  LocalTime timeEnd;

 private  Set<Appointment> appointments;

 private  Doctor doctor;

 private  Set<PatientWaitingAppointment> patientWaitingAppointments;

 private  boolean isAvailable;

 private Long userId;

// Constructors
public Slot() {
}public Slot(Doctor doctor, LocalDate date, LocalTime timeBegin, LocalTime timeEnd) {
    this.doctor = doctor;
    this.date = date;
    this.timeBegin = timeBegin;
    this.timeEnd = timeEnd;
}// Construtor cópia
public Slot(Slot slot, boolean isAvailable) {
    this.doctor = slot.getDoctor();
    this.date = slot.getDate();
    this.timeBegin = slot.getTimeBegin();
    this.timeEnd = slot.getTimeEnd();
    this.isAvailable = isAvailable;
}
public LocalTime getTimeEnd(){
    return timeEnd;
}


public String getTimeBeginStr(){
    return timeBegin.format(TIME_FORMATTER);
}


public String getTimeEndStr(){
    return timeEnd.format(TIME_FORMATTER);
}


public Doctor getDoctor(){
  this.doctor = doctorrequest.getDoctor(this.userId);
return this.doctor;
}


public Long getSlotId(){
    return slotId;
}


public String getDateStr(){
    return date.format(FORMATTER);
}


public LocalDate getDate(){
    return date;
}


public LocalTime getTimeBegin(){
    return timeBegin;
}


}