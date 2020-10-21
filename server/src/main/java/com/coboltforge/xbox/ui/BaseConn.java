package com.coboltforge.xbox.ui;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.KeyEvent;

import com.coboltforge.xbox.utils.Utils;

import java.util.concurrent.ConcurrentLinkedQueue;


public class BaseConn {

    // Useful shortcuts for modifier masks.
    public final static int CTRL_MASK = KeyEvent.META_SYM_ON;
    public final static int SHIFT_MASK = KeyEvent.META_SHIFT_ON;
    public final static int META_MASK = 0;
    public final static int ALT_MASK = KeyEvent.META_ALT_ON;
    public static final int MOUSE_BUTTON_NONE = 0;
    public static final int MOUSE_BUTTON_LEFT = 1;
    public static final int MOUSE_BUTTON_MIDDLE = 2;
    public static final int MOUSE_BUTTON_RIGHT = 4;
    public static final int MOUSE_BUTTON_SCROLL_UP = 8;
    public static final int MOUSE_BUTTON_SCROLL_DOWN = 16;


    protected Paint handleRREPaint;
        // message queue for communicating with the output worker thread
    protected ConcurrentLinkedQueue<OutputEvent> outputEventQueue = new ConcurrentLinkedQueue<OutputEvent>();

    private final static String TAG = "BaseConn";


    /*
            to make a connection, call
            Setup(), then
            Listen() (optional), then
            Init(), then
            Shutdown, then
            Cleanup()

            NB: If Init() fails, you have to call Setup() again!

            The semantic counterparts are:
               Setup() <-> Cleanup()
               Init()  <-> Shutdown()
     */
    protected byte[] backgroundColorBuffer = new byte[4];
    protected Paint handleZRLERectPaint = new Paint();
    protected int[] handleZRLERectPalette = new int[128];
    protected byte[] handleZlibRectBuffer = new byte[128];
    protected byte[] readPixelsBuffer = new byte[128];
    protected byte[] handleRawRectBuffer = new byte[128];


    public BaseConn() {
        handleRREPaint = new Paint();
        handleRREPaint.setStyle(Style.FILL);

        if (Utils.DEBUG()) Log.d(TAG, this + " constructed!");
    }

    protected void finalize() {
        if (Utils.DEBUG()) Log.d(TAG, this + " finalized!");
    }

    boolean Setup() {
        return true;
    }

    void Cleanup() {

    }

    protected class OutputEvent {

        public FullFramebufferUpdateRequest ffur;
        public FramebufferUpdateRequest fur;
        public PointerEvent pointer;
        public KeyboardEvent key;
        public ClientCutText cuttext;

        public OutputEvent(int x, int y, int modifiers, int pointerMask) {
            pointer = new PointerEvent();
            pointer.x = x;
            pointer.y = y;
            pointer.modifiers = modifiers;
            pointer.mask = pointerMask;
        }

        public OutputEvent(int keyCode, int metaState, boolean down) {
            key = new KeyboardEvent();
            key.keyCode = keyCode;
            key.metaState = metaState;
            key.down = down;
        }

        public OutputEvent(boolean incremental) {
            ffur = new FullFramebufferUpdateRequest();
            ffur.incremental = incremental;
        }

        public OutputEvent(int x, int y, int w, int h, boolean incremental) {
            fur = new FramebufferUpdateRequest();
            fur.x = x;
            fur.y = y;
            fur.w = w;
            fur.h = h;
            fur.incremental = incremental;
        }

        public OutputEvent(String text) {
            cuttext = new ClientCutText();
            cuttext.text = text;
        }

        protected class PointerEvent {
            int x;
            int y;
            int mask;
            int modifiers;
        }

        protected class KeyboardEvent {
            int keyCode;
            int metaState;
            boolean down;
        }

        protected class FullFramebufferUpdateRequest {
            boolean incremental;
        }

        protected class FramebufferUpdateRequest {
            int x, y, w, h;
            boolean incremental;
        }

        protected class ClientCutText {
            String text;
        }
    }



}

