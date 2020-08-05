package com.appointment.services.implementation;

import com.appointment.entity.Role;
import com.appointment.entity.Student;
import com.appointment.repositories.RoleRepository;
import com.appointment.repositories.StudentRepository;
import com.appointment.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Student register(Student student) {
        Role role = roleRepository.findByName("ROLE_USER");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setRoles(userRoles);


        Student registeredStudent = studentRepository.save(student);


        return registeredStudent;
    }

    @Override
    public List<Student> getAll() {
        List<Student> result = studentRepository.findAll();
        return result;
    }

    @Override
    public Student findByUsername(String username) {
        Student result = studentRepository.findByUsername(username);
        return result;
    }

    @Override
    public Student findById(Long id) {
        Student result =studentRepository.findById(id).orElse(null);

        if (result == null) {
            return null;
        }

        return result;
    }

    @Override
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }
}