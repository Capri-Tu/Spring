package com.lihuijuan.spring.beans;

public class HelloWorld {
    private String studentName;
    public HelloWorld(){
       System.out.println("Constructor...");
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
        System.out.println("setStudentName...");
    }

    public String getStudentName() {
        return studentName;
    }

    public void sayHello(){
        System.out.println("Hello"+" "+studentName);
    }
}
