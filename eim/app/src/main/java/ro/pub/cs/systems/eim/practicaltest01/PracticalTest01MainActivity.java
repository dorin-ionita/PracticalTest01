package ro.pub.cs.systems.eim.practicaltest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class PracticalTest01MainActivity extends AppCompatActivity {


    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("[Message]", intent.getStringExtra("message"));
        }
    }

    private IntentFilter intentFilter = new IntentFilter();

    private EditText leftEditText;
    private EditText rightEditText;
    private Button leftButton;
    private Button rightButton;
    Integer t1_val = 0;
    Integer t2_val = 0;

    private int serviceStatus = 0;

    private final static int SECONDARY_ACTIVITY_REQUEST_CODE = 1;
    private Button navigateToSecondaryActivityButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);
        leftEditText = (EditText)findViewById(R.id.left_edit_text);
        rightEditText = (EditText)findViewById(R.id.right_edit_text);
        leftButton = (Button)findViewById(R.id.left_button);
        rightButton = (Button)findViewById(R.id.right_button);

        leftEditText.setText(t1_val.toString());
        rightEditText.setText(t2_val.toString());

        navigateToSecondaryActivityButton = (Button)findViewById(R.id.navigate_to_secondary_activity_button);
        navigateToSecondaryActivityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                int numberOfClicks = Integer.parseInt(leftEditText.getText().toString()) +
                        Integer.parseInt(rightEditText.getText().toString());
                intent.putExtra("numberOfClicks", numberOfClicks);
                startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
            }
        });


        leftButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                t1_val++;
                leftEditText.setText(t1_val.toString());
                rightEditText.setText(t2_val.toString());

                if (t1_val + t2_val > 3
                        && serviceStatus == 0) {
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                    intent.putExtra("firstNumber", t1_val);
                    intent.putExtra("secondNumber", t2_val);
                    getApplicationContext().startService(intent);
                    serviceStatus = 1;
                }

            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                t2_val++;
                leftEditText.setText(t1_val.toString());
                rightEditText.setText(t2_val.toString());

                if (t1_val + t2_val > 3
                        && serviceStatus == 0) {
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                    intent.putExtra("firstNumber", t1_val);
                    intent.putExtra("secondNumber", t2_val);
                    getApplicationContext().startService(intent);
                    serviceStatus = 1;
                }
            }
        });

        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("leftCount", leftEditText.getText().toString());
        savedInstanceState.putString("rightCount", rightEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey("leftCount")) {
            leftEditText.setText(savedInstanceState.getString("leftCount"));
        } else {
            leftEditText.setText(String.valueOf(0));
        }
        if (savedInstanceState.containsKey("rightCount")) {
            rightEditText.setText(savedInstanceState.getString("rightCount"));
        } else {
            rightEditText.setText(String.valueOf(0));
        }
    }
}
