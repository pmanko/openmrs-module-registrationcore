package org.openmrs.module.registrationcore.api.mpi.openempi;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlMarshaller {

    public OpenEmpiPatientQuery getQuery(String path) throws Exception {
        File file = new File(ClassLoader.getSystemClassLoader().getResource(path).getPath());
        JAXBContext jaxbContext = JAXBContext.newInstance(OpenEmpiPatientQuery.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (OpenEmpiPatientQuery) jaxbUnmarshaller.unmarshal(file);
    }
}
