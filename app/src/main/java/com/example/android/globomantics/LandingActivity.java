package com.example.android.globomantics;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.globomantics.services.MessageService;
import com.example.android.globomantics.services.ServiceBuilder;

import retrofit2.Call;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        new GetMessagesTask().execute();
    }

    public class GetMessagesTask extends AsyncTask<Void, Void, Boolean> {

        private String message = "";

        @Override
        protected Boolean doInBackground(Void... params) {
            MessageService taskService = ServiceBuilder.buildService(MessageService.class);
            Call<String> call = taskService.getMessages("http://localhost:9000/");
            try {
                message = call.execute().body();
            } catch (Exception e) {
                // Todo
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            ((TextView) findViewById(R.id.message)).setText(message);
        }

        @Override
        protected void onCancelled() {
            // Todo
        }
    }

    public void GetStarted(View view){
        Intent intent = new Intent(this, IdeaListActivity.class);
        startActivity(intent);
    }
}
