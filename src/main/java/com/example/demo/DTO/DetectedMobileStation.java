package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

public class DetectedMobileStation {
    private String mobile_station_id;
    private float distance;
    private Date timestamp;

    public DetectedMobileStation() {
    }

    public DetectedMobileStation(String mobile_station_id, float distance, Date timestamp) {
        this.mobile_station_id = mobile_station_id;
        this.distance = distance;
        this.timestamp = timestamp;
    }

    public String getMobile_station_id() {
        return mobile_station_id;
    }

    public void setMobile_station_id(String mobile_station_id) {
        this.mobile_station_id = mobile_station_id;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
