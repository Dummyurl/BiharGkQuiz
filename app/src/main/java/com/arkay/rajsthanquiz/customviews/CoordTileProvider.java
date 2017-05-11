package com.arkay.rajsthanquiz.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;

/**
 * Created by arkayapps on 18/02/17.
 */

public class CoordTileProvider implements TileProvider {

    private static final int TILE_SIZE_DP = 256;

    private final float mScaleFactor;

    private final Bitmap mBorderTile;

    public CoordTileProvider(Context context) {
        /* Scale factor based on density, with a 0.2 multiplier to increase tile generation
         * speed */
        mScaleFactor = context.getResources().getDisplayMetrics().density * 0.2f;
        Paint paint = new Paint();
        paint.setColor(Color.argb(150,100,100,100));
        mBorderTile = Bitmap.createBitmap((int) (TILE_SIZE_DP * mScaleFactor),
                (int) (TILE_SIZE_DP * mScaleFactor), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBorderTile);
        canvas.drawRect(0, 0, TILE_SIZE_DP * mScaleFactor, TILE_SIZE_DP * mScaleFactor,
                paint);
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
        Bitmap coordTile = drawTileCoords(x, y, zoom);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        coordTile.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] bitmapData = stream.toByteArray();
        return new Tile((int) (TILE_SIZE_DP * mScaleFactor),
                (int) (TILE_SIZE_DP * mScaleFactor), bitmapData);
    }

    private Bitmap drawTileCoords(int x, int y, int zoom) {
        Bitmap copy = null;
        synchronized (mBorderTile) {
            copy = mBorderTile.copy(android.graphics.Bitmap.Config.ARGB_8888, true);
        }
        return copy;
    }
}