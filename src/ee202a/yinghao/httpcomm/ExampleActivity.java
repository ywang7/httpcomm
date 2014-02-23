package ee202a.yinghao.httpcomm;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ExampleActivity extends Activity{
	
	TextView httpStuff;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_example);
		httpStuff = (TextView) findViewById(R.id.tvHttp);
		
	}
}
