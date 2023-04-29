package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilmoRateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final GenreDbStorage genreStorage;
	private final MpaDbStorage mpaStorage;

	private final List<User> users = List.of(
			new User(1, "email1@gmail.com", "login1", "name1", LocalDate.now()),
			new User(2, "email2@gmail.com", "login2", "name2", LocalDate.now()),
			new User(3, "email3@gmail.com", "login3", "name3", LocalDate.now())
	);

	private final List<Film> films = List.of(
			new Film(1, "film1", new HashSet<>(), 0, new Mpa(1), "description1", LocalDate.now(), 1),
			new Film(2, "film2", new HashSet<>(List.of(new Genre(1))), 0, new Mpa(2), "description2", LocalDate.now(), 1),
			new Film(3, "film3", new HashSet<>(List.of(new Genre(2))), 0, new Mpa(3), "description3", LocalDate.now(), 2)
	);

	@BeforeAll
	public void fillDB() {
		userStorage.create(users.get(0));
		userStorage.create(users.get(1));
		userStorage.create(users.get(2));

		filmStorage.create(films.get(0));
		filmStorage.create(films.get(1));
		filmStorage.create(films.get(2));
	}

	@Test
	public void testGetAllMpa() {
		List<Mpa> mpaList = mpaStorage.getAll();
		List<Mpa> expected = List.of(
				new Mpa(1),
				new Mpa(2),
				new Mpa(3),
				new Mpa(4),
				new Mpa(5)
		);
		assertThat(mpaList).isEqualTo(expected);
	}

	@Test
	public void testGetMpaById() {
		assertThat(mpaStorage.getById(1)).isEqualTo(new Mpa(1));
		assertThat(mpaStorage.getById(2)).isEqualTo(new Mpa(2));
		assertThat(mpaStorage.getById(3)).isEqualTo(new Mpa(3));
		assertThat(mpaStorage.getById(4)).isEqualTo(new Mpa(4));
		assertThat(mpaStorage.getById(5)).isEqualTo(new Mpa(5));
	}

	@Test
	public void testGetAllGenres() {
		List<Genre> genreList = genreStorage.getAll();
		List<Genre> expected = List.of(
				new Genre(1),
				new Genre(2),
				new Genre(3),
				new Genre(4),
				new Genre(5),
				new Genre(6)
		);
		assertThat(genreList).isEqualTo(expected);
	}

	@Test
	public void testGetGenreById() {
		assertThat(genreStorage.getById(1)).isEqualTo(new Genre(1));
		assertThat(genreStorage.getById(2)).isEqualTo(new Genre(2));
		assertThat(genreStorage.getById(3)).isEqualTo(new Genre(3));
		assertThat(genreStorage.getById(4)).isEqualTo(new Genre(4));
		assertThat(genreStorage.getById(5)).isEqualTo(new Genre(5));
		assertThat(genreStorage.getById(6)).isEqualTo(new Genre(6));
	}

	// --- User tests ---
	@Test
	public void testFindAllUsers() {

		List<User> users = userStorage.getAll();

		assertThat(users).isEqualTo(this.users);
	}

	@Test
	public void testGetFriends() {
		var user = users.get(0);
		// Get friends
		assertThat(userStorage.getFriends(user)).isEqualTo(List.of());
		// Add friend
		userStorage.addFriend(user, users.get(1));
		// Get friends
		assertThat(userStorage.getFriends(user)).isEqualTo(List.of(users.get(1)));
		// Break friendship
		userStorage.breakFriendship(user, users.get(1));
		// Get friends
		assertThat(userStorage.getFriends(user)).isEqualTo(List.of());
	}

	public void testMutualFriends() {
		var u1 = users.get(0);
		var u2 = users.get(1);
		var mutualFriends = userStorage.getMutualFriends(u1, u2);
		assertThat(mutualFriends).isEqualTo(List.of());
		// Add mutual friend
		userStorage.addFriend(u1, users.get(2));
		userStorage.addFriend(u2, users.get(2));
		// Get mutual friends
		mutualFriends = userStorage.getMutualFriends(u1, u2);
		assertThat(mutualFriends).isEqualTo(List.of(users.get(2)));
	}

	// --- Film tests
	// Get All
	public void testGetAll() {
		var films = filmStorage.getAll();
		assertThat(films).isEqualTo(this.films);
	}

	// Get top-rated
	public void testGetTopRated() {
		var u1 = users.get(0);
		var u2 = users.get(1);
		var u3 = users.get(2);

		filmStorage.like(films.get(0), u1);
		filmStorage.like(films.get(1), u1);
		filmStorage.like(films.get(2), u1);

		filmStorage.like(films.get(0), u2);
		filmStorage.like(films.get(1), u2);

		filmStorage.like(films.get(0), u3);

		var topRated = filmStorage.getTopRated(3);
		assertThat(topRated).isEqualTo(List.of(films.get(0), films.get(1), films.get(2)));
	}
} 