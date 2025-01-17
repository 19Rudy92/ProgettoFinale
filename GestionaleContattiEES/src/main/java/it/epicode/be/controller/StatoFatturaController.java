package it.epicode.be.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.epicode.be.model.StatoFattura;
import it.epicode.be.service.StatoFatturaService;

@RestController
@RequestMapping("/api/statofatture")
public class StatoFatturaController {

	@Autowired
	private StatoFatturaService statoFatturaServ;

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<StatoFattura>> listaStatiFattura() {
		List<StatoFattura> lsf = statoFatturaServ.getAllStatoFatture();
		return new ResponseEntity<>(lsf, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<?> aggiungiStatoFattura(@RequestBody StatoFattura sf) {
		StatoFattura aggiunta = statoFatturaServ.aggiungiStatoFattura(sf);
		return new ResponseEntity<>(aggiunta, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/{id}")
	public Optional<StatoFattura> getStatoFatturaById(@PathVariable Long id) {
		Optional<StatoFattura> trovata = statoFatturaServ.getStatoFatturaById(id);
		return trovata;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteStatoFatturaByid(@PathVariable Long id) {
		Optional<StatoFattura> daEliminare = statoFatturaServ.getStatoFatturaById(id);
		if (daEliminare.isPresent()) {
			statoFatturaServ.deleteStatoFatturaById(id);
			return new ResponseEntity<>("Cancellazione Stato Fattura OK", HttpStatus.OK);
		}
		return new ResponseEntity<>("Stato Fattura NotFound", HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateStatoFattura(@PathVariable Long id, @RequestBody StatoFattura sf) {
		if (id != sf.getId()) {
			return new ResponseEntity<>("L id non è lo stesso", HttpStatus.BAD_REQUEST);
		}
		try {
			statoFatturaServ.updateStatoFattura(sf);
			return new ResponseEntity<>("Modifica avvenuta con successo", HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	
	
	
	

}
