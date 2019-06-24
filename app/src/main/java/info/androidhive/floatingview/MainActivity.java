package info.androidhive.floatingview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import laserlevel.bubblelevel.inclianomater.floatinglib.LatLonDistanceCalculator;
import laserlevel.bubblelevel.inclianomater.floatinglib.Point;

import static info.androidhive.floatingview.R.id.rl_main;

public class MainActivity extends AppCompatActivity {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private Context mContext = this;


    RelativeLayout rl_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl_main = findViewById(R.id.rl_main);
        Point buenosAiresObeliscoPoint =
                new Point((float) 17.415486, (float) 78.4286259);

        Point nycStatueOfLibertyPoint =
                new Point((float) 17.4484772, (float) 78.3741361);

        float distanceBetweenPoints = LatLonDistanceCalculator.calculateDistance(
                buenosAiresObeliscoPoint,
                nycStatueOfLibertyPoint);

        TextView kilometersLabel = new TextView(mContext);


        kilometersLabel.setTextColor(Color.BLUE);

        kilometersLabel.setTextSize(20);
        rl_main.addView(kilometersLabel);

        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        kilometersLabel.setLayoutParams(layoutParams);
        kilometersLabel.setText("Between the Madhapur and the Banjara Hills there are: \n" + distanceBetweenPoints + "KM");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            initializeView();
        }
    }

    /**
     * Set and initialize the view elements.
     */
    private void initializeView() {
        findViewById(R.id.notify_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, FloatingViewService.class));
                finish();
            }
        });
        /*startService(new Intent(MainActivity.this, FloatingViewService.class));
        finish();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
