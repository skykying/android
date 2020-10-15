package com.coboltforge.dontmind.xbox.ui;

import android.graphics.Paint;
import android.graphics.Rect;

import com.coboltforge.dontmind.xbox.protocol.RfbProto;
import com.coboltforge.dontmind.xbox.utils.Utils;

import java.io.IOException;
import java.util.Arrays;
import android.util.Log;

public class FullBufferBitmapData extends AbstractBitmapData {

    /**
     * Multiply this times total number of pixels to get estimate of process size with all buffers plus
     * safety factor
     */
    static final int CAPACITY_MULTIPLIER = 7;
    int xoffset;
    int yoffset;

    /**
     * @param p
     * @param c
     */
    public FullBufferBitmapData(RfbProto p, VncCanvas c, int capacity) {
        super(p, c);
        framebufferwidth = rfb.framebufferWidth;
        framebufferheight = rfb.framebufferHeight;
        bitmapwidth = Utils.nextPow2(framebufferwidth);
        bitmapheight = Utils.nextPow2(framebufferheight);
        bitmapPixels = new int[bitmapwidth * bitmapheight];

        Log.i("FBBM", "bitmapsize = (" + bitmapwidth + "," + bitmapheight + ")");
    }

    /* (non-Javadoc)
     * @see com.coboltforge.dontmind.multivnc.ui.AbstractBitmapData#copyRect(android.graphics.Rect, android.graphics.Rect, android.graphics.Paint)
     */
    @Override
    void copyRect(Rect src, Rect dest, Paint paint) {
        // TODO copy rect working?
        throw new RuntimeException("copyrect Not implemented");
    }


    /* (non-Javadoc)
     * @see com.coboltforge.dontmind.multivnc.ui.AbstractBitmapData#drawRect(int, int, int, int, android.graphics.Paint)
     */
    @Override
    synchronized void drawRect(int x, int y, int w, int h, Paint paint) {
        int color = paint.getColor();

        int offset = offset(x, y);
        if (w > 10) {
            for (int j = 0; j < h; j++, offset += bitmapwidth) {
                Arrays.fill(bitmapPixels, offset, offset + w, color);
            }
        } else {
            for (int j = 0; j < h; j++, offset += bitmapwidth - w) {
                for (int k = 0; k < w; k++, offset++) {
                    bitmapPixels[offset] = color;
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.coboltforge.dontmind.multivnc.ui.AbstractBitmapData#offset(int, int)
     */
    @Override
    int offset(int x, int y) {
        return x + y * bitmapwidth;
    }

    /* (non-Javadoc)
     * @see com.coboltforge.dontmind.multivnc.ui.AbstractBitmapData#scrollChanged(int, int)
     */
    public void scrollChanged(int newx, int newy) {
        xoffset = newx;
        yoffset = newy;
    }

    /* (non-Javadoc)
     * @see com.coboltforge.dontmind.multivnc.ui.AbstractBitmapData#syncScroll()
     */
    @Override
    void syncScroll() {
        // Don't need to do anything here

    }

    /* (non-Javadoc)
     * @see com.coboltforge.dontmind.multivnc.ui.AbstractBitmapData#updateBitmap(int, int, int, int)
     */
    @Override
    void updateBitmap(int x, int y, int w, int h) {
        // Don't need to do anything here
    }

    /* (non-Javadoc)
     * @see com.coboltforge.dontmind.multivnc.ui.AbstractBitmapData#validDraw(int, int, int, int)
     */
    @Override
    boolean validDraw(int x, int y, int w, int h) {
        return true;
    }

    /* (non-Javadoc)
     * @see com.coboltforge.dontmind.multivnc.ui.AbstractBitmapData#writeFullUpdateRequest(boolean)
     */
    @Override
    void writeFullUpdateRequest(boolean incremental) throws IOException {
        rfb.writeFramebufferUpdateRequest(0, 0, framebufferwidth, framebufferheight, incremental);
    }

}
