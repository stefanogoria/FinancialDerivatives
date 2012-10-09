package com.monte.carlo.derivatives;

public interface Option {
	double getPrice();
	double getDelta();
	double getGamma();
	double getTheta();
	double getSpeed();
	double getVega();
	double getRho();

	double getPrice(double Vol_);
	double getDelta(double Vol_);

	public static final String[] OptionParameters=null;
}
