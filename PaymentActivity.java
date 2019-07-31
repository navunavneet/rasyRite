package androidproject.com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidproject.com.myapplication.localDbClasses.LocalDatabase;

public class PaymentActivity extends AppCompatActivity {
    ImageView imgPaymentBack;
    EditText txtCreditCard, txtExpire;
    Button btnPaymentSave;
    String mCreditCard = "", mExpire = "";
    LocalDatabase localDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initVariables();
        imgPaymentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnPaymentSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCreditCard = txtCreditCard.getText().toString().trim();
                mExpire = txtExpire.getText().toString().trim();
                if (mCreditCard.isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Please enter credit card number", Toast.LENGTH_SHORT).show();
                } else if (mExpire.isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Please enter expiry date", Toast.LENGTH_SHORT).show();
                } else {
                    localDatabase.insertPayment(mCreditCard, mExpire);
                    Toast.makeText(PaymentActivity.this, "Payment detail saved successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initVariables() {
        imgPaymentBack = (ImageView) findViewById(R.id.imgPaymentBack);
        txtCreditCard = (EditText) findViewById(R.id.txtCreditCard);
        txtExpire = (EditText) findViewById(R.id.txtExpire);
        btnPaymentSave = (Button) findViewById(R.id.btnPaymentSave);
        localDatabase = new LocalDatabase(PaymentActivity.this);
    }
}
