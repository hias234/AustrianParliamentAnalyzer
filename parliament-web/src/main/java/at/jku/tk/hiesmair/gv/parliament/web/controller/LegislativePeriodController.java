package at.jku.tk.hiesmair.gv.parliament.web.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.jku.tk.hiesmair.gv.parliament.web.service.LegislativePeriodService;

@RestController
@RequestMapping("period")
public class LegislativePeriodController {

	@Inject
	private LegislativePeriodService periodService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public List<Integer> findAll() {
		return StreamSupport.stream(periodService.findAll().spliterator(), false).map(p -> p.getPeriod())
				.collect(Collectors.toList());
	}

}
