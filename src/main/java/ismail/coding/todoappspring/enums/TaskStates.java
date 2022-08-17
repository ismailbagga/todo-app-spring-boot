package ismail.coding.todoappspring.enums;

public enum TaskStates {
    COMPLETED(true) ,
    ALL(null) ,
    UNCOMPLETED(false ) ;
    private Boolean state ;
    private  TaskStates(Boolean state) {
        this.state = state ;
    }
    public Boolean getState() {
        return state ;
    }
}
