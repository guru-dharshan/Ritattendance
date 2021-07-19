package com.devgd.attendancev1;

import com.google.firebase.firestore.Exclude;

public class AttendanceModelClass {
    String name;
    String dep;
    String sec;
//    String regNo;
    String h1,h2,h3,h4,h5,h6,h7;
    String sem,year;
    long phno,rolno,regno;
    String  noofpresent;

    public long getRegno() {
        return regno;
    }

    public void setRegno(long regno) {
        this.regno = regno;
    }

    public AttendanceModelClass(String name, String dep, String sec, long phno, long regno) {
        this.name = name;
        this.dep = dep;
        this.sec = sec;
        this.phno = phno;
        this.regno = regno;
    }

    public long getPhno() {
        return phno;
    }

    public void setPhno(long phno) {
        this.phno = phno;
    }

//    public long getRolno() {
//        return rolno;
//    }
//
//    public void setRolno(long rolno) {
//        this.rolno = rolno;
//    }

    public AttendanceModelClass() {
    }



    public AttendanceModelClass(String name, String dep, String sec) {
        this.name = name;
        this.dep = dep;
        this.sec = sec;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getNoofpresent() {
        return noofpresent;
    }

    public void setNoofpresent(String noofpresent) {
        this.noofpresent = noofpresent;
    }

    public AttendanceModelClass(long regno, long phno, String name, String sec, String sem, String dep, String year, String h1, String h2,
                                String h3, String h4, String h5, String h6, String h7, String noofpresent) {
        this.h1 = h1;
        this.h2 = h2;
        this.h3 = h3;
        this.h4 = h4;
        this.h5 = h5;
        this.h6 = h6;
        this.h7 = h7;
        this.name=name;
        this.sec=sec;
        this.sem=sem;
        this.dep=dep;
        this.year=year;
        this.regno=regno;
        this.phno=phno;
        this.noofpresent=noofpresent;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public String getH2() {
        return h2;
    }

    public void setH2(String h2) {
        this.h2 = h2;
    }

    public String getH3() {
        return h3;
    }

    public void setH3(String h3) {
        this.h3 = h3;
    }

    public String getH4() {
        return h4;
    }

    public void setH4(String h4) {
        this.h4 = h4;
    }

    public String getH5() {
        return h5;
    }

    public void setH5(String h5) {
        this.h5 = h5;
    }

    public String getH6() {
        return h6;
    }

    public void setH6(String h6) {
        this.h6 = h6;
    }

    public String getH7() {
        return h7;
    }

    public void setH7(String h7) {
        this.h7 = h7;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

}
