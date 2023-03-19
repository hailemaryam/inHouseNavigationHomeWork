package com.example.demo.rest;

import com.example.demo.domain.BaseStation;
import com.example.demo.repository.BaseStationRepository;
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

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.BaseStation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BaseStationResource {

    private final Logger log = LoggerFactory.getLogger(BaseStationResource.class);

    private static final String ENTITY_NAME = "baseStation";

    @Value("${clientApp.name}")
    private String applicationName;

    private final BaseStationRepository baseStationRepository;

    public BaseStationResource(BaseStationRepository baseStationRepository) {
        this.baseStationRepository = baseStationRepository;
    }

    @PostMapping("/base-stations")
    public ResponseEntity<BaseStation> createBaseStation(@RequestBody BaseStation baseStation) throws URISyntaxException {
        log.debug("REST request to save BaseStation : {}", baseStation);
        if (baseStation.getId() != null) {
            throw new BadRequestAlertException("A new baseStation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BaseStation result = baseStationRepository.save(baseStation);
        return ResponseEntity
            .created(new URI("/api/base-stations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PutMapping("/base-stations/{id}")
    public ResponseEntity<BaseStation> updateBaseStation(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody BaseStation baseStation
    ) throws URISyntaxException {
        log.debug("REST request to update BaseStation : {}, {}", id, baseStation);
        if (baseStation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baseStation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baseStationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BaseStation result = baseStationRepository.save(baseStation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, baseStation.getId()))
            .body(result);
    }


    @GetMapping("/base-stations")
    public List<BaseStation> getAllBaseStations() {
        log.debug("REST request to get all BaseStations");
        return baseStationRepository.findAll();
    }

    @DeleteMapping("/base-stations/{id}")
    public ResponseEntity<Void> deleteBaseStation(@PathVariable String id) {
        log.debug("REST request to delete BaseStation : {}", id);
        baseStationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
