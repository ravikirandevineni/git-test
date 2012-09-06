package com.icelero.ias.ad.server.utilities;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class StatementUtilities {
	private StatementUtilities() {
	}

	public static void setString(PreparedStatement stmt, int parameterIndex, String value) throws SQLException {
		if (value == null) {
			stmt.setNull(parameterIndex, Types.VARCHAR);
		} else {
			stmt.setString(parameterIndex, value);
		}
	}

	public static void setInt(PreparedStatement stmt, int parameterIndex, int value) throws SQLException {
		stmt.setInt(parameterIndex, value);
	}

	public static void setDate(PreparedStatement stmt, int parameterIndex, Date value) throws SQLException {
		if (value == null) {
			stmt.setNull(parameterIndex, Types.DATE);
		} else {
			stmt.setDate(parameterIndex, value);
		}
	}

	public static void setUtilDate(PreparedStatement stmt, int parameterIndex, java.util.Date value) throws SQLException {
		if (value == null) {
			stmt.setNull(parameterIndex, Types.DATE);
		} else {
			stmt.setDate(parameterIndex, new java.sql.Date(value.getTime()));
		}
	}

	public static void setTimeStamp(PreparedStatement stmt, int parameterIndex, java.util.Date value) throws SQLException {
		if (value == null) {
			stmt.setNull(parameterIndex, Types.TIMESTAMP);
		} else {
			stmt.setTimestamp(parameterIndex, new java.sql.Timestamp(value.getTime()));
		}
	}

}
