package com.diegogark.homeiot.config;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diegogark.homeiot.R;
import com.diegogark.homeiot.model.Sensor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class AdapterListaMedidas extends RecyclerView.Adapter<AdapterListaMedidas.MyViewHolder> {
    private List<Sensor> listaSensor;

    public AdapterListaMedidas(){
    }

    public AdapterListaMedidas(List<Sensor> s){
        listaSensor = s;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicao_lista, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NumberFormat format = new DecimalFormat("0.0");
        Sensor sensor = listaSensor.get(position);
        if (sensor.getsNome() == null){
            holder.tvID.setText(sensor.getsID());
        }else {
            holder.tvID.setText(sensor.getsNome());
        }
        holder.tvTime.setText(sensor.getsTime());
        holder.tvValor.setText(format.format(sensor.getsValor()) + sensor.getsUnid());
        holder.tvMin.setText(format.format(sensor.getsVMin()) + sensor.getsUnid());
        holder.tvMax.setText(format.format(sensor.getsVMax()) + sensor.getsUnid());
    }

    @Override
    public int getItemCount() {
        return listaSensor.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvMax;
        TextView tvMin;
        TextView tvID;
        TextView tvValor;
        TextView tvTime;

        public MyViewHolder(View itemView){
            super(itemView);

            tvID = itemView.findViewById(R.id.tvID);
            tvMax = itemView.findViewById(R.id.tvMax);
            tvMin = itemView.findViewById(R.id.tvMin);
            tvValor = itemView.findViewById(R.id.tvValor);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
