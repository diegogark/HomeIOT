package com.diegogark.homeiot.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.diegogark.homeiot.R;
import com.diegogark.homeiot.activity.GraficoActivity;
import com.diegogark.homeiot.config.AdapterListaCompra;
import com.diegogark.homeiot.config.AdapterListaMedidas;
import com.diegogark.homeiot.config.ConfiguracaoFirebase;
import com.diegogark.homeiot.model.RecyclerItemClickListener;
import com.diegogark.homeiot.model.Sensor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedidasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedidasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference dbFire = ConfiguracaoFirebase.getFirebaseDatabase();
    private List<Sensor> listaSensor = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MedidasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedidasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedidasFragment newInstance(String param1, String param2) {
        MedidasFragment fragment = new MedidasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medidas, container, false);
        // Inflate the layout for this fragment
        // evento de clique
        final RecyclerView rvMedidas = view.findViewById(R.id.rvMedidas);
        rvMedidas.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rvMedidas, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getContext(), GraficoActivity.class);
                i.putExtra("sensor", listaSensor.get(position).getsID());
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final EditText etNome = new EditText(getContext());
                etNome.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                builder.setView(etNome);
                builder.setMessage("Insira o nome do sensor").setPositiveButton("sim",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listaSensor.get(position).setsNome(etNome.getText().toString());
                                dbFire.child("RTS").child("MEDICAO").child(listaSensor.get(position).getsID()).setValue(listaSensor.get(position));
                            }
                        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"resposta não",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setTitle("Nova lista");
                dialog.show();
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
        //geraBanco();
        //gera lista

        dbFire.child("RTS").child("MEDICAO").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaSensor.clear();
                for (DataSnapshot d: snapshot.getChildren()){
                    Sensor sensor = d.getValue(Sensor.class);
                    listaSensor.add(sensor);
                }
                geraLista(getView());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });/**/
        //------
        //geraLista(view);

        return view;
    }

    public void geraLista(View v){
        final AdapterListaMedidas adapterListaMedidas = new AdapterListaMedidas(listaSensor);
        final RecyclerView rvMedidas = v.findViewById(R.id.rvMedidas);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvMedidas.setLayoutManager(layoutManager);
        rvMedidas.setHasFixedSize(true);
        rvMedidas.setAdapter(adapterListaMedidas);
    }

    /*public void geraBanco(){
        listaSensor.clear();
        for (int i=0; i<10; i++){
            Sensor s = new Sensor();
            s.setsTime(i + "/07/20");
            s.setsID("Sensor " + i);
            s.setsValor(i + "");
            s.setsVMax(i*2 + "");
            s.setsVMin(i/2 + "");
            dbFire.child("RTS").child("MEDICAO").child(s.getsID()).setValue(s);
        }

    }*/
}