package net.cosmogrp.storage.sql.mysql;

import net.cosmogrp.storage.sql.identity.DataType;
import net.cosmogrp.storage.sql.identity.SQLConstraint;
import net.cosmogrp.storage.sql.identity.SQLElement;

import java.util.Arrays;
import java.util.List;

public class MySQLElement implements SQLElement {

    private final String column;
    private final DataType type;

    private final List<SQLConstraint> constraints;

    private String declaration;

    public MySQLElement(
            String column, DataType type,
            SQLConstraint... constraints
    ) {
        this.column = column;
        this.type = type;
        this.constraints = Arrays.asList(constraints);
    }

    @Override
    public boolean isPrimary() {
        return constraints.contains(SQLConstraint.PRIMARY);
    }

    @Override
    public boolean isNullable() {
        return !constraints.contains(SQLConstraint.NOT_NULL);
    }

    @Override
    public boolean isUnique() {
        return constraints.contains(SQLConstraint.UNIQUE);
    }

    @Override
    public String toParameter() {
        return ":" + column;
    }

    @Override
    public String toDeclaration() {
        if (declaration != null) {
            return declaration;
        }

        StringBuilder builder = new StringBuilder();

        builder
                .append(column)
                .append(" ")
                .append(type.getSql())
                .append(" ");

        constraints.forEach(constraint -> builder
                .append(constraint.toSql())
                .append(" "));

        declaration = builder.toString();

        return declaration;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public DataType getType() {
        return type;
    }

    @Override
    public List<SQLConstraint> getConstraints() {
        return constraints;
    }

}
