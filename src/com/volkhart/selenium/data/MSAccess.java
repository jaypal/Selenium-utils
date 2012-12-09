/*
 * Copyright (C) 2012 Marius Volkhart
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.volkhart.selenium.data;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Represents a single database and its connection. The connection is opened
 * when the database is created but must be closed manually.
 * 
 * @author Marius Volkhart
 */
public class MSAccess {

    private static final String sDataSource = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=";

    private enum Selection {
        STRING, DATE, BOOLEAN, INT
    }

    private String mDatabase;
    private Connection mConnection;
    private Statement mStatement;

    public MSAccess(String dBFilePath) {

        // Work around to handle different file systems
        mDatabase = (new File(dBFilePath)).getPath();

        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            mConnection = DriverManager.getConnection(sDataSource + mDatabase);
            mStatement = mConnection.createStatement();
        } catch (ClassNotFoundException cne) {
            // ClassNotFound should never happen as its part of the API.
        } catch (SQLException e) {
            // Caused by DriverManager.getConnection()
            e.printStackTrace();
        }
    }

    /**
     * Forcefully closes all connections to the database.
     */
    public void close() {
        if (mStatement != null) {
            try {
                mStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (mConnection != null) {
            try {
                mConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves data from the database. Only use if the column type is Text
     * 
     * @param table Name of the table being used
     * @param keyColumn Column which contains the key
     * @param key String used for lookup
     * @param column Column which contains the data
     * @return String represented in the table
     */
    public String getString(String table, String keyColumn, String key, String column) {
        return (String) getObject(Selection.STRING, table, keyColumn, key, column);
    }

    /**
     * Retrieves data from the database. Only use if the column type is Text
     * 
     * @param table Name of the table being used
     * @param keyColumn Column which contains the key
     * @param key String used for lookup
     * @param column Column which contains the data
     * @return String represented in the table
     */
    public String getString(String table, String keyColumn, int key, String column) {
        return (String) getObject(Selection.STRING, table, keyColumn, key, column);
    }

    /**
     * Retrieves data from the database. Only use if the column type is
     * Date/Time.
     * 
     * @param table Name of the table being used
     * @param keyColumn Column which contains the key
     * @param key String used for lookup
     * @param column Column which contains the data
     * @return Date represented in the table.
     */
    public Date getDate(String table, String keyColumn, String key, String column) {
        return (Date) getObject(Selection.DATE, table, keyColumn, key, column);
    }

    /**
     * Retrieves data from the database. Only use if the column type is
     * Date/Time.
     * 
     * @param table Name of the table being used
     * @param keyColumn Column which contains the key
     * @param key String used for lookup
     * @param column Column which contains the data
     * @return Date represented in the table.
     */
    public Date getDate(String table, String keyColumn, int key, String column) {
        return (Date) getObject(Selection.DATE, table, keyColumn, key, column);
    }

    /**
     * Retrieves data from the database. Only use if the column type is Yes/No
     * 
     * @param table Name of the table being used
     * @param keyColumn Column which contains the key
     * @param key String used for lookup
     * @param column Column which contains the data
     * @return Boolean represented in the table
     */
    public boolean getBoolean(String table, String keyColumn, String key, String column) {
        return ((Boolean) getObject(Selection.BOOLEAN, table, keyColumn, key, column))
                .booleanValue();
    }

    /**
     * Retrieves data from the database. Only use if the column type is Yes/No
     * 
     * @param table Name of the table being used
     * @param keyColumn Column which contains the key
     * @param key String used for lookup
     * @param column Column which contains the data
     * @return Boolean represented in the table
     */
    public boolean getBoolean(String table, String keyColumn, int key, String column) {
        return ((Boolean) getObject(Selection.BOOLEAN, table, keyColumn, key, column))
                .booleanValue();
    }

    /**
     * Retrieves data from the database. Only use if the column type is Number
     * 
     * @param table Name of the table being used
     * @param keyColumn Column which contains the key
     * @param key String used for lookup
     * @param column Column which contains the data
     * @return Object represented in the table
     */
    public int getInt(String table, String keyColumn, String key, String column) {
        return ((Integer) getObject(Selection.INT, table, keyColumn, key, column)).intValue();
    }

    /**
     * Retrieves data from the database. Only use if the column type is Number
     * 
     * @param table Name of the table being used
     * @param keyColumn Column which contains the key
     * @param key String used for lookup
     * @param column Column which contains the data
     * @return Object represented in the table
     */
    public int getInt(String table, String keyColumn, int key, String column) {
        return ((Integer) getObject(Selection.INT, table, keyColumn, key, column)).intValue();
    }

    private Object getObject(Selection selection, String table, String keyColumn,
            int key, String column) {
        Object toReturn = null;
        if (verifyParameters(table, keyColumn, column)) {
            String statement = "SELECT * FROM " + table + " WHERE " + keyColumn + " = " + key;
            toReturn = getObject(selection, column, statement);
        }
        return toReturn;
    }

    private Object getObject(Selection selection, String table, String keyColumn,
            String key, String column) {
        Object toReturn = null;
        if (verifyParameters(table, keyColumn, column)) {
            String statement = "SELECT * FROM " + table + " WHERE " + keyColumn + " = '" + key
                    + "'";
            toReturn = getObject(selection, column, statement);
        }
        return toReturn;
    }

    private Object getObject(Selection selection, String column, String statement) {
        Object toReturn = null;
        try {
            ResultSet results = mStatement.executeQuery(statement);

            if (results.next()) {
                switch (selection) {
                    case STRING:
                        toReturn = results.getString(column);
                        break;
                    case DATE:
                        toReturn = results.getDate(column);
                        break;
                    case BOOLEAN:
                        toReturn = Boolean.valueOf(results.getBoolean(column));
                        break;
                    case INT:
                        toReturn = Integer.valueOf(results.getInt(column));
                        break;
                }
            }
            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    private boolean verifyParameters(String table, String keyColumn, String column) {
        // columns may not contain spaces
        if (keyColumn.contains(" ") || column.contains(" ")) {
            return false;
        }

        // TODO add check for reserved words
        return true;
    }

}
