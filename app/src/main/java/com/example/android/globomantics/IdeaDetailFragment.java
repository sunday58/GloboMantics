package com.example.android.globomantics;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.android.globomantics.models.Idea;
import com.example.android.globomantics.services.IdeaServices;
import com.example.android.globomantics.services.ServiceBuilder;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdeaDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private Idea mItem;

    public IdeaDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.idea_detail, container, false);

        final Context context = getContext();

        Button updateIdea = rootView.findViewById(R.id.idea_update);
        Button deleteIdea = rootView.findViewById(R.id.idea_delete);

        final EditText ideaName = rootView.findViewById(R.id.idea_name);
        final EditText ideaDescription = rootView.findViewById(R.id.idea_description);
        final EditText ideaStatus = rootView.findViewById(R.id.idea_status);
        final EditText ideaOwner = rootView.findViewById(R.id.idea_owner);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Activity activity = this.getActivity();
            final CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);

            IdeaServices ideaService = ServiceBuilder.buildService(IdeaServices.class);
            Call<Idea> request = ideaService.getIdea(getArguments().getInt(ARG_ITEM_ID));

            request.enqueue(new Callback<Idea>() {
                @Override
                public void onResponse(Call<Idea> request, Response<Idea> response) {
                    mItem = response.body();

                    ideaName.setText(mItem.getName());
                    ideaDescription.setText(mItem.getDescription());
                    ideaOwner.setText(mItem.getOwner());
                    ideaStatus.setText(mItem.getStatus());

                    if (appBarLayout != null) { appBarLayout.setTitle(mItem.getName()); }
                }

                @Override
                public void onFailure(Call<Idea> request, Throwable t) {
                    Toast.makeText(context, "Failed to retrieve item.", Toast.LENGTH_SHORT).show();;
                }
            });
        }

        updateIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IdeaServices ideaService = ServiceBuilder.buildService(IdeaServices.class);
                Call<Idea> request = ideaService.updateIdea(
                        getArguments().getInt(ARG_ITEM_ID),
                        ideaName.getText().toString(),
                        ideaDescription.getText().toString(),
                        ideaStatus.getText().toString(),
                        ideaOwner.getText().toString()
                );

                request.enqueue(new Callback<Idea>() {
                    @Override
                    public void onResponse(Call<Idea> request, Response<Idea> response) {
                        Intent intent = new Intent(context, IdeaListActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Idea> request, Throwable t) {
                        Toast.makeText(context, "Failed to retrieve item.", Toast.LENGTH_SHORT).show();;
                    }
                });
            }
        });

        deleteIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IdeaServices ideaService = ServiceBuilder.buildService(IdeaServices.class);
                Call<Void> deleteRequest = ideaService.deleteIdea(getArguments().getInt(ARG_ITEM_ID));

                deleteRequest.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> request, Response<Void> response) {
                        Intent intent = new Intent(context, IdeaListActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Void> request, Throwable t) {
                        Toast.makeText(context, "Failed to delete item.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return rootView;
    }
}
