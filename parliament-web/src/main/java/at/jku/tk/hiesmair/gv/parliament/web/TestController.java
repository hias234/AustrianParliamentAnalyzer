package at.jku.tk.hiesmair.gv.parliament.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.jku.tk.hiesmair.gv.parliament.db.result.AbsenceResult;

@RestController
@RequestMapping("asdf")
public class TestController {

	@RequestMapping("asdf")
	public AbsenceResult gr(){
		return new AbsenceResult("asdf", 10L, 20L);
	}
	
}
