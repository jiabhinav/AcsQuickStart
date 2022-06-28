package com.contoso.acsquickstart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import com.azure.android.communication.common.CommunicationIdentifier;
import com.azure.android.communication.common.CommunicationUserIdentifier;
import com.azure.android.communication.calling.Call;
import com.azure.android.communication.calling.CallAgent;
import com.azure.android.communication.calling.CallClient;
import com.azure.android.communication.calling.HangUpOptions;
import com.azure.android.communication.common.CommunicationTokenCredential;
import com.azure.android.communication.calling.StartCallOptions;

public class MainActivity extends AppCompatActivity {
    private static final String[] allPermissions = new String[] { Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE };
    private static final String UserToken = "eyJ0eXAiOiJKV1QiLCJub25jZSI6Im1Yc0dhMS1FTmlMR1ZtaDRiN0ZxM251eTR6ZGwzLXM3RHA3ZGpXb3VsMWsiLCJhbGciOiJSUzI1NiIsIng1dCI6ImpTMVhvMU9XRGpfNTJ2YndHTmd2UU8yVnpNYyIsImtpZCI6ImpTMVhvMU9XRGpfNTJ2YndHTmd2UU8yVnpNYyJ9.eyJhdWQiOiIwMDAwMDAwMy0wMDAwLTAwMDAtYzAwMC0wMDAwMDAwMDAwMDAiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9kMGZlYmU5MS04NjYxLTQ4YzAtYjUwMC1kMjg3OThkYTJjMDkvIiwiaWF0IjoxNjU1ODkzMzY3LCJuYmYiOjE2NTU4OTMzNjcsImV4cCI6MTY1NTg5ODUwNSwiYWNjdCI6MCwiYWNyIjoiMSIsImFpbyI6IkFWUUFxLzhUQUFBQUZRMlVxMUxDL1h3RXMwRUlsOUxrSy9xT3RCbC96alduVFJCRm5EQ2t4L2J1RndJcHRRSy9pR0lkVkZ0d3gvMXV2Tm5MZFRJRDJCQXVTRW1aa0xJaXJZdmIydkZ3YnpmVUJpNGR5emFZeWc4PSIsImFtciI6WyJwd2QiLCJtZmEiXSwiYXBwX2Rpc3BsYXluYW1lIjoiR3JhcGggRXhwbG9yZXIiLCJhcHBpZCI6ImRlOGJjOGI1LWQ5ZjktNDhiMS1hOGFkLWI3NDhkYTcyNTA2NCIsImFwcGlkYWNyIjoiMCIsImZhbWlseV9uYW1lIjoiU2h1a2xhIiwiZ2l2ZW5fbmFtZSI6IkFiaGluYXYiLCJpZHR5cCI6InVzZXIiLCJpcGFkZHIiOiIxMjIuMTc2LjEwMC4zMiIsIm5hbWUiOiJBYmhpbmF2IFNodWtsYSIsIm9pZCI6IjQxNGE2M2M1LTY3OGYtNDA5Yi1hMTZkLTMwMmY2NjhhNTBlOCIsInBsYXRmIjoiMyIsInB1aWQiOiIxMDAzMjAwMUY2QjZFQTVEIiwicmgiOiIwLkFYRUFrYjctMEdHR3dFaTFBTktIbU5vc0NRTUFBQUFBQUFBQXdBQUFBQUFBQUFDSEFCby4iLCJzY3AiOiJGaWxlcy5SZWFkV3JpdGUgTWFpbC5SZWFkIG9wZW5pZCBwcm9maWxlIFVzZXIuUmVhZCBlbWFpbCIsInN1YiI6Ik1KTUN3dU1FQ0RlMEc3ZlRCbTJOemdHWDZ2RFIzdWZuYjBDQXZqSW4zS2siLCJ0ZW5hbnRfcmVnaW9uX3Njb3BlIjoiQVMiLCJ0aWQiOiJkMGZlYmU5MS04NjYxLTQ4YzAtYjUwMC1kMjg3OThkYTJjMDkiLCJ1bmlxdWVfbmFtZSI6IkFiaGluYXYuU2h1a2xhQHJhZGlhbnRpbmZvbmV0LmNvbSIsInVwbiI6IkFiaGluYXYuU2h1a2xhQHJhZGlhbnRpbmZvbmV0LmNvbSIsInV0aSI6IjZXR3hfSHBqWWtpZzVTNkJST2NwQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbImI3OWZiZjRkLTNlZjktNDY4OS04MTQzLTc2YjE5NGU4NTUwOSJdLCJ4bXNfc3QiOnsic3ViIjoiQVNOc2V5UmlhdDQ5ZFQ4TU82bTNENTVDWXpCNGkyWGpzWkh4UDh6dWMtTSJ9LCJ4bXNfdGNkdCI6MTY1MDUyNjU2OX0.iy55XqO-8qOgqiuAujNTp4AdS66FeU2rz9a63eUIuwy5OWXQf9ErHDuKE-k9s88pTSBvIZduhCyzUccBbJL0SNb-dksMUUr4atoojrCi3CBcxTE9rUUkR7ikFwNqR4zQ7hVjcZsmuhbcoZYh4Fq0WYNn4L3gXrU3yS-3VGUYtZPImshWdEScc05rty9Huzp4Zd0GEUilPK-XELFtgfqsziItN7lrUywOUZWY6H6LVhdq6LxA1B9QXMYCxWAOASKTTdS_Cxmk4wxsBnJ2gO8qcqu_e0uc8g2goYpdkctB4SZy8CvibjmP-rzxXZOxlYPNlgJFWKR2Er2PbB5lN0LsWg";

    TextView statusBar;

    private CallAgent agent;
    private Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllPermissions();
        createAgent();

        Button callButton = findViewById(R.id.call_button);
        callButton.setOnClickListener(l -> startCall());

        Button hangupButton = findViewById(R.id.hangup_button);
        hangupButton.setOnClickListener(l -> endCall());

        statusBar = findViewById(R.id.status_bar);
        
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    /**
     * Start a call
     */
    private void startCall() {
        if (UserToken.startsWith("<")) {
            Toast.makeText(this, "Please enter token in source code", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText calleeIdView = findViewById(R.id.callee_id);
        String calleeId = calleeIdView.getText().toString();
        if (calleeId.isEmpty()) {
            Toast.makeText(this, "Please enter callee", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<CommunicationIdentifier> participants = new ArrayList<>();
        participants.add(new CommunicationUserIdentifier(calleeId));

        Iterable<CommunicationIdentifier> routeIterator =participants;
        StartCallOptions options = new StartCallOptions();

        call = agent.startCall(
                getApplicationContext(),
                routeIterator,
                options);



        call.addOnStateChangedListener(p -> setStatus(call.getState().toString()));
    }


    /**
     * Ends the call previously started
     */
    private void endCall() {
        try {
            call.hangUp(new HangUpOptions()).get();
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(this, "Unable to hang up call", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Create the call agent
     */
    private void createAgent() {
        try {
            CommunicationTokenCredential credential = new CommunicationTokenCredential(UserToken);
            agent = new CallClient().createCallAgent(getApplicationContext(), credential).get();
        } catch (Exception ex) {
            Log.d("rdewewbferrrgg3", "createAgent: "+ex.getMessage());
            Toast.makeText(getApplicationContext(), "Failed to create call agent.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Request each required permission if the app doesn't already have it.
     */
    private void getAllPermissions() {
        ArrayList<String> permissionsToAskFor = new ArrayList<>();
        for (String permission : allPermissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToAskFor.add(permission);
            }
        }
        if (!permissionsToAskFor.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToAskFor.toArray(new String[0]), 1);
        }
    }

    /**
     * Ensure all permissions were granted, otherwise inform the user permissions are missing.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean allPermissionsGranted = true;
        for (int result : grantResults) {
            allPermissionsGranted &= (result == PackageManager.PERMISSION_GRANTED);
        }
        if (!allPermissionsGranted) {
            Toast.makeText(this, "All permissions are needed to make the call.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * Shows message in the status bar
     */
    private void setStatus(String status) {
        runOnUiThread(() -> statusBar.setText(status));
    }
}
