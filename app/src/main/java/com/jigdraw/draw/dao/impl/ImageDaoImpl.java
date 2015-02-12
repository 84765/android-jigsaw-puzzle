package com.jigdraw.draw.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jigdraw.draw.dao.ImageDao;
import com.jigdraw.draw.db.DBHelper;
import com.jigdraw.draw.model.ImageEntity;

import static com.jigdraw.draw.util.Base64Util.base64ToBitmap;
import static com.jigdraw.draw.util.DBUtil.DESC_COLUMN;
import static com.jigdraw.draw.util.DBUtil.ID_SELECTION;
import static com.jigdraw.draw.util.DBUtil.IMAGE_COLUMN;
import static com.jigdraw.draw.util.DBUtil.JIGSAW_TABLE;
import static com.jigdraw.draw.util.DBUtil.NAME_COLUMN;
import static com.jigdraw.draw.util.DBUtil.getIdSelection;
import static com.jigdraw.draw.util.EntityUtil.entityToContentValues;

/**
 * Default implementation for {@link com.jigdraw.draw.dao.ImageDao}. Provides
 * CRUD database operations for {@link com.jigdraw.draw.model.ImageEntity}
 *
 * @author Jay Paulynice
 */
public class ImageDaoImpl implements ImageDao {
    /** tag name for logging */
    private static final String TAG = "ImageDaoImpl";

    /** database */
    private SQLiteDatabase db;

    /**
     * Create new dao object with given context
     *
     * @param context the application context
     */
    public ImageDaoImpl(Context context) {
        DBHelper mdb = new DBHelper(context);
        db = mdb.getWritableDatabase();
    }

    @Override
    public long create(ImageEntity entity) {
        long id = db.insert(JIGSAW_TABLE, null, entityToContentValues(entity));
        Log.d(TAG, "successfully saved image...id: " + id);

        return id;
    }

    @Override
    public ImageEntity find(long id) {
        String[] cols = new String[]{NAME_COLUMN, IMAGE_COLUMN, DESC_COLUMN};
        Cursor cursor = db.query(JIGSAW_TABLE, cols, ID_SELECTION,
                getIdSelection(id), null, null,
                null);

        return getEntityFromCursor(cursor);
    }

    private ImageEntity getEntityFromCursor(Cursor cursor) {
        ImageEntity entity = null;
        if (cursor != null && cursor.moveToFirst()) {
            entity = getEntity(cursor);
            Log.d(TAG, "image entity found with name: " + entity.getName());
            cursor.close();
        }
        return entity;
    }

    private ImageEntity getEntity(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(NAME_COLUMN));
        String base64String = cursor.getString(cursor.getColumnIndex
                (IMAGE_COLUMN));
        String desc = cursor.getString(cursor.getColumnIndex(DESC_COLUMN));

        return new ImageEntity(base64ToBitmap(base64String), name, desc);
    }

    @Override
    public int update(ImageEntity entity) {
        Log.d(TAG, "Updating entity with id: " + entity.getId());
        ContentValues cv = entityToContentValues(entity);
        return db.update(JIGSAW_TABLE, cv, ID_SELECTION,
                getIdSelection(entity.getId()));
    }

    @Override
    public int delete(long id) {
        Log.d(TAG, "Deleting entity with id: " + id);
        return db.delete(JIGSAW_TABLE, ID_SELECTION,
                getIdSelection(id));
    }
}