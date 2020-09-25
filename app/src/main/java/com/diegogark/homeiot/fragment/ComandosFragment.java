package com.diegogark.homeiot.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.diegogark.homeiot.R;
import com.diegogark.homeiot.config.AdapterListaComandos;
import com.diegogark.homeiot.config.ConfiguracaoFirebase;
import com.diegogark.homeiot.model.Comandos;
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
 * Use the {@link ComandosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComandosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference dbFire = ConfiguracaoFirebase.getFirebaseDatabase();
    private List<Comandos> listaComandos = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ComandosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComandosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComandosFragment newInstance(String param1, String param2) {
        ComandosFragment fragment = new ComandosFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_comandos, container, false);
        final AdapterListaComandos adapterListaComandos = new AdapterListaComandos(listaComandos);
        final RecyclerView rvComandos = view.findViewById(R.id.rvComandos);
        //geraBanco();
        //gera lista
        dbFire.child("RTS").child("COMANDOS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaComandos.clear();
                for (DataSnapshot d: snapshot.getChildren()){
                    Comandos c = d.getValue(Comandos.class);
                    listaComandos.add(c);
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rvComandos.setLayoutManager(layoutManager);
                rvComandos.setHasFixedSize(true);
                rvComandos.setAdapter(adapterListaComandos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}