package com.example.demo.repository;

import com.example.demo.domain.BaseStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseStationRepository extends JpaRepository<BaseStation, String> {}
