package com.yourSystem.project;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.io.IOException;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course updateCourse(Long id, Course course) {
        Course existingCourse = courseRepository.findById(id).orElse(null);
        if (existingCourse == null) {
            return null;
        } else {
            course.setId(id);
            return courseRepository.save(course);
        }
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public Workbook exportToExcel() throws IOException {
        List<Course> courses = courseRepository.findAll();

        // Create a new Excel workbook
        Workbook workbook = new XSSFWorkbook();

        // Create a new sheet
        Sheet sheet = workbook.createSheet("Courses");

        // Create a row for the table header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Content");
        headerRow.createCell(3).setCellValue("Maximum number of participants");
        headerRow.createCell(4).setCellValue("URL");

        // Iterate over all the courses and create a row for each of them
        int rowNum = 1;
        for (Course course : courses) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(course.getId());
            row.createCell(1).setCellValue(course.getName());
            row.createCell(2).setCellValue(course.getContent());
            row.createCell(3).setCellValue(course.getMaxNumberOfParticipants());
            row.createCell(4).setCellValue(course.getUrl());
        }

        // Write the workbook to an Excel file
        FileOutputStream fileOut = new FileOutputStream("courses.xlsx");
        workbook.write(fileOut);
        fileOut.close();
        return workbook;
    }
}

