/* ====================================================================
 * The QueryForm License, Version 1.1
 *
 * Copyright (c) 1998 - 2004 David F. Glasser.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by 
 *        David F. Glasser."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "QueryForm" and "David F. Glasser" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact dglasser@pobox.com.
 *
 * 5. Products derived from this software may not be called "QueryForm",
 *    nor may "QueryForm" appear in their name, without prior written
 *    permission of David F. Glasser.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL DAVID F. GLASSER, THE APACHE SOFTWARE 
 * FOUNDATION OR ITS CONTRIBUTORS, OR ANY AUTHORS OR DISTRIBUTORS
 * OF THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
 *
 * ==================================================================== 
 *
 * $Source: /cvsroot/qform/qform/src/org/glasser/sql/Column.java,v $
 * $Revision: 1.2 $
 * $Author: dglasser $
 * $Date: 2004/09/28 01:37:57 $
 * 
 * --------------------------------------------------------------------
 */
package org.glasser.sql;

import java.util.Hashtable;

public class Column implements java.io.Serializable {

	private String tableCatalog = null;
	private String tableSchema = null;
	private String tableName = null;
	private String columnName = null;
	private int dataType = 0;
	private String typeName = null;
	private int columnSize = 0;
	private int decimalDigits = 0;
	private int radix = 0;
	private boolean nullable = true;
	private String remarks = null;
	private String columnDefaultValue = null;
	private int ordinal = -1;
	private boolean pkComponent = false;
	private boolean isForeignKey = false;

	// setters

	public void setTableCatalog(String tableCatalog) {
		this.tableCatalog = tableCatalog;
	}

	public void setTableSchema(String tableSchema) {
		this.tableSchema = tableSchema;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public void setRadix(int radix) {
		this.radix = radix;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setColumnDefaultValue(String columnDefaultValue) {
		this.columnDefaultValue = columnDefaultValue;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public void setPkComponent(boolean pkComponent) {
		this.pkComponent = pkComponent;
	}

	public void setIsForeignKey(boolean isForeignKey) {
		this.isForeignKey = isForeignKey;
	}

	// getters

	public String getTableCatalog() {
		return tableCatalog;
	}

	public String getTableSchema() {
		return tableSchema;
	}

	public String getTableName() {
		return tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public int getDataType() {
		return dataType;
	}

	public String getTypeName() {
		return typeName;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public int getRadix() {
		return radix;
	}

	public boolean getNullable() {
		return nullable;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getColumnDefaultValue() {
		return columnDefaultValue;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public boolean getPkComponent() {
		return pkComponent;
	}

	public boolean getIsForeignKey() {
		return isForeignKey;
	}

	/**
	 * Returns the columnName enclosed in the provided "quote" characters, if
	 * the columnName contains a space or a hyphen, otherwise returns the
	 * columnName as-is. Normally, the openQuote and closeQuote args will both
	 * be regular double quotation marks ("), however, Microsoft databases use
	 * square brackets ([ and ]).
	 */
	public String maybeQuoteColumnName(char openQuote, char closeQuote) {
		if (columnName == null)
			return null;
		if (columnName.trim().length() == 0)
			return "";
		if (columnName.indexOf(' ') < 0 && columnName.indexOf('-') < 0)
			return columnName;
		StringBuffer buffer = new StringBuffer(columnName.length() + 4);
		return buffer.append(openQuote).append(columnName).append(closeQuote).toString();
	}

	public class SqlTypes {
		private final String typeName;
		private final Integer typeInt;

		public SqlTypes(String typeName, Integer typeInt) {
			super();
			this.typeName = typeName;
			this.typeInt = typeInt;
		}

		public String getTypeName() {
			return typeName;
		}

		public Integer getTypeInt() {
			return typeInt;
		}
	}

	final static Hashtable<Integer, String> typeStrings = new Hashtable<>();

	static {
		Column c = new Column();
		SqlTypes[] sqlTypes = { c.new SqlTypes("ARRAY", new Integer(java.sql.Types.ARRAY)),
				c.new SqlTypes("BIGINT", new Integer(java.sql.Types.BIGINT)),
				c.new SqlTypes("BINARY", new Integer(java.sql.Types.BINARY)),
				c.new SqlTypes("BIT", new Integer(java.sql.Types.BIT)),
				c.new SqlTypes("BLOB", new Integer(java.sql.Types.BLOB)),
				c.new SqlTypes("CHAR", new Integer(java.sql.Types.CHAR)),
				c.new SqlTypes("CLOB", new Integer(java.sql.Types.CLOB)),
				c.new SqlTypes("DATE", new Integer(java.sql.Types.DATE)),
				c.new SqlTypes("DECIMAL", new Integer(java.sql.Types.DECIMAL)),
				c.new SqlTypes("DISTINCT", new Integer(java.sql.Types.DISTINCT)),
				c.new SqlTypes("DOUBLE", new Integer(java.sql.Types.DOUBLE)),
				c.new SqlTypes("FLOAT", new Integer(java.sql.Types.FLOAT)),
				c.new SqlTypes("INTEGER", new Integer(java.sql.Types.INTEGER)),
				c.new SqlTypes("JAVA_OBJECT", new Integer(java.sql.Types.JAVA_OBJECT)),
				c.new SqlTypes("LONGVARBINARY", new Integer(java.sql.Types.LONGVARBINARY)),
				c.new SqlTypes("LONGVARCHAR", new Integer(java.sql.Types.LONGVARCHAR)),
				c.new SqlTypes("NULL", new Integer(java.sql.Types.NULL)),
				c.new SqlTypes("NUMERIC", new Integer(java.sql.Types.NUMERIC)),
				c.new SqlTypes("OTHER", new Integer(java.sql.Types.OTHER)),
				c.new SqlTypes("REAL", new Integer(java.sql.Types.REAL)),
				c.new SqlTypes("REF", new Integer(java.sql.Types.REF)),
				c.new SqlTypes("SMALLINT", new Integer(java.sql.Types.SMALLINT)),
				c.new SqlTypes("STRUCT", new Integer(java.sql.Types.STRUCT)),
				c.new SqlTypes("TIME", new Integer(java.sql.Types.TIME)),
				c.new SqlTypes("TIMESTAMP", new Integer(java.sql.Types.TIMESTAMP)),
				c.new SqlTypes("TINYINT", new Integer(java.sql.Types.TINYINT)),
				c.new SqlTypes("VARBINARY", new Integer(java.sql.Types.VARBINARY)),
				c.new SqlTypes("VARCHAR", new Integer(java.sql.Types.VARCHAR)) };
		
		for (SqlTypes j : sqlTypes) {
			typeStrings.put(j.getTypeInt(), j.getTypeName());
		}
	}

}
