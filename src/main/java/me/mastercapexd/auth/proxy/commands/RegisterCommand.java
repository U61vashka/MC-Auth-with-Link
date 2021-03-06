package me.mastercapexd.auth.proxy.commands;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.steps.RegisterAuthenticationStep;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.annotations.AuthenticationAccount;
import me.mastercapexd.auth.proxy.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.proxy.commands.parameters.RegisterPassword;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;

@Command({"reg", "register"})
public class RegisterCommand {

    @Dependency
    private ProxyPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountStorage accountStorage;

    @Default
    @AuthenticationStepCommand(stepName = RegisterAuthenticationStep.STEP_NAME)
    public void register(ProxyPlayer player, @AuthenticationAccount Account account, RegisterPassword password) {
        AuthenticationStep currentAuthenticationStep = account.getCurrentAuthenticationStep();
        currentAuthenticationStep.getAuthenticationStepContext().setCanPassToNextStep(true);

        if (account.getHashType() != config.getActiveHashType())
            account.setHashType(config.getActiveHashType());
        account.setPasswordHash(account.getHashType().hash(password.getPassword()));

        accountStorage.saveOrUpdateAccount(account);

        account.nextAuthenticationStep(plugin.getAuthenticationContextFactoryDealership().createContext(account));

        player.sendMessage(config.getProxyMessages().getStringMessage("register-success"));
    }
}