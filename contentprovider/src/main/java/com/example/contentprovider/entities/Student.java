package com.example.contentprovider.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Student {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private int age;
    private int sex;
    private boolean isJob;
    @Generated(hash = 1268341294)
    public Student(Long id, String name, int age, int sex, boolean isJob) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.isJob = isJob;
    }
    @Generated(hash = 1556870573)
    public Student() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public int getSex() {
        return this.sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public boolean getIsJob() {
        return this.isJob;
    }
    public void setIsJob(boolean isJob) {
        this.isJob = isJob;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", isJob=" + isJob +
                '}';
    }
}
