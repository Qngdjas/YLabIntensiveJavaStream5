package ru.qngdjas.habitstracker.domain.service;

import ru.qngdjas.habitstracker.application.dto.user.UserDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserLoginDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserCreateDTO;
import ru.qngdjas.habitstracker.application.mapper.model.UserMapper;
import ru.qngdjas.habitstracker.application.utils.logger.ExecutionLoggable;
import ru.qngdjas.habitstracker.application.utils.validator.UserValidator;
import ru.qngdjas.habitstracker.application.utils.validator.ValidationException;
import ru.qngdjas.habitstracker.domain.model.user.EmailException;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.repository.IUserRepository;
import ru.qngdjas.habitstracker.domain.service.core.*;
import ru.qngdjas.habitstracker.infrastructure.persistance.UserRepository;

/**
 * Сервис обработки запросов управления пользователями.
 */
@ExecutionLoggable
public class UserService {

    /**
     * Репозиторий CRUD-операций над моделями пользователей.
     */
    private static final IUserRepository userRepository = new UserRepository();
    private static final UserMapper mapper = UserMapper.INSTANCE;

    /**
     * Метод аутентификации пользователя по почте и паролю.
     *
     * @param userDTO Учетные данные пользователя.
     * @return Аутентифицированный пользователь {@link User}, если учетные данные корректны, <p>иначе {@code null}.
     */
    public User login(UserLoginDTO userDTO) throws IncorrectPasswordException, NotFoundException {
        UserValidator.validate(userDTO);
        User user = userRepository.retrieveByEmail(userDTO.getEmail());
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь %s не найден", userDTO.getEmail()));
        }
        if (!user.getPassword().equals(userDTO.getPassword())) {
            throw new IncorrectPasswordException();
        }
        return user;
    }

    /**
     * Метод регистрации нового пользователя.
     *
     * @param userDTO Данные пользователя.
     * @return Зарегистрированный пользователь {@link User},
     * <p>{@code null} если регистрация не удалась.
     */
    public User register(UserCreateDTO userDTO) throws EmailException, ValidationException, AlreadyExistsException {
        UserValidator.validate(userDTO);
        if (userRepository.isExists(userDTO.getEmail())) {
            throw new AlreadyExistsException("Пользователь с таким email уже существует");
        }
        return userRepository.create(mapper.toUser(userDTO));
    }

    /**
     * Метод обновления профиля пользователя.
     *
     * @param id      Идентификатор пользователя, который выполняет операцию.
     * @param userDTO Обновленные данные пользователя.
     * @return Обновленный пользователь {@link User},
     * <p>{@code null} если обновление не удалось.
     */
    public User update(long id, UserDTO userDTO) throws EmailException, NotFoundException, ValidationException, AlreadyExistsException, RootlessException {
        UserValidator.validate(userDTO);
        User user = userRepository.retrieve(id);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь c id=%d не найден", id));
        }
        if (!user.getEmail().equals(userDTO.getEmail()) && userRepository.isExists(userDTO.getEmail())) {
            throw new AlreadyExistsException("Email занят");
        }
        if (user.getId() == userDTO.getId() || user.isAdmin()) {
            userDTO.setId(id);
            return userRepository.update(mapper.toUser(userDTO));
        }
        throw new RootlessException();
    }

    /**
     * Метод удаления профиля пользователя.
     *
     * @param id         Идентификатор пользователя, который выполняет операцию.
     * @param idToDelete Идентификатор пользователя, который подлежит удалению.
     * @return Удаленный пользователь {@link User},
     * <p>{@code null} если удаление не удалось.
     */
    public User delete(long id, long idToDelete) throws NotFoundException, RootlessException {
        User user = userRepository.retrieve(idToDelete);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (id == idToDelete || userRepository.retrieve(id).isAdmin()) {
            userRepository.delete(idToDelete);
            return user;
        }
        throw new RootlessException();
    }
}
