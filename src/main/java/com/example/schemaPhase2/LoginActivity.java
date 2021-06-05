package com.example.schemaPhase2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.schemaPhase2.Utils.DatabaseHandler;

public class LoginActivity extends AppCompatActivity{

    EditText username, password;
    DatabaseHandler db;

/*    Button button2;
    Button button1;*/





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.US1);
        password = (EditText) findViewById(R.id.PS1);
        db = new DatabaseHandler(this);

        /*why a manually defined context ?*/
        final Context context = this;

        //if(button !=null) {
Button button1 = (Button)findViewById(R.id.sign_up_fromloginpage);
Button button2= (Button)findViewById(R.id.button_sign_in_fromloginpage);
         /*   button2= (Button) findViewById(R.id.button_sign_in);*/

            button2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String user = username.getText().toString();
                    String pass = password.getText().toString();

                    if(user.equals("")||pass.equals(""))
                        Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    else{
                        Boolean checkUserPass = db.checkUsernamePassword(user, pass);
                        if(checkUserPass==true){
                            Toast.makeText(LoginActivity.this, "Login in successful", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent1);
                        }else{
                                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }

                }



            }

        //}
            });



 /*       button1 = (Button) findViewById(R.id.sign_up);*/

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);

            }

        });

    }
}
