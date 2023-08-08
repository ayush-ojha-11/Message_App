package com.as.mymessage.modals;

public class ContactRecyclerModal {
    private String id;
    private String contactPhotoUri;
    private String contactName;
    private String mobNumber;

    public ContactRecyclerModal(String id, String contactPhotoUri, String contactName, String mobNumber) {
        this.id = id;
        this.contactPhotoUri = contactPhotoUri;
        this.contactName = contactName;
        this.mobNumber = mobNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactPhoto() {
        return contactPhotoUri;
    }

    public void setContactPhoto(String contactPhoto) {
        this.contactPhotoUri = contactPhoto;
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
