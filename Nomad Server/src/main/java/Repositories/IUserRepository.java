package Repositories;

import DTO.LoginDTO;
import model.User;

public interface IUserRepository extends IRepository<User, Long>{

    User getExisting(LoginDTO loginDTO);
}
