/*
  ==============================================================================

   This file is part of the JUCE library.
   Copyright (c) 2015 - ROLI Ltd.

   Permission is granted to use this software under the terms of either:
   a) the GPL v2 (or any later version)
   b) the Affero GPL v3

   Details of these licenses can be found at: www.gnu.org/licenses

   JUCE is distributed in the hope that it will be useful, but WITHOUT ANY
   WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
   A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

   ------------------------------------------------------------------------------

   To release a closed-source product which uses JUCE, commercial licenses are
   available: visit www.juce.com for more information.

  ==============================================================================
*/

package com.codegarden.nativenavigation;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import android.view.*;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.graphics.*;
import android.text.ClipboardManager;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import java.lang.Runnable;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;
import java.util.TimerTask;
import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import android.media.AudioManager;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;

import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

//import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;

//==============================================================================
public class JuceActivity extends AppCompatActivity
{
    private native void appNewIntent (Intent intent);
    private native void appOnResume ();

    private native void testCallback();

    private native String getJsonDataString();
    public native void setMessage (String message);


    public native void deliverMessage (long value);
    private native void launchApp (String appFile, String appDataDir);
    private native void quitApp();
    private native void suspendApp();
    private native void resumeApp();
    private native void setScreenSize (int screenWidth, int screenHeight, int dpi);

    private List<Message> messages;
    private String drawerTitle = "Messages";
    private StringBuilder messageTitle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);

        isScreenSaverEnabled = true;
        viewHolder = new ViewHolder (this);
        setContentView (viewHolder);

        // -- Custom native UI
        setContentView(R.layout.main_activity);
        LinearLayout juceViewContainer = (LinearLayout) findViewById(R.id.juce_view_container);
        juceViewContainer.addView(viewHolder);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        messageTitle = new StringBuilder();
        messageTitle.append("JUCE meets Android");
        toolbar.setTitle(messageTitle);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initialiseData();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.left_drawer);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            public void onDrawerClosed(View view) {
                toolbar.setTitle(messageTitle);
            }

            public void onDrawerOpened(View drawerView) {
                toolbar.setTitle(drawerTitle);
            }
        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        MessageListAdapter adapter = new MessageListAdapter(this, messages, messageTitle, drawerLayout);
        recyclerView.setAdapter(adapter);
        //--------------------------------------

        setVolumeControlStream (AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onDestroy()
    {
        quitApp();
        super.onDestroy();

        clearDataCache();
    }

    @Override
    protected void onPause()
    {
        suspendApp();
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //resumeApp();
    }

    @Override
    public void onConfigurationChanged (Configuration cfg)
    {
        super.onConfigurationChanged (cfg);
        setContentView (viewHolder);
    }

    private void initialiseData() {
        Gson gson = new Gson();
        messages = new ArrayList<>();

        Type collectionType = new TypeToken<List<Message>>(){}.getType();
        String json = "";
        //String json = getJsonData();
        Log.d("JSON", json);
        messages = gson.fromJson(json, collectionType);
    }

    public String getJsonData()
    {
        return getJsonDataString();
    }

    private void callAppLauncher()
    {
        //launchApp (getApplicationInfo().publicSourceDir,
        //           getApplicationInfo().dataDir);
    }

    private android.os.Handler messageHandler = new android.os.Handler();

    public final void postMessage (long value)
    {
        messageHandler.post (new MessageCallback (value));
    }

    private final class MessageCallback  implements Runnable
    {
        public MessageCallback (long value_)        { value = value_; }
        public final void run()                     { deliverMessage (value); }

        private long value;
    }

    //==============================================================================
    private ViewHolder viewHolder;
    private boolean isScreenSaverEnabled;
    private java.util.Timer keepAliveTimer;

    public final ComponentPeerView createNewView (boolean opaque, long host)
    {
        ComponentPeerView v = new ComponentPeerView (this, opaque, host);
        viewHolder.addView (v);
        return v;
    }

    public final void deleteView (ComponentPeerView view)
    {
        ViewGroup group = (ViewGroup) (view.getParent());

        if (group != null)
            group.removeView (view);
    }

    public final void deleteNativeSurfaceView (NativeSurfaceView view)
    {
        ViewGroup group = (ViewGroup) (view.getParent());

        if (group != null)
            group.removeView (view);
    }

    final class ViewHolder  extends ViewGroup
    {
        public ViewHolder (Context context)
        {
            super (context);
            setDescendantFocusability (ViewGroup.FOCUS_AFTER_DESCENDANTS);
            setFocusable (false);
        }

        protected final void onLayout (boolean changed, int left, int top, int right, int bottom)
        {
            //setScreenSize (getWidth(), getHeight(), getDPI());

            if (isFirstResize)
            {
                isFirstResize = false;
                callAppLauncher();
            }
        }

        private final int getDPI()
        {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics (metrics);
            return metrics.densityDpi;
        }

        private boolean isFirstResize = true;
    }

    public final void excludeClipRegion (android.graphics.Canvas canvas, float left, float top, float right, float bottom)
    {
        canvas.clipRect (left, top, right, bottom, android.graphics.Region.Op.DIFFERENCE);
    }

    //==============================================================================
    public final void setScreenSaver (boolean enabled)
    {
        if (isScreenSaverEnabled != enabled)
        {
            isScreenSaverEnabled = enabled;

            if (keepAliveTimer != null)
            {
                keepAliveTimer.cancel();
                keepAliveTimer = null;
            }

            if (enabled)
            {
                getWindow().clearFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            else
            {
                getWindow().addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                // If no user input is received after about 3 seconds, the OS will lower the
                // task's priority, so this timer forces it to be kept active.
                keepAliveTimer = new java.util.Timer();

                keepAliveTimer.scheduleAtFixedRate (new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        android.app.Instrumentation instrumentation = new android.app.Instrumentation();

                        try
                        {
                            instrumentation.sendKeyDownUpSync (KeyEvent.KEYCODE_UNKNOWN);
                        }
                        catch (Exception e)
                        {
                        }
                    }
                }, 2000, 2000);
            }
        }
    }

    public final boolean getScreenSaver()
    {
        return isScreenSaverEnabled;
    }

    //==============================================================================
    public final String getClipboardContent()
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService (CLIPBOARD_SERVICE);
        return clipboard.getText().toString();
    }

    public final void setClipboardContent (String newText)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService (CLIPBOARD_SERVICE);
        clipboard.setText (newText);
    }

    //==============================================================================
    public final void showMessageBox (String title, String message, final long callback)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder (this);
        builder.setTitle (title)
               .setMessage (message)
               .setCancelable (true)
               .setPositiveButton ("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick (DialogInterface dialog, int id)
                        {
                            dialog.cancel();
                            JuceActivity.this.alertDismissed (callback, 0);
                        }
                    });

        builder.create().show();
    }

    public final void showOkCancelBox (String title, String message, final long callback)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder (this);
        builder.setTitle (title)
               .setMessage (message)
               .setCancelable (true)
               .setPositiveButton ("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick (DialogInterface dialog, int id)
                        {
                            dialog.cancel();
                            JuceActivity.this.alertDismissed (callback, 1);
                        }
                    })
               .setNegativeButton ("Cancel", new DialogInterface.OnClickListener()
                    {
                        public void onClick (DialogInterface dialog, int id)
                        {
                            dialog.cancel();
                            JuceActivity.this.alertDismissed (callback, 0);
                        }
                    });

        builder.create().show();
    }

    public final void showYesNoCancelBox (String title, String message, final long callback)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder (this);
        builder.setTitle (title)
               .setMessage (message)
               .setCancelable (true)
               .setPositiveButton ("Yes", new DialogInterface.OnClickListener()
                    {
                        public void onClick (DialogInterface dialog, int id)
                        {
                            dialog.cancel();
                            JuceActivity.this.alertDismissed (callback, 1);
                        }
                    })
               .setNegativeButton ("No", new DialogInterface.OnClickListener()
                    {
                        public void onClick (DialogInterface dialog, int id)
                        {
                            dialog.cancel();
                            JuceActivity.this.alertDismissed (callback, 2);
                        }
                    })
               .setNeutralButton ("Cancel", new DialogInterface.OnClickListener()
                    {
                        public void onClick (DialogInterface dialog, int id)
                        {
                            dialog.cancel();
                            JuceActivity.this.alertDismissed (callback, 0);
                        }
                    });

        builder.create().show();
    }

    public native void alertDismissed (long callback, int id);

    //==============================================================================
    public final class ComponentPeerView extends ViewGroup
                                         implements View.OnFocusChangeListener
    {
        private native void handlePaint (long host, Canvas canvas, Paint paint);
        private native void handleMouseDown (long host, int index, float x, float y, long time);
        private native void handleMouseDrag (long host, int index, float x, float y, long time);
        private native void handleMouseUp   (long host, int index, float x, float y, long time);
        private native void handleKeyDown (long host, int keycode, int textchar);
        private native void handleKeyUp (long host, int keycode, int textchar);
        private native void viewSizeChanged (long host);
        private native void focusChanged (long host, boolean hasFocus);

        public ComponentPeerView (Context context, boolean opaque_, long host)
        {
            super (context);
            this.host = host;
            setWillNotDraw (false);
            opaque = opaque_;

            setFocusable (true);
            setFocusableInTouchMode (true);
            setOnFocusChangeListener (this);
            requestFocus();

            // swap red and blue colours to match internal opengl texture format
            ColorMatrix colorMatrix = new ColorMatrix();

            float[] colorTransform = { 0,    0,    1.0f, 0,    0,
                                       0,    1.0f, 0,    0,    0,
                                       1.0f, 0,    0,    0,    0,
                                       0,    0,    0,    1.0f, 0 };

            colorMatrix.set (colorTransform);
            paint.setColorFilter (new ColorMatrixColorFilter (colorMatrix));
        }

        @Override
        public void onDraw (Canvas canvas)
        {
            handlePaint (host, canvas, paint);
        }

        @Override
        public boolean isOpaque()
        {
            return opaque;
        }

        private boolean opaque;
        private long host;
        private Paint paint = new Paint();

        @Override
        public boolean onTouchEvent (MotionEvent event)
        {
            int action = event.getAction();
            long time = event.getEventTime();

            switch (action & MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_DOWN:
                    handleMouseDown (host, event.getPointerId(0), event.getX(), event.getY(), time);
                    return true;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    handleMouseUp (host, event.getPointerId(0), event.getX(), event.getY(), time);
                    return true;

                case MotionEvent.ACTION_MOVE:
                {
                    int n = event.getPointerCount();
                    for (int i = 0; i < n; ++i)
                        handleMouseDrag (host, event.getPointerId(i), event.getX(i), event.getY(i), time);

                    return true;
                }

                case MotionEvent.ACTION_POINTER_UP:
                {
                    int i = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    handleMouseUp (host, event.getPointerId(i), event.getX(i), event.getY(i), time);
                    return true;
                }

                case MotionEvent.ACTION_POINTER_DOWN:
                {
                    int i = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    handleMouseDown (host, event.getPointerId(i), event.getX(i), event.getY(i), time);
                    return true;
                }

                default:
                    break;
            }

            return false;
        }

        public void showKeyboard (String type)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService (Context.INPUT_METHOD_SERVICE);

            if (imm != null)
            {
                if (type.length() > 0)
                {
                    imm.showSoftInput (this, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT);
                    imm.setInputMethod (getWindowToken(), type);
                }
                else
                {
                    imm.hideSoftInputFromWindow (getWindowToken(), 0);
                }
            }
        }

        @Override
        public boolean onKeyDown (int keyCode, KeyEvent event)
        {
            switch (keyCode)
            {
                case KeyEvent.KEYCODE_VOLUME_UP:
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    return super.onKeyDown (keyCode, event);

                default: break;
            }

            handleKeyDown (host, keyCode, event.getUnicodeChar());
            return true;
        }

        @Override
        public boolean onKeyUp (int keyCode, KeyEvent event)
        {
            handleKeyUp (host, keyCode, event.getUnicodeChar());
            return true;
        }

        @Override
        public boolean onKeyMultiple (int keyCode, int count, KeyEvent event)
        {
            if (keyCode != KeyEvent.KEYCODE_UNKNOWN || event.getAction() != KeyEvent.ACTION_MULTIPLE)
                return super.onKeyMultiple (keyCode, count, event);

            if (event.getCharacters() != null)
            {
                int utf8Char = event.getCharacters().codePointAt (0);
                handleKeyDown (host, utf8Char, utf8Char);
                return true;
            }

            return false;
        }

        // this is here to make keyboard entry work on a Galaxy Tab2 10.1
        @Override
        public InputConnection onCreateInputConnection (EditorInfo outAttrs)
        {
            outAttrs.actionLabel = "";
            outAttrs.hintText = "";
            outAttrs.initialCapsMode = 0;
            outAttrs.initialSelEnd = outAttrs.initialSelStart = -1;
            outAttrs.label = "";
            outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI;
            outAttrs.inputType = InputType.TYPE_NULL;

            return new BaseInputConnection (this, false);
        }

        //==============================================================================
        @Override
        protected void onSizeChanged (int w, int h, int oldw, int oldh)
        {
            super.onSizeChanged (w, h, oldw, oldh);
            viewSizeChanged (host);
        }

        @Override
        protected void onLayout (boolean changed, int left, int top, int right, int bottom)
        {
            for (int i = getChildCount(); --i >= 0;)
                requestTransparentRegion (getChildAt (i));
        }

        @Override
        public void onFocusChange (View v, boolean hasFocus)
        {
            if (v == this)
                focusChanged (host, hasFocus);
        }

        public void setViewName (String newName)    {}

        public boolean isVisible()                  { return getVisibility() == VISIBLE; }
        public void setVisible (boolean b)          { setVisibility (b ? VISIBLE : INVISIBLE); }

        public boolean containsPoint (int x, int y)
        {
            return true; //xxx needs to check overlapping views
        }
    }

    //==============================================================================
    public static class NativeSurfaceView    extends SurfaceView
                                          implements SurfaceHolder.Callback
    {
        private native void dispatchDrawNative (long nativeContextPtr, Canvas canvas);
        private native void surfaceCreatedNative (long nativeContextptr, SurfaceHolder holder);
        private native void surfaceDestroyedNative (long nativeContextptr, SurfaceHolder holder);
        private native void surfaceChangedNative (long nativeContextptr, SurfaceHolder holder,
                                                  int format, int width, int height);

        private long nativeContext = 0;

        NativeSurfaceView (Context context, long nativeContextPtr)
        {
            super (context);
            nativeContext = nativeContextPtr;
        }

        public Surface getNativeSurface()
        {
            Surface retval = null;

            SurfaceHolder holder = getHolder();
            if (holder != null)
                retval = holder.getSurface();

            return retval;
        }

        //==============================================================================
        @Override
        public void surfaceChanged (SurfaceHolder holder, int format, int width, int height)
        {
            surfaceChangedNative (nativeContext, holder, format, width, height);
        }

        @Override
        public void surfaceCreated (SurfaceHolder holder)
        {
            surfaceCreatedNative (nativeContext, holder);
        }

        @Override
        public void surfaceDestroyed (SurfaceHolder holder)
        {
            surfaceDestroyedNative (nativeContext, holder);
        }

        @Override
        protected void dispatchDraw (Canvas canvas)
        {
            super.dispatchDraw (canvas);
            dispatchDrawNative (nativeContext, canvas);
        }

        //==============================================================================
        @Override
        protected void onAttachedToWindow ()
        {
            super.onAttachedToWindow();
            getHolder().addCallback (this);
        }

        @Override
        protected void onDetachedFromWindow ()
        {
            super.onDetachedFromWindow();
            getHolder().removeCallback (this);
        }
    }

    public NativeSurfaceView createNativeSurfaceView(long nativeSurfacePtr)
    {
        return new NativeSurfaceView (this, nativeSurfacePtr);
    }

    //==============================================================================
    public final int[] renderGlyph (char glyph, Paint paint, android.graphics.Matrix matrix, Rect bounds)
    {
        Path p = new Path();
        paint.getTextPath (String.valueOf (glyph), 0, 1, 0.0f, 0.0f, p);

        RectF boundsF = new RectF();
        p.computeBounds (boundsF, true);
        matrix.mapRect (boundsF);

        boundsF.roundOut (bounds);
        bounds.left--;
        bounds.right++;

        final int w = bounds.width();
        final int h = Math.max (1, bounds.height());

        Bitmap bm = Bitmap.createBitmap (w, h, Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas (bm);
        matrix.postTranslate (-bounds.left, -bounds.top);
        c.setMatrix (matrix);
        c.drawPath (p, paint);

        final int sizeNeeded = w * h;
        if (cachedRenderArray.length < sizeNeeded)
            cachedRenderArray = new int [sizeNeeded];

        bm.getPixels (cachedRenderArray, 0, w, 0, 0, w, h);
        bm.recycle();
        return cachedRenderArray;
    }

    private int[] cachedRenderArray = new int [256];

    //==============================================================================
    public static class HTTPStream
    {
        public HTTPStream (HttpURLConnection connection_,
                           int[] statusCode, StringBuffer responseHeaders) throws IOException
        {
            connection = connection_;

            try
            {
                inputStream = new BufferedInputStream (connection.getInputStream());
            }
            catch (IOException e)
            {
                if (connection.getResponseCode() < 400)
                    throw e;
            }
            finally
            {
                statusCode[0] = connection.getResponseCode();
            }

            if (statusCode[0] >= 400)
                inputStream = connection.getErrorStream();
            else
                inputStream = connection.getInputStream();

            for (java.util.Map.Entry<String, java.util.List<String>> entry : connection.getHeaderFields().entrySet())
                if (entry.getKey() != null && entry.getValue() != null)
                    responseHeaders.append (entry.getKey() + ": "
                                             + android.text.TextUtils.join (",", entry.getValue()) + "\n");
        }

        public final void release()
        {
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {}

            connection.disconnect();
        }

        public final int read (byte[] buffer, int numBytes)
        {
            int num = 0;

            try
            {
                num = inputStream.read (buffer, 0, numBytes);
            }
            catch (IOException e)
            {}

            if (num > 0)
                position += num;

            return num;
        }

        public final long getPosition()                 { return position; }
        public final long getTotalLength()              { return -1; }
        public final boolean isExhausted()              { return false; }
        public final boolean setPosition (long newPos)  { return false; }

        private HttpURLConnection connection;
        private InputStream inputStream;
        private long position;
    }

    public static final HTTPStream createHTTPStream (String address, boolean isPost, byte[] postData,
                                                     String headers, int timeOutMs, int[] statusCode,
                                                     StringBuffer responseHeaders, int numRedirectsToFollow,
                                                     String httpRequestCmd)
    {
        // timeout parameter of zero for HttpUrlConnection is a blocking connect (negative value for juce::URL)
        if (timeOutMs < 0)
            timeOutMs = 0;
        else if (timeOutMs == 0)
            timeOutMs = 30000;

        // headers - if not empty, this string is appended onto the headers that are used for the request. It must therefore be a valid set of HTML header directives, separated by newlines.
        // So convert headers string to an array, with an element for each line
        String headerLines[] = headers.split("\\n");

        for (;;)
        {
            try
            {
                HttpURLConnection connection = (HttpURLConnection) (new URL(address).openConnection());

                if (connection != null)
                {
                    try
                    {
                        connection.setInstanceFollowRedirects (false);
                        connection.setConnectTimeout (timeOutMs);
                        connection.setReadTimeout (timeOutMs);

                        // Set request headers
                        for (int i = 0; i < headerLines.length; ++i)
                        {
                            int pos = headerLines[i].indexOf (":");

                            if (pos > 0 && pos < headerLines[i].length())
                            {
                                String field = headerLines[i].substring (0, pos);
                                String value = headerLines[i].substring (pos + 1);

                                if (value.length() > 0)
                                    connection.setRequestProperty (field, value);
                            }
                        }

                        connection.setRequestMethod (httpRequestCmd);
                        if (isPost)
                        {
                            connection.setDoOutput (true);

                            if (postData != null)
                            {
                                OutputStream out = connection.getOutputStream();
                                out.write(postData);
                                out.flush();
                            }
                        }

                        HTTPStream httpStream = new HTTPStream (connection, statusCode, responseHeaders);

                        // Process redirect & continue as necessary
                        int status = statusCode[0];

                        if (--numRedirectsToFollow >= 0
                             && (status == 301 || status == 302 || status == 303 || status == 307))
                        {
                            // Assumes only one occurrence of "Location"
                            int pos1 = responseHeaders.indexOf ("Location:") + 10;
                            int pos2 = responseHeaders.indexOf ("\n", pos1);

                            if (pos2 > pos1)
                            {
                                String newLocation = responseHeaders.substring(pos1, pos2);
                                // Handle newLocation whether it's absolute or relative
                                URL baseUrl = new URL (address);
                                URL newUrl = new URL (baseUrl, newLocation);
                                String transformedNewLocation = newUrl.toString();

                                if (transformedNewLocation != address)
                                {
                                    address = transformedNewLocation;
                                    // Clear responseHeaders before next iteration
                                    responseHeaders.delete (0, responseHeaders.length());
                                    continue;
                                }
                            }
                        }

                        return httpStream;
                    }
                    catch (Throwable e)
                    {
                        connection.disconnect();
                    }
                }
            }
            catch (Throwable e) {}

            return null;
        }
    }

    public final void launchURL (String url)
    {
        startActivity (new Intent (Intent.ACTION_VIEW, Uri.parse (url)));
    }

    public static final String getLocaleValue (boolean isRegion)
    {
        java.util.Locale locale = java.util.Locale.getDefault();

        return isRegion ? locale.getDisplayCountry  (java.util.Locale.US)
                        : locale.getDisplayLanguage (java.util.Locale.US);
    }

    //==============================================================================
    private final class SingleMediaScanner  implements MediaScannerConnectionClient
    {
        public SingleMediaScanner (Context context, String filename)
        {
            file = filename;
            msc = new MediaScannerConnection (context, this);
            msc.connect();
        }

        @Override
        public void onMediaScannerConnected()
        {
            msc.scanFile (file, null);
        }

        @Override
        public void onScanCompleted (String path, Uri uri)
        {
            msc.disconnect();
        }

        private MediaScannerConnection msc;
        private String file;
    }

    public final void scanFile (String filename)
    {
        new SingleMediaScanner (this, filename);
    }

    public final Typeface getTypeFaceFromAsset (String assetName)
    {
        try
        {
            return Typeface.createFromAsset (this.getResources().getAssets(), assetName);
        }
        catch (Throwable e) {}

        return null;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex (byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; ++j)
        {
            int v = bytes[j] & 0xff;
            hexChars[j * 2]     = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0f];
        }

        return new String (hexChars);
    }

    final private java.util.Map dataCache = new java.util.HashMap();

    synchronized private final File getDataCacheFile (byte[] data)
    {
        try
        {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance ("MD5");
            digest.update (data);

            String key = bytesToHex (digest.digest());

            if (dataCache.containsKey (key))
                return (File) dataCache.get (key);

            File f = new File (this.getCacheDir(), "bindata_" + key);
            f.delete();
            FileOutputStream os = new FileOutputStream (f);
            os.write (data, 0, data.length);
            dataCache.put (key, f);
            return f;
        }
        catch (Throwable e) {}

        return null;
    }

    private final void clearDataCache()
    {
        java.util.Iterator it = dataCache.values().iterator();

        while (it.hasNext())
        {
            File f = (File) it.next();
            f.delete();
        }
    }

    public final Typeface getTypeFaceFromByteArray (byte[] data)
    {
        try
        {
            File f = getDataCacheFile (data);

            if (f != null)
                return Typeface.createFromFile (f);
        }
        catch (Exception e)
        {
            Log.e ("JUCE", e.toString());
        }

        return null;
    }

    public final int getAndroidSDKVersion()
    {
        return android.os.Build.VERSION.SDK_INT;
    }

    public final String audioManagerGetProperty (String property)
    {
        Object obj = getSystemService (AUDIO_SERVICE);
        if (obj == null)
            return null;

        java.lang.reflect.Method method;
        try {
            method = obj.getClass().getMethod ("getProperty", String.class);
        } catch (SecurityException e) {
            return null;
        } catch (NoSuchMethodException e) {
            return null;
        }

        if (method == null)
            return null;

        try {
            return (String) method.invoke (obj, property);
        } catch (java.lang.IllegalArgumentException e) {
        } catch (java.lang.IllegalAccessException e) {
        } catch (java.lang.reflect.InvocationTargetException e) {
        }

        return null;
    }

    public final int setCurrentThreadPriority (int priority)
    {
        android.os.Process.setThreadPriority (android.os.Process.myTid(), priority);
        return android.os.Process.getThreadPriority (android.os.Process.myTid());
    }

    public final boolean hasSystemFeature (String property)
    {
        return getPackageManager().hasSystemFeature (property);
    }

    private static class JuceThread extends Thread
    {
        private native void runThread (long host);

        public JuceThread (long host)
        {
            _this = host;
        }

        public void run()
        {
            runThread(_this);
        }

        private long _this;
    }

    public final Thread createNewThread(long host)
    {
        return new JuceThread(host);
    }
}

