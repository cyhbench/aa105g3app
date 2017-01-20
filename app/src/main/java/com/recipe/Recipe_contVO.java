package com.recipe;

public class Recipe_contVO
{
	private String recipe_no;
	private Integer step;
	private byte[] step_pic;
	private String step_cont;
	
	
	public String getRecipe_no()
	{
		return recipe_no;
	}
	public void setRecipe_no(String recipe_no)
	{
		this.recipe_no = recipe_no;
	}
	public Integer getStep()
	{
		return step;
	}
	public void setStep(Integer step)
	{
		this.step = step;
	}
	public byte[] getStep_pic()
	{
		return step_pic;
	}
	public void setStep_pic(byte[] step_pic)
	{
		this.step_pic = step_pic;
	}
	public String getStep_cont()
	{
		return step_cont;
	}
	public void setStep_cont(String step_cont)
	{
		this.step_cont = step_cont;
	}
	
	
	public boolean equals(Object obj) {
    	if (this == obj) return true;                     
    	if(obj != null && getClass() == obj.getClass()) { 
    		if(obj instanceof Recipe_contVO) {
    			Recipe_contVO e = (Recipe_contVO)obj;
                if (step.equals(step) && recipe_no.equals(e.recipe_no)) {  
                    return true;
                }
        }
    	}	    	

    	return false;
    }
	
	public int hashCode(){
//      return this.ename.hashCode();               
 	  return new Integer(this.step).hashCode() + this.recipe_no.hashCode();  
  	
  }
	
}
