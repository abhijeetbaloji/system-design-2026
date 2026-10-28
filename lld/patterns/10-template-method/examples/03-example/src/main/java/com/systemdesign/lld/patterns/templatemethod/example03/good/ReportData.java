package com.systemdesign.lld.patterns.templatemethod.example03.good;

import java.util.List;
import java.util.Map;

public record ReportData(
        String title,
        Map<String, Object> metrics,
        List<String> items
) {}
