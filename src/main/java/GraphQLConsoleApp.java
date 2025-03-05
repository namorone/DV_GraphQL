import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import graphql.schema.idl.errors.SchemaProblem;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Paths;


public class GraphQLConsoleApp {


    private static List<Map<String, Object>> booksData = new ArrayList<>();

    public static void main(String[] args) {
        loadData("src/main/resources/books.json");


        GraphQLSchema schema = buildSchema("src/main/resources/schema.graphqls");

        // Створення GraphQL інстансу
        GraphQL graphQL = GraphQL.newGraphQL(schema).build();

        String query = loadQueryFromFile("src/main/resources/query.graphql");
        if (!query.isEmpty()) {
            ExecutionInput queryInput = ExecutionInput.newExecutionInput().query(query).build();
            ExecutionResult queryResult = graphQL.execute(queryInput);
            System.out.println("Результат запиту:");
            System.out.println(queryResult.toSpecification());
        } else {
            System.out.println("Не вдалося зчитати запит з файлу.");
        }


        String mutationQuery = loadQueryFromFile("src/main/resources/mutation.graphql");
        if (!mutationQuery.isEmpty()) {
            ExecutionInput mutationInput = ExecutionInput.newExecutionInput().query(mutationQuery).build();
            ExecutionResult mutationResult = graphQL.execute(mutationInput);
            System.out.println("Результат мутації:");
            System.out.println(mutationResult.toSpecification());
        } else {
            System.out.println("Не вдалося зчитати мутаційний запит з файлу.");
        }

        if (!query.isEmpty()) {
            ExecutionInput queryInput = ExecutionInput.newExecutionInput().query(query).build();
            ExecutionResult queryResult = graphQL.execute(queryInput);
            System.out.println("Результат запиту:");
            System.out.println(queryResult.toSpecification());
        } else {
            System.out.println("Не вдалося зчитати запит з файлу.");
        }
    }


    // Метод для завантаження даних з JSON файлу
    private static void loadData(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            booksData = mapper.readValue(new File(filePath), new TypeReference<List<Map<String, Object>>>(){});
            System.out.println("Дані завантажено: " + booksData.size() + " записів.");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Метод для зчитування запиту з файлу
    private static String loadQueryFromFile(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    // Метод побудови схеми з файлу
    private static GraphQLSchema buildSchema(String schemaFile) {
        try {
            // Зчитування файлу схеми
            File schemaFileObj = new File(schemaFile);
            TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFileObj);
            RuntimeWiring wiring = buildWiring();
            SchemaGenerator schemaGenerator = new SchemaGenerator();
            return schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
        } catch (SchemaProblem e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    // Побудова RuntimeWiring – зв’язування полів схеми з DataFetcher-ами
    private static RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        // Кастомний DataFetcher для поля books (фільтрація за title)
                        .dataFetcher("books", new DataFetcher<List<Map<String, Object>>>() {
                            @Override
                            public List<Map<String, Object>> get(DataFetchingEnvironment env) {
                                String titleArg = env.getArgument("title");
                                if (titleArg != null) {
                                    return booksData.stream()
                                            .filter(book -> ((String)book.get("title")).contains(titleArg))
                                            .collect(Collectors.toList());
                                }
                                return booksData;
                            }
                        })
                        // Кастомний DataFetcher для отримання книги за ID
                        .dataFetcher("bookById", new DataFetcher<Map<String, Object>>() {
                            @Override
                            public Map<String, Object> get(DataFetchingEnvironment env) {
                                String id = env.getArgument("id");
                                return booksData.stream()
                                        .filter(book -> id.equals(book.get("id")))
                                        .findFirst()
                                        .orElse(null);
                            }
                        })
                )
                .type("Mutation", typeWiring -> typeWiring
                        // Кастомний DataFetcher для мутації updateBook
                        .dataFetcher("updateBook", new DataFetcher<Map<String, Object>>() {
                            @Override
                            public Map<String, Object> get(DataFetchingEnvironment env) {
                                Map<String, Object> input = env.getArgument("input");
                                String id = (String) input.get("id");
                                String newTitle = (String) input.get("title");

                                for (Map<String, Object> book : booksData) {
                                    if (book.get("id").equals(id)) {
                                        // Зміна назви книги, якщо нове значення не null
                                        if (newTitle != null) {
                                            book.put("title", newTitle);
                                        }
                                        return book;
                                    }
                                }
                                return null;
                            }
                        })
                )
                // Для вкладених типів можна використовувати стандартний PropertyDataFetcher, наприклад для поля 'author' у типі Book
                .type("Book", typeWiring -> typeWiring
                        .dataFetcher("author", graphql.schema.PropertyDataFetcher.fetching("author"))
                )
                .build();
    }
}




