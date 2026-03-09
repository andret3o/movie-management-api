package com.example.movie_api.controller;

import com.example.movie_api.dto.ActorDTO;
import com.example.movie_api.dto.GenreResponseDTO;
import com.example.movie_api.dto.MovieDTO;
import com.example.movie_api.entities.Genre;
import com.example.movie_api.exception.DatabaseNotEmptyException;
import com.example.movie_api.repositories.ActorRepository;
import com.example.movie_api.repositories.GenreRepository;
import com.example.movie_api.repositories.MovieRepository;
import com.example.movie_api.service.ActorService;
import com.example.movie_api.service.GenreService;
import com.example.movie_api.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("/api/fill")
public class FillController {

    @Autowired
    private GenreService genreService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @PostMapping
    public ResponseEntity<String> fillData() {
        // Check if database is empty
        if (movieRepository.count() > 0 || actorRepository.count() > 0 || genreRepository.count() > 0) {
            throw new DatabaseNotEmptyException("Database is not empty. Please clear the database first using /api/clear");
        }

        // Create 5 genres
        GenreResponseDTO action = genreService.create(new Genre("Action"));
        GenreResponseDTO scifi = genreService.create(new Genre("Sci-Fi"));
        GenreResponseDTO drama = genreService.create(new Genre("Drama"));
        GenreResponseDTO comedy = genreService.create(new Genre("Comedy"));
        GenreResponseDTO thriller = genreService.create(new Genre("Thriller"));

        // Create 15 actors (without movies initially)
        ActorDTO leoDto = new ActorDTO();
        leoDto.setName("Leonardo DiCaprio");
        leoDto.setBirthDate(LocalDate.parse("1974-11-11"));
        Long leoId = actorService.create(leoDto).getId();

        ActorDTO tomHanksDto = new ActorDTO();
        tomHanksDto.setName("Tom Hanks");
        tomHanksDto.setBirthDate(LocalDate.parse("1956-07-09"));
        Long tomHanksId = actorService.create(tomHanksDto).getId();

        ActorDTO merylDto = new ActorDTO();
        merylDto.setName("Meryl Streep");
        merylDto.setBirthDate(LocalDate.parse("1949-06-22"));
        Long merylId = actorService.create(merylDto).getId();

        ActorDTO ellenDto = new ActorDTO();
        ellenDto.setName("Ellen Page");
        ellenDto.setBirthDate(LocalDate.parse("1987-02-21"));
        Long ellenId = actorService.create(ellenDto).getId();

        ActorDTO tomHardyDto = new ActorDTO();
        tomHardyDto.setName("Tom Hardy");
        tomHardyDto.setBirthDate(LocalDate.parse("1977-09-15"));
        Long tomHardyId = actorService.create(tomHardyDto).getId();

        ActorDTO bradDto = new ActorDTO();
        bradDto.setName("Brad Pitt");
        bradDto.setBirthDate(LocalDate.parse("1963-12-18"));
        Long bradId = actorService.create(bradDto).getId();

        ActorDTO angelinaDto = new ActorDTO();
        angelinaDto.setName("Angelina Jolie");
        angelinaDto.setBirthDate(LocalDate.parse("1975-06-04"));
        Long angelinaId = actorService.create(angelinaDto).getId();

        ActorDTO willDto = new ActorDTO();
        willDto.setName("Will Smith");
        willDto.setBirthDate(LocalDate.parse("1968-09-25"));
        Long willId = actorService.create(willDto).getId();

        ActorDTO jenniferDto = new ActorDTO();
        jenniferDto.setName("Jennifer Lawrence");
        jenniferDto.setBirthDate(LocalDate.parse("1990-08-15"));
        Long jenniferId = actorService.create(jenniferDto).getId();

        ActorDTO robertDto = new ActorDTO();
        robertDto.setName("Robert Downey Jr.");
        robertDto.setBirthDate(LocalDate.parse("1965-04-04"));
        Long robertId = actorService.create(robertDto).getId();

        ActorDTO scarlettDto = new ActorDTO();
        scarlettDto.setName("Scarlett Johansson");
        scarlettDto.setBirthDate(LocalDate.parse("1984-11-22"));
        Long scarlettId = actorService.create(scarlettDto).getId();

        ActorDTO chrisDto = new ActorDTO();
        chrisDto.setName("Chris Evans");
        chrisDto.setBirthDate(LocalDate.parse("1981-06-13"));
        Long chrisId = actorService.create(chrisDto).getId();

        ActorDTO emmaDto = new ActorDTO();
        emmaDto.setName("Emma Watson");
        emmaDto.setBirthDate(LocalDate.parse("1990-04-15"));
        Long emmaId = actorService.create(emmaDto).getId();

        ActorDTO danielDto = new ActorDTO();
        danielDto.setName("Daniel Radcliffe");
        danielDto.setBirthDate(LocalDate.parse("1989-07-23"));
        Long danielId = actorService.create(danielDto).getId();

        ActorDTO rupertDto = new ActorDTO();
        rupertDto.setName("Rupert Grint");
        rupertDto.setBirthDate(LocalDate.parse("1988-08-24"));
        Long rupertId = actorService.create(rupertDto).getId();

        // Create 20 movies with varied relationships (spanning 1990-2020)
        // Each genre has at least 2 movies, some movies multiple genres, actors varied

        // Movie 1: Inception (2010) - Action, Sci-Fi, Thriller - 3 actors
        MovieDTO inception = new MovieDTO();
        inception.setTitle("Inception");
        inception.setReleaseYear(2010);
        inception.setDuration(148);
        inception.setGenreIds(Arrays.asList(action.getId(), scifi.getId(), thriller.getId()));
        inception.setActorIds(Arrays.asList(leoId, ellenId, tomHardyId));
        movieService.create(inception);

        // Movie 2: Forrest Gump (1994) - Drama, Comedy - 1 actor
        MovieDTO forrest = new MovieDTO();
        forrest.setTitle("Forrest Gump");
        forrest.setReleaseYear(1994);
        forrest.setDuration(142);
        forrest.setGenreIds(Arrays.asList(drama.getId(), comedy.getId()));
        forrest.setActorIds(Collections.singletonList(tomHanksId));
        movieService.create(forrest);

        // Movie 3: The Devil Wears Prada (2006) - Comedy, Drama - 1 actor
        MovieDTO devil = new MovieDTO();
        devil.setTitle("The Devil Wears Prada");
        devil.setReleaseYear(2006);
        devil.setDuration(109);
        devil.setGenreIds(Arrays.asList(comedy.getId(), drama.getId()));
        devil.setActorIds(Collections.singletonList(merylId));
        movieService.create(devil);

        // Movie 4: The Matrix (1999) - Action, Sci-Fi - 2 actors
        MovieDTO matrix = new MovieDTO();
        matrix.setTitle("The Matrix");
        matrix.setReleaseYear(1999);
        matrix.setDuration(136);
        matrix.setGenreIds(Arrays.asList(action.getId(), scifi.getId()));
        matrix.setActorIds(Arrays.asList(leoId, tomHardyId));
        movieService.create(matrix);

        // Movie 5: Fight Club (1999) - Drama, Thriller - 2 actors
        MovieDTO fightClub = new MovieDTO();
        fightClub.setTitle("Fight Club");
        fightClub.setReleaseYear(1999);
        fightClub.setDuration(139);
        fightClub.setGenreIds(Arrays.asList(drama.getId(), thriller.getId()));
        fightClub.setActorIds(Arrays.asList(bradId, angelinaId));
        movieService.create(fightClub);

        // Movie 6: The Pursuit of Happyness (2006) - Drama - 1 actor
        MovieDTO pursuit = new MovieDTO();
        pursuit.setTitle("The Pursuit of Happyness");
        pursuit.setReleaseYear(2006);
        pursuit.setDuration(117);
        pursuit.setGenreIds(Collections.singletonList(drama.getId()));
        pursuit.setActorIds(Collections.singletonList(willId));
        movieService.create(pursuit);

        // Movie 7: The Hunger Games (2012) - Action, Sci-Fi - 1 actor
        MovieDTO hunger = new MovieDTO();
        hunger.setTitle("The Hunger Games");
        hunger.setReleaseYear(2012);
        hunger.setDuration(142);
        hunger.setGenreIds(Arrays.asList(action.getId(), scifi.getId()));
        hunger.setActorIds(Collections.singletonList(jenniferId));
        movieService.create(hunger);

        // Movie 8: Iron Man (2008) - Action, Sci-Fi - 1 actor
        MovieDTO ironMan = new MovieDTO();
        ironMan.setTitle("Iron Man");
        ironMan.setReleaseYear(2008);
        ironMan.setDuration(126);
        ironMan.setGenreIds(Arrays.asList(action.getId(), scifi.getId()));
        ironMan.setActorIds(Collections.singletonList(robertId));
        movieService.create(ironMan);

        // Movie 9: Avengers (2012) - Action, Sci-Fi - 3 actors
        MovieDTO avengers = new MovieDTO();
        avengers.setTitle("Avengers");
        avengers.setReleaseYear(2012);
        avengers.setDuration(143);
        avengers.setGenreIds(Arrays.asList(action.getId(), scifi.getId()));
        avengers.setActorIds(Arrays.asList(robertId, scarlettId, chrisId));
        movieService.create(avengers);

        // Movie 10: Harry Potter and the Philosopher's Stone (2001) - Sci-Fi, Thriller - 3 actors
        MovieDTO harry1 = new MovieDTO();
        harry1.setTitle("Harry Potter and the Philosopher's Stone");
        harry1.setReleaseYear(2001);
        harry1.setDuration(152);
        harry1.setGenreIds(Arrays.asList(scifi.getId(), thriller.getId()));
        harry1.setActorIds(Arrays.asList(emmaId, danielId, rupertId));
        movieService.create(harry1);

        // Movie 11: Harry Potter and the Chamber of Secrets (2002) - Sci-Fi - 2 actors
        MovieDTO harry2 = new MovieDTO();
        harry2.setTitle("Harry Potter and the Chamber of Secrets");
        harry2.setReleaseYear(2002);
        harry2.setDuration(161);
        harry2.setGenreIds(Collections.singletonList(scifi.getId()));
        harry2.setActorIds(Arrays.asList(emmaId, danielId));
        movieService.create(harry2);

        // Movie 12: Titanic (1997) - Drama - 1 actor
        MovieDTO titanic = new MovieDTO();
        titanic.setTitle("Titanic");
        titanic.setReleaseYear(1997);
        titanic.setDuration(194);
        titanic.setGenreIds(Collections.singletonList(drama.getId()));
        titanic.setActorIds(Collections.singletonList(leoId));
        movieService.create(titanic);

        // Movie 13: Mamma Mia! (2008) - Comedy - 1 actor
        MovieDTO mamma = new MovieDTO();
        mamma.setTitle("Mamma Mia!");
        mamma.setReleaseYear(2008);
        mamma.setDuration(108);
        mamma.setGenreIds(Collections.singletonList(comedy.getId()));
        mamma.setActorIds(Collections.singletonList(merylId));
        movieService.create(mamma);

        // Movie 14: The Iron Lady (2011) - Drama - 1 actor
        MovieDTO ironLady = new MovieDTO();
        ironLady.setTitle("The Iron Lady");
        ironLady.setReleaseYear(2011);
        ironLady.setDuration(105);
        ironLady.setGenreIds(Collections.singletonList(drama.getId()));
        ironLady.setActorIds(Collections.singletonList(merylId));
        movieService.create(ironLady);

        // Movie 15: Cast Away (2000) - Drama - 1 actor
        MovieDTO castAway = new MovieDTO();
        castAway.setTitle("Cast Away");
        castAway.setReleaseYear(2000);
        castAway.setDuration(143);
        castAway.setGenreIds(Collections.singletonList(drama.getId()));
        castAway.setActorIds(Collections.singletonList(tomHanksId));
        movieService.create(castAway);

        // Movie 16: Saving Private Ryan (1998) - Action, Drama - 1 actor
        MovieDTO saving = new MovieDTO();
        saving.setTitle("Saving Private Ryan");
        saving.setReleaseYear(1998);
        saving.setDuration(169);
        saving.setGenreIds(Arrays.asList(action.getId(), drama.getId()));
        saving.setActorIds(Collections.singletonList(tomHanksId));
        movieService.create(saving);

        // Movie 17: Mr. & Mrs. Smith (2005) - Action, Comedy - 2 actors
        MovieDTO mrMrs = new MovieDTO();
        mrMrs.setTitle("Mr. & Mrs. Smith");
        mrMrs.setReleaseYear(2005);
        mrMrs.setDuration(120);
        mrMrs.setGenreIds(Arrays.asList(action.getId(), comedy.getId()));
        mrMrs.setActorIds(Arrays.asList(bradId, angelinaId));
        movieService.create(mrMrs);

        // Movie 18: I, Robot (2004) - Sci-Fi - 1 actor
        MovieDTO iRobot = new MovieDTO();
        iRobot.setTitle("I, Robot");
        iRobot.setReleaseYear(2004);
        iRobot.setDuration(115);
        iRobot.setGenreIds(Collections.singletonList(scifi.getId()));
        iRobot.setActorIds(Collections.singletonList(willId));
        movieService.create(iRobot);

        // Movie 19: Silver Linings Playbook (2012) - Comedy, Drama - 1 actor
        MovieDTO silver = new MovieDTO();
        silver.setTitle("Silver Linings Playbook");
        silver.setReleaseYear(2012);
        silver.setDuration(122);
        silver.setGenreIds(Arrays.asList(comedy.getId(), drama.getId()));
        silver.setActorIds(Collections.singletonList(jenniferId));
        movieService.create(silver);

        // Movie 20: Black Widow (2021) - Action - 1 actor
        MovieDTO blackWidow = new MovieDTO();
        blackWidow.setTitle("Black Widow");
        blackWidow.setReleaseYear(2021);
        blackWidow.setDuration(134);
        blackWidow.setGenreIds(Collections.singletonList(action.getId()));
        blackWidow.setActorIds(Collections.singletonList(scarlettId));
        movieService.create(blackWidow);

        return ResponseEntity.ok("Sample data created successfully");
    }
}