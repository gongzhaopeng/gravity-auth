package cn.benbenedu.gravity.auth.model;

import cn.benbenedu.sundial.account.model.AccountRole;
import cn.benbenedu.sundial.account.model.AccountState;
import cn.benbenedu.sundial.account.model.AccountType;

import java.util.List;

public interface SundialUserAuthParams {

    String getId();

    AccountState getState();

    String getPassword();

    AccountType getType();

    String getName();

    String getNickname();

    List<AccountRole> getRoles();
}
