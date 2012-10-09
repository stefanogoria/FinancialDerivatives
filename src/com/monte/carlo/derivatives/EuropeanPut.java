package com.monte.carlo.derivatives;

import static java.lang.Math.*;

import com.monte.carlo.derivatives.Normals;

public class EuropeanPut implements Option {


	private double Spot;
	private double Strike;
	private double r;
	private double d;
	private double Expiry;
	private double Vol;
	public static final String[] EuropeanParameters ={"STRIKE","EXPIRY"};
	
	private double moneyness, standardDeviation, d1, d2;
	
	public	EuropeanPut(double Spot_, double Strike_, double r_, double d_, double T_, double vol_){
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
		return -Spot*exp(-d*Expiry)*Normals.CumulativeNormal(-d1) + Strike*exp(-r*Expiry)*Normals.CumulativeNormal(-d2);

	}

	@Override
	public double getDelta() {
		// TODO Auto-generated method stub
		return exp(-d*Expiry)*(Normals.CumulativeNormal(d1)-1);
	}

	@Override
	public double getGamma() {
		// TODO Auto-generated method stub
		return exp(-d*Expiry)*Normals.NormalDensity(d1)/(standardDeviation*Spot);
	}

	@Override
	public double getVega() {
		// TODO Auto-generated method stub
		return Spot*sqrt(Expiry)*exp(-d*Expiry)*Normals.NormalDensity(d1);
	}

	@Override
	public double getSpeed() {
		// TODO Auto-generated method stub
		return - exp(-d*Expiry)*Normals.NormalDensity(d1)/(Spot*Spot*standardDeviation*standardDeviation)
				*(d1+standardDeviation);
	}

	@Override
	public double getRho() {
		// TODO Auto-generated method stub
		return -Strike*Expiry*exp(-r*Expiry)*Normals.CumulativeNormal(-d2);
	}

	@Override
	public double getTheta() {
		// TODO Auto-generated method stub
		return - 0.5*Vol*Spot*exp(-d*Expiry)*Normals.NormalDensity(-d1)/sqrt(Expiry)
				- d*Spot*exp(-d*Expiry)*Normals.CumulativeNormal(-d1)
				+ r*Strike*exp(-r*Expiry)*Normals.CumulativeNormal(-d2);
	}


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
