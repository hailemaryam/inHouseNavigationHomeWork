package com.example.demo.DTO;

import java.util.List;

public class DetectionReport {
    private String base_station_id;
    List<DetectedMobileStation> reports;

    public DetectionReport() {
    }

    public DetectionReport(String base_station_id, List<DetectedMobileStation> reports) {
        this.base_station_id = base_station_id;
        this.reports = reports;
    }

    public String getBase_station_id() {
        return base_station_id;
    }

    public void setBase_station_id(String base_station_id) {
        this.base_station_id = base_station_id;
    }

    public List<DetectedMobileStation> getReports() {
        return reports;
    }

    public void setReports(List<DetectedMobileStation> reports) {
        this.reports = reports;
    }
}
