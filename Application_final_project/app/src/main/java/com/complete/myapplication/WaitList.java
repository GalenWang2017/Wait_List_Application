package com.complete.myapplication;

public class WaitList {
    String title,context,createtime,timeout;
    public WaitList(String Title,String Context,String Createtime,String Timeout){
        title=Title;
        context=Context;
        createtime=Createtime;
        timeout=Timeout;
    }
    public void setTitle(String Title){
        title=Title;
    }
    public void setContext(String Context){
        context=Context;
    }
    public void setCreatetime(String Create){
        createtime=Create;
    }
    public void setTimeout(String Timeout){
        timeout=Timeout;
    }
    public String getTitle(){
        return title;
    }
    public String getContext(){
        return context;
    }
    public String getCreatetime(){
        return createtime;
    }
    public String getTimeout(){
        return timeout;
    }
}
