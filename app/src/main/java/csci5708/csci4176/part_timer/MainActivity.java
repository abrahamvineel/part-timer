package csci5708.csci4176.part_timer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(navigation_listener);
        bottom_nav.setSelectedItemId(R.id.home);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigation_listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment fragment = null;
                    switch (item.getItemId()) {
                        case R.id.home:
                            fragment = new Home();
                            break;
                        case R.id.stats:
                            fragment = new Stats();
                            break;
                        case R.id.settings:
                            fragment = new Settings();
                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.home_frame,fragment).commit();

                    return true;

                }
            };
}
