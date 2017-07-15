package l200130134.restapi;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by User-PRO on 14/07/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<MahasiswaList> listItems;
    private Context context;

    public ListAdapter(List<MahasiswaList> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MahasiswaList items_list = listItems.get(position);

        holder.nim.setText(items_list.getNim());
        holder.nama.setText(items_list.getNama());
        holder.jenjang.setText(items_list.getJenjang());
        holder.pt.setText(items_list.getPt());
        holder.prodi.setText(items_list.getProdi());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You Clicked : " + items_list.getNim(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView nim;
        TextView nama;
        TextView jenjang;
        TextView pt;
        TextView prodi;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            nim = (TextView) itemView.findViewById(R.id.nim);
            nama = (TextView) itemView.findViewById(R.id.nama);
            jenjang = (TextView) itemView.findViewById(R.id.jenjang);
            pt = (TextView) itemView.findViewById(R.id.pt);
            prodi = (TextView) itemView.findViewById(R.id.prodi);
        }
    }
}
