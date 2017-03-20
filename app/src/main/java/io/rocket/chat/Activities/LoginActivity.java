package io.rocket.chat.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chat.Callback;
import com.chat.Models.ErrorObject;
import com.chat.Socket;

import org.json.JSONObject;

import io.rocket.chat.MyApplication;
import io.rocket.chat.R;

public class LoginActivity extends AppCompatActivity {

    EditText userName;
    EditText passwordText;
    Button loginButton;
    ProgressDialog dialog;
    Socket socket;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MyApplication application= (MyApplication) getApplication();

        userName= (EditText) findViewById(R.id.username);
        passwordText= (EditText) findViewById(R.id.input_password);
        loginButton= (Button) findViewById(R.id.btn_login);
        layout= (RelativeLayout) findViewById(R.id.root);

        socket=application.getmSocket();

        //Connect event to server
        socket.connect(new Callback() {
            @Override
            public void call(JSONObject jsonObject) {
                final Snackbar snackbar = Snackbar
                        .make(layout, "Connected to server", Snackbar.LENGTH_LONG);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackbar.show();
                    }
                });
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void login() {

        loginButton.setEnabled(false);

        if (!validate()){
            onLoginFailed("Login failed");
            return;
        }
        dialog=new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setMessage("Authenticating...");
        dialog.show();

        final String username=userName.getText().toString();
        final String password=passwordText.getText().toString();

        socket.setCredentials(username,password);

        socket.login(new Callback() {
            public void call(final JSONObject jsonObject) {
                if (jsonObject==null) {
                    dialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra("token", socket.getTokenObject().getAuthToken());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });

                }else{
//                    Models.ErrorObject object
                    dialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onLoginFailed(new ErrorObject(jsonObject).getMessage());
                        }
                    });
                }
            }
        });


    }

    private void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username=userName.getText().toString();
        String password=passwordText.getText().toString();

        if (username.isEmpty() ) {
            userName.setError("enter a valid user name");
            valid = false;
        } else {
            userName.setError(null);
        }

        if (password.isEmpty() ) {
            passwordText.setError("Enter valid password");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
