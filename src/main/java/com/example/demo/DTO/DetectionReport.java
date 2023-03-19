package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DetectionReport {
    private String base_station_id;
    List<DetectedMobileStation> reports;
}
