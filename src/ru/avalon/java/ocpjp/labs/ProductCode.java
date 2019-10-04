package ru.avalon.java.ocpjp.labs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Класс описывает представление о коде товара и отражает соответствующую 
 * таблицу базы данных Sample (таблица PRODUCT_CODE).
 * 
 * @author Daniel Alpatov <danial.alpatov@gmail.com>
 */
public class ProductCode {
    
    /**
     * Код товара.
     */
    private String code;
    
    /**
     * Кода скидки.
     */
    private char discountCode;
    
    /**
     * Описание.
     */
    private String description;
    
    /**
     * Основной конструктор типа {@link ProductCode}.
     * 
     * @param code - код товара.
     * @param discountCode - код скидки.
     * @param description - описание.
     */
    public ProductCode(String code, char discountCode, String description) {
        this.code = code;
        this.discountCode = discountCode;
        this.description = description;
    }
    
    /**
     * Инициализирует объект значениями из переданного {@link ResultSet}.
     * 
     * @param set - {@link ResultSet}, полученный в результате запроса, 
     * содержащего все поля таблицы PRODUCT_CODE базы данных Sample.
     * @throws SQLException
     */
    private ProductCode(ResultSet set) throws SQLException {
        code = set.getString("prod_code");
        discountCode = set.getString("discount_code").charAt(0);
        description = set.getString("description");
    }
    
    /**
     * Возвращает код товара.
     * 
     * @return Объект типа {@link String}.
     */
    public String getCode() {return code;}
    
    /**
     * Переменная для хранения прежнего значения кода товара.
     */
    private String previousCode;
    
    /**
     * Устанавливает новое значение кода товара, сохраняя прежнее в переменную
     * типа {@link String}.
     * 
     * @param code - код товара.
     */
    public void setCode(String code) {
        previousCode = getCode();
        this.code = code;
    }
    
    /**
     * Возвращает код скидки.
     * 
     * @return Объект типа {@link String}.
     */
    public char getDiscountCode() {return discountCode;}
    
    /**
     * Устанавливает код скидки.
     * 
     * @param discountCode - код скидки.
     */
    public void setDiscountCode(char discountCode) {
        this.discountCode = discountCode;
    }
    
    /**
     * Возвращает описание.
     * 
     * @return Объект типа {@link String}.
     */
    public String getDescription() {return description;}
    
    /**
     * Устанавливает описание.
     * 
     * @param description - описание.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Хеш-функция типа {@link ProductCode}.
     * 
     * @return Значение хеш-кода объекта типа {@link ProductCode}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(code, discountCode, description);
    }
    
    /**
     * Сравнивает некоторый произвольный объект с текущим объектом типа 
     * {@link ProductCode}.
     * 
     * @param obj - объект, с которым сравнивается текущий объект.
     * @return true, если объект obj тождественен текущему объекту. В обратном 
     * случае - false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductCode){
            ProductCode otherProductCode = (ProductCode) obj;
            return this.code.equals(otherProductCode.code)
                || this.description.equals(otherProductCode.description);
        }
        return false;
    }
    
    /**
     * Возвращает строковое представление кода товара.
     * 
     * @return Объект типа {@link String}.
     */
    @Override
    public String toString() {return code;}
    
    /**
     * Возвращает запрос на выбор всех записей из таблицы PRODUCT_CODE 
     * базы данных Sample
     * 
     * @param connection - действительное соединение с базой данных.
     * @return Запрос в виде объекта класса {@link PreparedStatement}.
     * @throws SQLException
     */
    public static PreparedStatement getSelectQuery(Connection connection) throws SQLException {
        String select = "SELECT * FROM product_code";
        return connection.prepareStatement(select);
    }
    
    /**
     * Возвращает запрос на добавление записи в таблицу PRODUCT_CODE 
     * базы данных Sample.
     * 
     * @param connection - действительное соединение с базой данных.
     * @return Запрос в виде объекта класса {@link PreparedStatement}.
     * @throws SQLException
     */
    public static PreparedStatement getInsertQuery(Connection connection) throws SQLException {
        String insert = "INSERT INTO product_code(prod_code, discount_code, description) VALUES(?, ?, ?)";
        return connection.prepareStatement(insert);
    }
    
    /**
     * Возвращает запрос на обновление значений записи в таблице PRODUCT_CODE 
     * базы данных Sample
     * 
     * @param connection - действительное соединение с базой данных.
     * @return Запрос в виде объекта класса {@link PreparedStatement}.
     * @throws SQLException
     */
    public static PreparedStatement getUpdateQuery(Connection connection) throws SQLException {
        String update = "UPDATE product_code SET prod_code = ?, discount_code = ?, description = ? WHERE prod_code = ?";
        return connection.prepareStatement(update);
    }
    
    /**
     * Преобразует {@link ResultSet} в коллекцию объектов типа {@link ProductCode}
     * 
     * @param set - {@link ResultSet}, полученный в результате запроса, содержащего 
     * все поля таблицы PRODUCT_CODE базы данных Sample.
     * @return Коллекция объектов типа {@link ProductCode}.
     * @throws SQLException 
     */
    public static Collection<ProductCode> convert(ResultSet set) throws SQLException {
        Collection<ProductCode> list = new ArrayList<>();
        while (set.next()) {
            list.add(new ProductCode(set));
        }
        return list;
    }
       
    /**
     * Сохраняет текущий объект в базе данных. 
     * <p>
     * Если запись ещё не существует, то выполняется запрос типа INSERT.
     * <p>
     * Если запись уже существует в базе данных, то выполняется запрос типа UPDATE.
     * 
     * @param connection - действительное соединение с базой данных
     * @throws SQLException
     */
    public void save(Connection connection) throws SQLException {
        Collection<ProductCode> previousState = all(connection);
        ProductCode actualProduct = new ProductCode(code, discountCode, description);
        PreparedStatement statement;
        if (previousState.contains(actualProduct)) {
            statement = getUpdateQuery(connection);
            statement.setString(4, previousCode);
        } else {
            statement = getInsertQuery(connection);
        }
            statement.setString(1, code);
            statement.setString(2, String.valueOf(discountCode));
            statement.setString(3, description);
            statement.execute();
    }
    
    /**
     * Возвращает все записи таблицы PRODUCT_CODE в виде коллекции объектов
     * типа {@link ProductCode}.
     * 
     * @param connection - действительное соединение с базой данных.
     * @return коллекция объектов типа {@link ProductCode}.
     * @throws SQLException 
     */
    public static Collection<ProductCode> all(Connection connection) throws SQLException {
        try (PreparedStatement statement = getSelectQuery(connection)) {
            try (ResultSet result = statement.executeQuery()) {
                return convert(result);
            }
        }
    }
    
}
