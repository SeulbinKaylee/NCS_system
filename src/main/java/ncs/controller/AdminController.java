package ncs.controller;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ncs.dto.AdminDormantMemberDTO;
import ncs.dto.AdminNcsTestDTO;
import ncs.dto.AdminNewMemberDTO;
import ncs.dto.DefaultCourseDTO;
import ncs.dto.InStudentListDTO;
import ncs.service.AdminService;
import ncs.service.InstitutionService;

@Controller
@RequestMapping("/admin/*")
public class AdminController {
	@Inject
	private AdminService as;
	@Autowired
	private JavaMailSender mailSender;
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@RequestMapping(value="/adminHome", method = RequestMethod.GET)
	public void adminHomeGet(Model model)  throws Exception{
	}
	
	@RequestMapping(value="/adminHome", method = RequestMethod.POST)
	public void adminHome(/*@RequestParam("is2ndAtuthrized") String is2ndAtuthrized, */Model model)  throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		String userid = null;
		Collection<? extends GrantedAuthority> userauthority = null;
		String userauth = null;
		if(principal != null) {
			userid = auth.getName();
			userauthority = auth.getAuthorities();
			userauth = userauthority.toString();
		}
		model.addAttribute("userid", userid);
		model.addAttribute("userauth", userauth);
		logger.info("adminHome");
		
//		model.addAttribute("is2ndAtuthrized", is2ndAtuthrized);		
	}
	
	//2??? ??????
	@RequestMapping(value="/admin2ndAuth", method = RequestMethod.GET)
	public void admin2ndAuth(Model model)  throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		String userid = null;
		Collection<? extends GrantedAuthority> userauthority = null;
		String userauth = null;
		if(principal != null) {
			userid = auth.getName();
			userauthority = auth.getAuthorities();
			userauth = userauthority.toString();
		}
		model.addAttribute("userid", userid);
		model.addAttribute("userauth", userauth);
		logger.info("admin2ndAuth");
	}
	// ????????? ??????
	@RequestMapping(value = "/mailCheck", method = RequestMethod.GET)
	@ResponseBody
	public String mailCheckGET(@RequestParam("email") String email) throws Exception {

		/* ???(View)????????? ????????? ????????? ?????? */
		logger.info("????????? ????????? ?????? ??????");
		logger.info("???????????? : " + email);
		/* ????????????(??????) ?????? */
		Random random = new Random();
		int checkNum = random.nextInt(888888) + 111111;
		logger.info("???????????? " + checkNum);
		/* ????????? ????????? */
		String setFrom = "qls5170@naver.com";
		String toMail = email;
		String title = "????????? ????????? 2????????? ????????? ?????????.";
		String content = "LUCKY SEVEN NCS????????????????????? ????????? ???????????? ?????? 2???????????? ?????????????????????.(???????????????)" + "<br><br>" + "?????? ????????? " + checkNum + "?????????."
				+ "<br>" + "?????? ??????????????? ???????????? ???????????? ???????????? ?????????.";

		try {

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(setFrom);
			helper.setTo(toMail);
			helper.setSubject(title);
			helper.setText(content, true);
			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
		String num = Integer.toString(checkNum);
		return num;
	}
	// ????????? ?????? ?????? ????????????
	@RequestMapping(value = "/emailCheck", method = RequestMethod.GET)
	@ResponseBody
	public int emailCheck(@RequestParam("email") String email) throws Exception {

		return as.adminemailCheck(email);
	}
	
	//?????? ??????
	@RequestMapping(value="/adminNewMember", method = RequestMethod.GET)
	public String adminNewMember(AdminNewMemberDTO anm,Model model)  throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		String userid = null;
		Collection<? extends GrantedAuthority> userauthority = null;
		String userauth = null;
		if(principal != null) {
			userid = auth.getName();
			userauthority = auth.getAuthorities();
			userauth = userauthority.toString();
		}
		model.addAttribute("userid", userid);
		model.addAttribute("userauth", userauth);	
			
		model.addAttribute("new_member",as.newMember_select(anm));
		return "/admin/adminNewMember";
	}
	// ????????? ?????? ??????
	@RequestMapping(value="/newMember_updete", method = RequestMethod.GET)
	public String NewMemberUpdate(@RequestParam("userid")String userid,Model model)  throws Exception{
		as.newMember_update(userid);
		return "redirect:/admin/adminNewMember";
	}
	// ????????? ?????? ??????
	@RequestMapping(value="/newMember_delete", method = RequestMethod.GET)
	public String NewMemberDelete(@RequestParam("userid")String userid,Model model)  throws Exception{
		as.newMember_delete2(userid);
		as.newMember_delete(userid);
		return "redirect:/admin/adminNewMember";
	}
	// ????????? ?????? ??????
	@RequestMapping(value="/newMember_search", method = RequestMethod.GET)
	public String adminnewMembersearch(@RequestParam("search")String search,Model model)  throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		String userid = null;
		Collection<? extends GrantedAuthority> userauthority = null;
		String userauth = null;
		if(principal != null) {
			userid = auth.getName();
			userauthority = auth.getAuthorities();
			userauth = userauthority.toString();
		}
		model.addAttribute("userid", userid);
		model.addAttribute("userauth", userauth);
		
		List<AdminNewMemberDTO> dtos = as.newMember_search(search);
		model.addAttribute("new_member",dtos);
		return"/admin/adminNewMember";
	}
	
	//?????? ??????
	@RequestMapping(value="/adminDormantMember", method = RequestMethod.GET)
	public String adminDormantMember(AdminDormantMemberDTO adm,Model model)  throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		String userid = null;
		Collection<? extends GrantedAuthority> userauthority = null;
		String userauth = null;
		if(principal != null) {
			userid = auth.getName();
			userauthority = auth.getAuthorities();
			userauth = userauthority.toString();
		}
		model.addAttribute("userid", userid);
		model.addAttribute("userauth", userauth);	
		
		
		model.addAttribute("dormant_member",as.dormant_member(adm));
		return "/admin/adminDormantMember";
	}
	//?????? ?????? ????????????
	@RequestMapping(value="/dormant_update", method = RequestMethod.GET)
	public String DormantMemberUpdate(@RequestParam("userid")String userid, Model model)  throws Exception{
		as.dormant_update(userid);
		return "redirect:/admin/adminDormantMember";
	}
	//?????? ?????? ?????????
	@RequestMapping(value="/dormant_update2", method = RequestMethod.GET)
	public String DormantMemberUpdate2(@RequestParam("userid")String userid,Model model)  throws Exception{
		as.dormant_update2(userid);
		return "redirect:/admin/adminDormantMember";
	}
	//?????? ?????? ??????
	@RequestMapping(value="/dormant_search", method = RequestMethod.GET)
	public String admindormantsearch(@RequestParam("search")String search,Model model)  throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		String userid = null;
		Collection<? extends GrantedAuthority> userauthority = null;
		String userauth = null;
		if(principal != null) {
			userid = auth.getName();
			userauthority = auth.getAuthorities();
			userauth = userauthority.toString();
		}
		model.addAttribute("userid", userid);
		model.addAttribute("userauth", userauth);
		
		List<AdminDormantMemberDTO> dtos = as.dormant_search(search);
		model.addAttribute("dormant_member",dtos);
		return"/admin/adminDormantMember";
	}
	
	//?????? ????????? ??????
	@RequestMapping(value="/adminNcsTest", method = RequestMethod.GET)
	public String adminNcsTest(AdminNcsTestDTO ant,Model model)  throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		String userid = null;
		Collection<? extends GrantedAuthority> userauthority = null;
		String userauth = null;
		if(principal != null) {
			userid = auth.getName();
			userauthority = auth.getAuthorities();
			userauth = userauthority.toString();
		}
		model.addAttribute("userid", userid);
		model.addAttribute("userauth", userauth);	
		
		
		model.addAttribute("ncstest",as.ncstest(ant));
		return "/admin/adminNcsTest";
	}
	@RequestMapping(value="/ncstest_search", method = RequestMethod.GET)
	public String adminNcsTestsearch(@RequestParam("search")String search,Model model)  throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		String userid = null;
		Collection<? extends GrantedAuthority> userauthority = null;
		String userauth = null;
		if(principal != null) {
			userid = auth.getName();
			userauthority = auth.getAuthorities();
			userauth = userauthority.toString();
		}
		model.addAttribute("userid", userid);
		model.addAttribute("userauth", userauth);
		
		List<AdminNcsTestDTO> dtos = as.ncsTest_search(search);
		model.addAttribute("ncstest",dtos);
		return "/admin/adminNcsTest";
	}
	//????????? ?????????
	@RequestMapping(value="/adminTestPage", method = RequestMethod.GET)
	public String adminTestPage(Model model)  throws Exception{
		return "/admin/adminTestPage";
	}
}
