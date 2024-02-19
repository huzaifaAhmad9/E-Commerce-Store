package com.example.coffeeshop.splash.paymentMethods;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.coffeeshop.R;
public class Payment_Detail extends AppCompatActivity {
    CardView card1, card2;
    TextView text1, text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCardBackground(card1, true, 45);
                setCardBackground(card2, false, 20);
                text1.setTextColor(Color.WHITE);
                text2.setTextColor(getResources().getColor(R.color.main));
                // toast
                Toast.makeText(Payment_Detail.this, "Coming Soon!!!", Toast.LENGTH_SHORT).show();
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCardBackground(card1, false, 20);
                setCardBackground(card2, true, 45);
                text2.setTextColor(Color.WHITE);
                text1.setTextColor(getResources().getColor(R.color.main));
                Intent i = new Intent(Payment_Detail.this, Cod.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void setCardBackground(CardView cardView, boolean isSelected, float cornerRadius) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(
                new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius,
                        cornerRadius, cornerRadius, cornerRadius, cornerRadius}, null, null));
        shapeDrawable.getPaint().setColor(isSelected ? Color.parseColor("#C67C4E") : Color.WHITE);
        cardView.setBackground(shapeDrawable);
    }

}



