package at.jku.tk.hiesmair.gv.parlament.db;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;

@Transactional
public interface ParliamentClubRepository extends CrudRepository<ParliamentClub, Long> {

}
