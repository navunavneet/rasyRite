package androidproject.com.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MenuActivity extends AppCompatActivity {
    ImageView menuBack;
    RelativeLayout rlProfile, rlPayment;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initVariables();  // calling function to initialize variables

        // adding onClick listener to the relative layouts
        rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MenuActivity.this, ProfileActivity.class); //will navigate to the profile screen
                startActivity(intent);
            }
        })h  x svsjpv

        rlPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MenuActivity.this, PaymentActivity.class);//will navigate to the payment screen
                startActivity(intent);
            }
        });

        menuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //will navigate to the previous screen
            }
        });

    }

    private void initVariables() {
        // giving Id to all the views (imageView, relative,linear )
        menuBack = (ImageView) findViewById(R.id.imgMenuBack);
        rlProfile = (RelativeLayout) findViewById(R.id.rlProfile);
        rlPayment = (RelativeLayout) findViewById(R.id.rlPayment);
    }
}
