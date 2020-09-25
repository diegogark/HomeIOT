package com.diegogark.homeiot.config;

import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diegogark.homeiot.R;
import com.diegogark.homeiot.model.Item;

import java.util.ArrayList;
import java.util.List;

public class AdapterListaCompra extends RecyclerView.Adapter<AdapterListaCompra.MyViewHolder> {

    private List<Item> listaItem;
    private String selecao;

    public AdapterListaCompra(){

    }

    public AdapterListaCompra(List<Item> lista, String sel){
        listaItem = lista;
        selecao = sel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_compra, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = listaItem.get(position);
        holder.tvItem.setText(item.getItem());
        if (item.isTitulo()){
            holder.llLista.setBackgroundResource(R.drawable.fundo_titulo);
            holder.llLista.setGravity(Gravity.BOTTOM);
            holder.tvItem.setTextSize(16);
            holder.tvItem.setGravity(Gravity.BOTTOM);
            holder.tvItem.setTextAppearance(R.style.TextAppearance_AppCompat_Title);
            if (selecao == item.getLista()){
                holder.llLista.setBackgroundResource(R.drawable.fundo_titulo_selecionado);
            }
        } else {
            if (item.isValidade()){
                holder.llLista.setBackgroundResource(R.drawable.fundo_transparent_lista);
            }else {
                holder.llLista.setBackgroundResource(R.drawable.remove);
            }
        }
        holder.ivSecao.setImageResource(item.getImagem());
    }

    @Override
    public int getItemCount() {
        return listaItem.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvItem;
        ImageView ivSecao;
        LinearLayout llLista;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            llLista = itemView.findViewById(R.id.llLista);
            tvItem = itemView.findViewById(R.id.tvItem);
            ivSecao = itemView.findViewById(R.id.ivSecao);
        }
    }

}
