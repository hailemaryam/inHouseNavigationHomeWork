package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mobile_station")
public class MobileStation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "last_known_x")
    private Float lastKnownX;

    @Column(name = "last_known_y")
    private Float lastKnownY;

    public String getId() {
        return this.id;
    }

    public MobileStation id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getLastKnownX() {
        return this.lastKnownX;
    }

    public MobileStation lastKnownX(Float lastKnownX) {
        this.setLastKnownX(lastKnownX);
        return this;
    }

    public void setLastKnownX(Float lastKnownX) {
        this.lastKnownX = lastKnownX;
    }

    public Float getLastKnownY() {
        return this.lastKnownY;
    }

    public MobileStation lastKnownY(Float lastKnownY) {
        this.setLastKnownY(lastKnownY);
        return this;
    }

    public void setLastKnownY(Float lastKnownY) {
        this.lastKnownY = lastKnownY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MobileStation)) {
            return false;
        }
        return id != null && id.equals(((MobileStation) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "MobileStation{" +
            "id=" + getId() +
            ", lastKnownX=" + getLastKnownX() +
            ", lastKnownY=" + getLastKnownY() +
            "}";
    }
}
