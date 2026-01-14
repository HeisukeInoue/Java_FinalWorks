package com.example.dockerapi.service;
import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dockerapi.model.Appearance;
import com.example.dockerapi.model.AppearanceDetail;
import com.example.dockerapi.repository.AppearanceRepository;

@Service
public class AppearanceService {
    @Autowired
    private AppearanceRepository appearanceRepository;

    public List<Appearance> getAllAppearance(){
        return appearanceRepository.getAllAppearance();
    }

    public AppearanceDetail getAppearanceDetail(int id){
        return appearanceRepository.getAppearanceDetail(id);
    }

    public void postNewAppearance(Date date, String title, String text){
        appearanceRepository.postNewAppearance(date, title, text);
    }

    public int updateAppearance(Date date, String title, String text, int id){
        return appearanceRepository.updateAppearance(date, title, text, id);
    }

    public int deleteAppearance(int id){
        return appearanceRepository.deleteAppearance(id);
    }
}