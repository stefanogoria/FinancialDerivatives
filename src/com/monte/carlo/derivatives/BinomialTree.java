package com.monte.carlo.derivatives;

public interface BinomialTree {

	public double GetThePrice(PayOff thePayOff, String ExerciseStyle);
	
	public double GetDelta();
	public double GetGamma();
	
}
