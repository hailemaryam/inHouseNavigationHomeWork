package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "base_station")
public class BaseStation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "x")
    private Float x;

    @Column(name = "y")
    private Float y;

    @Column(name = "detection_radius_in_meters")
    private Float detectionRadiusInMeters;


    public String getId() {
        return this.id;
    }

    public BaseStation id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public BaseStation name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getX() {
        return this.x;
    }

    public BaseStation x(Float x) {
        this.setX(x);
        return this;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return this.y;
    }

    public BaseStation y(Float y) {
        this.setY(y);
        return this;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getDetectionRadiusInMeters() {
        return this.detectionRadiusInMeters;
    }

    public BaseStation detectionRadiusInMeters(Float detectionRadiusInMeters) {
        this.setDetectionRadiusInMeters(detectionRadiusInMeters);
        return this;
    }

    public void setDetectionRadiusInMeters(Float detectionRadiusInMeters) {
        this.detectionRadiusInMeters = detectionRadiusInMeters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseStation)) {
            return false;
        }
        return id != null && id.equals(((BaseStation) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "BaseStation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", x=" + getX() +
            ", y=" + getY() +
            ", detectionRadiusInMeters=" + getDetectionRadiusInMeters() +
            "}";
    }
}
