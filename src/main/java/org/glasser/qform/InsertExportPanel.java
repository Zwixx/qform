/* ====================================================================
 * Copyright (c) 1998 - 2003 David F. Glasser.  All rights
 * reserved.
 *
 * This file is part of the QueryForm Database Tool.
 *
 * The QueryForm Database Tool is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * The QueryForm Database Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the QueryForm Database Tool; if not, write to:
 *
 *      The Free Software Foundation, Inc.,
 *      59 Temple Place, Suite 330
 *      Boston, MA  02111-1307  USA
 *
 * or visit http://www.gnu.org.
 *
 * ====================================================================
 *
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
 *
 * ==================================================================== 
 */
package org.glasser.qform;

import java.text.SimpleDateFormat;

import org.glasser.sql.CharFieldFormatter;
import org.glasser.sql.DBUtil;
import org.glasser.sql.NumberFieldFormatter;
import org.glasser.sql.TableInfo;
import org.glasser.util.Formatter;

public class InsertExportPanel extends ExportPanel {

	private static final Formatter charFormatter = new CharFieldFormatter() {
		public String toString() {
			return "<Copy Value>";
		}
	};

	private static final Formatter numFormatter = new NumberFieldFormatter() {
		public String toString() {
			return "<Copy Value>";
		}
	};

	private static final Formatter nullFormatter = new Formatter() {

		public String getFormattedString(Object o) {
			return "NULL";
		}

		public String toString() {
			return "NULL";
		}
	};

	private static class TimestampFormatter implements Formatter {

		public final static int TIME_ESCAPE = 0;
		public final static int DATE_ESCAPE = 1;
		public final static int TIMESTAMP_ESCAPE = 2;
		public final static int TIME_STRING = 3;
		public final static int DATE_STRING = 4;
		public final static int TIMESTAMP_STRING = 5;

		private int formatStyle = TIMESTAMP_STRING;
		private final static SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");
		private final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
		private final static SimpleDateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public TimestampFormatter(int formatStyle) {
			this.formatStyle = formatStyle;
		}

		public String getFormattedString(Object obj) {
			if (obj == null)
				return "NULL";

			java.util.Date date = (java.util.Date) obj;

			switch (formatStyle) {
			case TIME_ESCAPE:
				return "{t '" + TIME_FORMATTER.format(date) + "'}";
			case DATE_ESCAPE:
				return "{d '" + DATE_FORMATTER.format(date) + "'}";
			case TIMESTAMP_ESCAPE:
				return "{ts '" + TIMESTAMP_FORMATTER.format(date) + "'}";
			case TIME_STRING:
				return "'" + TIME_FORMATTER.format(date) + "'";
			case DATE_STRING:
				return "'" + DATE_FORMATTER.format(date) + "'";
			default:
				return "'" + TIMESTAMP_FORMATTER.format(date) + "'";

			}
		}

		public String toString() {

			switch (formatStyle) {
			case TIME_ESCAPE:
				return "{t 'hh:mm:ss'}";
			case DATE_ESCAPE:
				return "{d 'YYYY-MM-DD'}";
			case TIMESTAMP_ESCAPE:
				return "{ts 'YYYY-MM-DD hh:mm:ss'}";
			case TIME_STRING:
				return "'hh:mm:ss'";
			case DATE_STRING:
				return "'YYYY-MM-DD'";
			default: // TIMESTAMP_STRING
				return "'YYYY-MM-DD hh:mm:ss'";

			}
		}

	}

	private static final Object[] charChoices = { "", charFormatter, nullFormatter };

	private static final Object[] numChoices = { "", numFormatter, nullFormatter };

	private static final Object[] binChoices = { "", nullFormatter };

	private static final Object[] dateTimeChoices = { "", nullFormatter,
			new TimestampFormatter(TimestampFormatter.TIMESTAMP_STRING),
			new TimestampFormatter(TimestampFormatter.DATE_STRING),
			new TimestampFormatter(TimestampFormatter.TIME_STRING),
			new TimestampFormatter(TimestampFormatter.TIMESTAMP_ESCAPE),
			new TimestampFormatter(TimestampFormatter.DATE_ESCAPE),
			new TimestampFormatter(TimestampFormatter.TIME_ESCAPE) };

	public InsertExportPanel(TableInfo ti) {
		super(ti);

	}

	protected Object[] getFormatterChoices(int type) {
		if (DBUtil.isCharType(type)) {
			return charChoices;
		} else if (DBUtil.isNumericType(type)) {
			return numChoices;
		} else if (DBUtil.isDateTimeType(type)) {
			return dateTimeChoices;
		} else {
			return binChoices;
		}

	}

}
