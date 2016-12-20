package ruben.dam.izv.com.proyecto_0.vistas.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import harmony.java.awt.Color;
import ruben.dam.izv.com.proyecto_0.R;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;

/**
 * Created by Vir on 16/11/16.
 */

public class AdaptadorRvNota extends CursorRecyclerViewAdapter<AdaptadorRvNota.anViewHolder> {


    private AdapterView.OnItemClickListener onItemClickListener;

    private AdapterView.OnItemLongClickListener onItemLongClickListener;


    public AdaptadorRvNota(Context context, Cursor c) {
        super(context, c);
    }

    public class anViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        public TextView tvTitulo;
        public CardView item;

        public anViewHolder(View view) {
            super(view);
            tvTitulo = (TextView) view.findViewById(R.id.tvTituloNota);
            CardView item = (CardView) view.findViewById(R.id.cvNota);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);


        }


        @Override
        public void onClick(View v) {
            onItemHolderClick(this);
        }

        @Override
        public boolean onLongClick(View v) {
            onItemHolderLongClick(this);
            return false;
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


    private void onItemHolderClick(anViewHolder vh) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, vh.itemView, vh.getAdapterPosition(), vh.getItemId());
        }
    }

    private void onItemHolderLongClick(anViewHolder vh) {
        if (onItemLongClickListener != null) {
            onItemLongClickListener.onItemLongClick(null, vh.itemView, vh.getAdapterPosition(), vh.getItemId());
        }
    }

    @Override
    public anViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        anViewHolder vh = new anViewHolder(itemView);
        return vh;
    }


    private long obtenerIdNota(int posicion) {
        return getItemId(posicion);
    }

    @Override
    public void onBindViewHolder(anViewHolder holder, Cursor cursor) {
        Nota n = Nota.getNota(cursor);
        holder.tvTitulo.setText(n.getTitulo());
        if (n.getColor() != null) {
            switch (n.getColor()) {
                case "rojo":
                    holder.tvTitulo.setBackgroundResource(R.color.rojo);
                    holder.tvTitulo.setTextColor(Color.WHITE.getRGB());

                    break;
                case "azul":
                    holder.tvTitulo.setBackgroundResource(R.color.azul);
                    holder.tvTitulo.setTextColor(Color.black.getRGB());
                    break;
                case "verde":
                    holder.tvTitulo.setBackgroundResource(R.color.verde);
                    holder.tvTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "morado":
                    holder.tvTitulo.setBackgroundResource(R.color.morado);
                    holder.tvTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "magenta":
                    holder.tvTitulo.setBackgroundResource(R.color.magenta);
                    holder.tvTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "gris":
                    holder.tvTitulo.setBackgroundResource(R.color.gris);
                    holder.tvTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "azul_oscuro":
                    holder.tvTitulo.setBackgroundResource(R.color.azul_oscuro);
                    holder.tvTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "naranja":
                    holder.tvTitulo.setBackgroundResource(R.color.naranja);
                    holder.tvTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                default:
                    holder.tvTitulo.setBackgroundColor(Color.WHITE.getRGB());
                    holder.tvTitulo.setTextColor(Color.GRAY.getRGB());
            }
        }

    }
}
