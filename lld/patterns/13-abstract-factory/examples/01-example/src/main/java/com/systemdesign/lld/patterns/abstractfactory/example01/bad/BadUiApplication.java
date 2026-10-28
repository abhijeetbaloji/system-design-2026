package com.systemdesign.lld.patterns.abstractfactory.example01.bad;

public class BadUiApplication {
    private final String osType;
    private Object button;
    private Object checkbox;

    public BadUiApplication(String osType) {
        this.osType = osType;
        createUI();
    }

    // Flawed: Hardcoded branching, type ambiguity, and prone to mixing incompatible components
    private void createUI() {
        if ("WINDOWS".equalsIgnoreCase(osType)) {
            this.button = new WindowsButton("Submit");
            // BUG: Developer mistakenly copied and instantiated MacCheckbox on Windows!
            this.checkbox = new MacCheckbox("Remember credentials");
        } else if ("MAC".equalsIgnoreCase(osType)) {
            this.button = new MacButton("Submit");
            this.checkbox = new MacCheckbox("Remember credentials");
        } else {
            throw new IllegalArgumentException("Unsupported OS: " + osType);
        }
    }

    public String renderScreen() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Bad UI Screen (OS: ").append(osType).append(") ===\n");
        if (button instanceof WindowsButton wb) {
            sb.append(wb.render()).append("\n");
        } else if (button instanceof MacButton mb) {
            sb.append(mb.render()).append("\n");
        }

        if (checkbox instanceof WindowsCheckbox wc) {
            sb.append(wc.render()).append("\n");
        } else if (checkbox instanceof MacCheckbox mc) {
            sb.append(mc.render()).append("\n");
        }
        return sb.toString();
    }

    public Object getButton() {
        return button;
    }

    public Object getCheckbox() {
        return checkbox;
    }
}
