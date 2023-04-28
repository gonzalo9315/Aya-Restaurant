using Backend.Models.DataModels;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Backend.Domain.Interfaces
{
    public interface ICategorieORM
    {
        Task<IEnumerable<Categorie>> GetAll();
        Task<IEnumerable<Categorie>> GetAllAvalaible();
        Task<Categorie> GetByID(int id);
        Task<bool> Create(Categorie categorie);
        Task<bool> AddDish(DishCategorie dishCategorie);
        Task<bool> DeleteDish(DishCategorie dishCategorie);
        Task<bool> DeleteByID(int id);
        Task<bool> UpdateByID(int id, Categorie categorie);
    }
}
