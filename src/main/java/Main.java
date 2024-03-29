import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class Main {

    private static final String[] genders = {"М", "Ж"};
    private static final String[] empTypes = {"ШТАТ", "ВНС"};
    private static final String[] postTypes = {"ОСН", "СОВМ"};
    private static final String[] academicDegrees = {"дн", "кн", "", "", "", "", ""};
    private static final String[] educationTypes = {"ВЫСШЕЕ", "ВЫСШЕЕ (СПЕЦИАЛИСТ)", "ВЫСШЕЕ (МАГИСТР)"};

    //    private static final List<String[]> lastname = readCsv("sample_lastname.csv");
//    private static final List<String[]> firstname = readCsv("sample_firstname.csv");
//    private static final List<String[]> midname = readCsv("sample_midname.csv");
//    private static final List<String[]> depts = readCsv("sample_depts.csv");
//    private static final List<String[]> postname = readCsv("sample_postname.csv");
    private static final CsvDocument lastname = readCsv("sample_lastname.csv");
    private static final CsvDocument firstname = readCsv("sample_firstname.csv");
    private static final CsvDocument midname = readCsv("sample_midname.csv");
    private static final CsvDocument depts = readCsv("sample_depts.csv");
    private static final CsvDocument postname = readCsv("sample_postname.csv");
    //used to check for the uniqueness of the generated orgPostId
    private static Set<Integer> orgPostIds = new HashSet<>();

    public static void main(String[] args) throws Exception {
        int numberOfRecords;
        int startPersonId = 0;

        if (args.length > 0) {
            numberOfRecords = Integer.parseInt(args[0]);
            if (args.length > 1)
                startPersonId = Integer.parseInt(args[1]);
        } else
            throw new Exception("Specify the number of records to be generated (1-100000)");

        if (numberOfRecords > 0 && numberOfRecords <= 100000)
            write(generate(numberOfRecords, startPersonId));
        else
            throw new Exception("Specify the number of records to be generated (1-100000)");
    }

    private static CsvDocument readCsv(String fileName) {
        List<String[]> content = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();
        try (CSVParser parser = new CSVParser(new FileReader(fileName, StandardCharsets.UTF_8), format)) {
            headers = new ArrayList<>(parser.getHeaderNames());
            ;
            headers.set(0, headers.get(0).replaceAll("[\\p{Cf}]", ""));
            for (CSVRecord record : parser) {
                String[] row = new String[record.size()];
                for (int i = 0; i < record.size(); i++) {
                    row[i] = record.get(i);
                }
                content.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CsvDocument(fileName, headers, content);
    }

    private static List<Employee> generate(int numberOfRecords, int startPersonId) {
        List<Employee> emps = new ArrayList<>();

        Random rand = new Random();
        String gender;
        String academicDegree;
        for (int i = 0; i < numberOfRecords; i++) {
            gender = genders[(int) (Math.random() * 2)];
            academicDegree = academicDegrees[(int) (Math.random() * 7)];
            emps.add(i, new Employee(
                    startPersonId++,
                    getRandomName(lastname, gender, "lastname"),
                    getRandomName(firstname, gender, "firstname"),
                    getRandomName(midname, gender, "midname"),
                    getRandomDate(),
                    gender,
                    getRandomEntry(depts, "DEPT_ID"),
                    getRandomOrgPostId(),
                    empTypes[(int) (Math.random() * 2)],
                    postTypes[(int) (Math.random() * 2)],
                    getRandomEntry(postname, "postName"),
                    academicDegree,
                    getAcademicRank(academicDegree),
                    educationTypes[(int) (Math.random() * 3)]));
            //with a probability of 10%, a person holds two positions
            if (rand.nextInt(10) == 0) {
                emps.add(i++, getPersonWithDifferentPosition(emps.get(i - 1)));
                numberOfRecords++;
            }
        }
        return emps;
    }

    private static void write(List<Employee> list) {
        try (
                Writer writer = Files.newBufferedWriter(Paths.get("generated_emps.csv"), StandardCharsets.UTF_8);
                CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.builder().setHeader(
                        '\ufeff' + "orgPersonId", "lastName", "firstName", "midName", "birthDate", "gender", "orgDeptId",
                        "orgPostId", "empType", "postType", "postName", "academicDegree", "academicRank", "educationType").build())
        ) {
            for (Employee emp : list) {
                printer.printRecord(emp.getList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getRandomEntry(CsvDocument csv, String headerName) {
        int index = getHeaderIndex(csv, headerName);
        return csv.getContent().get((int) (Math.random() * csv.getContent().size()))[index];
    }

    private static String getRandomName(CsvDocument csv, String gender, String headerName) {
        String[] name = {"", ""};
        while (!name[getHeaderIndex(csv, "gender")].equals(gender)) {
            name = csv.getContent().get((int) (Math.random() * csv.getContent().size()));
        }
        return name[getHeaderIndex(csv, headerName)];
    }

    private static int getHeaderIndex(CsvDocument csv, String headerName) {
        return csv.getHeaderNames().indexOf(headerName);
    }

    private static String getRandomDate() {
        Random random = new Random();
        int minDay = (int) LocalDate.of(1960, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(1995, 1, 1).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);
        return LocalDate.ofEpochDay(randomDay).toString();
    }

    private static String getAcademicRank(String academicDegree) {
        if (academicDegree.equals("кн"))
            return "доцент";
        if (academicDegree.equals("дн"))
            return "профессор";
        return "";
    }

    private static String getRandomOrgPostId() {
        int id = (int) (Math.random() * (32000000 - 1000)) + 1000;
        while (!orgPostIds.add(id)) {
            id = (int) (Math.random() * (32000000 - 1000)) + 1000;
        }
        return String.valueOf(id);
    }

    private static Employee getPersonWithDifferentPosition(Employee emp) {
        return new Employee(
                Integer.parseInt(emp.getOrgPersonId()),
                emp.getLastName(),
                emp.getFirstName(),
                emp.getMidName(),
                emp.getBirthDate(),
                emp.getGender(),
                getRandomEntry(depts, "DEPT_ID"),
                getRandomOrgPostId(),
                emp.getEmpType(),
                emp.getPostType(),
                getRandomEntry(postname, "postName"),
                emp.getAcademicDegree(),
                emp.getAcademicRank(),
                emp.getEducationType());
    }
}
