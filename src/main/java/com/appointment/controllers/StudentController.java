package com.appointment.controllers;

import com.appointment.entity.Contract;
import com.appointment.entity.Lesson;
import com.appointment.entity.Student;

import com.appointment.services.implementation.ContractServiceImpl;
import com.appointment.services.implementation.EmailServiceImpl;
import com.appointment.services.implementation.LessonServiceImpl;
import com.appointment.services.implementation.StudentServiceImpl;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private LessonServiceImpl lessonService;

    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private ContractServiceImpl contractService;

    @Autowired
    private EmailServiceImpl emailService;

    @Value("${mail.bookingText}")
    private String bookingText;

    @Value("${mail.bookingText}")
    private String declineText;


    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/lessons")
    public ResponseEntity<?> getAllLessons(){
        List<Lesson> lessons =lessonService.getAll();
        return ResponseEntity.ok("Lessons: "+ lessons.toString());
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/book/{id}")
    public ResponseEntity<?> bookLesson (@PathVariable("id") Long id) throws NotFoundException, MessagingException {

        Student student = studentService.getCurrentStudent();
        Lesson lesson = lessonService.findById(id);
        Contract newContract  = contractService.createContract(student, lesson);
        //email to teacher for approve or decline contract
        emailService.sendMail(lesson.getTeacher().getUser().getEmail(),"Online University", bookingText );
        //email to student for decline contract, if he want
        emailService.sendMail(student.getUser().getEmail(), "Online University", declineText);

        return ResponseEntity.ok("Booking lesson " +id + " is successful");
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/refuse/{id}")
    public ResponseEntity<?> refuseLesson (@PathVariable("id") Long id) {
        Student student = studentService.getCurrentStudent();
        Contract contract = contractService.findById(id);
        contractService.delete(contract);
            return ResponseEntity.ok("Booking lesson is successful decline");



    }

}
