package com.bonlimousin.livestock.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.bonlimousin.livestock.web.rest.TestUtil;

public class CattleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CattleEntity.class);
        CattleEntity cattleEntity1 = new CattleEntity();
        cattleEntity1.setId(1L);
        CattleEntity cattleEntity2 = new CattleEntity();
        cattleEntity2.setId(cattleEntity1.getId());
        assertThat(cattleEntity1).isEqualTo(cattleEntity2);
        cattleEntity2.setId(2L);
        assertThat(cattleEntity1).isNotEqualTo(cattleEntity2);
        cattleEntity1.setId(null);
        assertThat(cattleEntity1).isNotEqualTo(cattleEntity2);
    }
}
