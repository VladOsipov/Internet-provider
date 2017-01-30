package by.epam.osipov.internet.provider.command.impl;

import by.epam.osipov.internet.provider.command.Command;
import by.epam.osipov.internet.provider.content.RequestContent;
import by.epam.osipov.internet.provider.exception.CommandException;
import by.epam.osipov.internet.provider.exception.ServiceException;
import by.epam.osipov.internet.provider.service.AccessService;

/**
 * Created by Lenovo on 26.01.2017.
 */
public class ChangePasswordCommand implements Command {

    private static final String CURRENT_PASSWORD_PARAM = "currentPassword";
    private static final String NEW_PASSWORD_PARAM = "newPassword";
    private static final String CONFIRM_PARAM = "confirm";
    private static final String PASSWORD_CHANGED_ATTR = "passwordChanged";
    private static final String USER_ATTR = "user";

    @Override
    public String execute(RequestContent content) throws CommandException {

        try {
            return tryExecute(content);
        } catch (ServiceException e) {
            throw new CommandException("Error while trying to execute Change password command", e);
        }
    }

    private String tryExecute(RequestContent content) throws ServiceException {

        String curPass = content.getParameter(CURRENT_PASSWORD_PARAM);
        String newPass = content.getParameter(NEW_PASSWORD_PARAM);
        String newPassConfirm = content.getParameter(CONFIRM_PARAM);
/*
        if(!newPass.equals(newPassConfirm)){
            content.setSessionAttribute(PASSWORD_CHANGED_ATTR, false);
        }
        */
        String login = content.getSessionAttribute(USER_ATTR).toString();

        AccessService accessService = new AccessService();
        if (accessService.isOldPassCorrect(login, curPass)) {
            accessService.changePassword(login, curPass, newPass);
            content.setAttribute(PASSWORD_CHANGED_ATTR, true);
        } else {
            content.setAttribute(PASSWORD_CHANGED_ATTR, false);
            return "/";
        }

        return "/";
    }
}
