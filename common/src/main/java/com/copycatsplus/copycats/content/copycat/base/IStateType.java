package com.copycatsplus.copycats.content.copycat.base;

public interface IStateType {

    /*
    * Used to identify a copycats type. Made to make instanceof's less needed
     */
    default StateType stateType() {
        return StateType.SINGULAR;
    }
}
