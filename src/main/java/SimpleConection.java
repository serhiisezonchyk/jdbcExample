import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.*;

public class SimpleConection implements AutoCloseable{
    Connection conn;
    
    public TableModel returnRequest(String sql) throws SQLException {
        //Хранимая модель
        TableModel tableRequet;
        //Имя пользователя и пароль
        String USER = System.getenv("user"), PASS = System.getenv("password");
        //URL: имя драйвера, хост, порт,имя бд
        String DB_URL = "jdbc:postgresql://localhost:5432/Showroom";
        conn = null;
        try {
            //Загрузка драйвера
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            tableRequet = new DefaultTableModel(new Object[]{"Something went wrong"}, 2);
            tableRequet.setValueAt("postgresql Driver (JDBS) is not found",0,0);
            tableRequet.setValueAt(e.toString(),1,0);
            return tableRequet;
        }
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            //Основной доступ к БД, проверка на nullpointer exception
            try (Statement s = conn.createStatement()) {
                //Если результат множество строк, получаем true
                boolean isRetrieved = s.execute(sql);
                if (isRetrieved) {
//                    Представление строк, проверка на nullpointer exception
                    try (ResultSet resSet = s.getResultSet()) {
                        //Определяем объект метаданных для текущего набора данных ResultSet.
                        //содержит информацию о результирующей таблице - количество колонок, тип значений колонок и т.д.
                        ResultSetMetaData resSetMD = resSet.getMetaData();
                        //Количество колонок
                        int column_count = resSetMD.getColumnCount();
                        //Массив для хранеиня оглавлений колонок
                        Object[] headers = new Object[column_count];
                        for (int i = 0; i < column_count; i++) {
                            headers[i] = (resSetMD.getColumnName(i + 1));
                        }
                        //В результиирующую таблицу заносив оглавления
                        tableRequet = new DefaultTableModel(headers, 0);
                        Object[] row = new Object[column_count];
                        //Пока есть строки
                        while (resSet.next()) {
                            for (int i = 0; i < column_count; i++) {
                                //Заполняем таблицу
                                row[i] = resSet.getString(i + 1);
                            }
                            ((DefaultTableModel) tableRequet).addRow(row);
                        }
                    }
                } else {
                    //Получаем количество измененных строк, если запрос не на вывод инфы
                    int count = s.getUpdateCount();
                    System.out.println(count);
                    tableRequet = new DefaultTableModel(new Object[]{"Result"}, 1);
                    tableRequet.setValueAt(count + " was changed", 0, 0);
                }

            }
        } catch (SQLException e) {
            tableRequet = new DefaultTableModel(new Object[]{"Error"}, 2);
            tableRequet.setValueAt("Connection is unsuccessful",0,0);
            tableRequet.setValueAt(e,1,0);
            e.printStackTrace();
            return tableRequet;
        }
        return tableRequet;
    }

    @Override
    public void close() throws Exception {
        conn.close();
    }
}