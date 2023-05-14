package org.functive;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FunctiveSymbolsTable {
    Stack<Map<String,Object>> stack;
    public Map<String,Object> currentTable;
    public FunctiveSymbolsTable(){
        stack = new Stack<>();
        currentTable = new HashMap<>();
    }

    public void enterBlock(boolean passVars) {
        stack.push(currentTable);
        if(!passVars){
            currentTable = new HashMap<>();
        }
        else{
            currentTable = new HashMap<>(currentTable);
        }
    }
    public void exitBlock(boolean applyChanges) {
        Map<String,Object> last = stack.pop();
        if(applyChanges) {
            last.replaceAll((k, v) -> currentTable.get(k));
        }
        currentTable = last;
    }
}
