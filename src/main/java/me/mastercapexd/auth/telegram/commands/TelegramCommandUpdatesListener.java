package me.mastercapexd.auth.telegram.commands;

import java.util.List;

import com.google.gson.Gson;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.ubivashka.lamp.telegram.TelegramActor;
import com.ubivashka.lamp.telegram.core.TelegramHandler;
import com.ubivashka.lamp.telegram.dispatch.CallbackQueryDispatchSource;
import com.ubivashka.lamp.telegram.dispatch.DispatchSource;
import com.ubivashka.lamp.telegram.dispatch.MessageDispatchSource;
import com.ubivashka.messenger.telegram.message.keyboard.TelegramKeyboard;
import com.ubivaska.messenger.common.identificator.Identificator;
import com.ubivaska.messenger.common.message.Message;
import com.ubivaska.messenger.common.message.Message.MessageBuilder;

import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.telegram.TelegramCommandActorWrapper;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.messenger.commands.custom.CustomCommandExecuteContext;
import me.mastercapexd.auth.messenger.commands.custom.MessengerCustomCommand;
import revxrsal.commands.command.ArgumentStack;

public class TelegramCommandUpdatesListener implements UpdatesListener {
    private static final Gson GSON = new Gson();
    private static final LinkType LINK_TYPE = TelegramLinkType.getInstance();

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates)
            processUpdate(update);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(Update update) {
        if (update.message() != null)
            processMessageUpdate(update);
        if (update.callbackQuery() != null)
            processCallbackUpdate(update);
    }

    private void processMessageUpdate(Update update) {
        com.pengrad.telegrambot.model.Message message = update.message();
        TelegramHandler.getInstances().forEach(handler -> {
            handleCommandDispatch(handler, new MessageDispatchSource(message));
            LINK_TYPE.getSettings().getCustomCommands().execute(new CustomCommandExecuteContext(message.text())).forEach(customCommand -> {
                Message customCommandMessageResponse = createMessageResponse(customCommand);
                customCommandMessageResponse.send(Identificator.of(message.chat().id()));
            });
        });
    }

    private void processCallbackUpdate(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        TelegramHandler.getInstances().forEach(handler -> {
            handleCommandDispatch(handler, new CallbackQueryDispatchSource(callbackQuery));
            if (callbackQuery.message() == null)
                return;
            LINK_TYPE.getSettings().getCustomCommands().execute(new CustomCommandExecuteContext(callbackQuery.data()).setButtonExecution(true)).forEach(customCommand -> {
                Message customCommandMessageResponse = createMessageResponse(customCommand);
                customCommandMessageResponse.send(Identificator.of(callbackQuery.message().chat().id()));
            });
        });
    }

    private void handleCommandDispatch(TelegramHandler handler, DispatchSource dispatchSource) {
        TelegramActor actor = new TelegramCommandActorWrapper(TelegramActor.wrap(handler, dispatchSource));
        ArgumentStack argumentStack = handler.parseArguments(dispatchSource.getExecutionText());
        if (argumentStack.isEmpty())
            return;
        handler.dispatch(actor, argumentStack);
    }

    private Message createMessageResponse(MessengerCustomCommand customCommand) {
        MessageBuilder builder = LINK_TYPE.newMessageBuilder(customCommand.getAnswer());
        if (customCommand.getSectionHolder().contains("keyboard"))
            builder.keyboard(new TelegramKeyboard(GSON.fromJson(customCommand.getSectionHolder().getString("keyboard"), InlineKeyboardMarkup.class)));
        return builder.build();
    }
}
