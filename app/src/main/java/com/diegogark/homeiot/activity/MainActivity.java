package com.diegogark.homeiot.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.diegogark.homeiot.R;
import com.diegogark.homeiot.config.ConfiguracaoFirebase;
import com.diegogark.homeiot.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private TextInputEditText tietLoginNome, tietLoginEmail, tietLoginSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // definição dos objetos de imagem
        tietLoginNome = findViewById(R.id.tietLoginNome);
        tietLoginEmail = findViewById(R.id.tietLoginEmail);
        tietLoginSenha = findViewById(R.id.tietLoginSenha);
        tietLoginEmail.hasFocus();
        final TextInputLayout tilNome = findViewById(R.id.textInputLayout3);
        final TextView cadastrar = findViewById(R.id.tvCadastrar);
        // esconder campo nome, mostrar apenas quando for cadastrar novo usuario
        tilNome.setVisibility(View.INVISIBLE);
        // verifica se ja existe usuario e vai pra tela principal do programa
        verificarUsuarioLogado();
        // Botão de login
        final Button btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usuario = new Usuario();
                usuario.setNome(tietLoginNome.getText().toString());
                usuario.setEmail(tietLoginEmail.getText().toString());
                usuario.setSenha(tietLoginSenha.getText().toString());
                if (tilNome.getVisibility() == View.INVISIBLE) { //executa quado se tenta fazer login
                    if (!usuario.getEmail().isEmpty()){
                        if (!usuario.getSenha().isEmpty()){
                            logarUsuario();
                        }else {
                            Toast.makeText(getApplicationContext(), "insira uma senha", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Insita um email valido", Toast.LENGTH_SHORT).show();
                    }
                }else { //cria um novo usuário
                    if (!usuario.getNome().isEmpty()) {
                        if (!usuario.getEmail().isEmpty()) {
                            if (!usuario.getSenha().isEmpty()) {
                                cadastrarUsuario();
                            } else {
                                Toast.makeText(getApplicationContext(), "insira uma senha", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Insita um email valido", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Insira seu nome", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // Evento do clique em cadastrar novo usuario
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), btnEntrar.getText().toString().toUpperCase(), Toast.LENGTH_SHORT).show();
                if (tilNome.getVisibility() == View.INVISIBLE) {
                    tilNome.setVisibility(View.VISIBLE);
                    btnEntrar.setText("criar");
                    cadastrar.setText("Entrar com um usuário cadastrado");
                    tietLoginNome.requestFocus();
                }else {
                    tilNome.setVisibility(View.INVISIBLE);
                    btnEntrar.setText("entrar");
                    cadastrar.setText("Cadastrar novo usuário");
                }

            }
        });

    }
    public void logarUsuario(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(tietLoginEmail.getText().toString(), tietLoginSenha.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //abre nova activity
                    verificarUsuarioLogado();
                    Toast.makeText(getApplicationContext(), "logado", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        Toast.makeText(getApplicationContext(), "Senha incorreta", Toast.LENGTH_SHORT).show();
                    }catch (FirebaseAuthInvalidUserException e){
                        Toast.makeText(getApplicationContext(), "Usuário não cadastrado", Toast.LENGTH_SHORT).show();
                    }catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Erro no firebase", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void cadastrarUsuario(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(tietLoginEmail.getText().toString(), tietLoginSenha.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //abre nova activity
                    verificarUsuarioLogado();
                    Toast.makeText(getApplicationContext(), "Usuario criado com sucesso", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        Toast.makeText(getApplicationContext(), "Senha muito fraca", Toast.LENGTH_SHORT).show();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        Toast.makeText(getApplicationContext(), "Email invalido", Toast.LENGTH_SHORT).show();
                    }catch (FirebaseAuthUserCollisionException e){
                        Toast.makeText(getApplicationContext(), "Usuario ja cadastrado", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "Erro no firebase", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void verificarUsuarioLogado(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        if (firebaseAuth.getCurrentUser() != null){
            //abre nova activity
            startActivity(new Intent(this, PrincipalActivity.class));
            finish();
        }
    }
}