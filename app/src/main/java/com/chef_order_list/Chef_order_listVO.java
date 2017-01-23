package com.chef_order_list;
import java.sql.Timestamp;

public class Chef_order_listVO implements java.io.Serializable{
	private String chef_ord_no;
	private String mem_no;
	private String chef_no;
	private Double chef_ord_cost;
	private Timestamp chef_act_date;
	private String chef_ord_place;
	private String chef_ord_cnt;
	private String chef_ord_con;
	private String chef_appr;
	private String chef_appr_cnt;
	private Timestamp chef_ord_date;
	
	public String getChef_ord_no()
	{
		return chef_ord_no;
	}
	public void setChef_ord_no(String chef_ord_no)
	{
		this.chef_ord_no = chef_ord_no;
	}
	public String getMem_no()
	{
		return mem_no;
	}
	public void setMem_no(String mem_no)
	{
		this.mem_no = mem_no;
	}
	public String getChef_no()
	{
		return chef_no;
	}
	public void setChef_no(String chef_no)
	{
		this.chef_no = chef_no;
	}
	public Double getChef_ord_cost()
	{
		return chef_ord_cost;
	}
	public void setChef_ord_cost(Double chef_ord_cost)
	{
		this.chef_ord_cost = chef_ord_cost;
	}
	public Timestamp getChef_act_date()
	{
		return chef_act_date;
	}
	public void setChef_act_date(Timestamp chef_act_date)
	{
		this.chef_act_date = chef_act_date;
	}
	public String getChef_ord_place()
	{
		return chef_ord_place;
	}
	public void setChef_ord_place(String chef_ord_place)
	{
		this.chef_ord_place = chef_ord_place;
	}
	public String getChef_ord_cnt()
	{
		return chef_ord_cnt;
	}
	public void setChef_ord_cnt(String chef_ord_cnt)
	{
		this.chef_ord_cnt = chef_ord_cnt;
	}
	public String getChef_ord_con()
	{
		return chef_ord_con;
	}
	public void setChef_ord_con(String chef_ord_con)
	{
		this.chef_ord_con = chef_ord_con;
	}
	public String getChef_appr()
	{
		return chef_appr;
	}
	public void setChef_appr(String chef_appr)
	{
		this.chef_appr = chef_appr;
	}
	public String getChef_appr_cnt()
	{
		return chef_appr_cnt;
	}
	public void setChef_appr_cnt(String chef_appr_cnt)
	{
		this.chef_appr_cnt = chef_appr_cnt;
	}
	public Timestamp getChef_ord_date()
	{
		return chef_ord_date;
	}
	public void setChef_ord_date(Timestamp chef_ord_date)
	{
		this.chef_ord_date = chef_ord_date;
	}
	
	
	
}
