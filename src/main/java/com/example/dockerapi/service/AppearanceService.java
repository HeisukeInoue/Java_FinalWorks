package com.example.dockerapi.service;
import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dockerapi.model.appearance;
import com.example.dockerapi.model.appearanceDetail;
import com.example.dockerapi.repository.appearanceRepository;

@Service
public class appearanceService {
    @Autowired
    private appearanceRepository appearanceRepository;

    public List<appearance> getAllappearance(){
        return appearanceRepository.getAllappearance();
    }

    public appearanceDetail getappearanceDetail(int id){
        return appearanceRepository.getappearanceDetail(id);
    }

    public void postNewappearance(Date date, String title, String text){
        appearanceRepository.postNewappearance(date, title, text);
    }

    public int updateappearance(Date date, String title, String text, int id){
        return appearanceRepository.updateappearance(date, title, text, id);
    }

    public int deleteappearance(int id){
        return appearanceRepository.deleteappearance(id);
    }
}