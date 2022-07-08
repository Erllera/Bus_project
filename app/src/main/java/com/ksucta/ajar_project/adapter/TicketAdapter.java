package com.ksucta.ajar_project.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksucta.ajar_project.R;
import com.ksucta.ajar_project.models.InfoModel;
import com.ksucta.ajar_project.models.TicketSample;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private final ItemClick onItemClickListener;
    ArrayList<TicketSample> ticketList;
    ArrayList<InfoModel> list;
    Context context;

    public TicketAdapter(Context context, ItemClick onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.context = context;
        ticketList = new ArrayList<>();
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TicketViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_item, parent, false));
    }

    public TicketSample getItemAt(int pos) {
        return ticketList.get(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        holder.bind(ticketList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.click(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addTicket(ArrayList<TicketSample> list) {
        this.ticketList = list;
        notifyDataSetChanged();
    }

    public TicketSample getItem(int position) {
        return ticketList.get(position);
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder {

        private TextView from, to, time, number, driver, textDate;


        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.textFrom);
            to = itemView.findViewById(R.id.textTo);
            time = itemView.findViewById(R.id.textTime);
            number = itemView.findViewById(R.id.textNumber);
            driver = itemView.findViewById(R.id.driverTextInfo);
            textDate = itemView.findViewById(R.id.textDate);
/*            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.click(getAdapterPosition());
                }
            });*/
        }

        public void bind(TicketSample ticketSample) {
            Locale ru = new Locale("ru", "RU");
            SimpleDateFormat date = new SimpleDateFormat("dd-MMM yyyy, HH:mm", ru);
            String dateFormat = String.valueOf(date.format(ticketSample.getDate()));
            textDate.setText("Билет был создан " + dateFormat);
            from.setText(ticketSample.getFrom());
            to.setText(ticketSample.getTo());
            time.setText(ticketSample.getTime());
            number.setText(ticketSample.getNumber());
            driver.setText(ticketSample.getDriverInfo());
        }
    }
}
