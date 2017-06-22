package ahmedattia.contactlisttestforteedji.activities;

/**
 * Created by Ahmed Attia on 06/04/2017.
 */

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ahmedattia.contactlisttestforteedji.R;
import ahmedattia.contactlisttestforteedji.adapters.AllContactsAdapter;
import ahmedattia.contactlisttestforteedji.model.ContactVO;

public class AllContacts extends AppCompatActivity {
    RecyclerView rvContacts;
    EditText search;
    List<ContactVO> contactVOList; // lists of contacts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_all_contacts);
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        search = (EditText) findViewById(R.id.search);

        final int PERMISSION_ALL = 1;
        final String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (!hasPermissions(AllContacts.this, PERMISSIONS)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Permissions !!")
                    .setMessage(" This app need Permission to Access to your Contact , call phone And your storage")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ActivityCompat.requestPermissions(AllContacts.this, PERMISSIONS, PERMISSION_ALL);

                        }
                    });
            builder.create().show();
        }

        //  getContactsDetails();
        addTextListener(); // search for contacts using the TextListener
        getWindow().setSoftInputMode(
                // hiding the key board on activity created
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,

                                           int[] grantResults) {

        if (requestCode == 1) {
            getContactsDetails();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        Toast.makeText(AllContacts.this, "All Permissions are Granted !!", Toast.LENGTH_LONG).show();
        getContactsDetails();
        return true;

    }

    private void addTextListener() {
        search.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<ContactVO> filteredList = new ArrayList<>();

                for (int i = 0; i < contactVOList.size(); i++) {
                    //  every time the user type something (name) we compare
                    // with the list content and display another list with results
                    // call filteredList
                    final String text = contactVOList.get(i).getContactName().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(contactVOList.get(i));


                    }
                }
                // set new adapter to the RecyclerView using the new list that we have
                AllContactsAdapter contactAdapter = new AllContactsAdapter(filteredList, getApplicationContext());
                rvContacts.setLayoutManager(new LinearLayoutManager(AllContacts.this));
                rvContacts.setAdapter(contactAdapter);
            }
        });

    }

    private void getContactsDetails() {
        contactVOList = new ArrayList();
        ContactVO contactVO;
        Cursor phones = AllContacts.this.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        while (phones.moveToNext()) {
            String Name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String Number = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            String image_uri = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            contactVO = new ContactVO();
            contactVO.setContactImage(image_uri);
            contactVO.setContactName(Name);
            contactVO.setContactNumber(Number);
            contactVOList.add(contactVO);


        }
        AllContactsAdapter contactAdapter = new AllContactsAdapter(contactVOList, getApplicationContext());
        rvContacts.setLayoutManager(new LinearLayoutManager(AllContacts.this));
        rvContacts.setAdapter(contactAdapter);
    }
}
