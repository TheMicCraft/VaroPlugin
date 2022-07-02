package de.varoplugin.varo;

public enum ShutdownState implements VaroLoadingState {

    INITIALIZING("INIT", null),
    STOPPING_BOTS("BOTS", "Shutting down %s bots..."),
    SAVING_STATS("STATS", "Saving stats of %s players..."),
    SUCCESS("FINISHED", "Saved all data");

    private final String name;
    private final String message;

    ShutdownState(String name, String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public boolean hasMessage() {
        return this.message != null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String formatMessage(Object... args) {
        return String.format(this.message, args);
    }

}