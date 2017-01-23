package com.recipetype;

public class Recipe_m_typeVO implements java.io.Serializable{
	private String recipe_m_type_no;
	private String m_type_name;
	private String parent_type;
	
	public String getRecipe_m_type_no() {
		return recipe_m_type_no;
	}
	public void setRecipe_m_type_no(String recipe_m_type_no) {
		this.recipe_m_type_no = recipe_m_type_no;
	}
	public String getM_type_name() {
		return m_type_name;
	}
	public void setM_type_name(String m_type_name) {
		this.m_type_name = m_type_name;
	}
	public String getParent_type() {
		return parent_type;
	}
	public void setParent_type(String parent_type) {
		this.parent_type = parent_type;
	}
	

}
