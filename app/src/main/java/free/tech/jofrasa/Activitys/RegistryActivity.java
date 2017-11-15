package free.tech.jofrasa.Activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private LinearLayout formRegistry;
    private TextView userEmail;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ProgressBar progressBar;

    //edit text
    private EditText name, last_name, phone, nit, address;

    //btn
    private Button btnSubmit;

    //api Service
    private ApiInterface mApiService;
    //model CLient
    private Client client;
    //shared preference
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        photoImageView = (ImageView) findViewById(R.id.photoUser);
        userEmail = (TextView) findViewById(R.id.userEmail);
        name = (EditText) findViewById(R.id.edt_name);
        last_name = (EditText) findViewById(R.id.edt_last_name);
        phone = (EditText) findViewById(R.id.edt_phone);
        nit = (EditText) findViewById(R.id.edt_nit);
        address = (EditText) findViewById(R.id.edt_address);
        //button submit
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        //layout registry
        formRegistry = (LinearLayout) findViewById(R.id.form_registry);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //sharedpreferences
        sharedpreferences = getSharedPreferences("mypreference",
                Context.MODE_PRIVATE);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
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
                client = new Client();

                String _name = name.getText().toString().trim();
                String _surname = last_name.getText().toString().trim();
                String _nit = nit.getText().toString().trim();
                String _adress = address.getText().toString().trim();
                String _email = userEmail.getText().toString().trim();
                String _cell_number = phone.getText().toString().trim();
                String _phone_number = phone.getText().toString().trim();

                client.setName(_name);
                client.setSurname(_surname);
                client.setNit(_nit);
                client.setAdress(_adress);
                client.setEmail(_email);
                client.setCellNumber(_cell_number);
                client.setPhoneNumber(_phone_number);


                if (!TextUtils.isEmpty(_name) && !TextUtils.isEmpty(_name) &&
                        !TextUtils.isEmpty(_surname) && !TextUtils.isEmpty(_nit) &&
                        !TextUtils.isEmpty(_email) && !TextUtils.isEmpty(_cell_number) &&
                        !TextUtils.isEmpty(_phone_number)) {
                    sendPost(client);

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
        Log.i("REGISTRY ", " email data " + user.getEmail().toString().trim());
        verifyEmail(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).into(photoImageView);
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, RegistryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void sendPost(final Client client) {
        progressBar.setVisibility(View.VISIBLE);
        mApiService.savePost(client).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Log.i("REGISTRY ", "post submitted to API." + response.body().getData());
                    if (response.body().getData().equals("Usuario insertado correctamente")) {
                        saveSharedPreferent(client);
                        goMainScreen();
                        Toast.makeText(getApplicationContext(),R.string.success_registry,Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),R.string.error_registry,Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Log.e("REGISTRY ", "Unable to submit post to API.");
            }
        });
    }

    public void verifyEmail(String email) {
        progressBar.setVisibility(View.VISIBLE);

        mApiService.verifyEmail(email).enqueue(new Callback<Client>() {

            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    if (response.body().getData().equals("Email no registrado")) {
                        Log.i("REGISTRY ", "MOSTRAR REGISTRO" + response.body().getData());
                        formRegistry.setVisibility(View.VISIBLE);
                    } else {
                        Log.i("REGISTRY ", "OCULTAR REGISTRO" + response.body().getData());
                        goMainScreen();

                    }
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Log.e("REGISTRY ", "Unable to submit get to API.");
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

    public void saveSharedPreferent( Client client) {
        String name = client.getName().toString();
        String surname = client.getSurname().toString();
        String nit = client.getNit().toString();
        String email = client.getEmail().toString();
        String cell_number = client.getCellNumber().toString();
        String phone_number = client.getPhoneNumber().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("name", name);
        editor.putString("surname", surname);
        editor.putString("nit", nit);
        editor.putString("email", email);
        editor.putString("cell_number", cell_number);
        editor.putString("phone_number", phone_number);
        editor.commit();
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
