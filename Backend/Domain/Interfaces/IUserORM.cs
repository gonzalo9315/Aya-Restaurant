using Backend.Models.DataModels;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Backend.Domain.Interfaces
{
    public interface IUserORM
    {
        Task<IEnumerable<User>> GetAll();
        Task<User> GetByID(int id);
        Task<bool> Create(User user);
        Task<bool> DeleteByID(int id);
        Task<bool> UpdateByID(int id, User user);
    }
}
