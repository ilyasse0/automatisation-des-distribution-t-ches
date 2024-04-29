package com.iben.gestiontaches.enums;

import lombok.AllArgsConstructor;

public class TaskDto  {
    private statusCode status;

    public String getStatusString() {
        if (status != null) {
            switch (status) {
                case NOTASSIGNED:
                    return "Not Assigned";
                case TODO:
                    return "To Do";
                case IN_PROGRESS:
                    return "In Progress";
                case DONE:
                    return "Done";
                case CANCELLED:
                    return "Cancelled";
                default:
                    return "";
            }
        }
        return "";
    }
}
