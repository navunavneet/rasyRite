package androidproject.com.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.util.List;

import androidproject.com.myapplication.localDbClasses.LocalDatabase;

public class ProfileActivity extends AppCompatActivity {
    ImageView imgProfileBack, imgUserImage;
    EditText txtFirstname, txtLastName;
    Button btnSaveProfile;
    LocalDatabase localDatabase;
    byte imageInByte[] = null;
    String mFirstName ="" , mLastName = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initVariables();

        imgProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstName = txtFirstname.getText().toString().trim() ;
                mLastName = txtLastName.getText().toString().trim() ;
                if (mFirstName.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
                } else if (mLastName.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Please enter your last name", Toast.LENGTH_SHORT).show();
                } else if (imageInByte == null) {
                    Toast.makeText(ProfileActivity.this, "Please upload an image", Toast.LENGTH_SHORT).show();
                } else {
                    localDatabase.insertProfile(mFirstName , mLastName , imageInByte);
                    Toast.makeText(ProfileActivity.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askRunTimePermission();
            }
        });
    }

    private void askRunTimePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            openCamera();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void initVariables() {
        imgProfileBack = (ImageView) findViewById(R.id.imgProfileBack);
        imgUserImage = (ImageView) findViewById(R.id.imgUserImage);
        txtFirstname = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        btnSaveProfile = (Button) findViewById(R.id.btnSaveProfile);
        localDatabase = new LocalDatabase(ProfileActivity.this);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Log.e("EXTRASEE", extras + "ff");
                    if (extras != null) {
                        Bitmap yourImage = extras.getParcelable("data");
                        imgUserImage.setImageBitmap(yourImage);
                        Log.e("IMAGEEE", yourImage + "ff");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        imageInByte =stream.toByteArray();
                        Log.e("BYTE IMAGE", imageInByte + "ff");
                    }


                }
            }
        }
    }
}
