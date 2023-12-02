package Repositories;

import DTO.LoginDTO;
import model.AppUser;

public interface IUserRepository extends IRepository<AppUser, Long>{

    AppUser getExisting(LoginDTO loginDTO);
}
