package com.example.braguia.model.app;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.braguia.model.app.converters.ContactListConverter;
import com.example.braguia.model.app.converters.PartnerListConverter;
import com.example.braguia.model.app.converters.SocialListConverter;
import com.google.gson.annotations.SerializedName;
import java.util.List;

@Entity(tableName = "app_info", indices = @Index(value = {"app_name"}, unique = true))
@TypeConverters({SocialListConverter.class, ContactListConverter.class, PartnerListConverter.class})
public class AppInfo {
    @PrimaryKey()
    @NonNull
    @SerializedName("app_name")
    @ColumnInfo(name = "app_name")
    private String appName;
    @SerializedName("socials")
    private List<Social> socials;
    @SerializedName("contacts")
    private List<Contact> contacts;
    @SerializedName("partners")
    private List<Partner> partners;

    @SerializedName("app_desc")
    @ColumnInfo(name = "app_desc")
    private String appDesc;
    @SerializedName("app_landing_page_text")
    @ColumnInfo(name = "app_landing_page_text")
    private String appLandingPageText;


    public AppInfo(String appName, List<Social> socials, List<Contact> contacts, List<Partner> partners, String appDesc, String appLandingPageText) {
        this.appName = appName;
        this.socials = socials;
        this.contacts = contacts;
        this.partners = partners;
        this.appDesc = appDesc;
        this.appLandingPageText = appLandingPageText;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<Social> getSocials() {
        return socials;
    }

    public void setSocials(List<Social> socials) {
        this.socials = socials;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Partner> getPartners() {
        return partners;
    }

    public void setPartners(List<Partner> partners) {
        this.partners = partners;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public String getAppLandingPageText() {
        return appLandingPageText;
    }

    public void setAppLandingPageText(String appLandingPageText) {
        this.appLandingPageText = appLandingPageText;
    }
    // Constructor, getters and setters
}
