package com.itzap.ebeans.test.dao;

import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.SqlUpdate;
import com.itzap.ebeans.AbstractSelectListEbeanHandler;
import com.itzap.ebeans.BaseBulkEbeanCommand;
import com.itzap.ebeans.DeleteEbeanHandler;
import com.itzap.ebeans.ListBaseEbeanCommand;
import com.itzap.ebeans.QueryParameter;
import com.itzap.ebeans.Servers;
import com.itzap.ebeans.test.model.User;
import io.reactivex.Observable;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class UserDao extends AbstractServiceDao {
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String WHERE_USER = " where username=:username";
    private static final String WHERE_USER_ID = " where id=:id";

    private static final String ADD_USER = "INSERT INTO public.users(" +
            "            username, password, enabled, email, object)" +
            "    VALUES (:username, :password, :enabled, :email, :object)";
    private static final String UPDATE_USER = "UPDATE public.users " +
            "   SET username=:username, password=:password, enabled=:enabled, email=:email, object=:object" +
            " WHERE id=:id";
    private static final String DELETE_USER = "DELETE FROM users WHERE id=:id";

    public UserDao(Servers.Server server) {
        super(server);
    }

    public Observable<User> getUsers() {
        return new SelectUsersCommand()
                .toObservable()
                .flatMap(Observable::fromIterable);
    }

    public Observable<User> getUserByName(String name) {
        return new SelectUsersCommand()
                .setName(name)
                .toObservable()
                .map(users -> users.get(0));
    }

    public Observable<User> getUser(Long userId) {
        return new SelectUsersCommand()
                .setUserId(userId)
                .toObservable()
                .map(users -> users.get(0));
    }

    public Observable<Integer> addUser(User user) {
        return new BaseBulkEbeanCommand<BaseBulkEbeanCommand>("cmd-addUser-cmd",
                new UserHandler(user)) {
            @Override
            protected BaseBulkEbeanCommand getThis() {
                return this;
            }

            @Override
            protected SqlUpdate createSql() {
                return server.getServer().createSqlUpdate(ADD_USER);
            }
        }.toObservable();
    }

    public Observable<Integer> updateUser(Long userId, User user) {
        return new SelectUsersCommand()
                .setName(user.getUsername())
                .toObservable()
                .flatMap(orgUser -> new BaseBulkEbeanCommand<BaseBulkEbeanCommand>("cmd-updateUser-cmd",
                        new UserHandler(userId, user, orgUser.get(0))) {
                    @Override
                    protected BaseBulkEbeanCommand getThis() {
                        return this;
                    }

                    @Override
                    protected SqlUpdate createSql() {
                        return server.getServer().createSqlUpdate(UPDATE_USER);
                    }
                }.toObservable());
    }

    public Observable<Integer> deleteUser(Long id) {
        return new BaseBulkEbeanCommand<BaseBulkEbeanCommand>("cmd-deleteUser-cmd",
                new DeleteEbeanHandler<>(QueryParameter.id(id))) {
            @Override
            protected BaseBulkEbeanCommand getThis() {
                return this;
            }

            @Override
            protected SqlUpdate createSql() {
                return server.getServer().createSqlUpdate(DELETE_USER);
            }
        }.toObservable();
    }

    class UserHandler extends AbstractAuditableHandler<User, User.Builder> {
        UserHandler(User user) {
            this(null, user, User.builder().build());
        }

        UserHandler(Long userId, User user, User dbUser) {
            super(userId, user, dbUser);
        }

        @Override
        protected User.Builder rowProperties(SqlUpdate updateQuery, User user) {
            updateQuery.setParameter("username",
                    StringUtils.defaultIfBlank(user.getUsername(), orgObject.getUsername()));

            String pass = getPassword(user);
            if (StringUtils.isBlank(pass)) {
                updateQuery.setParameter("password", orgObject.getPassword());
            } else {
                updateQuery.setParameter("password", pass);
            }
            updateQuery.setParameter("enabled", ObjectUtils.defaultIfNull(user.getEnabled(),
                    orgObject.getEnabled()));
            updateQuery.setParameter("email",
                    StringUtils.defaultIfBlank(user.getEmail(), orgObject.getEmail()));

            return audibalBuilder(user,  User.from(user));
        }
    }

    private static String getPassword(User user) {
        return user.getPassword();
    }

    private static  User.Builder fromRow(SqlRow row, User user) {
        return User.from(user)
                .setUsername(row.getString("username"))
                .setEmail(row.getString("email"))
                .setEnabled(row.getBoolean("enabled"))
                .setPassword(row.getString("password"));
    }

    class SelectUsersCommand extends ListBaseEbeanCommand<User, SelectUsersCommand> {
        private String name;
        private Long userId;

        public SelectUsersCommand setName(String name) {
            this.name = name;
            return getThis();
        }

        public SelectUsersCommand setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        SelectUsersCommand() {
            super("cmd-get-users",
                    new AbstractSelectListEbeanHandler<User, User.Builder>(User.class) {
                        @Override
                        protected User.Builder from(SqlRow row, User user) {
                            return fromRow(row, user);
                        }
                    });
        }

        @Override
        protected SelectUsersCommand getThis() {
            return this;
        }

        @Override
        protected SqlQuery createSql() {
            String sql = SELECT_ALL_USERS;
            if (StringUtils.isNotBlank(this.name)) {
                sql = sql + WHERE_USER;
            } else if (this.userId != null) {
                sql = sql + WHERE_USER_ID;
            }

            SqlQuery qry = server.getServer().createSqlQuery(sql);
            if (StringUtils.isNotBlank(this.name)) {
                qry.setParameter("username", this.name);
            } else if (this.userId != null) {
                qry.setParameter("id", this.userId);
            }
            return qry;
        }
    }
}
