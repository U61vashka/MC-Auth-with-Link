package me.mastercapexd.auth.link.confirmation.info;

import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;

public class DefaultConfirmationInfo extends AbstractLinkConfirmationInfo {

    private final String confirmationCode;

    public DefaultConfirmationInfo(LinkUserIdentificator userIdentificator, String confirmationCode) {
        super(userIdentificator);
        this.confirmationCode = confirmationCode;
    }

    @Override
    public String getConfirmationCode() {
        return confirmationCode;
    }

}
