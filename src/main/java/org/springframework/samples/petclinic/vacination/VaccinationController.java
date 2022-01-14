package org.springframework.samples.petclinic.vacination;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.pet.PetService;
import org.springframework.samples.petclinic.pet.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VaccinationController {
	
	@Autowired
	VaccinationService vs;
	@Autowired
	PetService ps;
	
	@GetMapping(path = "/vaccination/create")
	public String initCreationForm(ModelMap modelMap) {
		Vaccination vaccination = new Vaccination();
		modelMap.addAttribute("vaccination", vaccination);
		modelMap.addAttribute("vaccines", vs.getAllVaccines());
		modelMap.addAttribute("pets",ps.findAllPets());
		return "vaccination/createOrUpdateVaccinationForm";
	}
	
	@PostMapping(path = "/vaccination/create")
	public String processCreationForm(@Valid Vaccination vaccination, BindingResult result, ModelMap modelMap) throws UnfeasibleVaccinationException {
		if (result.hasErrors()) {
			modelMap.addAttribute("vaccination", vaccination);
			modelMap.addAttribute("vaccines", vs.getAllVaccines());
			modelMap.addAttribute("pets",ps.findAllPets());
			return "vaccination/createOrUpdateVaccinationForm";
		}
		
		else {
			try {                    
                this.vs.save(vaccination);                    
            } catch (UnfeasibleVaccinationException ex) {
                result.rejectValue("vaccine", "unfeasible", "La mascota seleccionada no puede recibir la vacuna especificada.");
                modelMap.addAttribute("vaccination", vaccination);
    			modelMap.addAttribute("vaccines", vs.getAllVaccines());
    			modelMap.addAttribute("pets",ps.findAllPets());
                return "vaccination/createOrUpdateVaccinationForm";
            }
			return "welcome";
		}
    
}
}
