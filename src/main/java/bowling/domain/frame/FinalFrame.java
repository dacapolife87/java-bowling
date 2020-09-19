package bowling.domain.frame;

import bowling.domain.Pin;
import bowling.domain.Score;
import bowling.domain.state.Ready;
import bowling.domain.state.State;
import bowling.exception.CannotCalculateException;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class FinalFrame extends Frame {

    private static final String FINAL_FRAME_RANGE = "FinalFrame의 인덱스는 10이여야합니다.";

    private LinkedList<State> states = new LinkedList<>();

    public FinalFrame(int frameIndex) {
        super(frameIndex);
        states.add(new Ready());
    }

    @Override
    protected void validationFrameIndex(int frameIndex) {
        if (frameIndex != MAX_FRAME_INDEX) {
            throw new IllegalArgumentException(FINAL_FRAME_RANGE);
        }
    }

    @Override
    public boolean rollingEnd() {
        return state.isFinish();
    }

    @Override
    public void bowl(Pin pin) {
        State currentState = states.getLast();

        if (currentState.isFinish()) {
            states.add(new Ready().bowl(pin));
            return;
        }
        states.removeLast();
        states.add(currentState.bowl(pin));
    }

    @Override
    public boolean isEndAllFrame() {
        State first = states.getFirst();
        if (!first.canBowlFinalFrame()) {
            return true;
        }
        if (states.size() == 3) {
            return true;
        }
        if (states.size() == 2 && containsSpare()) {
            return true;
        }
        return false;
    }

    private boolean containsSpare() {
        return states.getFirst().record().contains("/");
    }

    @Override
    public String index() {
        return String.valueOf(MAX_FRAME_INDEX);
    }

    @Override
    public String currentFrameStatus() {
        return states.stream()
                .map(State::record)
                .collect(Collectors.joining("|"));
    }

    public int score() {
        int finalFrameScore = -1;
        try {
            Score score = getFirstScore();

            score = calculateScore(score);

            finalFrameScore = score.getScore();
        } catch (CannotCalculateException e) {
            finalFrameScore = -1;
        }

        return finalFrameScore;
    }

    private Score calculateScore(Score score) {
        for (int i = 1; i < states.size(); i++) {
            State state = states.get(i);
            score = state.calculateAdditionalScore(score);
        }
        return score;
    }

    public Score calculateAdditionalScore(Score beforeScore) {
        Score score = beforeScore;
        for (State state : states) {
            score = state.calculateAdditionalScore(score);
        }
        return score;
    }

    private Score getFirstScore() {
        return states.getFirst().getScore();
    }
}
