package com.example.demo.rest;

import com.example.demo.domain.MobileStation;
import com.example.demo.repository.MobileStationRepository;
import com.example.demo.rest.errors.BadRequestAlertException;
import com.example.demo.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.MobileStation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MobileStationResource {

    private final Logger log = LoggerFactory.getLogger(MobileStationResource.class);

    private static final String ENTITY_NAME = "mobileStation";

    @Value("${clientApp.name}")
    private String applicationName;

    private final MobileStationRepository mobileStationRepository;

    public MobileStationResource(MobileStationRepository mobileStationRepository) {
        this.mobileStationRepository = mobileStationRepository;
    }

    @PostMapping("/mobile-stations")
    public ResponseEntity<MobileStation> createMobileStation(@RequestBody MobileStation mobileStation) throws URISyntaxException {
        log.debug("REST request to save MobileStation : {}", mobileStation);
        if (mobileStation.getId() != null) {
            throw new BadRequestAlertException("A new mobileStation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mobileStation.setId(UUID.randomUUID().toString());
        MobileStation result = mobileStationRepository.save(mobileStation);
        return ResponseEntity
            .created(new URI("/api/mobile-stations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PutMapping("/mobile-stations/{id}")
    public ResponseEntity<MobileStation> updateMobileStation(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody MobileStation mobileStation
    ) throws URISyntaxException {
        log.debug("REST request to update MobileStation : {}, {}", id, mobileStation);
        if (mobileStation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mobileStation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mobileStationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MobileStation result = mobileStationRepository.save(mobileStation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mobileStation.getId()))
            .body(result);
    }


    @GetMapping("/mobile-stations")
    public List<MobileStation> getAllMobileStations() {
        log.debug("REST request to get all MobileStations");
        return mobileStationRepository.findAll();
    }

    @DeleteMapping("/mobile-stations/{id}")
    public ResponseEntity<Void> deleteMobileStation(@PathVariable String id) {
        log.debug("REST request to delete MobileStation : {}", id);
        mobileStationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
