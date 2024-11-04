package roomescape.domain;

public class Reservation {
    private Long id;
    private String name;
    private String date;
    private String time;

    public Reservation() {
    }

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
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static Reservation toEntity(Long id, Reservation reservation) {
        if (isEmpty(reservation)) throw new IllegalArgumentException("입력되지 않은 변수 존재");
        return new Reservation(id, reservation.name, reservation.date, reservation.time);
    }

    private static boolean isEmpty(Reservation reservation) {
        return reservation.name.isEmpty() || reservation.date.isEmpty() || reservation.time.isEmpty();
    }

}
