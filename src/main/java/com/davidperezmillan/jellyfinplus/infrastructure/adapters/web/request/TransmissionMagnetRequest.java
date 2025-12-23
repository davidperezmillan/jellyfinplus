package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web.request;

public class TransmissionMagnetRequest {
    private String magnet;

    public TransmissionMagnetRequest() {}

    public TransmissionMagnetRequest(String magnet) {
        this.magnet = magnet;
    }

    public String getMagnet() {
        return magnet;
    }

    public void setMagnet(String magnet) {
        this.magnet = magnet;
    }
}
