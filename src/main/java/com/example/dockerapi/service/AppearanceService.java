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
    private AppearanceRepository AppearanceRepository;

    public List<Appearance> getAllAppearance(){
        return AppearanceRepository.getAllAppearance();
    }

    public AppearanceDetail getAppearanceDetail(int id){
        return AppearanceRepository.getAppearanceDetail(id);
    }

    public void postNewAppearance(Date date, String title, String text){
        AppearanceRepository.postNewAppearance(date, title, text);
    }

    public int updateAppearance(Date date, String title, String text, int id){
        return AppearanceRepository.updateAppearance(date, title, text, id);
    }

    public int deleteAppearance(int id){
        return AppearanceRepository.deleteAppearance(id);
    }
}