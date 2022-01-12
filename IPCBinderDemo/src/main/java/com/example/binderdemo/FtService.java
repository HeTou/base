package com.example.binderdemo;


public class FtService implements IFtService {

    private static FtService sInstance = null;

    private FtService() {
    }

    public static synchronized FtService getInstance() {
        if (sInstance == null) {
            sInstance = new FtService();
        }
        return sInstance;
    }

    String name="亦设有";

    public void setUser(String name) {
        this.name = name;
    }

    @Override
    public String getUser() {
        return name;
    }

    @Override
    public String getUser(String name) {
        return name;
    }
}
