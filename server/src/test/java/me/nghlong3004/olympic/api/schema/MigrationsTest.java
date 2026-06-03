package me.nghlong3004.olympic.api.schema;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.DriverManager;
import java.util.HashSet;
import java.util.Set;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
@Testcontainers
class MigrationsTest {

  @Container
  static final PostgreSQLContainer POSTGRES =
      new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"));

  @Test
  void migrate_FromEmptyDatabase_CreatesMvpCoreTablesAndSeedData() throws Exception {
    // Arrange
    var flyway =
        Flyway.configure()
            .dataSource(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword())
            .load();

    // Act
    flyway.migrate();

    // Assert
    try (var connection =
            DriverManager.getConnection(
                POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
        var statement = connection.createStatement()) {
      assertThat(readTableNames(statement))
          .contains(
              "users",
              "roles",
              "permissions",
              "subjects",
              "topics",
              "questions",
              "question_options",
              "exams",
              "exam_questions",
              "exam_attempts",
              "attempt_answers",
              "attempt_answer_options",
              "essay_gradings",
              "results");
      assertThat(countRows(statement, "roles")).isEqualTo(4);
      assertThat(countRows(statement, "permissions")).isEqualTo(16);
      assertThat(countRows(statement, "subjects")).isEqualTo(4);
    }
  }

  private Set<String> readTableNames(java.sql.Statement statement) throws Exception {
    try (var resultSet =
        statement.executeQuery(
            "select table_name from information_schema.tables where table_schema = 'public'")) {
      var tableNames = new HashSet<String>();
      while (resultSet.next()) {
        tableNames.add(resultSet.getString("table_name"));
      }
      return tableNames;
    }
  }

  private int countRows(java.sql.Statement statement, String tableName) throws Exception {
    try (var resultSet = statement.executeQuery("select count(*) from " + tableName)) {
      resultSet.next();
      return resultSet.getInt(1);
    }
  }
}
