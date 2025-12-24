/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.psdku.lmj.university_backend.util;

import com.psdku.lmj.university_backend.model.Course;
import com.psdku.lmj.university_backend.model.Student;
import com.psdku.lmj.university_backend.repository.CourseRepository;
import com.psdku.lmj.university_backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Course Data Initializer
 * 
 * Automatically creates sample courses for test student on application startup
 */
@Component
@Order(3)  // Run after AdminInitializer and StudentInitializer
public class CourseInitializer implements CommandLineRunner {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Override
    public void run(String... args) {
        createSampleCoursesIfNotExists();
    }
    
    private void createSampleCoursesIfNotExists() {
        String testStudentId = "STU0000000001";
        
        // Find the test student
        Optional<Student> studentOpt = studentRepository.findByStudentId(testStudentId);
        
        if (studentOpt.isEmpty()) {
            System.out.println("ℹ️  Test student not found - skipping course creation");
            return;
        }
        
        Student student = studentOpt.get();
        
        // Check if courses already exist for this student
        long existingCourses = courseRepository.countByStudentId(student.getId());
        
        if (existingCourses > 0) {
            System.out.println("ℹ️  Sample courses already exist - skipping creation");
            return;
        }
        
        System.out.println("=================================");
        System.out.println("Creating sample courses...");
        System.out.println("=================================");
        
        // Create sample courses
        createCourse(student, "CS101", "Introduction to Programming", "Dr. Smith", 4, "Mon/Wed 9:00-10:30", "Room 201", "A-", 85);
        createCourse(student, "CS201", "Data Structures", "Prof. Johnson", 4, "Tue/Thu 11:00-12:30", "Room 305", "B+", 65);
        createCourse(student, "MATH301", "Discrete Mathematics", "Dr. Williams", 3, "Mon/Wed 14:00-15:30", "Room 102", "A", 90);
        createCourse(student, "CS301", "Database Systems", "Prof. Brown", 3, "Tue/Thu 14:00-15:30", "Room 401", "In Progress", 45);
        createCourse(student, "CS350", "Software Engineering", "Dr. Davis", 3, "Wed/Fri 10:00-11:30", "Room 303", "In Progress", 30);
        
        System.out.println("✅ Sample courses created for student: " + testStudentId);
        System.out.println("   Total courses: 5");
        System.out.println("=================================");
    }
    
    private void createCourse(Student student, String code, String name, String instructor, 
                            int credits, String schedule, String room, String grade, int progress) {
        Course course = new Course();
        course.setCode(code);
        course.setName(name);
        course.setInstructor(instructor);
        course.setCredits(credits);
        course.setSchedule(schedule);
        course.setRoom(room);
        course.setGrade(grade);
        course.setProgress(progress);
        course.setStudent(student);
        
        courseRepository.save(course);
    }
}
