package com.diegogark.homeiot.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.diegogark.homeiot.R;
import com.diegogark.homeiot.config.ConfiguracaoFirebase;
import com.diegogark.homeiot.fragment.ComandosFragment;
import com.diegogark.homeiot.fragment.ListaDeComprasFragment;
import com.diegogark.homeiot.fragment.MedidasFragment;
import com.diegogark.homeiot.model.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private DatabaseReference dbFire = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    int tela= 0;
    //private RecyclerView rvListaCompras = findViewById(R.id.rvListaCompras);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        smartTabLayout = findViewById(R.id.viewpagertab);
        viewPager = findViewById(R.id.viewpager);
        getSupportActionBar().setElevation(0);

        //configuração das abas
        FragmentPagerItemAdapter adapterAbas = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Anotações", ListaDeComprasFragment.class)
                        .add("Sensores", MedidasFragment.class)
                        .add("Comandos", ComandosFragment.class)
                .create()
        );

        dbFire.child("tela").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    tela = snapshot.getValue(int.class);
                }
                viewPager.setCurrentItem(tela); //seleciona a tela a ser exibida ao iniciar o app "0 para a primeira"
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        viewPager.setAdapter(adapterAbas);
        smartTabLayout.setViewPager(viewPager);
        viewPager.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //esconde o teclado na mudança de tela
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                IBinder binder = v.getWindowToken();
                imm.hideSoftInputFromWindow(binder,InputMethodManager.HIDE_NOT_ALWAYS);
                // ooo
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        tela = viewPager.getCurrentItem();
        dbFire.child("tela").child(firebaseAuth.getUid()).setValue(tela);
    }
}