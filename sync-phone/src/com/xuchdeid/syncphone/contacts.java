package com.xuchdeid.syncphone;

import java.util.ArrayList;

import com.google.gson.Gson;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
//import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Data;

public class contacts {
	
	private transform t;	
	private Context context;
	
	public contacts(Context context,transform t){
		this.context = context;
		this.t = t;
	}
	
	public void getCursor(){
		Cursor c;
		Cursor other = null;
		//Cursor phoneNumber;
		//Cursor email;
		//Cursor Address;
		//Cursor Organiza;
		String mContact;
		contact cc = new contact();
		Gson gson = new Gson();
		int sum;
		
		String[] contacts = new String[]{
				Contacts._ID, // 0
	            Contacts.DISPLAY_NAME, // 1
	            Contacts.STARRED, // 2
	            Contacts.PHOTO_ID, // 3
	            Contacts.LOOKUP_KEY, // 4
	            Contacts.HAS_PHONE_NUMBER,// 5
		};
		
		String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
        + Contacts.DISPLAY_NAME + " != '' ))";
		c = context.getContentResolver().query(Contacts.CONTENT_URI, contacts, select,
                null, Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		
		t.put(c.getCount());//put sum of items
		
		while(c.moveToNext()){

			cc.id = c.getLong(0);//Contacts._ID
			cc.name = c.getString(1);//Contacts.DISPLAY_NAME
			cc.starred = c.getInt(2);//Contacts.STARRED
			//cc.contacted = c.getInt(3);//Contacts.TIMES_CONTACTED
			//cc.presence = c.getInt(4);//Contacts.CONTACT_PRESENCE
			cc.photoid = c.getLong(3);//Contacts.PHOTO_ID
			cc.key = c.getString(4);//Contacts.LOOKUP_KEY
			cc.hasnumber = c.getInt(5);//Contacts.HAS_PHONE_NUMBER
			//phone numbers type:number
			String id = String.valueOf(cc.id);
			if(cc.hasnumber == 1){
				other = context.getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
			
				ArrayList<Date> temp = new ArrayList<Date>();
				
				while(other.moveToNext()){
					Date Temp = new Date();
					Temp.type = other.getInt(other.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					Temp.value = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					Temp.lable = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
					temp.add(Temp);
				}
				sum = temp.size();
				cc.numbers = new Date[sum];
				for(int i = 0;i < sum;i++){
					cc.numbers[i] = temp.get(i);
				}
				//phoneNumber.close();
			}else cc.numbers = null;
			
			//Mails type:address
			other = context.getContentResolver().query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
					ContactsContract.CommonDataKinds.Email.CONTACT_ID +" = "+ id,null, null);
			ArrayList<Date> temp = new ArrayList<Date>();
			
			while(other.moveToNext()){
				Date Temp = new Date();
				Temp.type = other.getInt(other.getColumnIndex(
						ContactsContract.CommonDataKinds.Email.TYPE));
				Temp.value = other.getString(other.getColumnIndex(
						ContactsContract.CommonDataKinds.Email.DATA));
				Temp.lable = other.getString(other.getColumnIndex(
						ContactsContract.CommonDataKinds.Email.LABEL));
				temp.add(Temp);
			}
			//email.close();
			
			sum = temp.size();
			if(sum > 0){
				cc.emails = new Date[sum];
				for(int i = 0;i < sum;i++){
					cc.emails[i] = temp.get(i);
				}
			}
			else cc.emails = null;
			
			//address typr:address
			//other = context.getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
			//		null, ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + id, null, null);
			other = context.getContentResolver().query(Data.CONTENT_URI, null, Data.CONTACT_ID + "=?" + " AND "
	                  + Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE + "'",
	                  new String[] {id}, null);
			ArrayList<Postal> temp_postal = new ArrayList<Postal>();
			while(other.moveToNext()){
				Postal postal = new Postal();
				postal.type = other.getInt(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
				postal.lable = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.LABEL));
				postal.street = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
				postal.pobox = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
				postal.neighborhood = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD));
				postal.city = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
				postal.region = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
				postal.postcode = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
				postal.country = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
				temp_postal.add(postal);
			}
			//Address.close();
			sum = temp_postal.size();
			if(sum > 0){
				cc.address = new Postal[sum];
				for(int i = 0;i < sum;i++){
					cc.address[i] = temp_postal.get(i);
				}
			}else cc.address = null;
			
			//organization 
			other = context.getContentResolver().query(Data.CONTENT_URI, null, Data.CONTACT_ID + "=?" + " AND "
	                  + Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE + "'",
	                  new String[] {id}, null);
			ArrayList<Organization> temp_Organization = new ArrayList<Organization>();
			while(other.moveToNext()){
				Organization organiza = new Organization();
				organiza.company = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
				organiza.department = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DEPARTMENT));
				organiza.job_description = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Organization.JOB_DESCRIPTION));
				organiza.lable = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Organization.LABEL));
				organiza.office_location = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Organization.OFFICE_LOCATION));
				organiza.phonetlc_name = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Organization.PHONETIC_NAME));
				organiza.symbol = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Organization.SYMBOL));
				organiza.title = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
				organiza.type = other.getInt(other.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TYPE));
				temp_Organization.add(organiza);
			}
			sum = temp_Organization.size();
			if(sum > 0){
				cc.organ = new Organization[sum];
				for(int i = 0;i < sum;i++){
					cc.organ[i] = temp_Organization.get(i);
				}
			}else cc.organ = null;
			
			//struct name
			other = context.getContentResolver().query(Data.CONTENT_URI, null, Data.CONTACT_ID + "=?" + " AND "
	                  + Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE + "'",
	                  new String[] {id}, null);
			ArrayList<Name> temp_name = new ArrayList<Name>();
			while(other.moveToNext()){
				Name name = new Name();
				name.display_name = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
				name.family_name = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
				name.given_name = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
				name.middle_name = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
				name.phonetic_family_name = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME));
				name.phonetic_given_name = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_GIVEN_NAME));
				name.phonetic_middle_name = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_MIDDLE_NAME));
				name.prefix = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX));
				name.suffix = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.SUFFIX));
				temp_name.add(name);
			}
			sum = temp_name.size();
			if(sum > 0){
				cc.structname = new Name[sum];
				for(int i = 0;i < sum;i++){
					cc.structname[i] = temp_name.get(i);
				}
			}else cc.structname = null;
			
			//im
			other = context.getContentResolver().query(Data.CONTENT_URI, null, Data.CONTACT_ID + "=?" + " AND "
	                  + Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE + "'",
	                  new String[] {id}, null);
			ArrayList<IM> temp_im = new ArrayList<IM>();
			while(other.moveToNext()){
				IM im = new IM();
				im.custom_protocol = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Im.CUSTOM_PROTOCOL));
				im.data = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
				im.lable = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Im.LABEL));
				im.protocol = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));
				im.type = other.getInt(other.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
				temp_im.add(im);
			}
			sum = temp_im.size();
			if(sum > 0){
				cc.im = new IM[sum];
				for(int i = 0;i < sum;i++){
					cc.im[i] = temp_im.get(i);
				}
			}else cc.im = null;
			
			//nick name
			other = context.getContentResolver().query(Data.CONTENT_URI, null, Data.CONTACT_ID + "=?" + " AND "
	                  + Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE + "'",
	                  new String[] {id}, null);
			if(other.moveToNext()){
				Date d = new Date();
				d.lable = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.LABEL));
				d.type = other.getInt(other.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.TYPE));
				d.value = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME));
				cc.nickname = d;
			}else cc.nickname = null;
			
			//note
			other = context.getContentResolver().query(Data.CONTENT_URI, null, Data.CONTACT_ID + "=?" + " AND "
	                  + Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE + "'",
	                  new String[] {String.valueOf(id)}, null);
			if(other.moveToNext()){
				cc.note = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
			}else cc.note = null;
			
			//website
			other = context.getContentResolver().query(Data.CONTENT_URI, null, Data.CONTACT_ID + "=?" + " AND "
	                  + Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE + "'",
	                  new String[] {String.valueOf(id)}, null);
			temp.clear();
			while(other.moveToNext()){
				Date mdate = new Date();
				mdate.lable = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Website.LABEL));
				mdate.type = other.getInt(other.getColumnIndex(ContactsContract.CommonDataKinds.Website.TYPE));
				mdate.value = other.getString(other.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));
				temp.add(mdate);
			}
			sum = temp.size();
			if(sum > 0){
				cc.website = new Date[sum];
				for(int i = 0;i < sum;i++){
					cc.website[i] = temp.get(i);
				}
			}else cc.website = null;
			
			//make a json item
			mContact = gson.toJson(cc);
			//put the item to the click
			t.put(mContact);
		}
		
		other.close();
		c.close();
		
	}
	
	class contact{
		public long id;//Contacts._ID
		public String name;//Contacts.DISPLAY_NAME
		public int starred;//Contacts.STARRED
		//public int contacted;//Contacts.TIMES_CONTACTED
		//public int presence;//Contacts.CONTACT_PRESENCE
		public long photoid;//Contacts.PHOTO_ID
		public String key;//Contacts.LOOKUP_KEY
		public int hasnumber;//Contacts.HAS_PHONE_NUMBER
		public Date[] numbers;//phone numbers
		public Date[] emails;//email addresses
		public Postal[] address;//address
		public Organization[] organ;//Organization
		public Name[] structname;
		public IM[] im;
		public Date nickname;
		public String note;
		public Date[] website;
	}
	
	class Date{
		public int type;
		public String value;
		public String lable;
	}
	
	class Postal{
		public int type;
		public String lable;
		public String street;
		public String pobox;
		public String neighborhood;
		public String city;
		public String region;
		public String postcode;
		public String country;	
	}
	
	class Organization{
		public String company;
		public int type;
		public String lable;
		public String title;
		public String department;
		public String job_description;
		public String symbol;
		public String phonetlc_name;
		public String office_location;
		//public String phonetlc_name_style;
	}
	
	class Name{
		public String display_name;
		public String given_name;
		public String family_name;
		public String prefix;
		public String middle_name;
		public String suffix;
		public String phonetic_given_name;
		public String phonetic_middle_name;
		public String phonetic_family_name;		
	}
	
	class IM{
		public String data;
		public int type;
		public String lable;
		public String protocol;
		public String custom_protocol;
	}
	
	//callback class
	interface transform{
		public void put(String a);
		public void put(int a);
	}

}
