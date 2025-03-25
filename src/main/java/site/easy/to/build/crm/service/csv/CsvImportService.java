package site.easy.to.build.crm.service.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CsvImportService {

    private final CsvMapper csvMapper = new CsvMapper();
    private final Validator validator;

    public <T> List<T> read(Class<T> clazz, InputStream inputStream, StringBuilder errorMessage) throws IOException {
        // Configure CSV mapper
        csvMapper.enable(CsvParser.Feature.TRIM_SPACES);
        csvMapper.enable(CsvParser.Feature.ALLOW_COMMENTS);
        CsvSchema csvSchema = csvMapper.schemaFor(clazz).withHeader().withColumnSeparator(',');
        // Parse CSV data
        MappingIterator<T> mappingIterator = csvMapper.readerFor(clazz).with(csvSchema).readValues(inputStream);
        List<T> result = mappingIterator.readAll();

        // Validate each row and collect errors
        for (int i = 0; i < result.size(); i++) {
            T item = result.get(i);
            Set<ConstraintViolation<T>> violations = validator.validate(item);

            if (!violations.isEmpty()) {
                String rowErrors = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(","));

                errorMessage.append(String.format("Row %d has validation errors: [%s]%n", i + 2, rowErrors));
            }
        }

        return result;
    }
}