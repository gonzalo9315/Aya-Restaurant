using Backend.Models.DataModels;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Backend.Domain.Interfaces
{
    public interface IOrderORM
    {
        Task<IEnumerable<Order>> GetAll();
        Task<IEnumerable<Order>> GetMyOrders(int idUser);
        Task<IEnumerable<Dish>> GetDishesOfOrder(int id);
        Task<Order> GetByID(int id);
        Task<bool> Create(Order order);
        Task<bool> AddItem(OrderItem orderItem);
        Task<bool> DeleteByID(int id);
        Task<bool> UpdateByID(int id, Order order);
    }
}
