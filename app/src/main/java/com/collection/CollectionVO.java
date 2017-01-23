package com.collection;

public class CollectionVO implements java.io.Serializable {
	private String coll_no;
	private String mem_no;
	private String all_no;
	private String class_no;

	public CollectionVO(String coll_no,String mem_no, String all_no,  String class_no){
		this.coll_no = coll_no;
		this.mem_no = mem_no;
		this.all_no = all_no;
		this.class_no =class_no;
	}

	public String getColl_no() {
		return coll_no;
	}
	public void setColl_no(String coll_no) {
		this.coll_no = coll_no;
	}
	public String getMem_no() {
		return mem_no;
	}
	public void setMem_no(String mem_no) {
		this.mem_no = mem_no;
	}
	public String getAll_no() {
		return all_no;
	}
	public void setAll_no(String all_no) {
		this.all_no = all_no;
	}
	public String getClass_no() {
		return class_no;
	}
	public void setClass_no(String class_no) {
		this.class_no = class_no;
	}
}
