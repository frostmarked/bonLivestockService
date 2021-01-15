package com.bonlimousin.livestock.service;

import com.bonlimousin.livestock.domain.PhotoEntity;
import com.bonlimousin.livestock.repository.PhotoRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PhotoEntity}.
 */
@Service
@Transactional
public class PhotoService {

    private final Logger log = LoggerFactory.getLogger(PhotoService.class);

    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    /**
     * Save a photo.
     *
     * @param photoEntity the entity to save.
     * @return the persisted entity.
     */
    public PhotoEntity save(PhotoEntity photoEntity) throws IOException {
        log.debug("Request to save Photo : {}", photoEntity);
        try(InputStream is = new ByteArrayInputStream(photoEntity.getImage())) {
            BufferedImage bi = ImageIO.read(is);
            photoEntity.width(bi.getWidth()).height(bi.getHeight());
        }
        return photoRepository.save(photoEntity);
    }

    /**
     * Get all the photos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PhotoEntity> findAll(Pageable pageable) {
        log.debug("Request to get all Photos");
        Page<PhotoEntity> p =  photoRepository.findAll(pageable);
        p.getContent().forEach(pe -> {
            Hibernate.unproxy(pe);
            pe.setImage(PhotoQueryService.DEFAULT_LIST_IMAGE);
        });
        return p;
    }


    /**
     * Get one photo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PhotoEntity> findOne(Long id) {
        log.debug("Request to get Photo : {}", id);
        Optional<PhotoEntity> opt = photoRepository.findById(id);
        if(opt.isPresent() && !Hibernate.isPropertyInitialized(opt.get(), "image")) {
            // unnecessary code but makes it obvious that lob is being fetched
            Hibernate.initialize(opt.get().getImage());
        }
        return opt;
    }

    /**
     * Delete the photo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Photo : {}", id);
        photoRepository.deleteById(id);
    }
}
