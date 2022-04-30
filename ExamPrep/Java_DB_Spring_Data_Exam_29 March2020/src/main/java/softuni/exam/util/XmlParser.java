package softuni.exam.util;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Path;

public interface XmlParser {
    <E> E fromFile(Path filePath, Class<E> eClass) throws JAXBException, IOException;
}
