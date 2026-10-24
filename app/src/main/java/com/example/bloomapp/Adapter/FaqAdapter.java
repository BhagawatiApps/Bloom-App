package com.example.bloomapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloomapp.Model.FaqModel;
import com.example.bloomapp.R;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqViewHolder> {

    private final List<FaqModel> faqList;

    public FaqAdapter(List<FaqModel> faqList) {
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FaqViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_faq, parent, false);

        return new FaqViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull FaqViewHolder holder, int position) {

        FaqModel faq = faqList.get(position);
        holder.title.setText(faq.getTitle());
        holder.description.setText(faq.getDescription());
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    static class FaqViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        FaqViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvFaqTitle);
            description = itemView.findViewById(R.id.tvFaqDescription);
        }
    }
}