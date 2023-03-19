package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class DetectedMobileStation {
    private String mobile_station_id;
    private float distance;
    private Date timestamp;
}
