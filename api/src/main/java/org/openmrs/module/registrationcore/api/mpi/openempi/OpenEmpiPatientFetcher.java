package org.openmrs.module.registrationcore.api.mpi.openempi;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.module.registrationcore.api.impl.IdentifierBuilder;
import org.openmrs.module.registrationcore.api.impl.RegistrationCoreProperties;
import org.openmrs.module.registrationcore.api.mpi.common.MpiAuthenticator;
import org.openmrs.module.registrationcore.api.mpi.common.MpiPatientFetcher;
import org.openmrs.module.santedb.mpiclient.model.MpiPatient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class OpenEmpiPatientFetcher implements MpiPatientFetcher {

    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    @Qualifier("registrationcore.restQueryExecutor")
    private RestQueryExecutor queryExecutor;

    @Autowired
    @Qualifier("registrationcore.identifierBuilder")
    private IdentifierBuilder identifierBuilder;

    @Autowired
    @Qualifier("registrationcore.patientBuilder")
    private PatientBuilder patientBuilder;

    @Autowired
    @Qualifier("registrationcore.mpiAuthenticator")
    private MpiAuthenticator authenticator;

    @Autowired
    @Qualifier("registrationcore.coreProperties")
    private RegistrationCoreProperties coreProperties;

    @Override
    public Patient fetchMpiPatient(String patientId) {
        OpenEmpiPatientResult mpiPatient = queryExecutor.getPatientById(authenticator.getToken(), patientId);

        return buildPatient(mpiPatient);
    }

    @Override
    public Patient fetchMpiPatient(String patientId, String identifierTypeUuid) {
        throw new NotImplementedException("Method fetchMpiPatient for OpenEmpiPatientFetcher is not implemented yet");
    }

    @Override
    public MpiPatient fetchMpiPatientWithObservations(String patientId, String identifierTypeUuid) {
        throw new NotImplementedException("Method fetchMpiPatientWithObservations for OpenEmpiPatientFetcher is not implemented yet");
    }

    private Patient buildPatient(OpenEmpiPatientResult mpiPatient) {
        patientBuilder.setPatient(new Patient());
        Patient patient = patientBuilder.buildPatient(mpiPatient);

        if (!containsOpenMrsIdentifier(patient))
            addOpenMrsIdentifier(patient);
        return patient;
    }

    private boolean containsOpenMrsIdentifier(Patient patientQuery) {
        for (PatientIdentifier identifier : patientQuery.getIdentifiers()) {
            if (identifier.isPreferred())
                return true;
        }
        return false;
    }

    private void addOpenMrsIdentifier(Patient patient) {
        log.info("Generate OpenMRS identifier for imported Mpi patient.");
        Integer openMrsIdentifierId = coreProperties.getOpenMrsIdentifierSourceId();
        PatientIdentifier identifier = identifierBuilder.generateIdentifier(openMrsIdentifierId, null);
        identifier.setPreferred(true);
        patient.addIdentifier(identifier);
    }
}
