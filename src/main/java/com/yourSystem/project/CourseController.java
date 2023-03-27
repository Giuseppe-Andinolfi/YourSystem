package com.yourSystem.project;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCourse(@RequestBody Course course) {
        courseService.createCourse(course);
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return course;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return courses;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCourse(@PathVariable Long id, @RequestBody Course course) {
        courseService.updateCourse(id, course);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    public Workbook exportToExcel(String filePath) throws IOException {
        Workbook workbook = courseService.exportToExcel();
        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        outputStream.close();
        return workbook;
    }
    @GetMapping("/export/excel")
    @ResponseStatus(HttpStatus.OK)
    public ByteArrayResource exportToExcel() throws IOException {
        String filePath = "courses.xlsx";
        Workbook workbook = exportToExcel(filePath);
        File file = new File(filePath);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("attachment", "courses.xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ByteArrayResource(fileContent);
    }
}
