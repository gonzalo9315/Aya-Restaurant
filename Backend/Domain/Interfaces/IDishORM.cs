using Backend.Models.DataModels;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Backend.Domain.Interfaces
{
    public interface IDishORM
    {
        Task<IEnumerable<Dish>> GetAllByCategorie(int idCategorie);
        Task<IEnumerable<Dish>> GetAll();
        Task<Dish> GetByID(int id);
        Task<bool> Create(Dish dish);
        Task<bool> DeleteByID(int id);
        Task<bool> UpdateByID(int id, Dish dish);
    }
}
