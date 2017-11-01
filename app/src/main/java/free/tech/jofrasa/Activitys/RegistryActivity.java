package free.tech.jofrasa.Activitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import free.tech.jofrasa.ExtraClass.ApiUtils;
import free.tech.jofrasa.ExtraClass.Model.Client;
import free.tech.jofrasa.Interface.ApiInterface;
import free.tech.jofrasa.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistryActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView photoImageView;
    private TextView userEmail;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    //edit text
    private EditText name, last_name, phone, nit, address;

    //btn
    private Button btnSubmit;

    //api Service

    private ApiInterface mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        photoImageView = (ImageView) findViewById(R.id.photoUser);
        userEmail = (TextView)findViewById(R.id.userEmail);
        name = (EditText)findViewById(R.id.edt_name);
        last_name = (EditText) findViewById(R.id.edt_last_name);
        phone = (EditText) findViewById(R.id.edt_phone);
        nit = (EditText) findViewById(R.id.edt_nit);
        address = (EditText) findViewById(R.id.edt_address);
        //button submit
        btnSubmit = (Button) findViewById(R.id.btn_submit);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,  this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //instance post apiService
        mApiService = ApiUtils.getAPIService();
        //function to send data registry
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(RegistryActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                String _name = name.getText().toString().trim();
                String _surname = last_name.getText().toString().trim();
                String _nit = nit.getText().toString().trim();
                String _adress = address.getText().toString().trim();
                String _email = userEmail.getText().toString().trim();
                String _cell_number = phone.getText().toString().trim();
                String _phone_number = phone.getText().toString().trim();
                if(!TextUtils.isEmpty(_name) && !TextUtils.isEmpty(_name) &&
                        !TextUtils.isEmpty(_surname) && !TextUtils.isEmpty(_nit) &&
                        !TextUtils.isEmpty(_email) && !TextUtils.isEmpty(_cell_number) &&
                        !TextUtils.isEmpty(_phone_number)){
                    sendPost(_name,_surname,_nit,_email,_adress,_cell_number,_phone_number);
                }

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setUserData(user);

                } else {
                    goLogInScreen();
                }
            }
        };
    }
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }
    private void setUserData(FirebaseUser user) {
        userEmail.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).into(photoImageView);
    }
    private void goLogInScreen() {
        Intent intent = new Intent(this, RegistryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void sendPost(String name, String surname, String nit,String email, String adress, String cell_number, String phone_number ) {
        mApiService.savePost(name, surname, nit, email, adress, cell_number, phone_number).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {

                if(response.isSuccessful()) {
                    //showResponse(response.body().toString());
                    Log.i("REGISTRY ", "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Log.e("REGISTRY ", "Unable to submit post to API.");
            }
        });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //class validation
    public abstract class TextValidator implements TextWatcher {
        private final EditText editText;

        public TextValidator(EditText editText) {
            this.editText = editText;
        }

        public abstract void validate(EditText editText, String text);

        @Override
        final public void afterTextChanged(Editable s) {
            String text = editText.getText().toString();
            validate(editText, text);
        }

        @Override
        final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

        @Override
        final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
    }
}
