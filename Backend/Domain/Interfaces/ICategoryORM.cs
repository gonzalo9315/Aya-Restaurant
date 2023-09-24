using Backend.Models.DataModels;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Backend.Domain.Interfaces
{
    public interface ICategoryORM
    {
        Task<IEnumerable<Category>> GetAll();
        Task<IEnumerable<Category>> GetAllAvalaible();
        Task<Category> GetByID(int id);
        Task<bool> Create(Category categorie);
        Task<bool> AddDish(DishCategory dishCategorie);
        Task<bool> DeleteDish(DishCategory dishCategorie);
        Task<bool> DeleteByID(int id);
        Task<bool> UpdateByID(int id, Category categorie);
    }
}
