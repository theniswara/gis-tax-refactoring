/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.psdku.lmj.university_backend.service;

import com.psdku.lmj.university_backend.model.Course;
import com.psdku.lmj.university_backend.model.Student;
import com.psdku.lmj.university_backend.repository.CourseRepository;
import com.psdku.lmj.university_backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Course Service - Business logic for course operations
 * 
 * Features:
 * - Get student's current courses
 * - Enroll student in course
 * - Drop course
 * - Update course grade/progress
 */
@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * Get all courses for a specific student
     */
    public List<Course> getStudentCourses(Long studentId) {
        return courseRepository.findByStudentId(studentId);
    }
    
    /**
     * Get all courses for a student by student ID string
     */
    public List<Course> getStudentCoursesByStudentId(String studentId) {
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        if (student.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        return courseRepository.findByStudent(student.get());
    }
    
    /**
     * Get course by ID
     */
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }
    
    /**
     * Get all courses (admin only)
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    /**
     * Enroll student in a course
     */
    @Transactional
    public Course enrollStudentInCourse(Long studentId, Course courseData) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        Course course = new Course();
        course.setCode(courseData.getCode());
        course.setName(courseData.getName());
        course.setInstructor(courseData.getInstructor());
        course.setCredits(courseData.getCredits());
        course.setSchedule(courseData.getSchedule());
        course.setRoom(courseData.getRoom());
        course.setGrade("-"); // No grade yet
        course.setProgress(0); // Just started
        course.setStudent(student);
        
        return courseRepository.save(course);
    }
    
    /**
     * Update course information (admin can update any, student can update own)
     */
    @Transactional
    public Course updateCourse(Long courseId, Course courseData) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        
        if (courseData.getName() != null) {
            course.setName(courseData.getName());
        }
        if (courseData.getInstructor() != null) {
            course.setInstructor(courseData.getInstructor());
        }
        if (courseData.getGrade() != null) {
            course.setGrade(courseData.getGrade());
        }
        if (courseData.getProgress() != null) {
            course.setProgress(courseData.getProgress());
        }
        if (courseData.getSchedule() != null) {
            course.setSchedule(courseData.getSchedule());
        }
        if (courseData.getRoom() != null) {
            course.setRoom(courseData.getRoom());
        }
        if (courseData.getCredits() != null) {
            course.setCredits(courseData.getCredits());
        }
        
        return courseRepository.save(course);
    }
    
    /**
     * Drop a course
     */
    @Transactional
    public void dropCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(courseId);
    }
    
    /**
     * Get course statistics for a student
     */
    public CourseStatistics getStudentCourseStatistics(Long studentId) {
        List<Course> courses = courseRepository.findByStudentId(studentId);
        
        int totalCourses = courses.size();
        int totalCredits = courses.stream()
            .mapToInt(c -> c.getCredits() != null ? c.getCredits() : 0)
            .sum();
        
        double averageProgress = courses.stream()
            .mapToInt(c -> c.getProgress() != null ? c.getProgress() : 0)
            .average()
            .orElse(0.0);
        
        long completedCourses = courses.stream()
            .filter(c -> c.getProgress() != null && c.getProgress() >= 100)
            .count();
        
        return new CourseStatistics(totalCourses, totalCredits, averageProgress, completedCourses);
    }
    
    /**
     * Inner class for course statistics
     */
    public static class CourseStatistics {
        private int totalCourses;
        private int totalCredits;
        private double averageProgress;
        private long completedCourses;
        
        public CourseStatistics(int totalCourses, int totalCredits, double averageProgress, long completedCourses) {
            this.totalCourses = totalCourses;
            this.totalCredits = totalCredits;
            this.averageProgress = averageProgress;
            this.completedCourses = completedCourses;
        }
        
        public int getTotalCourses() {
            return totalCourses;
        }
        
        public int getTotalCredits() {
            return totalCredits;
        }
        
        public double getAverageProgress() {
            return averageProgress;
        }
        
        public long getCompletedCourses() {
            return completedCourses;
        }
    }
}
