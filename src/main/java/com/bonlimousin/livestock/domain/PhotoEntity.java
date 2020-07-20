package com.bonlimousin.livestock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.bonlimousin.livestock.domain.enumeration.UserRole;

/**
 * A PhotoEntity.
 */
@Entity
@Table(name = "bon_livestock_photo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PhotoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    
    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;

    @Column(name = "image_content_type", nullable = false)
    private String imageContentType;

    @NotNull
    @Size(min = 1, max = 140)
    @Column(name = "caption", length = 140, nullable = false)
    private String caption;

    @Min(value = 0)
    @Column(name = "height")
    private Integer height;

    @Min(value = 0)
    @Column(name = "width")
    private Integer width;

    @Column(name = "taken")
    private Instant taken;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private UserRole visibility;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "photos", allowSetters = true)
    private CattleEntity cattle;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public PhotoEntity image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public PhotoEntity imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getCaption() {
        return caption;
    }

    public PhotoEntity caption(String caption) {
        this.caption = caption;
        return this;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getHeight() {
        return height;
    }

    public PhotoEntity height(Integer height) {
        this.height = height;
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public PhotoEntity width(Integer width) {
        this.width = width;
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Instant getTaken() {
        return taken;
    }

    public PhotoEntity taken(Instant taken) {
        this.taken = taken;
        return this;
    }

    public void setTaken(Instant taken) {
        this.taken = taken;
    }

    public UserRole getVisibility() {
        return visibility;
    }

    public PhotoEntity visibility(UserRole visibility) {
        this.visibility = visibility;
        return this;
    }

    public void setVisibility(UserRole visibility) {
        this.visibility = visibility;
    }

    public CattleEntity getCattle() {
        return cattle;
    }

    public PhotoEntity cattle(CattleEntity cattle) {
        this.cattle = cattle;
        return this;
    }

    public void setCattle(CattleEntity cattle) {
        this.cattle = cattle;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhotoEntity)) {
            return false;
        }
        return id != null && id.equals(((PhotoEntity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhotoEntity{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", caption='" + getCaption() + "'" +
            ", height=" + getHeight() +
            ", width=" + getWidth() +
            ", taken='" + getTaken() + "'" +
            ", visibility='" + getVisibility() + "'" +
            "}";
    }
}
