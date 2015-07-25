package com.jigdraw.draw.dao;

import com.jigdraw.draw.model.ImageEntity;

import java.util.List;

/**
 * Simple interface for {@link ImageEntity} CRUD operations
 *
 * @author Jay Paulynice
 */
public interface ImageDao {
    /**
     * Save an image entity
     *
     * @param entity the entity to create
     * @return the generated id
     */
    Long create(ImageEntity entity);

    /**
     * Find entity by id
     *
     * @param id the id of the entity
     * @return the matching entity
     */
    ImageEntity find(Long id);

    /**
     * Find the jigsaw tiles for the original id
     *
     * @param id the original image id
     * @return the jigsaw entities
     */
    List<ImageEntity> findTiles(Long id);

    /**
     * Update the given image entity
     *
     * @param entity the entity to update
     * @return 1 for success and 0 for fail
     */
    int update(ImageEntity entity);

    /**
     * Delete the entity by id
     *
     * @param id the id of the entity
     * @return 1 for success and 0 for fail
     */
    int delete(Long id);

    /**
     * Find all the original images for history
     *
     * @return list of images user created
     */
    List<ImageEntity> findAllOriginals();
}