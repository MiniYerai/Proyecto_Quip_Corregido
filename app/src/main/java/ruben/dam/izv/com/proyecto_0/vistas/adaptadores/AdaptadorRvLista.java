package ruben.dam.izv.com.proyecto_0.vistas.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import ruben.dam.izv.com.proyecto_0.R;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.ElementoLista;

/**
 * Created by Vir on 21/11/2016.
 */

public class AdaptadorRvLista extends RecyclerView.Adapter<AdaptadorRvLista.alViewHolder>  {


    private ArrayList<ElementoLista> items;
    private View.OnClickListener listener;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;

    public class alViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnClickListener{
        // Campos respectivos de un item

        public TextView contenido;
        public CheckBox hecho;
        public ImageButton borrar;

        public alViewHolder(View v){
            super(v);

            contenido = (TextView) v.findViewById(R.id.tvElemento);
            hecho = (CheckBox) v.findViewById(R.id.cbHecho);
            borrar= (ImageButton) v.findViewById(R.id.btBorrar);
            borrar.setOnClickListener(this);


            v.setOnLongClickListener(this);
            //v.setOnClickListener(this);
        }

        public void onClick(View v) {
            onItemHolderClick(this);
        }

        public boolean onLongClick(View v) {
            onItemHolderLongClick(this);
            return false;
        }


    }

    public AdaptadorRvLista(ArrayList<ElementoLista> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public alViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista, viewGroup, false);

        alViewHolder vh= new alViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(alViewHolder viewHolder, int i) {

        viewHolder.contenido.setText(items.get(i).getTexto());

    }


    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


    private void onItemHolderClick(alViewHolder vh) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, vh.itemView, vh.getAdapterPosition(), vh.getItemId());
        }
    }

    private void onItemHolderLongClick(alViewHolder vh) {
        if (onItemLongClickListener != null) {
            onItemLongClickListener.onItemLongClick(null, vh.itemView, vh.getAdapterPosition(), vh.getItemId());
        }
    }



}
