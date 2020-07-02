package com.bonlimousin.livestock.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.bonlimousin.livestock.domain.enumeration.UserRole;

/**
 * A MatrilinealityEntity.
 */
@Entity
@Table(name = "bon_livestock_matrilineality")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MatrilinealityEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 127)
    @Column(name = "familyname", length = 127, nullable = false, unique = true)
    private String familyname;

    @NotNull
    @Min(value = 0)
    @Column(name = "ear_tag_id", nullable = false, unique = true)
    private Integer earTagId;

    @NotNull
    @Size(max = 127)
    @Column(name = "name", length = 127, nullable = false)
    private String name;

    @NotNull
    @Size(min = 2, max = 6)
    @Column(name = "country", length = 6, nullable = false)
    private String country;

    @Size(max = 1023)
    @Column(name = "description", length = 1023)
    private String description;

    @NotNull
    @Size(max = 255)
    @Column(name = "cattle_name_regex_pattern", length = 255, nullable = false)
    private String cattleNameRegexPattern;

    @NotNull
    @Column(name = "patri_id", nullable = false)
    private Integer patriId;

    @NotNull
    @Size(max = 127)
    @Column(name = "patri_name", length = 127, nullable = false)
    private String patriName;

    @NotNull
    @Size(min = 2, max = 6)
    @Column(name = "patri_country", length = 6, nullable = false)
    private String patriCountry;

    @NotNull
    @Column(name = "polled", nullable = false)
    private Boolean polled;

    @Column(name = "story_handle")
    private String storyHandle;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private UserRole visibility;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFamilyname() {
        return familyname;
    }

    public MatrilinealityEntity familyname(String familyname) {
        this.familyname = familyname;
        return this;
    }

    public void setFamilyname(String familyname) {
        this.familyname = familyname;
    }

    public Integer getEarTagId() {
        return earTagId;
    }

    public MatrilinealityEntity earTagId(Integer earTagId) {
        this.earTagId = earTagId;
        return this;
    }

    public void setEarTagId(Integer earTagId) {
        this.earTagId = earTagId;
    }

    public String getName() {
        return name;
    }

    public MatrilinealityEntity name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public MatrilinealityEntity country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public MatrilinealityEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCattleNameRegexPattern() {
        return cattleNameRegexPattern;
    }

    public MatrilinealityEntity cattleNameRegexPattern(String cattleNameRegexPattern) {
        this.cattleNameRegexPattern = cattleNameRegexPattern;
        return this;
    }

    public void setCattleNameRegexPattern(String cattleNameRegexPattern) {
        this.cattleNameRegexPattern = cattleNameRegexPattern;
    }

    public Integer getPatriId() {
        return patriId;
    }

    public MatrilinealityEntity patriId(Integer patriId) {
        this.patriId = patriId;
        return this;
    }

    public void setPatriId(Integer patriId) {
        this.patriId = patriId;
    }

    public String getPatriName() {
        return patriName;
    }

    public MatrilinealityEntity patriName(String patriName) {
        this.patriName = patriName;
        return this;
    }

    public void setPatriName(String patriName) {
        this.patriName = patriName;
    }

    public String getPatriCountry() {
        return patriCountry;
    }

    public MatrilinealityEntity patriCountry(String patriCountry) {
        this.patriCountry = patriCountry;
        return this;
    }

    public void setPatriCountry(String patriCountry) {
        this.patriCountry = patriCountry;
    }

    public Boolean isPolled() {
        return polled;
    }

    public MatrilinealityEntity polled(Boolean polled) {
        this.polled = polled;
        return this;
    }

    public void setPolled(Boolean polled) {
        this.polled = polled;
    }

    public String getStoryHandle() {
        return storyHandle;
    }

    public MatrilinealityEntity storyHandle(String storyHandle) {
        this.storyHandle = storyHandle;
        return this;
    }

    public void setStoryHandle(String storyHandle) {
        this.storyHandle = storyHandle;
    }

    public UserRole getVisibility() {
        return visibility;
    }

    public MatrilinealityEntity visibility(UserRole visibility) {
        this.visibility = visibility;
        return this;
    }

    public void setVisibility(UserRole visibility) {
        this.visibility = visibility;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatrilinealityEntity)) {
            return false;
        }
        return id != null && id.equals(((MatrilinealityEntity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatrilinealityEntity{" +
            "id=" + getId() +
            ", familyname='" + getFamilyname() + "'" +
            ", earTagId=" + getEarTagId() +
            ", name='" + getName() + "'" +
            ", country='" + getCountry() + "'" +
            ", description='" + getDescription() + "'" +
            ", cattleNameRegexPattern='" + getCattleNameRegexPattern() + "'" +
            ", patriId=" + getPatriId() +
            ", patriName='" + getPatriName() + "'" +
            ", patriCountry='" + getPatriCountry() + "'" +
            ", polled='" + isPolled() + "'" +
            ", storyHandle='" + getStoryHandle() + "'" +
            ", visibility='" + getVisibility() + "'" +
            "}";
    }
}
