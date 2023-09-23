package com.cst438.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentDTO;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@RestController
@CrossOrigin 
public class AssignmentController {
	
	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	Assignment assignment;
	
	@GetMapping("/assignment")
	public AssignmentDTO[] getAllAssignmentsForInstructor() {
		// get all assignments for this instructor
		String instructorEmail = "dwisneski@csumb.edu";  // user name (should be instructor's email) 
		List<Assignment> assignments = assignmentRepository.findByEmail(instructorEmail);
		AssignmentDTO[] result = new AssignmentDTO[assignments.size()];
		for (int i=0; i<assignments.size(); i++) {
			Assignment as = assignments.get(i);
			AssignmentDTO dto = new AssignmentDTO(
					as.getId(), 
					as.getName(), 
					as.getDueDate().toString(), 
					as.getCourse().getTitle(), 
					as.getCourse().getCourse_id());
			result[i]=dto;
		}
		return result;
	}
	
	// TODO create CRUD methods for Assignment
	
	// create a new assignment
	@PostMapping("/assignment")
	public int createAssignment(@RequestBody AssignmentDTO assignmentDTO) {
		String instructorEmail = "dwisneski@csumb.edu";  // user name (should be instructor's email) 
		 
			Course c  = courseRepository.findById(assignmentDTO.courseId()).orElse(null);
			if(!c.getInstructor().equals(instructorEmail) || c == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already Exist");
			}
			
			Assignment assignment = new Assignment();
			assignment.setCourse(c);
			assignment.setDueDate(java.sql.Date.valueOf(assignmentDTO.dueDate()));
			assignment.setName(assignmentDTO.assignmentName());
			assignmentRepository.save(assignment);
		return assignment.getId();
		
	}
	
	// retrieve the one assignment
	@GetMapping("assignment/{id}")
	public AssignmentDTO getAssignment(@PathVariable("id") int id) {
		Assignment assignment = assignmentRepository.findById(id).orElse(null);
		String instructorEmail = "dwisneski@csumb.edu";  // user name (should be instructor's email) 
		
		if( assignment == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND");	
		}
		
		if(assignment.getCourse().getInstructor().equals(instructorEmail)) {
			AssignmentDTO aDTO = new AssignmentDTO(assignment.getId(), assignment.getName(), assignment.getDueDate().toString(), assignment.getCourse().getTitle(), assignment.getCourse().getCourse_id());
		return aDTO;
		
		}
		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "FORBIDDEN");
	}
	
	
		
	
	// delete an assignment
	//  DELETE  /course/12389
	//  DELETE  /course/12389?force=yes

	@DeleteMapping("/assignment/{id}")
	public void deleteAssignment(@PathVariable("id") int id,  
			@RequestParam("force") Optional<String> force) {
	// delete code here.
		String instructorEmail = "dwisneski@csumb.edu";  // user name (should be instructor's email)
		// if assignment exists
		Assignment assignment = assignmentRepository.findById(id).orElse(null);
		if(assignment == null) {
			return;
		}
		
		if(!assignment.getCourse().getInstructor().equals(instructorEmail)) {

			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "FORBIDDEN");
		
		}
		
		if(assignment.getAssignmentGrades().isEmpty()) {
			assignmentRepository.deleteById(id);
		} 
		
		else {
			// 
			if(force.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no force");
			}
			else 
				assignmentRepository.deleteById(id);
			
		}
		
		assignmentRepository.deleteById(id);
		
		
	}
	
	// update assignment - combine post and get
	@PutMapping("/assignment/update/{id}")
	public void updateCourse(@RequestBody AssignmentDTO assignmentDTO, @PathVariable("id") int id) {
	//implement update here
		String instructorEmail = "dwisneski@csumb.edu";  // user name (should be instructor's email) 
		Assignment a = assignmentRepository.findById(id).orElse(null);
		if(a == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NOT_FOUND");
		}
		
		if(!assignment.getCourse().getInstructor().equals(instructorEmail)) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NOT_FOUND");
		
		}
		
		assignment.setDueDate(java.sql.Date.valueOf(assignmentDTO.dueDate()));
		assignment.setName(assignmentDTO.assignmentName());
		assignmentRepository.save(assignment);
		
	}
	

	
}
