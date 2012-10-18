/**
 * 
 */
package com.monte.carlo.derivatives;

/**
 * @author goria
 *
 */
public class PayOffDigitalCall implements PayOff {

	/* (non-Javadoc)
	 * @see com.monte.carlo.derivatives.PayOff#PayOff(double, double)
	 */
	@Override
	public double PayOff(double Spot, double Strike) {
		if(Spot>Strike)
			return 1.;
		else
			return 0.;
	}

}
