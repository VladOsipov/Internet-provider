package by.epam.osipov.internet.provider.command.impl;

import by.epam.osipov.internet.provider.command.Command;
import by.epam.osipov.internet.provider.content.RequestContent;
import by.epam.osipov.internet.provider.dao.impl.BanDAO;
import by.epam.osipov.internet.provider.exception.CommandException;
import by.epam.osipov.internet.provider.exception.ConnectionPoolException;
import by.epam.osipov.internet.provider.exception.DAOException;
import by.epam.osipov.internet.provider.pool.ConnectionPool;
import by.epam.osipov.internet.provider.pool.ConnectionProxy;

/**
 * Created by Lenovo on 18.01.2017.
 */
public class UnbanUserCommand implements Command {
    @Override
    public String execute(RequestContent content) throws CommandException {
        try {
            return tryExecute(content);
        } catch (ConnectionPoolException | DAOException | CommandException e) {
            throw new CommandException("Error while trying execute Unban User command", e);
        }
    }

    private String tryExecute(RequestContent content) throws ConnectionPoolException, DAOException, CommandException {

        String passport = content.getParameter("passport");

        try (ConnectionProxy connection = ConnectionPool.getInstance().getConnection();) {
            BanDAO banDAO = new BanDAO(connection);

            banDAO.deleteByKey(passport);
        }
        ShowUsersCommand showUsersCommand = new ShowUsersCommand();

        return showUsersCommand.execute(content);
    }
}
