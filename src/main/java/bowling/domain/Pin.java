package bowling.domain;

import java.util.Objects;

public class Pin {

    private final int MAXIMUM_PIN_COUNT = 10;
    private final int MINIMUM_PIN_COUNT = 0;

    private int pins;

    public Pin(int pins) {
        this.pins = pins;
        validationPinCount();
    }

    public int count() {
        return pins;
    }
    private void validationPinCount() {
        if(pins > MAXIMUM_PIN_COUNT || pins < MINIMUM_PIN_COUNT) {
            throw new IllegalArgumentException("볼링 한 구당 쓰러트릴수 있는 핀의 갯수는 0 ~ 10 사이입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pin pin = (Pin) o;
        return pins == pin.pins;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pins);
    }
}
