package org.openmrs.module.registrationcore.api.mpi.fhir;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.registrationcore.RegistrationCoreConstants;
import org.openmrs.module.registrationcore.api.mpi.common.MpiException;
import org.openmrs.module.registrationcore.api.mpi.common.MpiPatientFetcher;
import org.openmrs.module.registrationcore.api.mpi.openempi.PatientIdentifierMapper;
import org.openmrs.module.santedb.mpiclient.api.MpiClientService;
import org.openmrs.module.santedb.mpiclient.configuration.MpiClientConfiguration;
import org.openmrs.module.santedb.mpiclient.model.MpiPatient;
import org.springframework.beans.factory.annotation.Autowired;

public class FhirPatientFetcher implements MpiPatientFetcher {

    @Autowired
    private PatientIdentifierMapper identifierMapper;

    @Autowired
    private PatientService patientService;

    protected final Log log = LogFactory.getLog(this.getClass());

    @Override
    public Patient fetchMpiPatient(String patientIdentifier, String identifierTypeUuid) {
        try {
            MpiClientService service = Context.getService(MpiClientService.class);
            MpiClientConfiguration config = MpiClientConfiguration.getInstance();
            Patient patient = service.getPatient(patientIdentifier, config.getEnterprisePatientIdRoot()).toPatient();
            return patient;
        } catch(Exception e) {
            throw new MpiException("Error in PDQ fetch by identifier", e);
        }
    }


    @Override
    public MpiPatient fetchMpiPatientWithObservations(String patientIdentifier, String identifierTypeUuid) {
        try {
            MpiClientService service = Context.getService(MpiClientService.class);
            MpiClientConfiguration config = MpiClientConfiguration.getInstance();
            MpiPatient mpiPatient = service.getPatient(patientIdentifier, config.getEnterprisePatientIdRoot());
            return mpiPatient;
        } catch(Exception e) {
            throw new MpiException("Error in PDQ fetch by identifier", e);
        }
    }

    @Override
    public Patient fetchMpiPatient(String patientIdentifier) {
        PatientIdentifierType patientIdentifierType = patientService.getPatientIdentifierTypeByName(
                RegistrationCoreConstants.MPI_IDENTIFIER_TYPE_ECID_NAME);
        return fetchMpiPatient(patientIdentifier, patientIdentifierType.getUuid());
    }


    private Patient toPatientFromMpiPatient(Patient mpiPatient) {
        // it is a hack in order to save the MpiPatient class to DB (converting to the Patient class)
        Patient patient = new Patient(mpiPatient);
        patient.setIdentifiers(mpiPatient.getIdentifiers());
        for (PatientIdentifier pid : patient.getIdentifiers()) {
            pid.setPatient(patient);
        }
        return patient;
    }
}
