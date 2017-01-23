package com.talk;

public class Message
{
	private String from;
    private String to;
    private String content;
	private String memberName;
    	
    @Override
    public String toString() {
        return super.toString();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getMemberName()
   	{
   		return memberName;
   	}

   	public void setMemberName(String memberName)
   	{
   		this.memberName = memberName;
   	}
}
