package ahmedattia.contactlisttestforteedji.model;

/**
 * Created by Ahmed Attia on 06/04/2017.
 */

public class ContactVO {
    private String ContactImage;
    private String ContactName;
    private String ContactNumber;

    public String getContactImage() {
        return ContactImage;
    }

    public void setContactImage(String ContactImage) {
        this.ContactImage = ContactImage;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }
}