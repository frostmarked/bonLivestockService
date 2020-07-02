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
 * Criteria class for the {@link com.bonlimousin.livestock.domain.CattleEntity} entity. This class is used
 * in {@link com.bonlimousin.livestock.web.rest.CattleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cattles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CattleCriteria implements Serializable, Criteria {
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

    private IntegerFilter earTagId;

    private StringFilter name;

    private UserRoleFilter visibility;

    private BooleanFilter upForSale;

    private BooleanFilter showBlup;

    private BooleanFilter alert;

    private StringFilter storyHandle;

    private LongFilter photoId;

    private LongFilter noteId;

    private LongFilter matrilinealityId;

    public CattleCriteria() {
    }

    public CattleCriteria(CattleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.earTagId = other.earTagId == null ? null : other.earTagId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.visibility = other.visibility == null ? null : other.visibility.copy();
        this.upForSale = other.upForSale == null ? null : other.upForSale.copy();
        this.showBlup = other.showBlup == null ? null : other.showBlup.copy();
        this.alert = other.alert == null ? null : other.alert.copy();
        this.storyHandle = other.storyHandle == null ? null : other.storyHandle.copy();
        this.photoId = other.photoId == null ? null : other.photoId.copy();
        this.noteId = other.noteId == null ? null : other.noteId.copy();
        this.matrilinealityId = other.matrilinealityId == null ? null : other.matrilinealityId.copy();
    }

    @Override
    public CattleCriteria copy() {
        return new CattleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
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

    public UserRoleFilter getVisibility() {
        return visibility;
    }

    public void setVisibility(UserRoleFilter visibility) {
        this.visibility = visibility;
    }

    public BooleanFilter getUpForSale() {
        return upForSale;
    }

    public void setUpForSale(BooleanFilter upForSale) {
        this.upForSale = upForSale;
    }

    public BooleanFilter getShowBlup() {
        return showBlup;
    }

    public void setShowBlup(BooleanFilter showBlup) {
        this.showBlup = showBlup;
    }

    public BooleanFilter getAlert() {
        return alert;
    }

    public void setAlert(BooleanFilter alert) {
        this.alert = alert;
    }

    public StringFilter getStoryHandle() {
        return storyHandle;
    }

    public void setStoryHandle(StringFilter storyHandle) {
        this.storyHandle = storyHandle;
    }

    public LongFilter getPhotoId() {
        return photoId;
    }

    public void setPhotoId(LongFilter photoId) {
        this.photoId = photoId;
    }

    public LongFilter getNoteId() {
        return noteId;
    }

    public void setNoteId(LongFilter noteId) {
        this.noteId = noteId;
    }

    public LongFilter getMatrilinealityId() {
        return matrilinealityId;
    }

    public void setMatrilinealityId(LongFilter matrilinealityId) {
        this.matrilinealityId = matrilinealityId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CattleCriteria that = (CattleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(earTagId, that.earTagId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(visibility, that.visibility) &&
            Objects.equals(upForSale, that.upForSale) &&
            Objects.equals(showBlup, that.showBlup) &&
            Objects.equals(alert, that.alert) &&
            Objects.equals(storyHandle, that.storyHandle) &&
            Objects.equals(photoId, that.photoId) &&
            Objects.equals(noteId, that.noteId) &&
            Objects.equals(matrilinealityId, that.matrilinealityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        earTagId,
        name,
        visibility,
        upForSale,
        showBlup,
        alert,
        storyHandle,
        photoId,
        noteId,
        matrilinealityId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CattleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (earTagId != null ? "earTagId=" + earTagId + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (visibility != null ? "visibility=" + visibility + ", " : "") +
                (upForSale != null ? "upForSale=" + upForSale + ", " : "") +
                (showBlup != null ? "showBlup=" + showBlup + ", " : "") +
                (alert != null ? "alert=" + alert + ", " : "") +
                (storyHandle != null ? "storyHandle=" + storyHandle + ", " : "") +
                (photoId != null ? "photoId=" + photoId + ", " : "") +
                (noteId != null ? "noteId=" + noteId + ", " : "") +
                (matrilinealityId != null ? "matrilinealityId=" + matrilinealityId + ", " : "") +
            "}";
    }

}
