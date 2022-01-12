package com.example.binderdemo;

import com.example.ipcbinder.servicemanager.annotion.ClassId;

@ClassId("com.example.binderdemo.FtService")
public interface IFtService {

    String getUser();

    String getUser(String name);
}
