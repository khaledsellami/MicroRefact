package com.gp.cricket.NEW;
 import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.gp.cricket.repository.ClubRepository;
import com.gp.cricket.entity.Club;
@Service
public class ClubSponsorClubService {

@Autowired
 private ClubRepository clubrepository;


public void setClubId(Integer clubIdv2,Club clubId){
clubrepository.setClubId(clubIdv2,clubId);
}


public Club getClubId(Integer clubIdv2){
return clubrepository.getClubId(clubIdv2);
}


}