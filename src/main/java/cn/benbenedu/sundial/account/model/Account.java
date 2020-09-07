package cn.benbenedu.sundial.account.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.CredentialsContainer;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document("account")
public class Account implements CredentialsContainer {

    @Id
    private String id;
    private Long createTime;
    @JsonProperty("lUTime")
    private Long lUTime;
    private AccountBrief creator;

    private AccountState state;

    private AccountAffiliation affiliation;

    private String idNumber;
    private Boolean identified;
    private String mobile;
    private Boolean mobileVerified;
    private String email;
    private Boolean emailVerified;
    private String password;

    private String mobileToken;

    private WechatInfo wechat;

    private ByteDanceInfo byteDance;

    private AccountType type;

    private String name;
    private String nickname;
    private AccountGender gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private AccountAddress address;

    private String avatar;

    private List<AccountRole> roles;

    private StudyingStatus studyingStatus;

    private Map<String, String> extInfo;

    @Override
    public void eraseCredentials() {
        password = null;
    }
}
