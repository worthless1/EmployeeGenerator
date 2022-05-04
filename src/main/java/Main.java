import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static final String[] genders = {"М", "Ж"};
    private static final String[] empTypes = {"ШТАТ", "ВНС"};
    private static final String[] postTypes = {"ОСН", "СОВМ"};
    private static final String[] academicDegrees = {"дн", "кн", "", "", "", "", ""};
    private static final String[] academicRanks = {"доцент", "профессор", ""};
    private static final String[] educationTypes = {"ВЫСШЕЕ", "ВЫСШЕЕ (СПЕЦИАЛИСТ)", "ВЫСШЕЕ (МАГИСТР)"};

    private static final List<String[]> lastname = readCsv("sample_lastname.csv");
    private static final List<String[]> firstname = readCsv("sample_firstname.csv");
    private static final List<String[]> midname = readCsv("sample_midname.csv");
    private static final List<String[]> depts = readCsv("sample_depts.csv");
    private static final List<String[]> postname = readCsv("sample_postname.csv");

    public static void main(String[] args) throws Exception {
        int numberOfRecords;

        if (args.length > 0)
            numberOfRecords = Integer.parseInt(args[0]);
        else
            throw new Exception("Specify the number of records to be generated (1-100000)");

        if (numberOfRecords > 0 && numberOfRecords <= 100000)
            write(generate(numberOfRecords));
        else
            throw new Exception("Specify the number of records to be generated (1-100000)");
    }

    private static List<String[]> readCsv(String fileName) {
        List<String[]> list = new ArrayList<>();
        try (CSVParser parser = new CSVParser(new FileReader(fileName), CSVFormat.DEFAULT)) {
            for (CSVRecord record : parser) {
                String[] row = new String[record.size()];
                for (int i = 0; i < record.size(); i++) {
                    row[i] = record.get(i);
                }
                list.add(row);
            }
            //parsing
//            parser.getRecords().forEach(record -> list.add(record.toList().toArray(new String[record.size()])));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static List<Employee> generate(int numberOfRecords) {
        List<Employee> emps = new ArrayList<>();
        String gender;
        String academicDegree;
        for (int i = 0; i < numberOfRecords; i++) {
            gender = genders[(int) (Math.random() * 2)];
            academicDegree = academicDegrees[(int) (Math.random() * 7)];
            emps.add(i, new Employee(
                    getRandomName(lastname, gender),
                    getRandomName(firstname, gender),
                    getRandomName(midname, gender),
                    getRandomDate(),
                    gender,
                    getRandomEntry(depts, 0),
                    empTypes[(int) (Math.random() * 2)],
                    postTypes[(int) (Math.random() * 2)],
                    getRandomEntry(postname, 0),
                    academicDegree,
                    getAcademicRank(academicDegree),
                    educationTypes[(int) (Math.random() * 3)]));
        }
        return emps;
    }

    private static void write(List<Employee> list) {
        try (
                Writer writer = Files.newBufferedWriter(Paths.get("generated_emps.csv"), StandardCharsets.UTF_8);
                CSVPrinter printer = new CSVPrinter(writer, CSVFormat.EXCEL.builder().setHeader(
                        '\ufeff' +"orgPersonId", "lastName", "firstName", "midName", "birthDate", "gender", "orgDeptId",
                        "orgPostId", "empType", "postType", "postName", "academicDegree", "academicRank", "educationType").build())
        ) {
            for (Employee emp : list) {
                printer.printRecord(emp.getList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getRandomEntry(List<String[]> list, int number) {
        return list.get((int) (Math.random() * list.size()))[number];
    }

    private static String getRandomName(List<String[]> list, String gender) {
        String[] name = {"", ""};
        while (!name[1].equals(gender)) {
            name = list.get((int) (Math.random() * list.size()));
        }
        return name[0];
    }

    //    private static String getRandomName(List<String[]> list, String gender) {
//        List<String[]> names = list.parallelStream().filter(row -> row[1].equals(gender)).collect(Collectors.toList());
//        return names.get((int) (Math.random() * names.size()))[0];
//    }

    private static String getRandomDate() {
        Random random = new Random();
        int minDay = (int) LocalDate.of(1960, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(1995, 1, 1).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);

        return LocalDate.ofEpochDay(randomDay).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private static String getAcademicRank(String academicDegree) {
        if (academicDegree.equals("кн"))
            return "доцент";
        if (academicDegree.equals("дн"))
            return "профессор";
        return "";
    }

}
