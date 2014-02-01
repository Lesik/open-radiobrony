package com.lesikapk.openradiobrony;

import android.os.Build;

public class Utils {

	/**
     * Used to determine if the device is running Froyo or greater
     * 
     * @return True if the device is running Froyo or greater, false otherwise
     */
    public static final boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * Used to determine if the device is running Gingerbread or greater
     * 
     * @return True if the device is running Gingerbread or greater, false
     *         otherwise
     */
    public static final boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * Used to determine if the device is running Honeycomb or greater
     * 
     * @return True if the device is running Honeycomb or greater, false
     *         otherwise
     */
    public static final boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Used to determine if the device is running Honeycomb-MR1 or greater
     * 
     * @return True if the device is running Honeycomb-MR1 or greater, false
     *         otherwise
     */
    public static final boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * Used to determine if the device is running ICS or greater
     * 
     * @return True if the device is running Ice Cream Sandwich or greater,
     *         false otherwise
     */
    public static final boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * Used to determine if the device is running Jelly Bean or greater
     * 
     * @return True if the device is running Jelly Bean or greater, false
     *         otherwise
     */
    public static final boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }
	
}
