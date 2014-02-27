package ee202a.yinghao.httpcomm;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    Button mybutton1;
    Button mybutton2;
    Button mybutton3;
    Button mybutton4;
    Button mybutton5;
    TextView display;
    TextView display2;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mybutton1= (Button) findViewById(R.id.funcbutton1);
        mybutton2= (Button) findViewById(R.id.funcbutton2);
        mybutton3= (Button) findViewById(R.id.funcbutton3);
        mybutton4= (Button) findViewById(R.id.funcbutton4);
        mybutton5= (Button) findViewById(R.id.funcbutton5);
        display= (TextView) findViewById(R.id.textView1);
        display2= (TextView) findViewById(R.id.textView2);
        
        final JSONObject jsob = new JSONObject();
		try {
			jsob.put("name",123);
			jsob.put("pi", 3.14159);
			jsob.put("hello", true);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        mybutton1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				display.setText("func1");
				

				Thread mythread= new Thread() {
					@Override
					public void run(){
						Comms.addFriend(jsob);
					}
				};
				mythread.start();
				display2.setText(mythread.toString());
			}
        });
        
        mybutton2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				display.setText("func2");
				
				Thread mythread= new Thread() {
					@Override
					public void run(){
						Comms.deleteFriend(jsob);
					}
				};
				mythread.start();
				display2.setText(mythread.toString());
			}
		});
        mybutton3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				display.setText("func3");
				
				Thread mythread= new Thread() {
					@Override
					public void run(){
						Comms.getData(jsob);
					}
				};
				mythread.start();
				display2.setText(mythread.toString());
			}
		});
        mybutton4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				display.setText("func4");
				
				Thread mythread= new Thread() {
					@Override
					public void run(){
						Comms.writeArriveData(jsob);
						Comms.writeLeaveData(jsob);
						
					}
				};
				mythread.start();
				display2.setText(mythread.toString());
			}
		});
        mybutton5.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				display.setText("func5");
				
				Thread mythread= new Thread() {
					@Override
					public void run(){
						Comms.deletemyData(jsob);
					}
				};
				mythread.start();
				display2.setText(mythread.toString());
			}
		});
        		
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
