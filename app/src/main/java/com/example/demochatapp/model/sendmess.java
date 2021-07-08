package com.example.demochatapp.model;

import java.util.Date;

public class sendmess {
    public String nguoigui;
    public String nguoinhan;
    public String tinnhan;
    public long  thoigiangui;


    public sendmess() {
    }

    public sendmess(String nguoigui,String nguoinhan, String tinnhan,long  thoigiangui) {
        this.nguoigui = nguoigui;
        this.nguoinhan = nguoinhan;
        this.tinnhan = tinnhan;
        this.thoigiangui=thoigiangui;

    }

}
