/**
 * 
 */
package com.monte.carlo.derivatives;

/**
 * @author goria
 *
 */
import static java.lang.Math.log;
import static java.lang.Math.sqrt;
import static java.lang.Math.exp;


	public class EuropeanDigitalCall implements Option {

		public	EuropeanDigitalCall(double Spot_, double Strike_, double r_, double d_, double T_, double vol_){
			Spot = Spot_;
			Strike = Strike_;
			r = r_;
			d = d_;
			Expiry = T_;
			Vol = vol_;
			//pre-compute these
			standardDeviation = Vol*sqrt(Expiry);
			moneyness = log(Spot/Strike);
			d1 = (moneyness + (r-d)*Expiry + 0.5*standardDeviation*standardDeviation)/standardDeviation;
			d2 = d1 - standardDeviation;
		}
		
		@Override
		public double getPrice() {
			// TODO Auto-generated method stub
			return exp(-r*Expiry)*Normals.CumulativeNormal(d2);
		}

		@Override
		public double getDelta() {
			// TODO Auto-generated method stub
			return exp(-r*Expiry)*Normals.NormalDensity(d2)/(Spot*standardDeviation);
		}

		@Override
		public double getGamma() {
			// TODO Auto-generated method stub
			return -d1*exp(-r*Expiry)*Normals.NormalDensity(d2)/(Spot*Spot*standardDeviation*standardDeviation);
		}

		@Override
		public double getVega() {
			// TODO Auto-generated method stub
			return -exp(-r*Expiry)*Normals.NormalDensity(d2)*(sqrt(Expiry)+d2/Vol);
		}

		@Override
		public double getSpeed() {
			// TODO Auto-generated method stub
			return -exp(-r*Expiry)*Normals.NormalDensity(d2)
					*(-2*d1 + (1-d1*d2)/standardDeviation)/(Spot*Spot*Spot*standardDeviation*standardDeviation);
		}

		@Override
		public double getRho() {
			// TODO Auto-generated method stub
			return -Expiry*exp(-r*Expiry)*Normals.CumulativeNormal(d2)
					+sqrt(Expiry)/Vol*exp(-r*Expiry)*Normals.NormalDensity(d2);
		}

		@Override
		public double getTheta() {
			// TODO Auto-generated method stub
			return + r*exp(-r*Expiry)*Normals.CumulativeNormal(d2)
					+ exp(-r*Expiry)*Normals.NormalDensity(d2)*(0.5*d1/Expiry - (r-d)/standardDeviation);
		}


		private double Spot;
		private double Strike;
		private double r;
		private double d;
		private double Expiry;
		private double Vol;
		public static final String[] EuropeanParameters ={"SPOT","STRIKE","RATE","DIVIDEND","VOLATILITY","EXPIRY"};
		
		private double moneyness, standardDeviation, d1, d2;

		@Override
		public double getPrice(double Vol_) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double getDelta(double Vol_) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}


