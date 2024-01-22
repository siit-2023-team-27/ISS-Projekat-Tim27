package model.enums;

public enum ReservationStatus {
    PENDING, ACCEPTED, REJECTED, CANCELED;


    public static ReservationStatus fromString(String str){
        if(str.equals("PENDING")){
            return ReservationStatus.PENDING;
        }else if(str.equals("ACCEPTED")){
            return ReservationStatus.ACCEPTED;
        }else if(str.equals("REJECTED")){
            return ReservationStatus.REJECTED;
        }else if(str.equals("CANCELED")){
            return ReservationStatus.CANCELED;
        }
        return null;
    }
}

