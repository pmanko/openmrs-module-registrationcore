package org.openmrs.module.registrationcore.api.xdssender.impl;

import org.dcm4chee.xds2.common.exception.XDSException;
import org.openmrs.Patient;
import org.openmrs.module.registrationcore.api.xdssender.XdsCcdImporter;
import org.openmrs.module.xdssender.api.domain.Ccd;
import org.openmrs.module.xdssender.api.model.DocumentInfo;
import org.openmrs.module.xdssender.api.service.CcdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

public class XdsCcdImporterImpl implements XdsCcdImporter {

    @Autowired
    @Qualifier("ccdService")
    CcdService ccdService;

    @Override
    public Ccd getLocallyStoredCcd(Patient patient) {
        return ccdService.getLocallyStoredCcd(patient);
    }

    @Override
    public Ccd downloadAndSaveCcd(Patient patient) throws XDSException, IOException {
        return ccdService.downloadAndSaveCcd(patient);
    }
}
