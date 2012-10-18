package com.monte.carlo.derivatives;
import static java.lang.Math.*;
public class PayOffCall implements PayOff {

	@Override
	public double PayOff(double Spot, double Strike) {
		
		return max(Spot-Strike,0.0);
	}

}
