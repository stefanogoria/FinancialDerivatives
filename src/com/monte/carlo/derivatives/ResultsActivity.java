package com.monte.carlo.derivatives;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Map;

public class ResultsActivity extends Activity {

    private String OptionName;
	private Double Spot;
	private Double Volatility;
	private Double Rate;
	private Double Dividend;
	private Double Strike;
	private Double Expiry;
	
	private Double Barrier;
	private Double Rebate;
	
	private Integer Steps;
	
	private String PayOffType;
	private String ExerciseStyle;
	
	//results
	private Double Price=-10000.;
	private Double Delta=-10000.;
	private Double Gamma=-10000.;
	
	private Double BinPrice = -100000.;
	private Double BinDelta = -1000000.;
	private Double BinGamma = -1000000.;
	
	private Map<String,Double> ParametersMap = new HashMap<String,Double>();
	
	private Map<String,PayOff> PayOffMap = new HashMap<String,PayOff>();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        
        PayOffMap.put("Call", new PayOffCall());
        PayOffMap.put("Put", new PayOffPut());
        
        setParams();
        
        updateParameterList();
        
        updateResultsList();
        
        computePriceGreeks();
        
        showResults();

    }
	
	private void updateResultsList(){
		//method to show or not some results
		if(ExerciseStyle.equals("American")){
			LinearLayout analytical_res = (LinearLayout)findViewById(R.id.results_group_id);
			analytical_res.setVisibility(View.GONE);
		}
		
	}

	private void showResults() {
		//TODO: find a working way to do so with BigDecimal
		int digits = 6;
		Price = truncate(Price,digits);
		Delta = truncate(Delta,digits);
		Gamma = truncate(Gamma,digits);
		
		BinPrice = truncate(BinPrice,digits);
		BinDelta = truncate(BinDelta,digits);
		BinGamma = truncate(BinGamma,digits);
		
	TextView price = (TextView)findViewById(R.id.text_price_id);
	price.setText(Price.toString());
	
	TextView delta = (TextView)findViewById(R.id.text_delta_id);
	delta.setText(Delta.toString());
	
	TextView gamma = (TextView)findViewById(R.id.text_gamma_id);
	gamma.setText(Gamma.toString());
	
	TextView bin_price = (TextView)findViewById(R.id.bin_text_price_id);
	bin_price.setText(BinPrice.toString());
	
	TextView bin_delta = (TextView)findViewById(R.id.bin_text_delta_id);
	bin_delta.setText(BinDelta.toString());
	
	TextView bin_gamma = (TextView)findViewById(R.id.bin_text_gamma_id);
	bin_gamma.setText(BinGamma.toString());
	}

	private void computePriceGreeks(){
	Option option = OptionManager.getOptionPricer(OptionManager.OptionMap.get(OptionName),
			OptionManager.PayOffMap.get(PayOffType), OptionManager.StyleMap.get(ExerciseStyle),
			ParametersMap);
		if(option!=null){
			Price = option.getPrice();
			Delta = option.getDelta();
			Gamma = option.getGamma();
		}
		
	//TODO FINCAD: move to the option MANAGER
		BinomialTree tree = new BinomialImprovedCRR(Spot, Strike, Rate,
				Dividend, Expiry, Volatility, Steps);

			BinPrice = tree.GetThePrice(OptionManager.getPayOff(OptionManager.OptionMap.get(OptionName),
			OptionManager.PayOffMap.get(PayOffType), OptionManager.StyleMap.get(ExerciseStyle),
			ParametersMap), ExerciseStyle);
			BinDelta = tree.GetDelta();
			BinGamma = tree.GetGamma();
	

	}
	
    private void setParams() {
    	//get params
    	Intent i = getIntent();
    	OptionName = i.getStringExtra("option_name");
    	PayOffType = i.getStringExtra("pay_off_type");
    	ExerciseStyle = i.getStringExtra("exercise_style");
    	
    	Spot = (Double)i.getExtras().get("spot_price");
    	Volatility = (Double)i.getExtras().get("vol");
    	Rate = (Double)i.getExtras().get("rate");
    	Dividend = (Double)i.getExtras().get("div");
    	//option parameters
       	Strike = (Double)i.getExtras().get("strike");
    	Expiry = (Double)i.getExtras().get("expiry");
      	Barrier = (Double)i.getExtras().get("barrier");
    	Rebate = (Double)i.getExtras().get("rebate");
    	
    	Steps = (Integer)i.getExtras().get("steps");

    	//set Params
TextView OptName = (TextView)findViewById(R.id.option_name);
OptName.setText(ExerciseStyle + " " + OptionName + " " + PayOffType);

TextView spot = (TextView)findViewById(R.id.text_spot_id);
spot.setText(Spot.toString());

TextView vol = (TextView)findViewById(R.id.text_vol_id);
Double volPerc = Volatility*100;
vol.setText(volPerc.toString());

TextView rate = (TextView)findViewById(R.id.text_rate_id);
Double ratePerc = Rate*100;
rate.setText(ratePerc.toString());

TextView div = (TextView)findViewById(R.id.text_div_id);
Double divPerc = Dividend*100;
div.setText(divPerc.toString());

TextView strike = (TextView)findViewById(R.id.text_strike_id);
strike.setText(Strike.toString());

TextView expiry = (TextView)findViewById(R.id.text_expiry_id);
expiry.setText(Expiry.toString());

TextView barrier = (TextView)findViewById(R.id.text_barrier_id);
barrier.setText(Barrier.toString());

TextView rebate = (TextView)findViewById(R.id.text_rebate_id);
rebate.setText(Rebate.toString());

TextView steps = (TextView)findViewById(R.id.text_step_id);
steps.setText(Steps.toString());

//populate parameters map
ParametersMap.put("Spot", Spot);
ParametersMap.put("Volatility", Volatility);
ParametersMap.put("Rate", Rate);
ParametersMap.put("Dividend", Dividend);

ParametersMap.put("Strike", Strike);
ParametersMap.put("Expiry", Expiry);

ParametersMap.put("Barrier", Barrier);
ParametersMap.put("Rebate", Rebate);
    }
    	

    private void updateParameterList(){
    	ArrayList<String> paramsList = new ArrayList<String>();
    	paramsList = OptionManager.getParameterList(
    			OptionManager.OptionMap.get(OptionName));
    	

    	RelativeLayout barrier_layout = (RelativeLayout)findViewById(R.id.input_barrier_id);
    	RelativeLayout rebate_layout = (RelativeLayout)findViewById(R.id.input_rebate_id);
    	//TODO FINCAD - strip me
    	RelativeLayout dividend_layout = (RelativeLayout)findViewById(R.id.input_div_id);
    	dividend_layout.setVisibility(View.GONE);
   
    	if(paramsList.contains("Barrier")){
    		barrier_layout.setVisibility(View.VISIBLE);
    	}else{
    		barrier_layout.setVisibility(View.GONE);
    	}
    	
    	if(paramsList.contains("Rebate")){
    		rebate_layout.setVisibility(View.VISIBLE);
    	}else{
    		rebate_layout.setVisibility(View.GONE);
    	}
    }    
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_parameters, menu);
        return true;
    }
	
	public void onClickBack(View view){
	super.onBackPressed();
	}
	
	
	public static double truncate(double value, int places) {
//		BigDecimal trunc = new BigDecimal(value);
			//trunc.setScale(1,BigDecimal.ROUND_HALF_EVEN);
		 double multiplier = Math.pow(10, places);
		    return Math.floor(multiplier * value) / multiplier;
			//return trunc;
		}
}


