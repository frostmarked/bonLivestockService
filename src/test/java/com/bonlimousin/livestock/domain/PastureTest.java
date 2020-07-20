package com.bonlimousin.livestock.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.bonlimousin.livestock.web.rest.TestUtil;

public class PastureTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PastureEntity.class);
        PastureEntity pastureEntity1 = new PastureEntity();
        pastureEntity1.setId(1L);
        PastureEntity pastureEntity2 = new PastureEntity();
        pastureEntity2.setId(pastureEntity1.getId());
        assertThat(pastureEntity1).isEqualTo(pastureEntity2);
        pastureEntity2.setId(2L);
        assertThat(pastureEntity1).isNotEqualTo(pastureEntity2);
        pastureEntity1.setId(null);
        assertThat(pastureEntity1).isNotEqualTo(pastureEntity2);
    }
}
