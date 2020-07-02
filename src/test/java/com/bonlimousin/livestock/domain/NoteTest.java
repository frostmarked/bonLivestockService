package com.bonlimousin.livestock.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.bonlimousin.livestock.web.rest.TestUtil;

public class NoteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NoteEntity.class);
        NoteEntity noteEntity1 = new NoteEntity();
        noteEntity1.setId(1L);
        NoteEntity noteEntity2 = new NoteEntity();
        noteEntity2.setId(noteEntity1.getId());
        assertThat(noteEntity1).isEqualTo(noteEntity2);
        noteEntity2.setId(2L);
        assertThat(noteEntity1).isNotEqualTo(noteEntity2);
        noteEntity1.setId(null);
        assertThat(noteEntity1).isNotEqualTo(noteEntity2);
    }
}
