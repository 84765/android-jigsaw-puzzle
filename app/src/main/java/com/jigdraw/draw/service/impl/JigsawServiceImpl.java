package com.jigdraw.draw.service.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.jigdraw.draw.dao.ImageDao;
import com.jigdraw.draw.dao.impl.ImageDaoImpl;
import com.jigdraw.draw.model.ImageEntity;
import com.jigdraw.draw.model.enums.Difficulty;
import com.jigdraw.draw.service.JigsawService;

import java.util.UUID;

/**
 * Default implementation for {@link com.jigdraw.draw.service.JigsawService}
 *
 * @author Jay Paulynice
 */
public class JigsawServiceImpl implements JigsawService {
    /** Class name for logging */
    private static final String TAG = "JigsawServiceImpl";

    /** Image entity dao */
    private ImageDao dao;

    /**
     * Create new jigsaw service given context
     *
     * @param context the application context
     */
    public JigsawServiceImpl(Context context) {
        dao = new ImageDaoImpl(context);
    }

    @Override
    public long create(Bitmap original, Difficulty level) {
        return createImageTiles(original, level.getValue());
    }

    /**
     * Slice original image into tiles then save in the db
     *
     * @param original the original image to slice up
     * @param n        how many slices to cut the image into
     */
    private long createImageTiles(Bitmap original, int n) {
        Long originalId = saveOriginal(original);

        int w = original.getWidth();
        int h = original.getHeight();

        int tile_width = w / n;
        int tile_height = h / n;

        for (int y = 0; y + tile_height <= h; y += tile_height) {
            for (int x = 0; x + tile_width <= w; x += tile_width) {
                Bitmap tile = Bitmap.createBitmap(original, x, y, tile_width,
                        tile_height);
                saveTile(tile, x, y, originalId);
            }
        }

        return originalId;
    }

    /**
     * Save the original image with a random UUID
     *
     * @param original image to save
     */
    private Long saveOriginal(Bitmap original) {
        String originalName = UUID.randomUUID() + ".png";
        String originalDesc = "original image " + originalName;
        Log.d(TAG, "image name: " + originalName);

        return saveEntity(original, originalName, originalDesc, null);
    }

    /**
     * Save created tile in the database
     *
     * @param tile the tile to save
     * @param x    the tile's x coordinate
     * @param y    the tile's y coordinate
     */
    private void saveTile(Bitmap tile, int x, int y, Long originalId) {
        String name = "tile-" + x + "-" + y + ".png";
        String desc = "sub image " + name;
        Log.d(TAG, "image name: " + name);

        saveEntity(tile, name, desc, originalId);
    }

    /**
     * Create an image entity with the give parameters and save
     *
     * @param image the bitmap image
     * @param name  the name
     * @param desc  the description
     */
    private Long saveEntity(Bitmap image, String name, String desc,
                            Long originalId) {
        return dao.create(new ImageEntity(image, name, desc, originalId));
    }
}