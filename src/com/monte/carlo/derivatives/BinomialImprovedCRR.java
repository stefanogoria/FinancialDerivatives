package com.monte.carlo.derivatives;

import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class BinomialImprovedCRR implements BinomialTree {

	private double Spot,Strike,r,vol,T,u,d,p_u,p_d,Delta, Gamma;
	private int  Steps;

	private double[] spotTree;
	private double[] priceTree;
	
	public BinomialImprovedCRR(double Spot_, double Strike_, double r_,
			double div_, double T_, double vol_, int Steps_){
		
		Spot = Spot_;
		Strike = Strike_;
		r = r_;
		T = T_;
		vol = vol_;
		Steps = Steps_;
		
		double delta_t = T/Steps;
		double discount = exp(-r*delta_t);

		u = exp(vol*sqrt(delta_t));
		d = 1/u;
		double p = (exp(r*delta_t) - d)/(u-d);
		p_u = discount*p;
		p_d = discount*(1.-p);
		
		spotTree = new double[2*Steps+1];
		priceTree = new double[2*Steps+1];
		
		BuildTree();
	}
	
	private void BuildTree(){
		//read pg 413 of Brandimarte: num methods in finance and ec
		spotTree[0] = Spot*pow(d,Steps);
		for(int i=1; i<2*Steps+1; i++)
			spotTree[i]=u*spotTree[i-1];			
		}
	
	@Override
	public double GetThePrice(PayOff thePayOff, String ExerciseStyle) {
		//final values
		for(int i =0; i< 2*Steps+1; i+=2)
			priceTree[i] = thePayOff.PayOff(spotTree[i],Strike);
		//work backwards	(stopping at < Steps -1) to save the old value of Steps
		for(int tau=0; tau< Steps-1; tau++){
			for(int i = tau+1; i<2*Steps+1-tau; i+=2)
				priceTree[i] = p_u*priceTree[i+1]+p_d*priceTree[i-1];
			
		}
		
		//Gamma
		double delta2 = (priceTree[Steps+2]-priceTree[Steps])/(spotTree[Steps+2]-spotTree[Steps]);
		double delta1 = (priceTree[Steps]-priceTree[Steps-2])/(spotTree[Steps]-spotTree[Steps-2]);
		Gamma = (delta2-delta1)/(spotTree[Steps+1]-spotTree[Steps-1]);
		//Delta
		Delta = (priceTree[Steps+1]-priceTree[Steps-1])/(spotTree[Steps+1]-spotTree[Steps-1]);
		//the price
		priceTree[Steps] = p_u*priceTree[Steps+1]+p_d*priceTree[Steps-1];
		
		return priceTree[Steps];
	}

	@Override
	public double GetDelta() {
		
		return Delta;
	}

	@Override
	public double GetGamma() {
		// TODO Auto-generated method stub
		return Gamma;
	}

}
