package club.gamebreakers.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TableEntry<R, C, V> {

    private R row;
    private C column;
    private V value;

}