using Backend.Models;
using Backend.Models.DataModels;
using System.Threading.Tasks;

namespace Backend.Domain.Interfaces
{
    public interface IAuthORM
    {
        UserTokens Login(string email, string password);
        Task<bool> Register(User user);
        Task<bool> Update(int id, User user);
        Task<User> Profile(int id);
        Task<bool> Delete(int id);
    }
}
