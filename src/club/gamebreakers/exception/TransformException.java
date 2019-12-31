package club.gamebreakers.exception;

public class TransformException extends RuntimeException {

    public TransformException(String message) {
        super(message);
    }

    public TransformException(Throwable cause) {
        super(cause);
    }
}
