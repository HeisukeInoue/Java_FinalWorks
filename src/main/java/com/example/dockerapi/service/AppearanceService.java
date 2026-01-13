package com.example.dockerapi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import com.example.dockerapi.dto.BlogListResponse;
import com.example.dockerapi.model.Appearance;
import com.example.dockerapi.repository.AppearanceRepository;

@Service
public class AppearanceService {
    @Autowired
    private AppearanceRepository AppearanceRepository;

    public List<Appearance> getAllAppearance(){
        return AppearanceRepository.getAllAppearance();
    }
}
