package com.pan.spring.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pan.spring.dao.InterestedDao;
import com.pan.spring.dao.JobApplicationDao;
import com.pan.spring.dao.JobPostingDao;
import com.pan.spring.dao.JobSeekerDao;
import com.pan.spring.entity.JobApplication;
import com.pan.spring.mail.EmailServiceImpl;

@Controller
@RequestMapping(value = "/application")
public class JobApplicationController {
	@Autowired
	JobSeekerDao jobSeekerDao;
@Autowired
	EmailServiceImpl emailService;

	@Autowired
	JobPostingDao jobDao;

	@Autowired
	JobApplicationDao jobAppDao;

	@Autowired
	InterestedDao interestedDao;

	@PersistenceContext
	private EntityManager entityManager;

	private static String UPLOADED_FOLDER = "C:/";

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String applyPage(@RequestParam("userId") String jobSeekerId, @RequestParam("jobId") String jobId,
			Model model) {

		return "jobapplication";
	}

	/**
	 * @param jobSeekerId
	 * @param jobId
	 * @param resumeFlag
	 * @param resumePath
	 * @return The newly created application
	 */


	/**
	 * @param jobAppId
	 * @return True if the job application was cancelled
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String cancelApplication(@RequestParam("jobApplicationId") String jobAppId) {
		boolean deleted = jobAppDao.cancel(Integer.parseInt(jobAppId));
		if (deleted)
			return "Cancelled";
		return "Unable to delete";
	}

	/**
	 * @param jobAppId
	 * @param state
	 * @return true if the state has been modified
	 */
	@RequestMapping(value = "/modifyapplicationstate", method = RequestMethod.POST)
	public String modifyApplicationState(@RequestParam("jobAppId") String jobAppId,
			@RequestParam("state") String state) {
		JobApplication ja = jobAppDao.modifyJobApplicationStatus(Integer.parseInt(jobAppId), Integer.parseInt(state));
		if (ja == null) {
			return "Error";
		}
		return "modified";
	}

	// ***************************************************
	@RequestMapping("/viewResume")
	public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("Id") int id) throws IOException {
		JobApplication j1 = jobAppDao.getJobApplication(id);
		String path = j1.getResumePath();

		File file = new File(path);
		System.out.println(file);

		if (file.exists()) {
			System.out.println("File Found");
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			// Copy bytes from source to destination(outputstream in this
			// example), closes both streams.
			FileCopyUtils.copy(inputStream, response.getOutputStream());

		}

	}

	@PostMapping("/upload") // //new annotation since 4.3
	public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}

		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);
			System.out.println(path);

			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + file.getOriginalFilename() + "'");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/uploadStatus";
	}

	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}

	@RequestMapping(value = "/company/getAppliedJobs", method = RequestMethod.GET)
	public ResponseEntity<?> getAppliedJobs(@RequestParam("companyId") int id) {
		Query query = entityManager.createQuery("SELECT jobId FROM JobPosting jp WHERE jp.companyId = :id");
		query.setParameter("id", id);
		List<Integer> list = new ArrayList<Integer>();
		List<?> querylist = query.getResultList();
		for (Iterator<?> iterator = querylist.iterator(); iterator.hasNext();) {
			int uid = (int) (Integer) iterator.next();
			list.add(uid);
			System.out.println(uid);
		}

		return ResponseEntity.ok("data");
	}

	public List<Integer> getAppliedJobs(@RequestParam("jobSeekerId") String jobSeekerId) {
		List<?> jobSeekerAppliedList = jobSeekerDao.getJobSeeker(Integer.parseInt(jobSeekerId)).getJobApplicationList();
		List<Integer> jobIdList = new ArrayList<Integer>();
		for (Iterator iterator = jobSeekerAppliedList.iterator(); iterator.hasNext();) {
			JobApplication ja = (JobApplication) iterator.next();
			int jobId = ja.getJobPosting().getJobId();
			jobIdList.add(jobId);
		}
		return jobIdList;
	}

}
