package com.bonlimousin.livestock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.bonlimousin.livestock.domain.enumeration.UserRole;

/**
 * A CattleEntity.
 */
@Entity
@Table(name = "bon_livestock_cattle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CattleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "ear_tag_id", nullable = false, unique = true)
    private Integer earTagId;

    @NotNull
    @Size(max = 127)
    @Column(name = "name", length = 127, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private UserRole visibility;

    @NotNull
    @Column(name = "up_for_sale", nullable = false)
    private Boolean upForSale;

    @NotNull
    @Column(name = "show_blup", nullable = false)
    private Boolean showBlup;

    @NotNull
    @Column(name = "alert", nullable = false)
    private Boolean alert;

    @Column(name = "story_handle")
    private String storyHandle;

    @ManyToOne
    @JsonIgnoreProperties(value = "cattles", allowSetters = true)
    private MatrilinealityEntity matrilineality;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEarTagId() {
        return earTagId;
    }

    public CattleEntity earTagId(Integer earTagId) {
        this.earTagId = earTagId;
        return this;
    }

    public void setEarTagId(Integer earTagId) {
        this.earTagId = earTagId;
    }

    public String getName() {
        return name;
    }

    public CattleEntity name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRole getVisibility() {
        return visibility;
    }

    public CattleEntity visibility(UserRole visibility) {
        this.visibility = visibility;
        return this;
    }

    public void setVisibility(UserRole visibility) {
        this.visibility = visibility;
    }

    public Boolean isUpForSale() {
        return upForSale;
    }

    public CattleEntity upForSale(Boolean upForSale) {
        this.upForSale = upForSale;
        return this;
    }

    public void setUpForSale(Boolean upForSale) {
        this.upForSale = upForSale;
    }

    public Boolean isShowBlup() {
        return showBlup;
    }

    public CattleEntity showBlup(Boolean showBlup) {
        this.showBlup = showBlup;
        return this;
    }

    public void setShowBlup(Boolean showBlup) {
        this.showBlup = showBlup;
    }

    public Boolean isAlert() {
        return alert;
    }

    public CattleEntity alert(Boolean alert) {
        this.alert = alert;
        return this;
    }

    public void setAlert(Boolean alert) {
        this.alert = alert;
    }

    public String getStoryHandle() {
        return storyHandle;
    }

    public CattleEntity storyHandle(String storyHandle) {
        this.storyHandle = storyHandle;
        return this;
    }

    public void setStoryHandle(String storyHandle) {
        this.storyHandle = storyHandle;
    }

    public MatrilinealityEntity getMatrilineality() {
        return matrilineality;
    }

    public CattleEntity matrilineality(MatrilinealityEntity matrilineality) {
        this.matrilineality = matrilineality;
        return this;
    }

    public void setMatrilineality(MatrilinealityEntity matrilineality) {
        this.matrilineality = matrilineality;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CattleEntity)) {
            return false;
        }
        return id != null && id.equals(((CattleEntity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CattleEntity{" +
            "id=" + getId() +
            ", earTagId=" + getEarTagId() +
            ", name='" + getName() + "'" +
            ", visibility='" + getVisibility() + "'" +
            ", upForSale='" + isUpForSale() + "'" +
            ", showBlup='" + isShowBlup() + "'" +
            ", alert='" + isAlert() + "'" +
            ", storyHandle='" + getStoryHandle() + "'" +
            "}";
    }
}
