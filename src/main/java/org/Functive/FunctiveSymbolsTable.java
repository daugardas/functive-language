package org.functive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class FunctiveSymbolsTable {
    private class SymbolTableEntry {
        private final Map<String, Object> table;
        private final Map<String, ArrayList<Object>> phoonkResultValues;

        public SymbolTableEntry(Map<String, Object> table, Map<String, ArrayList<Object>> phoonkResultValues) {
            this.table = table;
            this.phoonkResultValues = phoonkResultValues;
        }

        public Map<String, Object> getTable() {
            return this.table;
        }

        public Map<String, ArrayList<Object>> getPhoonkResultValues() {
            return this.phoonkResultValues;
        }
    }

    Stack<SymbolTableEntry> stack;
    public Map<String, Object> currentTable;
    public Map<String, ArrayList<Object>> currentTablePhoonkResultValues;

    public FunctiveSymbolsTable() {
        stack = new Stack<>();
        currentTable = new HashMap<>();
        currentTablePhoonkResultValues = new HashMap<>();
    }

    public void enterBlock() {
        stack.push(new SymbolTableEntry(currentTable, currentTablePhoonkResultValues));
        Map<String, Object> scopedTable = new HashMap<>();
        // add outside variables to the scopedTable
        for (Map.Entry<String, Object> entry : currentTable.entrySet()) {
            scopedTable.put(entry.getKey(), entry.getValue());
        }
        currentTable = scopedTable;

        Map<String, ArrayList<Object>> scopedTablePhoonkResultValues = new HashMap<>();
        // add outside variables to the scopedPhunkResultTable
        for (Map.Entry<String, ArrayList<Object>> entry : currentTablePhoonkResultValues.entrySet()) {
            scopedTablePhoonkResultValues.put(entry.getKey(), entry.getValue());
        }
        currentTablePhoonkResultValues = scopedTablePhoonkResultValues;

    }

    public void exitBlock() {
        Map<String, Object> exitedScopedTable = currentTable;
        Map<String, ArrayList<Object>> exitedScopedTablePhoonkResultValues = currentTablePhoonkResultValues;
        SymbolTableEntry exitedEntry = stack.pop();
        currentTable = exitedEntry.getTable();
        currentTablePhoonkResultValues = exitedEntry.getPhoonkResultValues();

        // add the variables which have the same names as the outside variables to the
        // currentTable
        for (Map.Entry<String, Object> entry : exitedScopedTable.entrySet()) {
            if (currentTable.containsKey(entry.getKey())) {
                currentTable.put(entry.getKey(), entry.getValue());
            }
        }

        // add the variables which have the same names as the outside variables to the
        // currentTablePhoonkResultValues
        for (Map.Entry<String, ArrayList<Object>> entry : exitedScopedTablePhoonkResultValues.entrySet()) {
            if (currentTablePhoonkResultValues.containsKey(entry.getKey())) {
                currentTablePhoonkResultValues.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
