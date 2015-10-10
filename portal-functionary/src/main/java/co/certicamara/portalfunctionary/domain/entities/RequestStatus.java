package co.certicamara.portalfunctionary.domain.entities;

public enum RequestStatus {

    PENDING("pending"), WAITING_FOR_APPROVAL ("waiting_for_approval"),  APPROVED("approved"), REJECTED("rejected"), FINISHED("finished");

    private final String name;

    private RequestStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
