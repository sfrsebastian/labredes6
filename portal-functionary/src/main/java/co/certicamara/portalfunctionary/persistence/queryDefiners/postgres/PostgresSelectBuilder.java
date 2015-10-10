package co.certicamara.portalfunctionary.persistence.queryDefiners.postgres;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL Select Builder created by John Krasnay. It has been modified to also include innerJoins, limit and offset.
 * 
 * http://john.krasnay.ca/2010/02/15/building-sql-in-java.html
 * @author John Krasnay
 *
 */
public class PostgresSelectBuilder {

    private List<String> columns = new ArrayList<String>();

    private List<String> tables = new ArrayList<String>();

    private List<String> joins = new ArrayList<String>();

    private List<String> innerJoins = new ArrayList<String>();

    private List<String> leftJoins = new ArrayList<String>();

    private List<String> wheres = new ArrayList<String>();

    private List<String> orderBys = new ArrayList<String>();

    private List<String> groupBys = new ArrayList<String>();

    private List<String> havings = new ArrayList<String>();

    private List<String> limits = new ArrayList<String>(); 

    private List<String> offsets = new ArrayList<String>();		


    public PostgresSelectBuilder() {

    }

    public PostgresSelectBuilder(String table) {
        tables.add(table);
    }

    private void appendList(StringBuilder sql, List<String> list, String init, String sep) {
        boolean first = true;
        for (String s : list) {
            if (first) {
                sql.append(init);
            } else {
                sql.append(sep);
            }
            sql.append(s);
            first = false;
        }
    }

    public PostgresSelectBuilder column(String name) {
        columns.add(name);
        return this;
    }

    public PostgresSelectBuilder column(String name, boolean groupBy) {
        columns.add(name);
        if (groupBy) {
            groupBys.add(name);
        }
        return this;
    }

    public PostgresSelectBuilder from(String table) {
        tables.add(table);
        return this;
    }

    public PostgresSelectBuilder groupBy(String expr) {
        groupBys.add(expr);
        return this;
    }

    public PostgresSelectBuilder having(String expr) {
        havings.add(expr);
        return this;
    }

    public PostgresSelectBuilder join(String join) {
        joins.add(join);
        return this;
    }

    public PostgresSelectBuilder innerJoin(String join) {
        innerJoins.add(join);
        return this;
    }

    public PostgresSelectBuilder leftJoin(String join) {
        leftJoins.add(join);
        return this;
    }

    public PostgresSelectBuilder orderBy(String name) {
        orderBys.add(name);
        return this;
    }

    public PostgresSelectBuilder limit(String name) {
        limits.add(name);
        return this;
    }

    public PostgresSelectBuilder offset(String name) {
        offsets.add(name);
        return this;
    }

    @Override
    public String toString() {

        StringBuilder sql = new StringBuilder("SELECT ");

        if (columns.isEmpty()) {
            sql.append("*");
        } else {
            appendList(sql, columns, "", ", ");
        }

        appendList(sql, tables, " FROM ", ", ");
        appendList(sql, joins, " JOIN ", " JOIN ");
        appendList(sql, leftJoins, " LEFT JOIN ", " LEFT JOIN ");
        appendList(sql, innerJoins, " INNER JOIN ", " INNER JOIN ");
        appendList(sql, wheres, " WHERE ", " AND ");
        appendList(sql, groupBys, " GROUP BY ", ", ");
        appendList(sql, havings, " HAVING ", " AND ");
        appendList(sql, orderBys, " ORDER BY ", ", ");
        appendList(sql, limits, " LIMIT ", "");
        appendList(sql, offsets, " OFFSET ", "");

        return sql.toString();
    }

    public PostgresSelectBuilder where(String expr) {
        wheres.add(expr);
        return this;
    }

}
