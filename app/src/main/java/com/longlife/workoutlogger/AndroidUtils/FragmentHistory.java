package com.longlife.workoutlogger.AndroidUtils;

import java.util.ArrayList;

// Keeps track of fragment history that has been navigated to.
public class FragmentHistory {
    private ArrayList<Integer> stackArr;

    /**
     * constructor to create stack with size
     *
     * @param
     */
    public FragmentHistory() {
        stackArr = new ArrayList<>();


    }


    public int getStackSize() {
        return stackArr.size();
    }

    /**
     * This method adds new entry to the top
     * of the stack
     *
     * @param entry
     * @throws Exception
     */
    public void push(int entry) {

        if (isAlreadyExists(entry)) {
            return;
        }
        stackArr.add(entry);

    }

    // Methods
    private boolean isAlreadyExists(int entry) {
        return (stackArr.contains(entry));
    }

    /**
     * This method removes an entry from the
     * top of the stack.
     *
     * @return
     * @throws Exception
     */
    public int pop() {

        int entry = -1;
        if (!isEmpty()) {

            entry = stackArr.get(stackArr.size() - 1);

            stackArr.remove(stackArr.size() - 1);
        }
        return entry;
    }

    public boolean isEmpty() {
        return (stackArr.size() == 0);
    }

    /**
     * This method removes an entry from the
     * top of the stack.
     *
     * @return
     * @throws Exception
     */
    public int popPrevious() {

        int entry = -1;

        if (!isEmpty()) {
            entry = stackArr.get(stackArr.size() - 2);
            stackArr.remove(stackArr.size() - 2);
        }
        return entry;
    }

    /**
     * This method returns top of the stack
     * without removing it.
     *
     * @return
     */
    public int peek() {
        if (!isEmpty()) {
            return stackArr.get(stackArr.size() - 1);
        }

        return -1;
    }

    public void emptyStack() {

        stackArr.clear();
    }
}
