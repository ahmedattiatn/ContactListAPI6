package ahmedattia.contactlisttestforteedji.activities;

/**
 * Created by Ahmed Attia on 06/04/2017.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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

public class AllContactsVerySimplePermission extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST = 100;
    RecyclerView rvContacts;
    EditText search;
    List<ContactVO> contactVOList; // lists of contacts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_all_contacts);
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        search = (EditText) findViewById(R.id.search);
        requestPermission();
         // search for contacts using the TextListener
        getWindow().setSoftInputMode(
                // hiding the key board on activity created
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    private void requestPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            addTextListener();
            getContactsDetails();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                addTextListener();
                getContactsDetails();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
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
                rvContacts.setLayoutManager(new LinearLayoutManager(AllContactsVerySimplePermission.this));
                rvContacts.setAdapter(contactAdapter);
            }
        });

    }

    private void getContactsDetails() {
        contactVOList = new ArrayList();
        ContactVO contactVO;
        Cursor phones = AllContactsVerySimplePermission.this.getContentResolver().query(
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
        rvContacts.setLayoutManager(new LinearLayoutManager(AllContactsVerySimplePermission.this));
        rvContacts.setAdapter(contactAdapter);
    }
}
