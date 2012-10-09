package com.monte.carlo.derivatives;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
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
	
	private String PayOffType;
	private String ExerciseStyle;
	
	//results
	private Double Price=-10000.;
	private Double Delta=-10000.;
	private Double Gamma=-10000.;
	
	private Map<String,Double> ParametersMap = new HashMap<String,Double>();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        
        setParams();
        
        updateParameterList();
        
        computePriceGreeks();
        
        showResults();

    }

	private void showResults() {
	TextView price = (TextView)findViewById(R.id.text_price_id);
	price.setText(Price.toString());
	
	TextView delta = (TextView)findViewById(R.id.text_delta_id);
	delta.setText(Delta.toString());
	
//	BigDecimal pr = new BigDecimal(Price);

	
	TextView gamma = (TextView)findViewById(R.id.text_gamma_id);
	gamma.setText(Gamma.toString());
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
}
