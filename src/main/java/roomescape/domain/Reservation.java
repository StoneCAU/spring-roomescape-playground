package roomescape.domain;

public class Reservation {
    private final Long id;
    private final String name;
    private final String date;
    private final String time;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Reservation(Long id, String name, String date, String time) {
        validate();
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    private void validate() {
        validateName(name);
        validateDate(date);
        validateTime(time);
    }

    private void validateName(String name) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
    }

    private void validateDate(String date) {
        if (isBlank(date)) {
            throw new IllegalArgumentException("Date cannot be blank");
        }
    }

    private void validateTime(String time) {
        if (isBlank(time)) {
            throw new IllegalArgumentException("Time cannot be blank");
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

}
