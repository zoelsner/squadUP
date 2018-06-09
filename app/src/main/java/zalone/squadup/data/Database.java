package zalone.squadup.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database implements EventListener<DocumentSnapshot> {

    public interface OnResultListener<T> {
        void onResult(T result);

    }
    private static Database INSTANCE;
    public static Database get() {
        if (Database.INSTANCE == null) {
            Database.INSTANCE = new Database();
        }

        return Database.INSTANCE;
    }

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private boolean registered = false;
    private User user;

    private Database() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public User getCachedUser() {
        return user;
    }


    @Override
    public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            //Log.d("howdy", "fuck", e);
            this.user = null;
            return;
        }

        if (document.exists()) {
            this.user = document.toObject(User.class);
        } else {
            this.user = null;
        }
    }

    public boolean isLoggedIn() {
        return this.getFirebaseUser() != null;
    }

    public FirebaseUser getFirebaseUser() {
        return auth.getCurrentUser();
    }

    public DocumentReference getUserDocumentReference() {
        return db.collection("users").document(getFirebaseUser().getUid());
    }

    public void setUser(User user, final OnResultListener<Void> listener) {
        this.getUserDocumentReference().set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onResult(null);
            }
        });
    }

    /**
     * Returns null if not logged in or there is an error, otherwise returns a User object.
     */
    public void getUser(final OnResultListener<User> resultListener) {
        final FirebaseUser firebaseUser = this.getFirebaseUser();

        if (firebaseUser == null) {
            resultListener.onResult(null);
            return;
        }

        if (user != null) {
            resultListener.onResult(user);
            return;
        }

        if (!registered) {
            getUserDocumentReference().addSnapshotListener(this);
            registered = true;
        }

        final DocumentReference userDocRef = this.getUserDocumentReference();
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) {
                    resultListener.onResult(null);
                    return;
                }

                DocumentSnapshot doc = task.getResult();

                if (!doc.exists()) {
                    final User user = new User(
                            firebaseUser.getDisplayName(),
                            firebaseUser.getPhotoUrl().toString(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            0,
                            0
                    );

                    userDocRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                resultListener.onResult(null);
                                return;
                            }

                            resultListener.onResult(user);
                        }
                    });
                    return;
                }

                User user = task.getResult().toObject(User.class);
                resultListener.onResult(user);
            }
        });
    }

    public void getRecentUsers(final OnResultListener<List<User>> resultListener) {
        Query recentUsersQuery = this.db.collection("users")
                .limit(30L)
                .orderBy("lastSquaddingUp", Query.Direction.DESCENDING);

        recentUsersQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> recentUsers = new ArrayList<>();

                if (!task.isSuccessful()) {
                    //Log.d("howdy", "get recent users task failed!");
                    resultListener.onResult(recentUsers);
                    return;
                }

                List<DocumentSnapshot> recentUserDocuments = task.getResult().getDocuments();
                for (DocumentSnapshot doc : recentUserDocuments) {
                    User user = doc.toObject(User.class);
                    user.setFirebaseId(doc.getId());
                    recentUsers.add(user);
                }

                resultListener.onResult(recentUsers);
            }
        });
    }

}
