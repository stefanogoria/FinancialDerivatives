package com.monte.carlo.derivatives;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;

public class BinomialTreeCRR implements BinomialTree{
	
	private double Spot,Strike,r,vol,T,u,d,p_u,p_d;
	private int  Steps;

	private double[][] spotTree;
	private double[][] priceTree;
	
	public BinomialTreeCRR(double Spot_, double Strike_, double r_,
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
		
		spotTree = new double[Steps+1][Steps+1];
		priceTree = new double[Steps+1][Steps+1];
		
		BuildTree();
	}
	
	private void BuildTree(){
		
		for(int i=0; i<=Steps; i++){
		
			
			spotTree[i][0] = Spot*pow(d,i);
			for(int k=1; k<=i; k++){
				spotTree[i][k] = spotTree[i][k-1]*u*u;
				}
			}
			
		}

	public double GetThePrice(PayOff thePayOff, String ExerciseStyle){
	
		double standardDeviation = vol*sqrt(T);
		double ThisTime;
			for(int k=0; k<=Steps ; k++){		
				priceTree[Steps][k] = thePayOff.PayOff(spotTree[Steps][k],Strike);
	
				}

			if(ExerciseStyle.equals("European")){
			for(int i=1; i <= Steps; i++){
				int index = Steps-i;
				ThisTime = index*T/Steps;
				
				for(int  k=0; k<=index; k++){
					double FutureDiscountedValue = priceTree[index+1][k]*p_d
							+ priceTree[index+1][k+1]*p_u;
					priceTree[index][k] = FutureDiscountedValue;
					}
				}	
			}else if(ExerciseStyle.equals("American")){
				for(int i=1; i <= Steps; i++){
					int index = Steps-i;
					ThisTime = index*T/Steps;
					
					for(int  k=0; k<=index; k++){
						double FutureDiscountedValue = priceTree[index+1][k]*p_d
								+ priceTree[index+1][k+1]*p_u;
						priceTree[index][k] = max(FutureDiscountedValue,
								thePayOff.PayOff(spotTree[index][k],Strike));
						}
					}	
			}
				
			
			return priceTree[0][0];	
	
		}
	
	public double GetTheAmericanPrice(PayOff thePayOff){
		
		double standardDeviation = vol*sqrt(T);

			for(int k=0; k<=Steps ; k++){		
				priceTree[Steps][k] = thePayOff.PayOff(spotTree[Steps][k],Strike);
	
				}

			for(int i=1; i <= Steps; i++){
				int index = Steps-i;
				double ThisTime = index*T/Steps;
				
				for(int  k=0; k<=index; k++){
					double FutureDiscountedValue = priceTree[index+1][k]*p_d
							+ priceTree[index+1][k+1]*p_u;
					priceTree[index][k] = max(FutureDiscountedValue,
							thePayOff.PayOff(spotTree[index][k],Strike));
					}
				}	
				
			
			return priceTree[0][0];	
	
		}

	public double GetDelta() {
	
			return (priceTree[1][1]-priceTree[1][0])/(spotTree[1][1]-spotTree[1][0]);

		}
		
		public double GetGamma() {

			
			double delta1 = (priceTree[2][1]-priceTree[2][0])/(spotTree[2][1]-spotTree[2][0]);
			double delta2 = (priceTree[2][2]-priceTree[2][1])/(spotTree[2][2]-spotTree[2][1]);
			return (delta2-delta1)/(spotTree[1][1]-spotTree[1][0]);
	
		}

	
}
