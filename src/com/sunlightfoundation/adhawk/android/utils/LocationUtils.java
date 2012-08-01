package com.sunlightfoundation.adhawk.android.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class LocationUtils {
	
	// cheap way to get last location using whatever available providers are on
	public static Location getLastKnownLocation(Context context) {
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location location = null;
		
		if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location == null && manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		return location;
	}
	
	
	// currently unused, but could be used to more robustly determine the current location
	
//	public static final int TIMEOUT = 20000; // milliseconds
//	public static final int MIN_TIME = 6000; // milliseconds
//	public static final int MIN_DIST = 100; // meters
//
//	public interface LocationListenerTimeout extends LocationListener {
//		public void onTimeout(String provider);
//	}
//
//	private static Message timeoutMsg(String provider) {
//		Message msg = new Message();
//		msg.obj = provider;
//		return msg;
//	}
//
//	public static class LocationTimer extends Timer {
//		private LocationListenerTimeout listener;
//		private LocationManager manager;
//		private String provider;
//		private Handler handler;
//
//		public LocationTimer(LocationListenerTimeout listener, LocationManager manager, String provider, Handler handler) {
//			this.listener = listener;
//			this.manager = manager;
//			this.provider = provider;
//			this.handler = handler;
//		}
//
//		public void start() {
//			if (!manager.isProviderEnabled(provider)) {
//				//Log.d(Utils.TAG, "LocationUtils - start(): provider " + provider + " is not enabled!");
//				handler.sendMessage(timeoutMsg(provider));
//			} else {
//				//Log.d(Utils.TAG, "LocationUtils - start(): started timer for provider " + provider);
//				Timeout task = new Timeout(listener, manager, provider, handler);
//				schedule(task, TIMEOUT);
//				manager.requestLocationUpdates(provider, MIN_TIME, MIN_DIST, listener);
//			}
//		}
//
//		@Override
//		public void cancel() {
//			super.cancel();
//			manager.removeUpdates(listener);
//			//Log.d(Utils.TAG, "LocationUtils - cancel(): cancel updating timer and remove listener");
//		}
//	}
//
//	public static class Timeout extends TimerTask {
//		private LocationListenerTimeout listener;
//		private LocationManager manager;
//		private String provider;
//		private Handler handler;
//
//		public Timeout(LocationListenerTimeout listener, LocationManager manager, String provider, Handler handler) {
//			this.listener = listener;
//			this.manager = manager;
//			this.provider = provider;
//			this.handler = handler;
//		}
//
//		@Override
//		public void run() {
//			//Log.d(Utils.TAG, "LocationUtils - run(): Timeout! Remove listener from provider " + provider);
//			manager.removeUpdates(listener);
//			handler.sendMessage(timeoutMsg(provider));
//		}
//	}
//
//	public static LocationTimer requestLocationUpdate(Context context, Handler handler, String provider) {
//		//Log.d(Utils.TAG, "LocationUtils - requestLocationUpdate(): from provider " + provider);
//		
//		LocationListenerTimeout listener = (LocationListenerTimeout) context;
//		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//
//		LocationTimer timer = new LocationTimer(listener, manager, provider, handler);
//		timer.start();
//		return timer;
//	}

}
