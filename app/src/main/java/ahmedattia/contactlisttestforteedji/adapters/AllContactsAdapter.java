package ahmedattia.contactlisttestforteedji.adapters;

/**
 * Created by Ahmed Attia on 06/04/2017.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.squareup.picasso.Picasso;

import java.util.List;

import ahmedattia.contactlisttestforteedji.R;
import ahmedattia.contactlisttestforteedji.activities.Profile;
import ahmedattia.contactlisttestforteedji.model.ContactVO;

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder> {
    TextDrawable drawable;
    private List<ContactVO> contactVOList;
    private Context mContext;

    public AllContactsAdapter(List<ContactVO> contactVOList, Context mContext) {
        this.contactVOList = contactVOList;
        this.mContext = mContext;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
        final ContactVO contactVO = contactVOList.get(position);
        holder.tvContactName.setText(contactVO.getContactName());
        String phoneNum = null;
        try {
            phoneNum = FormatphoneNumber(contactVO.getContactNumber());
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        holder.tvPhoneNumber.setText(phoneNum);

        // setting the imageView of the contact
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        String character = contactVO.getContactName().charAt(0) + "";
        if (character.compareTo(".") == 0) {
            // if the first character of the user begin with . so we put ? as default

            character = "?";
        }
        drawable = TextDrawable.builder()
                .buildRoundRect(character, color, 20);

        if (contactVO.getContactImage() != null) {
            Picasso.with(mContext).load(contactVO.getContactImage()).into(holder.ivContactImage);
        } else {
            holder.ivContactImage.setImageDrawable(drawable);

        }
        final String finalPhoneNum = phoneNum;
        holder.ivContactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // sending info to the Profile activity
                intent.putExtra("nameContact", contactVOList.get(position).getContactName());
                intent.putExtra("phoneContact", finalPhoneNum);
                intent.putExtra("MailContact", "This user has no Mail");
                intent.putExtra("imgUri", contactVOList.get(position).getContactImage());
                mContext.startActivity(intent);
            }
        });
        // call the contact
        Picasso.with(mContext).load(R.mipmap.ic_phonecall).into(holder.ivContactImageCall);
        holder.ivContactImageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = contactVO.getContactNumber().toString();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mContext.startActivity(intent);
            }
        });
    }

    private String FormatphoneNumber(String contactNumber) throws NumberParseException {
        String upToNCharacters = contactNumber.substring(0, Math.min(contactNumber.length(), 3));
        String a = null;
        if (upToNCharacters.compareTo("+33") == 0) {
            try {
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber numberProto;
                numberProto = phoneUtil.parse(contactNumber, "");
                a = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);

            } catch (NumberParseException e) {
                e.printStackTrace();
            }
        } else {
            a = contactNumber;
        }
        return a;
    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView ivContactImage, ivContactImageCall;
        TextView tvContactName;
        TextView tvPhoneNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);
            // get all items of the RecyclerView
            ivContactImageCall = (ImageView) itemView.findViewById(R.id.ivContactImageCall);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
        }
    }


}
