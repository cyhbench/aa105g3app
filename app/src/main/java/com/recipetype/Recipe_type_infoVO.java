package com.recipetype;

public class Recipe_type_infoVO
{
	private String recipe_no;
	private String recipe_type_no;
	private String type_range;
	private String target_recipe_type_no;
	
	
	public String getTarget_recipe_type_no()
	{
		return target_recipe_type_no;
	}
	public void setTarget_recipe_type_no(String target_recipe_type_no)
	{
		this.target_recipe_type_no = target_recipe_type_no;
	}
	public String getRecipe_no()
	{
		return recipe_no;
	}
	public void setRecipe_no(String recipe_no)
	{
		this.recipe_no = recipe_no;
	}
	public String getRecipe_type_no()
	{
		return recipe_type_no;
	}
	public void setRecipe_type_no(String recipe_type_no)
	{
		this.recipe_type_no = recipe_type_no;
	}
	public String getType_range()
	{
		return type_range;
	}
	public void setType_range(String type_range)
	{
		this.type_range = type_range;
	}
	
}
