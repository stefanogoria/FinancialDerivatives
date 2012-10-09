package com.monte.carlo.derivatives;

import static java.lang.Math.*;

import com.monte.carlo.derivatives.Normals;

public class EuropeanCall implements Option {

	private double Spot;
	private double Strike;
	private double r;
	private double d;
	private double Expiry;
	private double Vol;
	public static final String[] Parameters ={"Strike", "Expiry"};
	
	private double moneyness, standardDeviation, d1, d2;
	
	public	EuropeanCall(double Spot_, double Strike_, double r_, double d_, double T_, double vol_){
		Spot = Spot_;
		Strike = Strike_;
		r = r_;
		d = d_;
		Expiry = T_;
		Vol = vol_;
		//precompute these
		standardDeviation = Vol*sqrt(Expiry);
		moneyness = log(Spot/Strike);
		d1 = (moneyness + (r-d)*Expiry + 0.5*standardDeviation*standardDeviation)/standardDeviation;
		d2 = d1 - standardDeviation;
	}

	@Override
	public double getPrice() {
		return Spot*exp(-d*Expiry)*Normals.CumulativeNormal(d1) - Strike*exp(-r*Expiry)*Normals.CumulativeNormal(d2);
	}

	public double getDelta(){
		return exp(-d*Expiry)*Normals.CumulativeNormal(d1);
	}
	
	public double getGamma(){
		return exp(-d*Expiry)*Normals.NormalDensity(d1)/(standardDeviation*Spot);
	}

	public double getTheta(){
		return - 0.5*Vol*Spot*exp(-d*Expiry)*Normals.NormalDensity(d1)/sqrt(Expiry)
				+ d*Spot*exp(-d*Expiry)*Normals.CumulativeNormal(d1)
				- r*Strike*exp(-r*Expiry)*Normals.CumulativeNormal(d2);
	}

	public double getSpeed(){
		return - exp(-d*Expiry)*Normals.NormalDensity(d1)/(Spot*Spot*standardDeviation*standardDeviation)
				*(d1+standardDeviation);
	}

	public double getVega(){
		return Spot*sqrt(Expiry)*exp(-d*Expiry)*Normals.NormalDensity(d1);
	}

	public double getRho(){
		//sensitivity to interest rate
		return Strike*Expiry*exp(-r*Expiry)*Normals.CumulativeNormal(d2);
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
