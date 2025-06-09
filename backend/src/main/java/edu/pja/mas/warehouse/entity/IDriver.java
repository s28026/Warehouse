package edu.pja.mas.warehouse.entity;

public interface IDriver {
    // Normal license
    String NORMAL_LICENSE_PREFIX = "DL-";
    int NORMAL_LICENSE_LEN = 10;

    // Forklift license
    String FORKLIFT_LICENSE_PREFIX = "FL-";
    int FORKLIFT_LICENSE_LEN = 7;

    void validateDriverLicense();
}
