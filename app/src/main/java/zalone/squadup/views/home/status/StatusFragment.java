package zalone.squadup.views.home.status;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import zalone.squadup.R;
import zalone.squadup.data.Database;
import zalone.squadup.data.User;

public class StatusFragment extends Fragment implements EventListener<QuerySnapshot> {

    StatusRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    public StatusFragment() {
    }

    public static StatusFragment newInstance() {
        return new StatusFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status_list, container, false);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.status_fragment_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new StatusRecyclerViewAdapter(new ArrayList<User>(), getContext());
        recyclerView.setAdapter(adapter);

        fetchStatuses();

        return view;
    }


    public void fetchStatuses() {
        FirebaseFirestore.getInstance().collection("users")
                .orderBy("lastSquaddingUp", Query.Direction.DESCENDING)
                .limit(30L)
                .addSnapshotListener(getActivity(), this);
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
        List<User> users = new ArrayList<>();

        for (DocumentSnapshot doc : docs) {
            User user = doc.toObject(User.class);
            if (!user.equals(Database.get().getCachedUser())) {
                user.setFirebaseId(doc.getId());
                users.add(user);
            }
        }

        adapter = new StatusRecyclerViewAdapter(new ArrayList<User>(), getContext());
        recyclerView.setAdapter(adapter);

        adapter.setUsers(users);
        adapter.notifyDataSetChanged();
    }
}