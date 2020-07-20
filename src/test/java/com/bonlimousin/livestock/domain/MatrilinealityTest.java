package com.bonlimousin.livestock.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.bonlimousin.livestock.web.rest.TestUtil;

public class MatrilinealityTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatrilinealityEntity.class);
        MatrilinealityEntity matrilinealityEntity1 = new MatrilinealityEntity();
        matrilinealityEntity1.setId(1L);
        MatrilinealityEntity matrilinealityEntity2 = new MatrilinealityEntity();
        matrilinealityEntity2.setId(matrilinealityEntity1.getId());
        assertThat(matrilinealityEntity1).isEqualTo(matrilinealityEntity2);
        matrilinealityEntity2.setId(2L);
        assertThat(matrilinealityEntity1).isNotEqualTo(matrilinealityEntity2);
        matrilinealityEntity1.setId(null);
        assertThat(matrilinealityEntity1).isNotEqualTo(matrilinealityEntity2);
    }
}
