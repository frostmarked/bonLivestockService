package com.bonlimousin.livestock.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.bonlimousin.livestock.web.rest.TestUtil;

public class PhotoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhotoEntity.class);
        PhotoEntity photoEntity1 = new PhotoEntity();
        photoEntity1.setId(1L);
        PhotoEntity photoEntity2 = new PhotoEntity();
        photoEntity2.setId(photoEntity1.getId());
        assertThat(photoEntity1).isEqualTo(photoEntity2);
        photoEntity2.setId(2L);
        assertThat(photoEntity1).isNotEqualTo(photoEntity2);
        photoEntity1.setId(null);
        assertThat(photoEntity1).isNotEqualTo(photoEntity2);
    }
}
