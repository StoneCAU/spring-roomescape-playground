package roomescape.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    NOT_FOUND_TIME("해당 시간을 찾을 수 없습니다."),
    NOT_FOUND_RESERVATION("해당 예약을 찾을 수 없습니다.");


    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

}
