package geogebra.touch;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableEvent;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableHandler;
import com.googlecode.gwtphonegap.client.event.BackButtonPressedHandler;

public class PhoneGapManager {
	
	private static PhoneGap phoneGap = (PhoneGap) GWT.create(PhoneGap.class);
	
	public static PhoneGap getPhoneGap() {
		return phoneGap;
	}

	public static void initializePhoneGap(final BackButtonPressedHandler handler) {
		if(handler != null){
		phoneGap.addHandler(new PhoneGapAvailableHandler(){

			@Override
            public void onPhoneGapAvailable(PhoneGapAvailableEvent event) {
	            phoneGap.getEvent().getBackButton().addBackButtonPressedHandler(handler);
	            
            }});
		}
	    phoneGap.initializePhoneGap();
    }
}
