package com.user;

public class CPSbookEntity {

	/*
	 * create table scrapbook( title varchar2(100) not null, write_time
	 * varchar2(100) not null, ID varchar2(20) not null, constraint ID foreign
	 * key(ID) references Login_Create(ID), write_form varchar(1) check (write_form
	 * in ('0','1','2')), contents CLOB, input_URL1 varchar2(30) not null,
	 * input_URL2 varchar2(30), input_URL3 varchar2(30) )
	 */

	private int scrapbook_num;
	private String title;
	private String write_time;
	private String id;
	private String write_form;
	private String contents;
	private String input_URL1;
	private String input_URL2;
	private String input_URL3;

	public int getScrapbook_num() {
		return scrapbook_num;
	}

	public void setScrapbook_num(int scrapbook_num) {
		this.scrapbook_num = scrapbook_num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWrite_time() {
		return write_time;
	}

	public void setWrite_time(String write_time) {
		this.write_time = write_time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWrite_form() {
		return write_form;
	}

	public void setWrite_form(String write_form) {
		this.write_form = write_form;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getInput_URL1() {
		return input_URL1;
	}

	public void setInput_URL1(String input_URL1) {
		this.input_URL1 = input_URL1;
	}

	public String getInput_URL2() {
		return input_URL2;
	}

	public void setInput_URL2(String input_URL2) {
		this.input_URL2 = input_URL2;
	}

	public String getInput_URL3() {
		return input_URL3;
	}

	public void setInput_URL3(String input_URL3) {
		this.input_URL3 = input_URL3;
	}

	public CPSbookEntity(int scrapbook_num, String title, String write_time, String id, String write_form,
			String contents, String input_URL1, String input_URL2, String input_URL3) {
		super();
		this.scrapbook_num = scrapbook_num;
		this.title = title;
		this.write_time = write_time;
		this.id = id;
		this.write_form = write_form;
		this.contents = contents;
		this.input_URL1 = input_URL1;
		this.input_URL2 = input_URL2;
		this.input_URL3 = input_URL3;
	}

	public CPSbookEntity() {
		super();
	}

	@Override
	public String toString() {
		return "CPBulletinEntity [scrapbook_num=" + scrapbook_num + ", title=" + title + ", write_time=" + write_time
				+ ", id=" + id + ", write_form=" + write_form + ", contents=" + contents + ", input_URL1=" + input_URL1
				+ ", input_URL2=" + input_URL2 + ", input_URL3=" + input_URL3 + "]";
	}

}