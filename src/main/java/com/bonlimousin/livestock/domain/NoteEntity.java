package com.bonlimousin.livestock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

import com.bonlimousin.livestock.domain.enumeration.NoteCategory;

/**
 * A NoteEntity.
 */
@Entity
@Table(name = "bon_livestock_note")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NoteEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private NoteCategory category;

    @Size(max = 512)
    @Column(name = "note", length = 512)
    private String note;

    @Column(name = "actual_date")
    private LocalDate actualDate;

    @ManyToOne
    @JsonIgnoreProperties(value = "notes", allowSetters = true)
    private PastureEntity pasture;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "notes", allowSetters = true)
    private CattleEntity cattle;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NoteCategory getCategory() {
        return category;
    }

    public NoteEntity category(NoteCategory category) {
        this.category = category;
        return this;
    }

    public void setCategory(NoteCategory category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public NoteEntity note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getActualDate() {
        return actualDate;
    }

    public NoteEntity actualDate(LocalDate actualDate) {
        this.actualDate = actualDate;
        return this;
    }

    public void setActualDate(LocalDate actualDate) {
        this.actualDate = actualDate;
    }

    public PastureEntity getPasture() {
        return pasture;
    }

    public NoteEntity pasture(PastureEntity pasture) {
        this.pasture = pasture;
        return this;
    }

    public void setPasture(PastureEntity pasture) {
        this.pasture = pasture;
    }

    public CattleEntity getCattle() {
        return cattle;
    }

    public NoteEntity cattle(CattleEntity cattle) {
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
        if (!(o instanceof NoteEntity)) {
            return false;
        }
        return id != null && id.equals(((NoteEntity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoteEntity{" +
            "id=" + getId() +
            ", category='" + getCategory() + "'" +
            ", note='" + getNote() + "'" +
            ", actualDate='" + getActualDate() + "'" +
            "}";
    }
}
