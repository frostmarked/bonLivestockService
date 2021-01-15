package com.bonlimousin.livestock.service;

import com.bonlimousin.livestock.BonLivestockServiceApp;
import com.bonlimousin.livestock.domain.CattleEntity;
import com.bonlimousin.livestock.domain.PhotoEntity;
import com.bonlimousin.livestock.domain.enumeration.UserRole;
import com.bonlimousin.livestock.repository.CattleRepository;
import com.bonlimousin.livestock.repository.PhotoRepository;
import com.bonlimousin.livestock.web.rest.CattleResourceIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BonLivestockServiceApp.class)
public class PhotoServiceIT {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private CattleRepository cattleRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Test
    @Transactional
    void should_update_dimensions_on_save() throws IOException {
        PhotoEntity pe = createPhotoEntity();
        pe = photoService.save(pe);
        assertThat(pe.getWidth()).isEqualTo(4);
        assertThat(pe.getHeight()).isEqualTo(4);
        pe.setWidth(100);
        pe.setHeight(100);
        pe = photoService.save(pe);
        assertThat(pe.getWidth()).isEqualTo(4);
        assertThat(pe.getHeight()).isEqualTo(4);
    }

    @Test
    void should_find_fully_loaded_photo() throws IOException {
        PhotoEntity pe = photoService.save(createPhotoEntity());
        Optional<PhotoEntity> loaded = photoService.findOne(pe.getId());
        assertThat(loaded).isPresent();
        assertThat(loaded.get().getImage()).isNotEmpty();
    }

    @AfterEach
    public void cleanUp() {
        photoRepository.deleteAll();
        cattleRepository.deleteAll();
    }

    private PhotoEntity createPhotoEntity() {
        CattleEntity ce = CattleResourceIT.createEntity(null);
        ce = cattleRepository.save(ce);
        byte[] img = Base64Utils.decodeFromString(
            "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAE0lEQVR42mP8z/C/ngEJMJIuAACQHwn5Nb9RUAAAAABJRU5ErkJggg==");
        return new PhotoEntity()
            .cattle(ce)
            .image(img)
            .imageContentType("image/png")
            .caption("hoho")
            .width(4)
            .height(4)
            .taken(Instant.now())
            .visibility(UserRole.ROLE_ANONYMOUS);
    }
}
