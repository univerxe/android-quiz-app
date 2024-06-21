package com.example.app_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VocabAdapter extends RecyclerView.Adapter<VocabAdapter.VocabViewHolder> {

    private final List<Vocab> vocabList;
    private final LayoutInflater mInflater;

    public VocabAdapter(Context context, List<Vocab> vocabList) {
        mInflater = LayoutInflater.from(context);
        this.vocabList = vocabList;
    }

    @NonNull
    @Override
    public VocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.card_vocab, parent, false);
        return new VocabViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull VocabViewHolder holder, int position) {
        Vocab currentVocab = vocabList.get(position);
        holder.wordTextView.setText(currentVocab.getKorean());
        holder.itemView.setOnClickListener(v -> {
            if (holder.wordTextView.getText().toString().equals(currentVocab.getKorean())) {
                holder.wordTextView.setText(currentVocab.getEnglish());
            } else {
                holder.wordTextView.setText(currentVocab.getKorean());
            }
        });
    }

    @Override
    public int getItemCount() {
        return vocabList.size();
    }

    class VocabViewHolder extends RecyclerView.ViewHolder {
        public final TextView wordTextView;
        final VocabAdapter mAdapter;

        public VocabViewHolder(View itemView, VocabAdapter adapter) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.textView);
            this.mAdapter = adapter;
        }
    }
}
