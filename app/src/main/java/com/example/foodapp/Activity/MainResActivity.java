    package com.example.foodapp.Activity;

    import android.content.Intent;
    import android.os.Bundle;

    import androidx.activity.EdgeToEdge;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import com.example.foodapp.R;
    import com.example.foodapp.databinding.ActivityMainBinding;
    import com.example.foodapp.databinding.ActivityMainResBinding;

    public class MainResActivity extends BaseActivity {
        ActivityMainResBinding binding;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding= ActivityMainResBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            setVariable();
        }

        private void setVariable() {

        }
    }