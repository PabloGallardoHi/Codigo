package com.proyecto.antonio.projectdesign.peepers;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;

import android.view.SurfaceView;
import android.widget.TextView;
import android.view.SurfaceHolder;
import android.os.PowerManager.WakeLock;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import com.proyecto.antonio.projectdesign.R;


public class StreamCameraFragment extends Fragment implements SurfaceHolder.Callback{

    private static final String PREF_CAMERA = "camera";
    private static final int PREF_CAMERA_INDEX_DEF = 0; //0 camara trasera y 1 camara frontal
    private static final String PREF_FLASH_LIGHT = "flash_light";
    private static final boolean PREF_FLASH_LIGHT_DEF = false;
    private static final String PREF_PORT = "port";
    private static final int PREF_PORT_DEF = 8080;
    private static final String PREF_JPEG_SIZE = "size";
    private static final String PREF_JPEG_QUALITY = "jpeg_quality";
    private static final int PREF_JPEG_QUALITY_DEF = 40;
    // preview sizes will always have at least one element, so this is safe
    private static final int PREF_PREVIEW_SIZE_INDEX_DEF = 0;
    private static final String WAKE_LOCK_TAG = "peepers";

    private boolean mRunning = false;
    private SurfaceHolder mPreviewDisplay = null;
    private boolean mPreviewDisplayCreated = false;
    private CameraStreamer mCameraStreamer = null;

    private SharedPreferences mPrefs = null;
    private int mCameraIndex = PREF_CAMERA_INDEX_DEF;
    private boolean mUseFlashLight = PREF_FLASH_LIGHT_DEF;
    private int mPort = PREF_PORT_DEF;
    private int mJpegQuality = PREF_JPEG_QUALITY_DEF;
    private int mPrevieSizeIndex = PREF_PREVIEW_SIZE_INDEX_DEF;
    private String mIpAddress = "";
    private WakeLock mWakeLock = null;


    public StreamCameraFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View fragmentView = inflater.inflate(R.layout.fragment_stream_camera, container, false);

        new LoadPreferencesTask().execute();

        mPreviewDisplay = ((SurfaceView) fragmentView.findViewById(R.id.camera)).getHolder();
        mPreviewDisplay.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mPreviewDisplay.addCallback(this);

        mIpAddress = tryGetIpV4Address();
        updatePrefCacheAndUi();

        final PowerManager powerManager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, WAKE_LOCK_TAG);

        return fragmentView;
    }


    @Override
    public void onResume(){
        super.onResume();
        mRunning = true;
        if(mPrefs != null){
            mPrefs.registerOnSharedPreferenceChangeListener(mSharedPreferenceListener);
        }
        updatePrefCacheAndUi();
        tryStartCameraStreamer();
        mWakeLock.acquire();
    }


    @Override
    public void onPause(){
        super.onPause();
        mWakeLock.release();
        mRunning = false;
        if(mPrefs != null){
            mPrefs.unregisterOnSharedPreferenceChangeListener(mSharedPreferenceListener);
        }
        ensureCameraStreamerStopped();
    }


    @Override
    public void surfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height){
        //Lo ignoramos
    }


    @Override
    public void surfaceCreated(final SurfaceHolder holder){
        mPreviewDisplayCreated = true;
        tryStartCameraStreamer();
    }


    @Override
    public void surfaceDestroyed(final SurfaceHolder holder){
        mPreviewDisplayCreated = false;
        ensureCameraStreamerStopped();
    } // surfaceDestroyed(SurfaceHolder)


    private void tryStartCameraStreamer(){
        if(mRunning && mPreviewDisplayCreated && mPrefs != null){
            mCameraStreamer = new CameraStreamer(mCameraIndex, mUseFlashLight, mPort,
                    mPrevieSizeIndex, mJpegQuality, mPreviewDisplay);
            mCameraStreamer.start();
        }
    }


    private void ensureCameraStreamerStopped(){
        if(mCameraStreamer != null){
            mCameraStreamer.stop();
            mCameraStreamer = null;
        }
    }


    private final class LoadPreferencesTask extends AsyncTask<Void, Void, SharedPreferences> {

        // Constructor
        private LoadPreferencesTask(){
            super();
        }


        @Override
        protected SharedPreferences doInBackground(final Void... noParams){
            return PreferenceManager.getDefaultSharedPreferences(getActivity());
        }


        @Override
        protected void onPostExecute(final SharedPreferences prefs){
            StreamCameraFragment.this.mPrefs = prefs;
            prefs.registerOnSharedPreferenceChangeListener(mSharedPreferenceListener);
            updatePrefCacheAndUi();
            tryStartCameraStreamer();
        }
    }


    private final SharedPreferences.OnSharedPreferenceChangeListener mSharedPreferenceListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(final SharedPreferences prefs,
                                                      final String key) {
                    updatePrefCacheAndUi();
                }
            };


    private final int getPrefInt(final String key, final int defValue){

        try{
            return Integer.parseInt(mPrefs.getString(key, null));
        } catch(final NullPointerException e){
            return defValue;
        } catch(final NumberFormatException e){
            return defValue;
        }
    }


    private final void updatePrefCacheAndUi(){
        mCameraIndex = getPrefInt(PREF_CAMERA, PREF_CAMERA_INDEX_DEF);
        if(hasFlashLight()){
            if(mPrefs != null){
                mUseFlashLight = mPrefs.getBoolean(PREF_FLASH_LIGHT, PREF_FLASH_LIGHT_DEF);
            } else{
                mUseFlashLight = PREF_FLASH_LIGHT_DEF;
            }
        } else{
            mUseFlashLight = false;
        }

        mPort = getPrefInt(PREF_PORT, PREF_PORT_DEF);
        // El puerto debe estar en el rango [1024 65535]
        if(mPort < 1024){
            mPort = 1024;
        } else if(mPort > 65535){
            mPort = 65535;
        }

        mPrevieSizeIndex = getPrefInt(PREF_JPEG_SIZE, PREF_PREVIEW_SIZE_INDEX_DEF);
        mJpegQuality = getPrefInt(PREF_JPEG_QUALITY, PREF_JPEG_QUALITY_DEF);
        if(mJpegQuality < 0){
            mJpegQuality = 0;
        } else if(mJpegQuality > 100){
            mJpegQuality = 100;
        }
    }


    private boolean hasFlashLight(){
        return getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }


    private static String tryGetIpV4Address(){
        try{
            final Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while(en.hasMoreElements()){
                final NetworkInterface intf = en.nextElement();
                final Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while(enumIpAddr.hasMoreElements()){
                    final InetAddress inetAddress = enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress()){
                        final String addr = inetAddress.getHostAddress().toUpperCase();
                        if(InetAddressUtils.isIPv4Address(addr)){
                            return addr;
                        }
                    }
                }
            }
        } catch(final Exception e){
            //Lo ignoramos
        }
        return null;
    }
}