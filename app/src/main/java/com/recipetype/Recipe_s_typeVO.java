package com.recipetype;

public class Recipe_s_typeVO implements java.io.Serializable{
	private String recipe_s_type_no;
	private String s_type_name;
	private String parent_type;
	
	public String getRecipe_s_type_no() {
		return recipe_s_type_no;
	}
	public void setRecipe_s_type_no(String recipe_s_type_no) {
		this.recipe_s_type_no = recipe_s_type_no;
	}
	public String getS_type_name() {
		return s_type_name;
	}
	public void setS_type_name(String s_type_name) {
		this.s_type_name = s_type_name;
	}
	public String getParent_type() {
		return parent_type;
	}
	public void setParent_type(String parent_type) {
		this.parent_type = parent_type;
	}	
}
