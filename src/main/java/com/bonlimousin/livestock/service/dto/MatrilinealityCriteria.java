package com.bonlimousin.livestock.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.bonlimousin.livestock.domain.enumeration.UserRole;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.bonlimousin.livestock.domain.MatrilinealityEntity} entity. This class is used
 * in {@link com.bonlimousin.livestock.web.rest.MatrilinealityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /matrilinealities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MatrilinealityCriteria implements Serializable, Criteria {
    /**
     * Class for filtering UserRole
     */
    public static class UserRoleFilter extends Filter<UserRole> {

        public UserRoleFilter() {
        }

        public UserRoleFilter(UserRoleFilter filter) {
            super(filter);
        }

        @Override
        public UserRoleFilter copy() {
            return new UserRoleFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter familyname;

    private IntegerFilter earTagId;

    private StringFilter name;

    private StringFilter country;

    private StringFilter description;

    private StringFilter cattleNameRegexPattern;

    private IntegerFilter patriId;

    private StringFilter patriName;

    private StringFilter patriCountry;

    private BooleanFilter polled;

    private StringFilter storyHandle;

    private UserRoleFilter visibility;

    public MatrilinealityCriteria() {
    }

    public MatrilinealityCriteria(MatrilinealityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.familyname = other.familyname == null ? null : other.familyname.copy();
        this.earTagId = other.earTagId == null ? null : other.earTagId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.cattleNameRegexPattern = other.cattleNameRegexPattern == null ? null : other.cattleNameRegexPattern.copy();
        this.patriId = other.patriId == null ? null : other.patriId.copy();
        this.patriName = other.patriName == null ? null : other.patriName.copy();
        this.patriCountry = other.patriCountry == null ? null : other.patriCountry.copy();
        this.polled = other.polled == null ? null : other.polled.copy();
        this.storyHandle = other.storyHandle == null ? null : other.storyHandle.copy();
        this.visibility = other.visibility == null ? null : other.visibility.copy();
    }

    @Override
    public MatrilinealityCriteria copy() {
        return new MatrilinealityCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFamilyname() {
        return familyname;
    }

    public void setFamilyname(StringFilter familyname) {
        this.familyname = familyname;
    }

    public IntegerFilter getEarTagId() {
        return earTagId;
    }

    public void setEarTagId(IntegerFilter earTagId) {
        this.earTagId = earTagId;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCountry() {
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getCattleNameRegexPattern() {
        return cattleNameRegexPattern;
    }

    public void setCattleNameRegexPattern(StringFilter cattleNameRegexPattern) {
        this.cattleNameRegexPattern = cattleNameRegexPattern;
    }

    public IntegerFilter getPatriId() {
        return patriId;
    }

    public void setPatriId(IntegerFilter patriId) {
        this.patriId = patriId;
    }

    public StringFilter getPatriName() {
        return patriName;
    }

    public void setPatriName(StringFilter patriName) {
        this.patriName = patriName;
    }

    public StringFilter getPatriCountry() {
        return patriCountry;
    }

    public void setPatriCountry(StringFilter patriCountry) {
        this.patriCountry = patriCountry;
    }

    public BooleanFilter getPolled() {
        return polled;
    }

    public void setPolled(BooleanFilter polled) {
        this.polled = polled;
    }

    public StringFilter getStoryHandle() {
        return storyHandle;
    }

    public void setStoryHandle(StringFilter storyHandle) {
        this.storyHandle = storyHandle;
    }

    public UserRoleFilter getVisibility() {
        return visibility;
    }

    public void setVisibility(UserRoleFilter visibility) {
        this.visibility = visibility;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MatrilinealityCriteria that = (MatrilinealityCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(familyname, that.familyname) &&
            Objects.equals(earTagId, that.earTagId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(country, that.country) &&
            Objects.equals(description, that.description) &&
            Objects.equals(cattleNameRegexPattern, that.cattleNameRegexPattern) &&
            Objects.equals(patriId, that.patriId) &&
            Objects.equals(patriName, that.patriName) &&
            Objects.equals(patriCountry, that.patriCountry) &&
            Objects.equals(polled, that.polled) &&
            Objects.equals(storyHandle, that.storyHandle) &&
            Objects.equals(visibility, that.visibility);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        familyname,
        earTagId,
        name,
        country,
        description,
        cattleNameRegexPattern,
        patriId,
        patriName,
        patriCountry,
        polled,
        storyHandle,
        visibility
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatrilinealityCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (familyname != null ? "familyname=" + familyname + ", " : "") +
                (earTagId != null ? "earTagId=" + earTagId + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (cattleNameRegexPattern != null ? "cattleNameRegexPattern=" + cattleNameRegexPattern + ", " : "") +
                (patriId != null ? "patriId=" + patriId + ", " : "") +
                (patriName != null ? "patriName=" + patriName + ", " : "") +
                (patriCountry != null ? "patriCountry=" + patriCountry + ", " : "") +
                (polled != null ? "polled=" + polled + ", " : "") +
                (storyHandle != null ? "storyHandle=" + storyHandle + ", " : "") +
                (visibility != null ? "visibility=" + visibility + ", " : "") +
            "}";
    }

}
