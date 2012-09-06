package com.icelero.ias.ad.server.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtilities {

	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample", "root", "telugudb");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/*
	 * public static Connection getConnection() { Connection connection = null;
	 * try { Context initContext = new InitialContext(); Context envContext =
	 * (Context) initContext.lookup("java:/comp/env"); DataSource dataSource =
	 * (DataSource) envContext.lookup("jdbc/ias"); connection =
	 * dataSource.getConnection(); } catch (NamingException e) { } catch
	 * (SQLException e) { } return connection; }
	 */

	public static void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception ignore) {

		}
	}

	public static void closeStatement(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception ignore) {

		}
	}

	public static void closeResultSet(ResultSet rset) {
		try {
			if (rset != null) {
				rset.close();
			}
		} catch (Exception ignore) {

		}
	}

	public static void main(String args[]) {

		System.out.println(DatabaseUtilities.getConnection());
	}

	public static void rollback(Connection con) {
		try {
			if (con != null) {
				con.rollback();
			}
		} catch (Exception ignore) {

		}
	}

	public static void setAutoCommitTrue(Connection con) {
		try {
			if (con != null) {
				con.setAutoCommit(true);
			}
		} catch (Exception ignore) {

		}
	}

}
