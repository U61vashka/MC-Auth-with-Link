package me.mastercapexd.auth.authentication.step.steps.link;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.MessageableAuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.config.message.proxy.ProxyMessageContext;
import me.mastercapexd.auth.link.entryuser.LinkEntryUser;
import me.mastercapexd.auth.link.entryuser.google.GoogleLinkEntryUser;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public class GoogleCodeAuthenticationStep extends AbstractAuthenticationStep implements MessageableAuthenticationStep {
    public static final String STEP_NAME = "GOOGLE_LINK";
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
    private final LinkEntryUser entryUser;

    public GoogleCodeAuthenticationStep(AuthenticationStepContext context) {
        super(STEP_NAME, context);
        entryUser = new GoogleLinkEntryUser(context.getAccount());
    }

    @Override
    public boolean shouldPassToNextStep() {
        return entryUser.isConfirmed();
    }

    @Override
    public boolean shouldSkip() {
        Account account = authenticationStepContext.getAccount();

        if (!PLUGIN.getConfig().getGoogleAuthenticatorSettings().isEnabled()) // Ignore if google was disabled in
            // configuration
            return true;

        if (Auth.getLinkEntryAuth().hasLinkUser(account.getId(), GoogleLinkType.getInstance())) // Ignore if user
            // already confirming
            return true;

        if (account.isSessionActive(PLUGIN.getConfig().getSessionDurability())) // Ignore if player has active session
            return true;

        LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElse(null);

        if (linkUser == null)
            return true;

        LinkUserInfo linkUserInfo = linkUser.getLinkUserInfo();

        if (linkUserInfo == null || linkUserInfo.getIdentificator().asString() == AccountFactory.DEFAULT_GOOGLE_KEY ||
                !linkUserInfo.getConfirmationState().shouldSendConfirmation())
            return true;

        Auth.getLinkEntryAuth().addLinkUser(entryUser);
        return false;
    }

    @Override
    public void process(ProxyPlayer player) {
        Account account = authenticationStepContext.getAccount();
        PluginConfig config = ProxyPlugin.instance().getConfig();
        Messages<ProxyComponent> googleMessages = config.getProxyMessages().getSubMessages("google");
        player.sendMessage(googleMessages.getMessage("need-code-chat", new ProxyMessageContext(account)));
        ProxyPlugin.instance().getCore().createTitle(googleMessages.getStringMessage("need-code-title")).subtitle(googleMessages.getStringMessage("need-code-subtitle")).stay(120).send(player);
    }

    public static class GoogleLinkAuthenticationStepCreator extends AbstractAuthenticationStepCreator {
        public GoogleLinkAuthenticationStepCreator() {
            super(STEP_NAME);
        }

        @Override
        public AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
            return new GoogleCodeAuthenticationStep(context);
        }
    }
}
