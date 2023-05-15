package org.functive;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FunctiveSymbolsTable {
    Stack<Map<String, Object>> stack;
    public Map<String, Object> currentTable;

    public FunctiveSymbolsTable() {
        stack = new Stack<>();
        currentTable = new HashMap<>();
    }

    public void enterBlock() {
        stack.push(currentTable);
        Map<String, Object> scopedTable = new HashMap<>();
        // add outside variables to the currentTable
        for (Map.Entry<String, Object> entry : currentTable.entrySet()) {
            scopedTable.put(entry.getKey(), entry.getValue());
        }
        currentTable = scopedTable;
    }

    public void exitBlock() {
        Map<String, Object> exitedScopedTable = currentTable;
        currentTable = stack.pop();

        // add the variables which have the same names as the outside variables to the
        // currentTable
        for (Map.Entry<String, Object> entry : exitedScopedTable.entrySet()) {
            if (currentTable.containsKey(entry.getKey())) {
                currentTable.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
