package com.systemdesign.lld.patterns.singleton.example02.good;

import java.util.List;

public interface LoggerService {
    void debug(String message);

    void info(String message);

    void warn(String message);

    void error(String message);

    List<LogRecord> getLogs();

    void setLogLevel(LogLevel level);

    LogLevel getLogLevel();

    void clearLogs();
}
