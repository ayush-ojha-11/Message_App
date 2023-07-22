package com.as.mymessage.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.as.mymessage.R;

import java.io.IOException;
import java.io.InputStream;

public class ContactCheckerUtil {

    public static boolean isNumberInContacts (Context context, String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = new String[]{ContactsContract.PhoneLookup._ID};

        try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return true; // The number is found in contacts
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // The number is not found in contacts
    }
    public static String getContactNameFromNumber(Context context, String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                return cursor.getString(nameIndex); // Return the contact name
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // The contact name is not found
    }

    @SuppressLint("Range")
    public static long getContactPhotoIdFromPhoneNumber(Context context, String phoneNumber) {
        long photoId = -1; // Default value if no photo ID is found

        // Define the columns to retrieve from the Contacts table
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.PhoneLookup._ID
        };

        // Query the Contacts table using the given phone number
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = context.getContentResolver().query(
                contactUri,
                projection,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Retrieve the photo ID if available
                photoId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
            }
            cursor.close();
        }

        return photoId;
    }


    public static Bitmap getContactPhotoFromPhoneNumber(Context context, String phoneNumber) {
        long photoId = getContactPhotoIdFromPhoneNumber(context, phoneNumber);

        if (photoId == -1) {
            // No photo ID found for the given phone number
            return null;
        }

        // Build the photo URI using the photo ID
        Uri photoUri = Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, String.valueOf(photoId));

        // Query the ContactsContract.Data table to get the contact's photo
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
                photoUri,
                new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO},
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Retrieve the photo data from the cursor
                @SuppressLint("Range") byte[] photoData = cursor.getBlob(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO));
                cursor.close();

                if (photoData != null && photoData.length > 0) {
                    // Convert the photo data to a Bitmap and return it
                    return BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
                }
            }
            cursor.close();
        }

        return null; // If no photo is found or there was an error, return null
    }
}
