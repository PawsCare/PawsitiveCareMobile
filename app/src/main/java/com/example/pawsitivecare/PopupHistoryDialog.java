package com.example.pawsitivecare;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class PopupHistoryDialog extends DialogFragment {

    private static final String ARG_TRANSACTION = "transaction";

    public static PopupHistoryDialog newInstance(Transaction transaction) {
        PopupHistoryDialog fragment = new PopupHistoryDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRANSACTION, (Serializable) transaction);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_history, container, false);

        TextView dateTextView = view.findViewById(R.id.date);
        TextView transactionCodeTextView = view.findViewById(R.id.transaction_code);
        TextView usernameTextView = view.findViewById(R.id.username);
        TextView emailTextView = view.findViewById(R.id.email);
        TextView noHpTextView = view.findViewById(R.id.no_hp);
        TextView addressTextView = view.findViewById(R.id.address);
        TextView metodePembayaranTextView = view.findViewById(R.id.metode_pembayaran);
        TextView totalTextView = view.findViewById(R.id.total);
        TextView finishButton = view.findViewById(R.id.finish_button);

        if (getArguments() != null) {
            Transaction transaction = (Transaction) getArguments().getSerializable(ARG_TRANSACTION);

            if (transaction != null) {
                dateTextView.setText(transaction.getDate());
                transactionCodeTextView.setText(transaction.getTransaction_code());
                usernameTextView.setText(transaction.getUsername());
                emailTextView.setText(transaction.getEmail());
                noHpTextView.setText(transaction.getNo_hp());
                addressTextView.setText(transaction.getAlamat());
                metodePembayaranTextView.setText(transaction.getMetode_pembayaran());
                totalTextView.setText(String.valueOf(transaction.getTotal()));
            }
        }

        finishButton.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
