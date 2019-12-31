package club.gamebreakers.utils;

import java.util.ArrayList;

public class Table<R, C, V> extends ArrayList<TableEntry<R, C, V>> {

    public void put(R row, C column, V value) {
        this.add(new TableEntry<>(row, column, value));
    }

    public TableEntry getByRow(R row) {
        return this.stream().filter(entry -> entry.getRow().equals(row)).findFirst().orElse(null);
    }

    public TableEntry getByColumn(C column) {
        return this.stream().filter(entry -> entry.getColumn().equals(column)).findFirst().orElse(null);
    }

    public void removeValue(V value) {
        this.removeIf(entry -> entry.getValue().equals(value));
    }

    public void removeRow(R row) {
        this.removeIf(entry -> entry.getRow().equals(row));
    }

    public void removeColumn(C column) {
        this.removeIf(entry -> entry.getColumn().equals(column));
    }

    public void removeRCPair(R row, C column) {
        this.removeIf(entry -> entry.getRow().equals(row) && entry.getColumn().equals(column));
    }

}