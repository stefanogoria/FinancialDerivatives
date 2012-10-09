package com.monte.carlo.derivatives;

import static java.lang.Math.abs;
import static java.lang.Math.exp;
import static java.lang.Math.sqrt;


public class Normals {

	private static final double OneOverRootTwoPi = 0.398942280401433;

	public static double NormalDensity(double x){
		return OneOverRootTwoPi*exp(-x*x*0.5);
		}
	


public static double CumulativeNormal(double x){
	
	
	double[] a = {
		 0.319381530,
		-0.356563782,
		 1.781477937,
		-1.821255978,
		 1.330274429
		};

double result;

if(x<-7.0){
	result = NormalDensity(x)/sqrt(1.+x*x);
	}else{
		double tmp = 1.0/(1.0+0.2316419*abs(x));
		
		result = 1-NormalDensity(x)*(
									tmp*(a[0]+tmp*(a[1]+tmp*(a[2]+tmp*(a[3]+tmp*a[4]))))
										);
		if(x<=0.0) result = 1.0-result;
		}
		
	return result;
	}


}
