package cn.benbenedu.sundial.account.model;

import lombok.Data;

@Data
public class AccountAddress {

    private String country;
    private String province;
    private String city;
    private String county;
}
