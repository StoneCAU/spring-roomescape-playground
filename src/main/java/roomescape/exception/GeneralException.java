package roomescape.exception;

public class GeneralException extends IllegalArgumentException {
    private static final String PREFIX = "[ERROR] ";

    public GeneralException(String message) {
        super(PREFIX + message);
    }
}
