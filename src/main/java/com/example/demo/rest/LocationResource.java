package com.example.demo.rest;

import com.example.demo.DTO.DetectionReport;
import com.example.demo.domain.MobileStation;
import com.example.demo.repository.MobileStationRepository;
import com.example.demo.service.PrepareReport;
import com.example.demo.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for the home work basic functionality.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LocationResource {

    private final Logger log = LoggerFactory.getLogger(LocationResource.class);

    @Value("${clientApp.name}")
    private String applicationName;


    private final MobileStationRepository mobileStationRepository;

    private final PrepareReport prepareReport;

    public LocationResource(MobileStationRepository mobileStationRepository, PrepareReport prepareReport) {
        this.mobileStationRepository = mobileStationRepository;
        this.prepareReport = prepareReport;
    }

    @GetMapping("/reports")
    public List<DetectionReport> getAllBaseStations() {
        log.debug("REST request to get all BaseStations detection report");
        return this.prepareReport.getDetectionReport();
    }

    @GetMapping("/location/{id}")
    public ResponseEntity<MobileStation> getMobileStation(@PathVariable String id) {
        log.debug("REST request to get MobileStation : {}", id);
        Optional<MobileStation> mobileStation = mobileStationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mobileStation);
    }

}
