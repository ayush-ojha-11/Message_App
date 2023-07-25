package com.as.mymessage.modals;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactRecyclerModal {
    String contactPhoto;
    String contactName;
    String mobNumber;

    public ContactRecyclerModal(String contactPhoto, String contactName, String mobNumber) {
        this.contactPhoto = contactPhoto;
        this.contactName = contactName;
        this.mobNumber = mobNumber;
    }

    public String getContactPhoto() {
        return contactPhoto;
    }

    public void setContactPhoto(String contactPhoto) {
        this.contactPhoto = contactPhoto;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMobNumber() {
        return mobNumber;
    }

    public void setMobNumber(String mobNumber) {
        this.mobNumber = mobNumber;
    }
}
