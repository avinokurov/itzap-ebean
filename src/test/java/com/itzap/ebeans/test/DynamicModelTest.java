package com.itzap.ebeans.test;

import com.google.common.collect.Lists;
import com.itzap.common.AnyConfig;
import com.itzap.config.ConfigBuilder;
import com.itzap.config.ConfigType;
import com.itzap.ebeans.Servers;
import com.itzap.ebeans.test.dao.UserDao;
import com.itzap.ebeans.test.model.User;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.avaje.classpath.scanner.Location.FILESYSTEM_PREFIX;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DynamicModelTest {
    private Servers.Server server;
    private AnyConfig config;
    private UserDao userDao;

    private List<User> users = Lists.newArrayList(User.builder()
            .setUsername("user1")
            .setPassword("password1")
            .setEnabled(true)
            .setEmail("user1@user.com")
            .setAccountname("test")
            .setPhone("123")
            .setFirstName("first")
            .setLastName("last")
            .build(),
            User.builder()
                    .setUsername("user2")
                    .setPassword("password2")
                    .setEnabled(true)
                    .setEmail("user2@user.com")
                    .setAccountname("test")
                    .setPhone("111")
                    .setFirstName("second")
                    .setLastName("third")
                    .build());

    @Before
    public void setup() {
        String properties = this.getClass().getResource("/test-db.properties").getFile();
        String migrations = this.getClass().getResource("/migrations/").getFile();
        config = ConfigBuilder.builder(ConfigType.TYPE_SAFE)
                .setFileName(properties)
                .build();

        Flyway flyway = new Flyway();
        flyway.setLocations(FILESYSTEM_PREFIX + migrations);
        flyway.setDataSource(Servers.getDataSource("test.datasource", config));
        flyway.migrate();

        server = Servers.get("test.datasource", config);
        userDao = new UserDao(server);
    }

    @Test
    public void runTest() {
        userDao.addUser(users.get(0)).blockingSingle();
        userDao.addUser(users.get(1)).blockingSingle();

        Map<String, User> usrs = userDao.getUsers()
                .toList()
                .blockingGet()
                .stream().collect(Collectors.toMap(User::getUsername, u -> u));

        assertThat(usrs.size(), is(2));
        users.forEach(u -> {
            User dbUser = usrs.get(u.getUsername());
            assertThat(dbUser.getUsername(), is(u.getUsername()));
            assertThat(dbUser.getPassword(), is(u.getPassword()));
            assertThat(dbUser.getEnabled(), is(u.getEnabled()));
            assertThat(dbUser.getEmail(), is(u.getEmail()));
            assertThat(dbUser.getAccountname(), is(u.getAccountname()));
            assertThat(dbUser.getPhone(), is(u.getPhone()));
            assertThat(dbUser.getFirstName(), is(u.getFirstName()));
            assertThat(dbUser.getLastName(), is(u.getLastName()));
        });
    }
}
