package com.project.sysumobilelibrary.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

public class SearchBook extends Book implements Cloneable {

	protected String book_num;
	protected int no;

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	public void getFromCursor(Cursor cursor) {
		super.author = cursor.getString(cursor.getColumnIndex("author"));
		super.doc_number = cursor.getString(cursor.getColumnIndex("doc_number"));
		super.name = cursor.getString(cursor.getColumnIndex("name"));
		super.publisher = cursor.getString(cursor.getColumnIndex("publisher"));
		super.year = cursor.getString(cursor.getColumnIndex("year"));
		super.img_url = cursor.getString(cursor.getColumnIndex("img_url"));
		book_num = cursor.getString(cursor.getColumnIndex("book_num"));
	}
	
	public void getFromJSONObject(JSONObject jsonObject) throws JSONException{
		super.author = jsonObject.getString("author");
		super.doc_number = jsonObject.getString("doc_number");
		super.name = jsonObject.getString("name");
		no = jsonObject.getInt("index");
		super.publisher = jsonObject.getString("publisher");
		super.year = jsonObject.getString("year");
		super.img_url = jsonObject.getString("img_url");
		try {
			book_num = jsonObject.getString("book_num");
		} catch (Exception e) {
			book_num = "";
		}
		
	}


	public String getBook_num() {
		return book_num;
	}


	public void setBook_num(String book_num) {
		this.book_num = book_num;
	}


	public int getNo() {
		return no;
	}


	public void setNo(int no) {
		this.no = no;
	}

	


	

}
