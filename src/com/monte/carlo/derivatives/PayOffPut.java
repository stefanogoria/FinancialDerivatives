package com.monte.carlo.derivatives;
import static java.lang.Math.*;
public class PayOffPut implements PayOff {

	@Override
	public double PayOff(double Spot, double Strike) {
		// TODO Auto-generated method stub
		return max(Strike-Spot,0.0);
	}

}
