public enum Status {
    TODO("Todo"),
    IN_PROGRESS("in_progress"),
    DONE("done");

    final private String value;

    Status(String value){
        this.value = value;
    }

    public  String getValue(){
        return value;
    }

    public  static Status fromValue(String value){
        for(Status s : Status.values()){
            if(s.getValue().equalsIgnoreCase(value)){
                return  s;
            }
        }
        throw new IllegalArgumentException("Status invalido " + value);
    }
}
