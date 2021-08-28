package com.opencv.businesscamera.opencvc1;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tay on 10/5/2018.
 */

public class ContactActivity extends AppCompatActivity {
    int from_Where_I_Am_Coming = 0;
    private DBHelper mydb ;

    TextView inputName ;
    TextView inputPhone;
    TextView inputEmail;
    TextView inputAddress;
    int id_To_Update = 0;
    CameraMainActivity cmain;
    private static final String TAG="Contact Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_field);

        inputName = (TextView) findViewById(R.id.input1);
        inputPhone = (TextView) findViewById(R.id.input3);
        inputEmail = (TextView) findViewById(R.id.input2);
        inputAddress = (TextView) findViewById(R.id.input4);

        mydb = new DBHelper(this);
       // Log.d(TAG,cmain.name);

        Bundle extras = getIntent().getExtras();
        Log.d(TAG,"details="+bundle2string(extras));


        if(extras !=null) {
            int Value = extras.getInt("id");

            if(Value>0){
                //means this is the view part not the add contact part.
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                final String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
                String emai = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));
                final String phon = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
                String stree = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_ADDRESS));

                if (!rs.isClosed())  {
                    rs.close();
                }
                Button b = (Button)findViewById(R.id.save_btn);
                b.setVisibility(View.INVISIBLE);
                Button cancel = (Button)findViewById(R.id.cancel_save_contact);
                cancel.setVisibility(View.INVISIBLE);

                inputName.setText((CharSequence) nam);
                inputName.setFocusable(false);
                inputName.setClickable(false);

                inputEmail.setText((CharSequence)emai);
                inputEmail.setFocusable(false);
                inputEmail.setClickable(false);

                inputPhone.setText ((CharSequence)phon);
                inputPhone.setFocusable(false);
                inputPhone.setClickable(false);

                inputAddress.setText((CharSequence)stree);
                inputAddress.setFocusable(false);
                inputAddress.setClickable(false);

                Button share = (Button)findViewById(R.id.share_button);
                share.setVisibility(View.VISIBLE);
                final String shareContact = nam+"\n"+phon;
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareWhatsappContact(shareContact);
                    }
                });


            }


        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                getMenuInflater().inflate(R.menu.display_contact, menu);
            } else if (Value ==0 ){
                //no menu
            } //else {getMenuInflater().inflate(R.menu.main_menu,menu);}
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.Edit_Contact:           //edit contact
                Log.d(TAG+"Edit","Edit page is opened");
                Button b = (Button)findViewById(R.id.save_btn);
                b.setVisibility(View.VISIBLE);
                Button share = (Button)findViewById(R.id.share_button);
                share.setVisibility(View.INVISIBLE);
                Button cancel = (Button)findViewById(R.id.cancel_save_contact);
                cancel.setVisibility(View.VISIBLE);

                inputName.setEnabled(true);
                inputName.setFocusableInTouchMode(true);
                inputName.setClickable(true);

                inputPhone.setEnabled(true);
                inputPhone.setFocusableInTouchMode(true);
                inputPhone.setClickable(true);

                inputEmail.setEnabled(true);
                inputEmail.setFocusableInTouchMode(true);
                inputEmail.setClickable(true);

                inputAddress.setEnabled(true);
                inputAddress.setFocusableInTouchMode(true);
                inputAddress.setClickable(true);


                return true;
            case R.id.Delete_Contact:        //delete contact

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteContact)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteContact(id_To_Update);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),DisplayContactActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show(  );
                return true;
            case R.id.Save_To_Phone_Contact:  //save to phone contact
                //save contact to phone

                Cursor rs = mydb.getData(id_To_Update);
                rs.moveToFirst();
                String namePhoneContact = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
                String emailPhoneContact = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));
                String phoneContact = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
                String streetPhoneContact = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_ADDRESS));

                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                int rawContactInsertIndex = ops.size();

                ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, namePhoneContact) // Name of the person
                        .build());
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(
                                ContactsContract.Data.RAW_CONTACT_ID,   rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneContact) // Number of the person
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); // Type of mobile number
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(
                                ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA,
                                emailPhoneContact)
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                                ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                        .build());                      //email of the person
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(
                                ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                        .withValue(
                                ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                                streetPhoneContact)
                        .withValue(
                                ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                                ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
                        .build());

                try
                {   ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);   }
                catch (RemoteException e)
                {    /* error*/   }
                catch (OperationApplicationException e)
                {   /* error*/   }

                Toast.makeText(getApplicationContext(), "Saved to phone contact successfully",
                        Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                if(mydb.updateContact(id_To_Update,inputName.getText().toString(),
                        inputEmail.getText().toString(),inputPhone.getText().toString(),
                        inputAddress.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),DisplayContactActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else{
                if(mydb.insertContact(inputName.getText().toString(), inputEmail.getText().toString(),
                        inputPhone.getText().toString(), inputAddress.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Save contact successfully",
                            Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),DisplayContactActivity.class);
                startActivity(intent);
            }
        }
    }

    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }

    public void shareWhatsappContact(String shareCon) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareCon);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Whatsapp have not been installed.",
                    Toast.LENGTH_SHORT).show();
        }

    }


    public void btnAdd_Contact_onClick(View view) {



    }

    public void onBackPressed() {
        super.onBackPressed();

//        Intent intent = new Intent(getApplicationContext(),DisplayContactActivity.class);
//        startActivity(intent);
    }



}
