package com.cst438.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.FinalGradeDTO;
import com.cst438.domain.Assignment;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Enrollment;

@Service
@ConditionalOnProperty(prefix = "registration", name = "service", havingValue = "rest")
@RestController
public class RegistrationServiceREST implements RegistrationService {

	
	RestTemplate restTemplate = new RestTemplate();
	
	@Value("${registration.url}") 
	String registration_url;
	
	public RegistrationServiceREST() {
		System.out.println("REST registration service ");
	}
	
	@Override
	public void sendFinalGrades(int course_id , FinalGradeDTO[] grades) { 
		
		//TODO use restTemplate to send final grades to registration service
		// Throw exceptions
//		String studentEmail = FinalGradeDTO.studentEmail();
//		Course c = courseRepository.findById(course_id).orElse(null);
//	    if (c==null || ! c.getClass().equals(studentEmail)) {
//	    	throw  new ResponseStatusException( HttpStatus.NOT_FOUND, "assignment not found or not authorized "+id);
//	    }
//	    
//	    c.setCourse_id(course_id);
//	    c.setTitle(studentEmail);
//	    courseRepository.save(c);
	    
		// create the url
		registration_url = "/course/{course_id}/finalgrades";
		String URL = registration_url.replace("{course_id}", String.valueOf(course_id));
		// put request for url
		restTemplate.put(URL, grades);
	
	}
	
	@Autowired
	CourseRepository courseRepository;

	@Autowired
	EnrollmentRepository enrollmentRepository;

	
	/*
	 * endpoint used by registration service to add an enrollment to an existing
	 * course.
	 */
	@PostMapping("/enrollment")
	@Transactional
	public EnrollmentDTO addEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
		
		// Receive message from registration service to enroll a student into a course.
		
		System.out.println("GradeBook addEnrollment "+enrollmentDTO);
		
		// Throw Exceptions
//		String studentEmail = enrollmentDTO.studentEmail();
//		Course c = courseRepository.findById(enrollmentDTO.courseId()).orElse(null);
//		if (c==null || ! c.getInstructor().equals(studentEmail)) {
//			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "course id not found or not authorized "+enrollmentDTO.courseId());
//		}
		
		//TODO remove following statement when complete.
		registration_url = "/enrollment";
		
		String URL = registration_url;
		
		EnrollmentDTO response = restTemplate.postForObject(URL, enrollmentDTO,EnrollmentDTO.class);
		
		return response;
		
	}

}
