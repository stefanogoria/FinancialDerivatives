package com.monte.carlo.derivatives;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ParametersActivity extends Activity {

    private boolean isUnderlyingOpen = true;
    private boolean isOptionOpen = true;
	private Double Spot = 100.0;
	private Double Volatility = 0.2;
	private Double Rate = 0.08;
	private Double Dividend = 0.0;
	private Double Strike = 110.0;
	private Double Expiry = 1.0;
	private Double Barrier = 105.;
	private Double Rebate = 3.;
	private int Steps = 100;
	
	private String OptionName;
	private String PayOffType = "Call";
	private String ExerciseStyle = "European";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);
       
        
        init(savedInstanceState);

        this.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  savedInstanceState.putString("PayOffType", PayOffType);
	  savedInstanceState.putString("ExerciseStyle", ExerciseStyle);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  PayOffType = savedInstanceState.getString("PayOffType");
	  ExerciseStyle = savedInstanceState.getString("ExerciseStyle");
	}
	
	private void init(Bundle savedInstanceState){
		OptionManager.initOptionManager();
		//init spinner
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		       this, R.array.option_array, R.layout.spinnerlayout);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        Object item = parent.getItemAtPosition(pos);
	        
		        updateExerciseList();
		        updateParameterList();
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});

		//init switchers
        if (savedInstanceState == null) {
        	return;
        }  
        PayOffType = savedInstanceState.getString("PayOffType");
        ExerciseStyle = savedInstanceState.getString("ExerciseStyle");
        
    	ViewSwitcher  viewSwitcher =   (ViewSwitcher)findViewById(R.id.viewPayOffSwitcher);
   	 if (PayOffType.equals("Put")){
	       viewSwitcher.showPrevious();  
       }else{ //do nothing 
    	   }
   	 
 	ViewSwitcher  viewSwitcher2 =   (ViewSwitcher)findViewById(R.id.viewExerciseSwitcher);
	 if (ExerciseStyle.equals("American")){
	       viewSwitcher2.showPrevious();  
    }else{ //do nothing 
 	   }
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_parameters, menu);
        return true;
    }
    
    private void updateExerciseList(){
    	
    	ArrayList<OptionManager.ExerciseStyle> exerciseList = new ArrayList<OptionManager.ExerciseStyle>();
    	exerciseList = OptionManager.getExerciseStyle(
    			OptionManager.OptionMap.get(		
    	    					((Spinner)findViewById(R.id.spinner1)).getSelectedItem().toString()));
    	
    	//ViewSwitcher payoff = (ViewSwitcher)findViewById(R.id.viewPayOffSwitcher);
    	ViewSwitcher style = (ViewSwitcher)findViewById(R.id.viewExerciseSwitcher);
    	
    	if(exerciseList.contains(OptionManager.ExerciseStyle.AMERICAN)){
    	style.setVisibility(View.VISIBLE);	
    	}else{
    		style.setVisibility(View.GONE);	
    	}

    }
    
    private void updateParameterList(){
    	ArrayList<String> paramsList = new ArrayList<String>();
    	paramsList = OptionManager.getParameterList(
    			OptionManager.OptionMap.get(
    					((Spinner)findViewById(R.id.spinner1)).getSelectedItem().toString()));
    	
    	RelativeLayout strike_layout = (RelativeLayout)findViewById(R.id.strike_id);
    	RelativeLayout expiry_layout = (RelativeLayout)findViewById(R.id.expiry_id);
    	RelativeLayout barrier_layout = (RelativeLayout)findViewById(R.id.barrier_id);
    	RelativeLayout rebate_layout = (RelativeLayout)findViewById(R.id.rebate_id);
    	//TODO: FINCAD - strip me
    	RelativeLayout dividend_layout = (RelativeLayout)findViewById(R.id.dividend_id);
    	dividend_layout.setVisibility(View.GONE);
    	
    	if(paramsList.contains("Strike")){
    		strike_layout.setVisibility(View.VISIBLE);
    	}else{
    		strike_layout.setVisibility(View.GONE);
    	}
    	
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
    
    // GUI methods
    public void onTogglePayOffClicked(View view) {
    	ViewSwitcher  viewSwitcher =   (ViewSwitcher)findViewById(R.id.viewPayOffSwitcher);
    	 View  myFirstView= findViewById(R.id.viewPayOff1);
    	 View  mySecondView = findViewById(R.id.viewPayOff2);

       // if (viewSwitcher.getCurrentView() != myFirstView){
    	 if (PayOffType.equals("Call")){
 	       viewSwitcher.showPrevious();  
 	       PayOffType = "Put";
        }else{
       // if (viewSwitcher.getCurrentView() != mySecondView)
        if (PayOffType.equals("Put"))
		      viewSwitcher.showNext();
        	  PayOffType = "Call";
        	  }
        }
       
        public void onToggleExerciseClicked(View view) {
        	ViewSwitcher  viewSwitcher =   (ViewSwitcher)findViewById(R.id.viewExerciseSwitcher);
        	 View  myFirstView= findViewById(R.id.viewExercise1);
        	 View  mySecondView = findViewById(R.id.viewExercise2);

            if (viewSwitcher.getCurrentView() != myFirstView){
     	       viewSwitcher.showPrevious(); 
     	       ExerciseStyle = "European";
            }else{
            if (viewSwitcher.getCurrentView() != mySecondView)
    		      viewSwitcher.showNext();
            	  ExerciseStyle = "American";
            }    

    }
        public void onToggleUnderlying(View view){
        	LinearLayout underlyingView = (LinearLayout) findViewById(R.id.underlying_parameters_group);
        	if(isUnderlyingOpen==true){
        		underlyingView.setVisibility(View.GONE);
        		isUnderlyingOpen = false;
        	}else{
        		underlyingView.setVisibility(View.VISIBLE);
        		isUnderlyingOpen = true; 		
        	} 	
        }
        
        public void onToggleOption(View view){
        	LinearLayout optionView = (LinearLayout) findViewById(R.id.option_parameters_group);

        	if(isOptionOpen==true){
        		optionView.setVisibility(View.GONE);
        		isOptionOpen = false;
        	}else{
        		optionView.setVisibility(View.VISIBLE);
        		isOptionOpen = true; 
        			
        	} 	
        }        
        
        public void plusClickSpot(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_spot_id);
        	Double spot;
        	if(edt.getText().toString().equals("")){
        		spot = Spot;
        	}else{
        	spot = Double.valueOf(edt.getText().toString());
        	}
        	spot += 5;
        	edt.setText(spot.toString());
        }
        
        public void minusClickSpot(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_spot_id);
        	Double spot;
        	if(edt.getText().toString().equals("")){
        		spot = Spot;
        	}else{
        	spot = Double.valueOf(edt.getText().toString());
        	}
        	spot -= 5;
        	edt.setText(spot.toString());
        }
        
        public void plusClickVol(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_vol_id);
        	Double vol;
        	if(edt.getText().toString().equals("")){
        		vol = Volatility*100;
        	}else{
        	vol = Double.valueOf(edt.getText().toString());
        	}
        	vol += 1;
        	edt.setText(vol.toString());
        }
        
        public void minusClickVol(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_vol_id);
        	Double vol;
        	if(edt.getText().toString().equals("")){
        		vol = Volatility*100;
        	}else{
        	vol = Double.valueOf(edt.getText().toString());
        	}
        	vol -= 1;
        	edt.setText(vol.toString());
        }
        
        public void plusClickRate(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_rate_id);
        	Double rate;
        	if(edt.getText().toString().equals("")){
        		rate = Rate*100;
        	}else{
        	rate = Double.valueOf(edt.getText().toString());
        	}
        	rate += 1;
        	edt.setText(rate.toString());
        }
        
        public void minusClickRate(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_rate_id);
        	Double rate;
        	if(edt.getText().toString().equals("")){
        		rate = Rate*100;
        	}else{
        	rate = Double.valueOf(edt.getText().toString());
        	}
        	rate -= 1;
        	edt.setText(rate.toString());
        }   
        
        public void plusClickDiv(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_div_id);
        	Double div;
        	if(edt.getText().toString().equals("")){
        		div = Dividend*100;
        	}else{
        		div = Double.valueOf(edt.getText().toString());
        	}
        	div += 1;
        	edt.setText(div.toString());
        }
        
        public void minusClickDiv(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_div_id);
        	Double div;
        	if(edt.getText().toString().equals("")){
        		div = Dividend*100;
        	}else{
        	div = Double.valueOf(edt.getText().toString());
        	}
        	div -= 1;
        	edt.setText(div.toString());
        }      
        
        public void plusClickStrike(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_strike_id);
        	Double strike;
        	if(edt.getText().toString().equals("")){
        		strike = Strike;
        	}else{
        		strike = Double.valueOf(edt.getText().toString());
        	}
        	strike += 5;
        	edt.setText(strike.toString());
        }
        
        public void minusClickStrike(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_strike_id);
        	Double strike;
        	if(edt.getText().toString().equals("")){
        		strike = Strike;
        	}else{
        	strike = Double.valueOf(edt.getText().toString());
        	}
        	strike -= 5;
        	edt.setText(strike.toString());
        }
        
        public void plusClickExpiry(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_expiry_id);
        	Double expiry;
        	if(edt.getText().toString().equals("")){
        		expiry = Expiry;
        	}else{
        		expiry = Double.valueOf(edt.getText().toString());
        	}
        	expiry += 0.5;
        	edt.setText(expiry.toString());
        }
        
        public void minusClickExpiry(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_expiry_id);
        	Double expiry;
        	if(edt.getText().toString().equals("")){
        		expiry = Expiry;
        	}else{
        	expiry = Double.valueOf(edt.getText().toString());
        	}
        	expiry -= 0.5;
        	edt.setText(expiry.toString());
        }  
        
        public void plusClickBarrier(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_barrier_id);
        	Double barrier;
        	if(edt.getText().toString().equals("")){
        		barrier = Barrier;
        	}else{
        		barrier = Double.valueOf(edt.getText().toString());
        	}
        	barrier += 5.;
        	edt.setText(barrier.toString());
        }
        
        public void minusClickBarrier(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_barrier_id);
        	Double barrier;
        	if(edt.getText().toString().equals("")){
        		barrier = Barrier;
        	}else{
        	barrier = Double.valueOf(edt.getText().toString());
        	}
        	barrier -= 5.;
        	edt.setText(barrier.toString());
        }  
        
        public void plusClickRebate(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_rebate_id);
        	Double rebate;
        	if(edt.getText().toString().equals("")){
        		rebate = Rebate;
        	}else{
        		rebate = Double.valueOf(edt.getText().toString());
        	}
        	rebate += 1.;
        	edt.setText(rebate.toString());
        }
        
        public void minusClickRebate(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_rebate_id);
        	Double rebate;
        	if(edt.getText().toString().equals("")){
        		rebate = Rebate;
        	}else{
        	rebate = Double.valueOf(edt.getText().toString());
        	}
        	rebate -= 1.;
        	edt.setText(rebate.toString());
        }  
        
        public void plusClickStep(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_steps_id);
        	int steps;
        	if(edt.getText().toString().equals("")){
        		steps = Steps;
        	}else{
        		steps = Integer.valueOf(edt.getText().toString());
        	}
        	if(steps<10000)
        	steps += 25;
        	edt.setText(((Integer)steps).toString());
        }
        
        public void minusClickStep(View view){
        	EditText edt = (EditText) findViewById(R.id.edit_steps_id);
        	int steps;
        	if(edt.getText().toString().equals("")){
        		steps = Steps;
        	}else{
        	steps =Integer.valueOf(edt.getText().toString());
        	}
        	if(steps>=25)
        	steps -= 25;
        	edt.setText(((Integer)steps).toString());
        }
        
        //price stuff
        
        public void onClickPrice(View view){

        	setData();
//TODO FINCAD
        	if(Steps>10000)
        		Steps=10000;
        	 //Starting a new Intent
            Intent nextScreen = new Intent(this, ResultsActivity.class);

            //Sending data to another Activity
            nextScreen.putExtra("option_name", OptionName);
            nextScreen.putExtra("pay_off_type", PayOffType);
            nextScreen.putExtra("exercise_style", ExerciseStyle);
            //underlying parameters
            nextScreen.putExtra("spot_price", Spot);
            nextScreen.putExtra("vol", Volatility);
            nextScreen.putExtra("rate", Rate);
            nextScreen.putExtra("div", Dividend);
            //option parameters
            nextScreen.putExtra("strike", Strike);
            nextScreen.putExtra("expiry", Expiry);
            
            nextScreen.putExtra("barrier", Barrier);
            nextScreen.putExtra("rebate", Rebate);
            
            nextScreen.putExtra("steps", Steps);

            startActivity(nextScreen);
        }
        
        private  void setData(){
        	
        	OptionName = ((Spinner)findViewById(R.id.spinner1)).getSelectedItem().toString();
        	
        	//underlying parameters
        	if(!((EditText)findViewById(R.id.edit_spot_id)).getText().toString().equals(""))
        	Spot = Double.valueOf(((EditText)findViewById(R.id.edit_spot_id)).getText().toString());
        	if(!((EditText)findViewById(R.id.edit_vol_id)).getText().toString().equals(""))
        	Volatility = Double.valueOf(((EditText)findViewById(R.id.edit_vol_id)).getText().toString())/100;
        	if(!((EditText)findViewById(R.id.edit_rate_id)).getText().toString().equals(""))
        	Rate = Double.valueOf(((EditText)findViewById(R.id.edit_rate_id)).getText().toString())/100;
        	if(!((EditText)findViewById(R.id.edit_div_id)).getText().toString().equals(""))
        	Dividend = Double.valueOf(((EditText)findViewById(R.id.edit_div_id)).getText().toString())/100;
        	
        	//option parameters
        	if(!((EditText)findViewById(R.id.edit_strike_id)).getText().toString().equals(""))
        	Strike = Double.valueOf(((EditText)findViewById(R.id.edit_strike_id)).getText().toString());
        	if(!((EditText)findViewById(R.id.edit_expiry_id)).getText().toString().equals(""))
        	Expiry = Double.valueOf(((EditText)findViewById(R.id.edit_expiry_id)).getText().toString());
        	
        	if(!((EditText)findViewById(R.id.edit_barrier_id)).getText().toString().equals(""))
        	Barrier = Double.valueOf(((EditText)findViewById(R.id.edit_barrier_id)).getText().toString());
        	if(!((EditText)findViewById(R.id.edit_rebate_id)).getText().toString().equals(""))
        	Rebate = Double.valueOf(((EditText)findViewById(R.id.edit_rebate_id)).getText().toString());
        	
        	if(!((EditText)findViewById(R.id.edit_steps_id)).getText().toString().equals(""))
            Steps = Integer.valueOf(((EditText)findViewById(R.id.edit_steps_id)).getText().toString());
          
        }
        
        public static Animation expand(final View v, final boolean expand) {
            try {
                Method m = v.getClass().getDeclaredMethod("onMeasure", int.class, int.class);
                m.setAccessible(true);
                m.invoke(
                    v,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(((View)v.getParent()).getMeasuredWidth(), MeasureSpec.AT_MOST)
                );
            } catch (Exception e) {
            	e.printStackTrace();
            }
            
            final int initialHeight = v.getMeasuredHeight();
            
            if (expand) {
            	v.getLayoutParams().height = 0;
            }
            else {
            	v.getLayoutParams().height = initialHeight;
            }
            v.setVisibility(View.VISIBLE);
            
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    int newHeight = 0;
                    if (expand) {
                    	newHeight = (int) (initialHeight * interpolatedTime);
                    } else {
                    	newHeight = (int) (initialHeight * (1 - interpolatedTime));
                    }
                    v.getLayoutParams().height = newHeight;	            
                    v.requestLayout();
                    
                    if (interpolatedTime == 1 && !expand)
                    	v.setVisibility(View.GONE);
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };
            a.setDuration(300);
            return a;
        }
}
