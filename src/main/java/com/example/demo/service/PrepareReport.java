package com.example.demo.service;

import com.example.demo.DTO.DetectedMobileStation;
import com.example.demo.DTO.DetectionReport;
import com.example.demo.domain.BaseStation;
import com.example.demo.domain.MobileStation;
import com.example.demo.repository.BaseStationRepository;
import com.example.demo.repository.MobileStationRepository;
import com.example.demo.util.CalculateDetectionDistance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrepareReport {

    private final BaseStationRepository baseStationRepository;
    private final MobileStationRepository mobileStationRepository;

    public PrepareReport(BaseStationRepository baseStationRepository, MobileStationRepository mobileStationRepository) {
        this.baseStationRepository = baseStationRepository;
        this.mobileStationRepository = mobileStationRepository;
    }

    public List<DetectionReport> getDetectionReport(){
        List<MobileStation> allMobileStation = this.mobileStationRepository.findAll();
        return this.baseStationRepository
                .findAll()
                .stream()
                .map(baseStation -> new DetectionReport(baseStation.getId(), getDetectedMobileStation(allMobileStation, baseStation)))
                .collect(Collectors.toList());
    }

    private List<DetectedMobileStation> getDetectedMobileStation(List<MobileStation> allMobileStation, BaseStation baseStation){
        return allMobileStation
                .stream()
                .map(mobileStation -> new DetectedMobileStation(mobileStation.getId(), CalculateDetectionDistance.calculate(baseStation, mobileStation), new Date()))
                .filter(detectedMobileStation -> detectedMobileStation.getDistance() < baseStation.getDetectionRadiusInMeters())
                .collect(Collectors.toList());
    }
}
