package ua.softgroup.medreview.persistent.entity;

/**
 * E-mail: sg.volodymyr@gmail.com
 * Created by Vladimir on 05.01.2017.
 */
public enum NoteStatus {
    APPROVED("Approved"), DISAPPROVED("Disapproved"), IN_REVIEW("In review"), REMOVED("Removed");
    private String status;

    NoteStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
