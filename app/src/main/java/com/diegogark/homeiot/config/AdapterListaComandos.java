package com.diegogark.homeiot.config;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diegogark.homeiot.R;
import com.diegogark.homeiot.fragment.ComandosFragment;
import com.diegogark.homeiot.model.Comandos;
import com.diegogark.homeiot.model.Sensor;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class AdapterListaComandos extends RecyclerView.Adapter<AdapterListaComandos.MyViewHolder> {
    private List<Comandos> listaComandos;
    private DatabaseReference dbFire = ConfiguracaoFirebase.getFirebaseDatabase();
    private static final int EXECUTADO = 0;
    private static final int LIGAR = 1;
    private static final int DESLIGAR = 2;
    private static final int PULSAR = 3;


    public AdapterListaComandos(){
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comandos, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Comandos c = listaComandos.get(position);
        holder.nome.setText(c.getsID());
        holder.time.setText(c.getsTime());
        if (c.isComRetorno()){
            holder.btComando.setVisibility(View.INVISIBLE);
            if (c.getiComando() == 1){
                holder.swComando.setChecked(true);
                holder.swComando.setText("Ligado");
            } else {
                holder.swComando.setChecked(false);
                holder.swComando.setText("Desligado");
            }
        }else {
            holder.swComando.setVisibility(View.INVISIBLE);
        }
        if (c.isResposta()){
            holder.resposta.setText("Comando Executado!");
            holder.btComando.setText("Enviar Comando");
        } else {
            holder.resposta.setText("Comando enviado");
            holder.btComando.setText("Reiniciar");
        }
        holder.btComando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.resposta.getText() == "Comando enviado"){
                    dbFire.child("RTS").child("COMANDOS").child(c.getsID()).child("iComando").setValue(EXECUTADO);
                    dbFire.child("RTS").child("COMANDOS").child(c.getsID()).child("resposta").setValue(true);
                }
                else{
                    dbFire.child("RTS").child("COMANDOS").child(c.getsID()).child("iComando").setValue(PULSAR);
                    dbFire.child("RTS").child("COMANDOS").child(c.getsID()).child("resposta").setValue(false);
                }

            }
        });
        holder.swComando.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    dbFire.child("RTS").child("COMANDOS").child(c.getsID()).child("iComando").setValue(LIGAR);
                    dbFire.child("RTS").child("COMANDOS").child(c.getsID()).child("resposta").setValue(false);
                } else {
                    dbFire.child("RTS").child("COMANDOS").child(c.getsID()).child("iComando").setValue(DESLIGAR);
                    dbFire.child("RTS").child("COMANDOS").child(c.getsID()).child("resposta").setValue(false);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaComandos.size();
    }

    public AdapterListaComandos(List<Comandos> c){
        listaComandos = c;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nome;
        TextView time;
        TextView resposta;
        Button btComando;
        Switch swComando;

        public MyViewHolder(final View itemView){
            super(itemView);
            nome = itemView.findViewById(R.id.tvComandoNome);
            time = itemView.findViewById(R.id.tvComandoData);
            resposta = itemView.findViewById(R.id.tvComandoResposta);
            btComando = itemView.findViewById(R.id.btnComando);
            swComando = itemView.findViewById(R.id.swComando);
        }
    }
}
