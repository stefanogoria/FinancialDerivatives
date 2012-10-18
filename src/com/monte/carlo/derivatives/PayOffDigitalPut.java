package com.monte.carlo.derivatives;

public class PayOffDigitalPut implements PayOff {

	@Override
	public double PayOff(double Spot, double Strike) {
		if(Spot<Strike)
			return 1.;
		else
			return 0.;
	}

}
