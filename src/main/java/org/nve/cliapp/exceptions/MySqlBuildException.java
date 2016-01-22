package org.nve.cliapp.exceptions;

public class MySqlBuildException extends Exception {
    private static final long serialVersionUID = 01L;  

    public MySqlBuildException() {
    }

    public MySqlBuildException(String msg) {
        super(msg);
    }
}
