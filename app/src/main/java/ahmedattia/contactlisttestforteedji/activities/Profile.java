package ahmedattia.contactlisttestforteedji.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import ahmedattia.contactlisttestforteedji.R;

/**
 * Created by Ahmed Attia on 06/04/2017.
 */

public class Profile extends Activity {
    TextView Name, Number, Mail;

    ImageView imageViewProfile, imgName, imgNumber, imgMail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);

        Name = (TextView) findViewById(R.id.tvnameContactProfile);
        Number = (TextView) findViewById(R.id.tvphonerofile);
        Mail = (TextView) findViewById(R.id.tvmailCotactProfile);
        imageViewProfile = (ImageView) findViewById(R.id.ivContactImageProfile);
        imgName = (ImageView) findViewById(R.id.iconname);
        imgNumber = (ImageView) findViewById(R.id.img_call);
        imgMail = (ImageView) findViewById(R.id.imageViewmail);
        // set icons for mail , phone and name
        imgName.setImageResource(R.mipmap.ic_user);
        imgNumber.setImageResource(R.mipmap.ic_phonecall);
        imgMail.setImageResource(R.mipmap.ic_mail);

        // get info from the AllContactsAdapter
        Number.setText(getIntent().getExtras().getString("phoneContact"));
        Name.setText(getIntent().getExtras().getString("nameContact"));
        Mail.setText(getIntent().getExtras().getString("MailContact"));
        // set the image view of the user
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        String character = getIntent().getExtras().getString("nameContact").charAt(0) + "";
        if (character.compareTo(".") == 0) {
            // if the first character of the user begin with . so we put ? as default
            character = "?";
        }
        Drawable drawable = TextDrawable.builder()
                .buildRoundRect(character, color, 60);
        if (getIntent().getExtras().getString("imgUri") != null) {
            Picasso.with(Profile.this).load(getIntent().getExtras().getString("imgUri")).into(imageViewProfile);
        } else {
            imageViewProfile.setImageDrawable(drawable);
        }


        // send messages using the FloatingActionButton
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fabbutton);
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + Number.getText().toString()));
                startActivity(sendIntent);
            }
        });
    }
}
