package com.voxel.android.livewallpaper;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView.Renderer;
import android.view.SurfaceHolder;

public abstract class OpenGLES30WallpaperService extends GLWallpaperService {
	@Override
	public Engine onCreateEngine() {
		return new OpenGLES2Engine();
	}
	
	class OpenGLES2Engine extends GLWallpaperService.GLEngine {

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			
			// Check if the system supports OpenGL ES 2.0.
			final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

			final boolean supportsEs31 = configurationInfo.reqGlEsVersion >= 0x30000;
			
			if (supportsEs31)
			{
				// Request an OpenGL ES 3.1 compatible context.
				setEGLContextClientVersion(3);
				
				// On Honeycomb+ devices, this improves the performance when
				// leaving and resuming the live wallpaper.
				setPreserveEGLContextOnPause(true);

				// Set the renderer to our user-defined renderer.
				setRenderer(getNewRenderer());
			} 
			else 
			{
				// This is where you could create an OpenGL ES 1.x compatible
				// renderer if you wanted to support both ES 1 and ES 2.
				return;
			}			
		}
	}	
	
	abstract Renderer getNewRenderer();
}
