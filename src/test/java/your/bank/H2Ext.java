package your.bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//db obj

public class H2Ext implements AfterTestExecutionCallback, ParameterResolver {

        private static final Logger LOGGER = LoggerFactory.getLogger(H2Ext.class);

        private JdbcDataSource jdbcDataSource;

        @Override
        public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
            try (Connection connection = jdbcDataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SHUTDOWN")) {
                LOGGER.info("Shutting down {}", jdbcDataSource.getUrl());

                preparedStatement.execute();
            } finally {
                jdbcDataSource = null;
            }
        }

        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            return parameterContext.getParameter().getType().isAssignableFrom(JdbcDataSource.class);
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            if (null == jdbcDataSource) {
                LOGGER.info("Creating HSQLDB DataSource");

                JdbcDataSource dataSource = new JdbcDataSource();
                dataSource.setUrl(String.format("jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false", "testDB"));
                dataSource.setUser("sa");
                dataSource.setPassword("");

                //DataSourceManager.executeScripts(dataSource, "/schema.sql", "/data.sql");
                //maybe imp later

                this.jdbcDataSource = dataSource;
            }

            return jdbcDataSource;
        }
}
