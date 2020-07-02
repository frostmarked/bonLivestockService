package com.bonlimousin.livestock.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.bonlimousin.livestock.domain.enumeration.NoteCategory;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.bonlimousin.livestock.domain.NoteEntity} entity. This class is used
 * in {@link com.bonlimousin.livestock.web.rest.NoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NoteCriteria implements Serializable, Criteria {
    /**
     * Class for filtering NoteCategory
     */
    public static class NoteCategoryFilter extends Filter<NoteCategory> {

        public NoteCategoryFilter() {
        }

        public NoteCategoryFilter(NoteCategoryFilter filter) {
            super(filter);
        }

        @Override
        public NoteCategoryFilter copy() {
            return new NoteCategoryFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private NoteCategoryFilter category;

    private StringFilter note;

    private LocalDateFilter actualDate;

    private LongFilter pastureId;

    private LongFilter cattleId;

    public NoteCriteria() {
    }

    public NoteCriteria(NoteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.actualDate = other.actualDate == null ? null : other.actualDate.copy();
        this.pastureId = other.pastureId == null ? null : other.pastureId.copy();
        this.cattleId = other.cattleId == null ? null : other.cattleId.copy();
    }

    @Override
    public NoteCriteria copy() {
        return new NoteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public NoteCategoryFilter getCategory() {
        return category;
    }

    public void setCategory(NoteCategoryFilter category) {
        this.category = category;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public LocalDateFilter getActualDate() {
        return actualDate;
    }

    public void setActualDate(LocalDateFilter actualDate) {
        this.actualDate = actualDate;
    }

    public LongFilter getPastureId() {
        return pastureId;
    }

    public void setPastureId(LongFilter pastureId) {
        this.pastureId = pastureId;
    }

    public LongFilter getCattleId() {
        return cattleId;
    }

    public void setCattleId(LongFilter cattleId) {
        this.cattleId = cattleId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NoteCriteria that = (NoteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(category, that.category) &&
            Objects.equals(note, that.note) &&
            Objects.equals(actualDate, that.actualDate) &&
            Objects.equals(pastureId, that.pastureId) &&
            Objects.equals(cattleId, that.cattleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        category,
        note,
        actualDate,
        pastureId,
        cattleId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (category != null ? "category=" + category + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (actualDate != null ? "actualDate=" + actualDate + ", " : "") +
                (pastureId != null ? "pastureId=" + pastureId + ", " : "") +
                (cattleId != null ? "cattleId=" + cattleId + ", " : "") +
            "}";
    }

}
