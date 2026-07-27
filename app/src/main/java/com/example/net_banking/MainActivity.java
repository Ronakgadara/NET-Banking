package com.example.net_banking;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Custom Exception
class NotSufficientFund extends Exception {
    public NotSufficientFund(String message) {
        super(message);
    }
}

// Transaction Model for Firebase
class TransactionRecord {
    public String type;
    public int amount;
    public String timestamp;

    public TransactionRecord() {} // Required for Firebase

    public TransactionRecord(String type, int amount, String timestamp) {
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }
}

public class MainActivity extends AppCompatActivity {

    private int balance = 20; // Initial local balance
    private final int MIN_BALANCE = 20;

    private TextView tvBalance;
    private TextInputEditText etAmount;
    private LinearLayout transactionContainer;
    private TextView tvEmptyState;
    private ImageView imageView;
    // Firebase References
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize Firebase (pointing to the root of your DB)
        dbRef = FirebaseDatabase.getInstance().getReference("account_data");

        // 2. Initialize UI Views
        tvBalance = findViewById(R.id.tvBalance);
        etAmount = findViewById(R.id.etAmount);
        transactionContainer = findViewById(R.id.transactionContainer);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        imageView = findViewById(R.id.imageView);
        MaterialButton btnDeposit = findViewById(R.id.btnDeposit);
        MaterialButton btnWithdraw = findViewById(R.id.btnWithdraw);

        // 3. Load Data from Firebase on startup
        loadDataFromFirebase();

        // 4. Deposit Logic
        btnDeposit.setOnClickListener(v -> {
            String input = etAmount.getText().toString();
            if (!input.isEmpty()) {
                int amount = Integer.parseInt(input);
                if(amount > 0){
                    performTransaction("Deposit", amount);
                }
                else{
                    Toast.makeText(this, "Enter valid amount", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
            }
        });

        imageView.setOnClickListener(view -> {

            new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete History")
                    .setMessage("Are you sure you want to delete all transaction history?")
                    .setPositiveButton("Delete", (dialog, which) -> {

                        dbRef.child("history").removeValue()
                                .addOnSuccessListener(unused -> {
                                    transactionContainer.removeAllViews();
                                    tvEmptyState.setVisibility(View.VISIBLE);

                                    Toast.makeText(MainActivity.this,
                                            "History deleted successfully",
                                            Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(MainActivity.this,
                                                "Failed to delete history",
                                                Toast.LENGTH_SHORT).show());
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
        // 5. Withdraw Logic
        btnWithdraw.setOnClickListener(v -> {
            String input = etAmount.getText().toString();
            if (!input.isEmpty()) {
                int amount = Integer.parseInt(input);
                if(amount > 0){
                    try {
                        if (amount > balance) throw new NotSufficientFund("Insufficient balance!");
                        if ((balance - amount) < MIN_BALANCE) throw new NotSufficientFund("Min balance $20 required!");

                        performTransaction("Withdrawal", amount);
                    } catch (NotSufficientFund e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Enter valid amount", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performTransaction(String type, int amount) {
        // Update local balance
        if (type.equals("Deposit")) {
            balance += amount;
        } else {
            balance -= amount;
        }

        // Create timestamp
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        // Save to Firebase: Update Balance
        dbRef.child("balance").setValue(balance);

        // Save to Firebase: Push Transaction to History
        TransactionRecord record = new TransactionRecord(type, amount, time);
        dbRef.child("history").push().setValue(record);

        updateUI("Transaction successful!");
    }

    private void loadDataFromFirebase() {
        // Listen for balance changes
        dbRef.child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    balance = snapshot.getValue(Integer.class);
                    tvBalance.setText("$" + balance + ".00");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // Listen for history changes
        dbRef.child("history").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactionContainer.removeAllViews(); // Clear UI
                tvEmptyState.setVisibility(snapshot.exists() ? View.GONE : View.VISIBLE);

                for (DataSnapshot ds : snapshot.getChildren()) {
                    TransactionRecord record = ds.getValue(TransactionRecord.class);
                    if (record != null) {
                        addEntryToUI(record.type, record.amount, record.timestamp);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void addEntryToUI(String type, int amount, String time) {
        TextView tvEntry = new TextView(this);
        String sign = type.equals("Deposit") ? "+$" : "-$";
        tvEntry.setText(type + ": " + sign + amount + " (" + time + ")");
        tvEntry.setPadding(0, 8, 0, 8);
        tvEntry.setTextSize(16);
        tvEntry.setTextColor(type.equals("Deposit") ? Color.parseColor("#2E7D32") : Color.parseColor("#C62828"));

        // Add to top of container
        transactionContainer.addView(tvEntry, 0);
    }

    private void updateUI(String message) {
        etAmount.setText("");
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}