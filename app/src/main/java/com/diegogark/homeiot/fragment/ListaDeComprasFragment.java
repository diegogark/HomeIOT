package com.diegogark.homeiot.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.diegogark.homeiot.R;
import com.diegogark.homeiot.config.AdapterListaCompra;
import com.diegogark.homeiot.config.ConfiguracaoFirebase;
import com.diegogark.homeiot.model.Item;
import com.diegogark.homeiot.model.RecyclerItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaDeComprasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaDeComprasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference dbFire = ConfiguracaoFirebase.getFirebaseDatabase();
    private AutoCompleteTextView etAdd;
    private FloatingActionButton fabSend;
    private List<Item> listaItens = new ArrayList<>();
    private List<Item> listaValidos = new ArrayList<>();
    private List<Item> listaExcluidos = new ArrayList<>();
    private List <String> listPesquisa = new ArrayList<>();
    private List<String> listaLista = new ArrayList<>();
    private String[] sPesquisa;
    private ImageView ivExibirLista;
    private boolean mostraExcluidos = true;
    private String listaAtual = "COMUM";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListaDeComprasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaDeCompras.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaDeComprasFragment newInstance(String param1, String param2) {
        ListaDeComprasFragment fragment = new ListaDeComprasFragment();
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
        // testes com cloud fire store

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lista_de_compras, container, false);
        // evento de clic na lista
        final RecyclerView rvListaCompras = view.findViewById(R.id.rvListaCompras);
        rvListaCompras.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(),
                rvListaCompras,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (listaItens.get(position).isTitulo()){
                            listaAtual = listaItens.get(position).getItem();
                            geraLista(getView());
                        } else {
                            if (listaItens.get(position).isValidade()){
                                listaItens.get(position).setValidade(false);
                            }else {
                                listaItens.get(position).setValidade(true);
                            }
                            gravaBanco(listaItens.get(position));
                        }

                    }

                    @Override
                    public void onLongItemClick(View view, final int position) {
                        if (listaItens.get(position).isTitulo()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("A lista " + listaItens.get(position).getItem() + "será excluida permanentemente. Excluir?" ).setPositiveButton("sim",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dbFire.child("lista").child(listaItens.get(position).getItem()).removeValue();
                                            Toast.makeText(getContext(),"excluido",Toast.LENGTH_SHORT).show();
                                        }
                                    }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getContext(),"resposta não",Toast.LENGTH_SHORT).show();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.setTitle("Excluir Lista");
                            dialog.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("excluir nome da lista de pesquisa rapida?").setPositiveButton("sim",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dbFire.child("pesquisa").child(listaItens.get(position).getItem()).removeValue();
                                            Toast.makeText(getContext(),"excluido",Toast.LENGTH_SHORT).show();
                                        }
                                    }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getContext(),"resposta não",Toast.LENGTH_SHORT).show();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.setTitle("Excluir item da pesquisa rápida");
                            dialog.show();
                            Toast.makeText(getContext(),listaItens.get(position).getItem(),Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));
        //mostra itens excluidos
        ivExibirLista = view.findViewById(R.id.ivExibirLista);
        ivExibirLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mostraExcluidos){
                    mostraExcluidos = false;
                    ivExibirLista.setImageResource(R.drawable.ic_esconder);
                }else {
                    mostraExcluidos = true;
                    ivExibirLista.setImageResource(R.drawable.ic_mostrar);
                }
                geraBanco();
            }
        });
        //pesquisa
        dbFire.child("pesquisa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPesquisa.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    listPesquisa.add(d.getKey());
                }
                sPesquisa = null;
                sPesquisa = listPesquisa.toArray(new String[listPesquisa.size()]);
                pesquisa();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // fim pesquisa

        etAdd = view.findViewById(R.id.etAdd);
        geraBanco();
        // Funcões para o edit text
        fabSend = view.findViewById(R.id.fabSend);
        etAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // troca a imagem do FAB
                if (s.length()>0){
                    fabSend.setImageResource(R.drawable.ic__send_24);
                }else {
                    fabSend.setImageResource(R.drawable.ic_edit_24);
                }
                // segue
            }
        });
        // ação do FAB
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Item item = new Item();
                item.setItem(etAdd.getText().toString());
                item.setImagem(R.drawable.ic__send_24);
                item.setCaminho("verdadeiro");
                item.setLista(listaAtual);
                if (!etAdd.getText().toString().isEmpty()) {
                    item.setValidade(true);
                    gravaBanco(item);
                    boolean contem = false;
                    for (String s: sPesquisa){
                        if (s.contains(item.getItem())){
                            contem = true;
                        }
                    }
                    if (!contem){
                        dbFire.child("pesquisa").child(item.getItem()).setValue(true);
                        Toast.makeText(getContext(),"contem",Toast.LENGTH_SHORT).show();
                    }
                    etAdd.setText("");
                    geraLista(getView());
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    final EditText etTitulo = new EditText(getContext());
                    etTitulo.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    builder.setView(etTitulo);
                    builder.setMessage("Insira o nome da nova lista").setPositiveButton("sim",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    item.setItem(etTitulo.getText().toString().toUpperCase());
                                    item.setTitulo(true);
                                    item.setCaminho(item.getItem());
                                    item.setValidade(true);
                                    item.setImagem(R.drawable.ic_edit_24);
                                    item.setLista(item.getItem());
                                    gravaBanco(item);
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
                    Toast.makeText(getContext(),"criar lista", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void gravaBanco(Item item){
        if (item.isTitulo()){
            dbFire.child("lista").child(item.getItem()).setValue(true);
            listaAtual = item.getLista();
        } else {
            dbFire.child("lista").child(item.getLista()).child(item.getItem()).setValue(item);
        }
    }

    public void geraBanco(){
        // carrega lista de compras
        dbFire.child("lista").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaItens.clear();
                for (DataSnapshot listas: snapshot.getChildren()){
                    Item item = new Item();
                    item.setItem(listas.getKey());
                    item.setLista(listas.getKey());
                    item.setValidade(true);
                    item.setTitulo(true);
                    item.setImagem(R.drawable.ic_edit_24);
                    item.setCaminho(item.getItem());
                    listaItens.add(item);
                    listaExcluidos.clear();
                    listaValidos.clear();
                    for (DataSnapshot dados: snapshot.child(listas.getKey()).getChildren()){
                        item = dados.getValue(Item.class);
                        if (item.isValidade()){
                            listaValidos.add(item);
                        } else {
                            listaExcluidos.add(item);
                        }
                    }
                    if (mostraExcluidos){
                        listaItens.addAll(listaValidos);
                    }else {
                        listaItens.addAll(listaValidos);
                        listaItens.addAll(listaExcluidos);
                    }
                }
                geraLista(getView());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void geraLista(View v){

        final AdapterListaCompra adapterListaCompra = new AdapterListaCompra(listaItens, listaAtual);
        final RecyclerView rvListaCompras = v.findViewById(R.id.rvListaCompras);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvListaCompras.setLayoutManager(layoutManager);
        rvListaCompras.setHasFixedSize(true);
        rvListaCompras.setAdapter(adapterListaCompra);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final AdapterListaCompra adapterListaCompra = new AdapterListaCompra(listaItens, listaAtual);
                        rvListaCompras.setAdapter(adapterListaCompra);
                    }
                });
            }
        }).start();*/

    }
    public void pesquisa(){
        if (sPesquisa.length>0) {
            ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, sPesquisa);
            etAdd.setAdapter(adp);
        }
    }
}