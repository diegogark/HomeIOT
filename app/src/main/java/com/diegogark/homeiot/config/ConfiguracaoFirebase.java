package com.diegogark.homeiot.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ConfiguracaoFirebase {
    private static FirebaseAuth firebaseAuth;
    private static DatabaseReference firebase;


    //retorna a instancia do firebaseAuth
    public static FirebaseAuth getFirebaseAuth(){
        if (firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    //retorna a instancia do firebaseDataBase

    public static DatabaseReference getFirebaseDatabase(){
        if (firebase == null){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // verificar se funciona. ativa a persistencia de dados
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }
}
