package com.recipe;

import java.io.Serializable;
import java.sql.Timestamp;

public class RecipeVO implements Serializable
{
	private String recipe_no;
	private String mem_no;
	private String recipe_name;
	private String recipe_intro;
	private String food_mater;
	private byte[] recipe_pic;
	private Integer recipe_like;
	private Integer recipe_total_views;
	private Integer recipe_week_views;
	private Timestamp recipe_time;
	private String recipe_edit;
	private String recipe_classify;
	
	
	public String getRecipe_no()
	{
		return recipe_no;
	}
	public void setRecipe_no(String recipe_no)
	{
		this.recipe_no = recipe_no;
	}
	public String getMem_no()
	{
		return mem_no;
	}
	public void setMem_no(String mem_no)
	{
		this.mem_no = mem_no;
	}
	public String getRecipe_name()
	{
		return recipe_name;
	}
	public void setRecipe_name(String recipe_name)
	{
		this.recipe_name = recipe_name;
	}
	public String getRecipe_intro()
	{
		return recipe_intro;
	}
	public void setRecipe_intro(String recipe_intro)
	{
		this.recipe_intro = recipe_intro;
	}
	public String getFood_mater()
	{
		return food_mater;
	}
	public void setFood_mater(String food_mater)
	{
		this.food_mater = food_mater;
	}
	public byte[] getRecipe_pic()
	{
		return recipe_pic;
	}
	public void setRecipe_pic(byte[] recipe_pic)
	{
		this.recipe_pic = recipe_pic;
	}
	public Integer getRecipe_like()
	{
		return recipe_like;
	}
	public void setRecipe_like(Integer recipe_like)
	{
		this.recipe_like = recipe_like;
	}
	public Integer getRecipe_total_views()
	{
		return recipe_total_views;
	}
	public void setRecipe_total_views(Integer recipe_total_views)
	{
		this.recipe_total_views = recipe_total_views;
	}
	public Integer getRecipe_week_views()
	{
		return recipe_week_views;
	}
	public void setRecipe_week_views(Integer recipe_week_views)
	{
		this.recipe_week_views = recipe_week_views;
	}
	public Timestamp getRecipe_time()
	{
		return recipe_time;
	}
	public void setRecipe_time(Timestamp recipe_time)
	{
		this.recipe_time = recipe_time;
	}
	public String getRecipe_edit()
	{
		return recipe_edit;
	}
	public void setRecipe_edit(String recipe_edit)
	{
		this.recipe_edit = recipe_edit;
	}
	public String getRecipe_classify()
	{
		return recipe_classify;
	}
	public void setRecipe_classify(String recipe_classify)
	{
		this.recipe_classify = recipe_classify;
	}
	
	
		
	
}
